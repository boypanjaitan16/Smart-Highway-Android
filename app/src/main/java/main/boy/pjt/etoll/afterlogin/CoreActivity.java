package main.boy.pjt.etoll.afterlogin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentActivityLog;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentDeviceList;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentProfile;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentRoadList;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.response.ResponseBalance;
import main.boy.pjt.etoll.response.ResponseRoad;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoreActivity extends AppCompatActivity {

    private Menu optionMenu;
    private BluetoothDevice device;
    private BluetoothAdapter adapter;
    private BluetoothSocket socket;
    private OutputStream out;
    private MyAlert alert;
    private MySession session;
    private LinearLayout container;
    private MyRetrofitInterface retrofitInterface;
    private static final int BLUETOOTH_REQ_CODE = 133;
    private static final int CHOOSE_DEVICE_REQ_CODE = 144;

    //private LinearLayout layoutBottomSheet;
    //private BottomSheetBehavior sheetBehavior;

    public BluetoothAdapter getAdapter() {
        return adapter;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_list:
                    startFragment(new FragmentRoadList());
                    return true;
                case R.id.nav_log:
                    startFragment(new FragmentActivityLog());
                    return true;
                case R.id.nav_profile:
                    startFragment(new FragmentProfile());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        startFragment(new FragmentRoadList());

        //show notification
        Bundle extras   = getIntent().getExtras();
        if(extras != null){
            String title    = extras.getString("title");
            String body     = extras.getString("body");

            showNotification(title, body);
            return;
        }

        initialize();
    }

    private void initialize(){

        //layoutBottomSheet   = findViewById(R.id.bottom_sheet);
        //sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        container   = findViewById(R.id.container);
        session     = new MySession(this);
        retrofitInterface   = MyRetrofit.getClient().create(MyRetrofitInterface.class);
        alert           = new MyAlert(this, false);
        adapter         = BluetoothAdapter.getDefaultAdapter();

        if (adapter != null){
            if (adapter.isEnabled()){
                if(socket == null) {
                    connectBluetoothSocket();
                }
            }
            else {
                turnOnAdapter();
            }
        }
        else{
            alert.alertInfoTitle("Bluetooth", "Maaf, perangkat anda tidak mendukung fitur bluetooth");
        }


        /*
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Toast.makeText(getApplicationContext(), "Expand Sheet", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        Toast.makeText(getApplicationContext(), "Collapse Sheet", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        */

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //btnBottomSheet.setText("Expand sheet");
        }
    }
    */

    private void showNotification(final String title, String body){
        final View layout = getLayoutInflater().inflate(R.layout.layout_notification, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView txtTitle   = layout.findViewById(R.id.title);
        TextView txtBody    = layout.findViewById(R.id.body);
        TextView ok         = layout.findViewById(R.id.okBtn);
        txtTitle.setText(title);
        txtBody.setText(body);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void moveGate(final int action, String roadId, final ResponseRoad.OutGate gate, final Callable<Void> methodParam) {
        if (adapter != null){
            if (adapter.isEnabled()){
                if (socket.isConnected()){
                    try {
                        out = socket.getOutputStream();
                    } catch (IOException e) {
                        alert.alertInfo(e.getMessage());
                        e.printStackTrace();
                        return;
                    }

                    if(action == 1) {
                        Call<ResponseBalance.RetrofitResponse> callPlaceOrder = retrofitInterface.placeRoadOrder(session.getCOSTUMER_ID(), roadId, gate.getId());

                        callPlaceOrder.enqueue(
                                new Callback<ResponseBalance.RetrofitResponse>() {
                                    @Override
                                    public void onResponse(Call<ResponseBalance.RetrofitResponse> call, Response<ResponseBalance.RetrofitResponse> response) {
                                        if (response.body().getStatus().equals(MyConstant.System.responseSuccess)) {
                                            final int balance = response.body().getData().getBalance();
                                            //this is the one who did the show
                                            String command = action + "." + balance;
                                            try {
                                                out.write(command.getBytes());
                                                out.flush();

                                                Log.d("FLUSH", command);
                                                Snackbar snackbar = Snackbar
                                                        .make(container, "Silahkan lewat", Snackbar.LENGTH_LONG)
                                                        .setAction("SISA SALDO", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Snackbar snackbar1 = Snackbar.make(container, "Sisa saldo anda Rp." + balance, Snackbar.LENGTH_SHORT);
                                                                snackbar1.show();
                                                            }
                                                        });

                                                snackbar.show();
                                                try {
                                                    methodParam.call();

                                                } catch (Exception e) {
                                                    alert.alertInfo(e.getMessage());
                                                }

                                            } catch (IOException e) {
                                                alert.alertInfo(e.getMessage());
                                                e.printStackTrace();
                                            }

                                        } else {
                                            alert.alertInfo(response.body().getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBalance.RetrofitResponse> call, Throwable t) {
                                        alert.alertInfo(t.getMessage());
                                    }
                                }
                        );
                    }
                    else{
                        String command = action + "." + gate.getName();
                        try {
                            out.write(command.getBytes());
                            out.flush();

                            Log.d("FLUSH", command);

                            try {
                                methodParam.call();

                            } catch (Exception e) {
                                alert.alertInfo(e.getMessage());
                            }

                        } catch (IOException e) {
                            alert.alertInfo(e.getMessage());
                            e.printStackTrace();
                        }
                    }


                }
                else {
                    alert.alertWithConfirm("Bluetooth", "Perangkat anda belum terhubung, apakah anda ingin menghubungkannya sekarang ?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    connectBluetoothSocket();
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                }


            }
            else {
                turnOnAdapter();
            }
        }
        else {
            alert.alertInfoTitle("Bluetooth", "Maaf, perangkat anda tidak mendukung fitur bluetooth");
        }
    }



    private void turnOnAdapter(){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, BLUETOOTH_REQ_CODE);
    }

    public void connectBluetoothSocket(){
        startActivityForResult(new Intent(this, ChooseDeviceActivity.class), CHOOSE_DEVICE_REQ_CODE);
    }

    public void startFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack("fragments");
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionMenu  = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.status:
                if (adapter != null){
                    if (adapter.isEnabled()) {
                        if (socket != null && socket.isConnected()) {
                            alert.alertInfo("Perangkat anda sedang terhubung dengan perangkat Bluetooth " + device.getName() + " (" + device.getAddress() + ")");
                        } else {
                            alert.alertWithConfirm("Bluetooth", "Perangkat anda belum terhubung, apakah anda ingin menghubungkannya sekarang ?",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            connectBluetoothSocket();
                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                        }
                    }
                    else {
                        turnOnAdapter();
                    }
                }
                else {
                    alert.alertInfo("Perangkat anda tidak mendukung fitur Bluetooth");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BLUETOOTH_REQ_CODE){
            if (resultCode == RESULT_OK){
                connectBluetoothSocket();
            }
            else {
                Toast.makeText(this, "Bluetooth tidak diaktifkan", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == CHOOSE_DEVICE_REQ_CODE){
            if (resultCode == RESULT_OK){
                String deviceAdress = data.getStringExtra("DEVICE_ADDRESS");
                device              = adapter.getRemoteDevice(deviceAdress);
                Log.d("DEVICE_ADDRESS", deviceAdress);
                try {
                    ParcelUuid[] uuids      = device.getUuids();
                    socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    out = socket.getOutputStream();

                    optionMenu.getItem(0).setIcon(R.drawable.ic_check_white);
                    Toast.makeText(this, "Perangkat anda terhubung sekarang dengan "+device.getName(), Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    alert.alertInfoTitle("Bluetooth Error", "Terjadi kesalahan saat menghubungkan perangkat. (" + e.getMessage()+")");
                }
            }
        }
    }
}
