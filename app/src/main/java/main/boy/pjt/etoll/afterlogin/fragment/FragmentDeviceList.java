package main.boy.pjt.etoll.afterlogin.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.adapter.AdapterBluetoothList;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.response.ResponseDevices;

/**
 * Created by Boy Panjaitan on 15/06/2018.
 */

public class FragmentDeviceList extends Fragment {
    MyAlert alert;
    MySession session;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    Set<BluetoothDevice> devices;
    List<ResponseDevices> responseDevices;
    BluetoothAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_device_list, container, false);

        ((CoreActivity)getActivity()).getSupportActionBar().setTitle("Daftar Perangkat");

        session     = new MySession(getContext());
        alert       = new MyAlert(getContext(), false);
        refreshLayout   = view.findViewById(R.id.swipe);
        recyclerView    = view.findViewById(R.id.device_list);
        responseDevices = new ArrayList<>();
        adapter         = ((CoreActivity)getActivity()).getAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadDevices();
                    }
                }
        );

        loadDevices();

        return view;
    }

    private void loadDevices(){
        if(adapter != null){
            devices = adapter.getBondedDevices();

            if(devices.size() > 0){
                responseDevices.clear();
                for(BluetoothDevice device: devices){
                    responseDevices.add(new ResponseDevices(device.getName(), device.getAddress()));
                }

                AdapterBluetoothList adapterBluetoothList = new AdapterBluetoothList(getContext(), responseDevices);

                recyclerView.setAdapter(adapterBluetoothList);

                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
            }
        }
    }
}
