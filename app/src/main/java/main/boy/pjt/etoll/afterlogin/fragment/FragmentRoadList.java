package main.boy.pjt.etoll.afterlogin.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.adapter.AdapterRoadList;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.interfaces.OnGateItemClickInterface;
import main.boy.pjt.etoll.response.ResponseRoad;
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
    ResponseRoad.Values mValues;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_road_list, container, false);

        ((CoreActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        roadlist        = view.findViewById(R.id.road_list);
        refreshLayout   = view.findViewById(R.id.swipe);
        session         = new MySession(getContext());
        alert           = new MyAlert(getContext(), false);


        registerForContextMenu(refreshLayout);

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
        Call<ResponseRoad.RetrofitResponse> call       = retrofitInterface.getRoadList(session.getCOSTUMER_ID());

        call.enqueue(
                new Callback<ResponseRoad.RetrofitResponse>() {
                    @Override
                    public void onResponse(Call<ResponseRoad.RetrofitResponse> call, Response<ResponseRoad.RetrofitResponse> response) {
                        if (response.body().getStatus().equals(MyConstant.System.responseSuccess)) {
                            List<ResponseRoad.Values> values = response.body().getData();
                            AdapterRoadList adapterRoadList = new AdapterRoadList(getContext(), values, new OnGateItemClickInterface() {
                                @Override
                                public void onItemClick(ResponseRoad.Values values) {

                                    mValues = values;
                                    refreshLayout.showContextMenu();

                                }
                            });

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
                    public void onFailure(Call<ResponseRoad.RetrofitResponse> call, Throwable t) {
                        alert.alertInfo(t.getMessage());
                    }
                }
        );
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Pilih Jenis Kendaraan");

        menu.add(0, 1, 0, "GOLONGAN I");
        menu.add(0, 2, 0, "GOLONGAN II");
        menu.add(0, 3, 0, "GOLONGAN III");
        menu.add(0, 4, 0, "GOLONGAN IV");
        menu.add(0, 5, 0, "GOLONGAN V");
        menu.add(0, 6, 0, "GOLONGAN VI");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        Bundle bundle   = new Bundle();
        bundle.putSerializable("value", mValues);
        bundle.putString("jenis", item.getTitle().toString());

        FragmentOutGateList fragment = new FragmentOutGateList();
        fragment.setArguments(bundle);

        ((CoreActivity)getActivity()).startFragment(fragment);
        return super.onContextItemSelected(item);
    }
}
