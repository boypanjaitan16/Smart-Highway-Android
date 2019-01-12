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

import java.util.List;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.adapter.AdapterRoadList;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.values.ValueRoad;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Boy Panjaitan on 06/06/2018.
 */

public class FragmentRoadList extends Fragment {

    RecyclerView roadlist;
    SwipeRefreshLayout refreshLayout;
    MySession session;
    MyAlert alert;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_road_list, container, false);

        ((CoreActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        roadlist        = view.findViewById(R.id.road_list);
        refreshLayout   = view.findViewById(R.id.swipe);
        session         = new MySession(getContext());
        alert           = new MyAlert(getContext(), false);


        roadlist.setLayoutManager(new LinearLayoutManager(getContext()));
        roadlist.setItemAnimator(new DefaultItemAnimator());
        roadlist.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getRoadList();
                    }
                }
        );

        getRoadList();

        return view;
    }

    private void getRoadList(){
        MyRetrofitInterface retrofitInterface   = MyRetrofit.getClient().create(MyRetrofitInterface.class);
        Call<ValueRoad.RetrofitResponse> call       = retrofitInterface.getRoadList(session.getCOSTUMER_ID());

        call.enqueue(
                new Callback<ValueRoad.RetrofitResponse>() {
                    @Override
                    public void onResponse(Call<ValueRoad.RetrofitResponse> call, Response<ValueRoad.RetrofitResponse> response) {
                        if (response.body().getStatus().equals(MyConstant.System.responseSuccess)) {
                            List<ValueRoad.Values> values = response.body().getData();
                            AdapterRoadList adapterRoadList = new AdapterRoadList(getContext(), values);

                            roadlist.setAdapter(adapterRoadList);

                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.setRefreshing(false);
                            }
                        }
                        else {
                            alert.alertInfo(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ValueRoad.RetrofitResponse> call, Throwable t) {
                        alert.alertInfo(t.getMessage());
                    }
                }
        );
    }

    public void showDetailRoad(Bundle bundle){
        FragmentRoadDetail fragment = new FragmentRoadDetail();
        fragment.setArguments(bundle);

        if (((CoreActivity)getActivity()).getAdapter() != null){
            if (((CoreActivity)getActivity()).getAdapter().isEnabled()){
                if (((CoreActivity)getActivity()).getSocket() != null && ((CoreActivity)getActivity()).getSocket().isConnected()){
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentContainer, fragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack("fragments");
                    ft.commit();
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
