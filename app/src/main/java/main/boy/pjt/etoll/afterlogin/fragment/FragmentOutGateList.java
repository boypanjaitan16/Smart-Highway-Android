package main.boy.pjt.etoll.afterlogin.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Callable;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.adapter.AdapterOutGateList;
import main.boy.pjt.etoll.adapter.AdapterRoadList;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.response.ResponseRoad;

public class FragmentOutGateList extends Fragment {

    MyAlert alert;
    ResponseRoad.Values roadValue;
    RecyclerView gateList;
    SwipeRefreshLayout refreshLayout;
    TextView jenis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_out_gate_list, container, false);

        ((CoreActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_outgate);

        alert           = new MyAlert(getContext(), false);
        roadValue       = (ResponseRoad.Values) getArguments().getSerializable("value");
        gateList        = view.findViewById(R.id.gate_list);
        refreshLayout   = view.findViewById(R.id.swipe);
        jenis           = view.findViewById(R.id.jenis_kendaraan);

        jenis.setText(getArguments().getString("jenis"));

        gateList.setLayoutManager(new LinearLayoutManager(getContext()));
        gateList.setItemAnimator(new DefaultItemAnimator());
        gateList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadGates();
                    }
                }
        );

        loadGates();

        return view;
    }

    private void loadGates(){
        List<ResponseRoad.OutGate> gates = roadValue.getOutGates();
        AdapterOutGateList adapterOutGateList = new AdapterOutGateList(getContext(), gates);

        gateList.setAdapter(adapterOutGateList);

        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    public void showDetailRoad(Bundle bundle){
        final FragmentRoadDetail fragment = new FragmentRoadDetail();
        bundle.putSerializable("value", roadValue);
        fragment.setArguments(bundle);


        if (((CoreActivity)getActivity()).getAdapter() != null){
            if (((CoreActivity)getActivity()).getAdapter().isEnabled()){
                if (((CoreActivity)getActivity()).getSocket() != null && ((CoreActivity)getActivity()).getSocket().isConnected()){
                    ResponseRoad.OutGate gate   = (ResponseRoad.OutGate) bundle.getSerializable("gate");
                    ((CoreActivity)getActivity()).moveGate(1, roadValue.getId(), gate, new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            ((CoreActivity)getActivity()).startFragment(fragment);
                            return null;
                        }
                    });

                }
                else {
                    alert.alertWithConfirm("Bluetooth", "Perangkat anda belum terhubung, apakah anda ingin menghubungkannya sekarang ?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((CoreActivity)getActivity()).connectBluetoothSocket();
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
            else{
                alert.alertInfoTitle("Bluetooth", "Bluetooth pada perangkat anda belum menyala, silahkan klik tombol bluetooth pada sudut kanan atas untuk panduan lebih lanjut");
            }
        }
        else {
            alert.alertInfoTitle("Bluetooth", "Maaf, perangkat anda tidak mendukung fitur bluetooth");
        }

    }
}
