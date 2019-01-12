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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentActivityLog;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentDeviceList;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentProfile;
import main.boy.pjt.etoll.afterlogin.fragment.FragmentRoadList;
import main.boy.pjt.etoll.helper.MyAlert;

public class CoreActivity extends AppCompatActivity {

    private Menu optionMenu;
    private BluetoothDevice device;
    private BluetoothAdapter adapter;
    private BluetoothSocket socket;
    private OutputStream out;
    private MyAlert alert;
    private static final int BLUETOOTH_REQ_CODE = 133;
    private static final int CHOOSE_DEVICE_REQ_CODE = 144;

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
        setContentView(R.layout.activity_core);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initialize();
        startFragment(new FragmentRoadList());
    }

    public void moveGate(int condition, int balance, int roda) throws IOException {
        if (adapter != null){
            if (adapter.isEnabled()){
                if (socket.isConnected()){
                    String command  = condition+"."+roda+"."+balance;
                    out.write(command.getBytes());
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

    private void initialize(){

        alert           = new MyAlert(this, false);
        adapter         = BluetoothAdapter.getDefaultAdapter();

        if (adapter != null){
            if (adapter.isEnabled()){
                connectBluetoothSocket();
            }
            else {
                turnOnAdapter();
            }
        }
        else{
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
