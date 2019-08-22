package main.boy.pjt.etoll.afterlogin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.adapter.AdapterBluetoothList;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.response.ResponseDevices;

public class ChooseDeviceActivity extends AppCompatActivity {

    MyAlert alert;
    MySession session;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    Set<BluetoothDevice> devices;
    List<ResponseDevices> responseDevices;
    BluetoothAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);

        session     = new MySession(this);
        alert       = new MyAlert(this, false);
        refreshLayout   = findViewById(R.id.swipe);
        recyclerView    = findViewById(R.id.device_list);
        responseDevices = new ArrayList<>();
        adapter         = BluetoothAdapter.getDefaultAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadDevices();
                    }
                }
        );

        loadDevices();
    }

    private void loadDevices(){
        if(adapter != null){
            devices = adapter.getBondedDevices();

            if(devices.size() > 0){
                responseDevices.clear();
                for(BluetoothDevice device: devices){
                    responseDevices.add(new ResponseDevices(device.getName(), device.getAddress()));
                }

                AdapterBluetoothList adapterBluetoothList = new AdapterBluetoothList(this, responseDevices);

                recyclerView.setAdapter(adapterBluetoothList);

                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
            }
        }
    }

    public void setResult(String deviceAddress){
        Intent i    = new Intent();
        i.putExtra("DEVICE_ADDRESS", deviceAddress);
        setResult(RESULT_OK, i);
        finish();
    }
}
