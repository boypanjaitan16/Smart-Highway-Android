package main.boy.pjt.etoll.afterlogin.fragment;

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

import java.util.List;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.adapter.AdapterActivityList;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.response.ResponseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Boy Panjaitan on 06/06/2018.
 */

public class FragmentActivityLog extends Fragment {
    MyAlert alert;
    MySession session;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_activity_log, container, false);

        ((CoreActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_history);

        session     = new MySession(getContext());
        alert       = new MyAlert(getContext(), false);
        refreshLayout   = view.findViewById(R.id.swipe);
        recyclerView    = view.findViewById(R.id.activity_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadActivities();
                    }
                }
        );

        loadActivities();

        return view;
    }

    private void loadActivities(){
        MyRetrofitInterface retrofitInterface   = MyRetrofit.getClient().create(MyRetrofitInterface.class);
        Call<ResponseActivity.RetrofitResponse> call       = retrofitInterface.getActivities(session.getCOSTUMER_ID());

        call.enqueue(
                new Callback<ResponseActivity.RetrofitResponse>() {
                    @Override
                    public void onResponse(Call<ResponseActivity.RetrofitResponse> call, Response<ResponseActivity.RetrofitResponse> response) {
                        if (response.body().getStatus().equals(MyConstant.System.responseSuccess)){
                            List<ResponseActivity.Values> values   = response.body().getData();
                            AdapterActivityList adapterActivityList = new AdapterActivityList(values);

                            recyclerView.setAdapter(adapterActivityList);

                            if (refreshLayout.isRefreshing()){
                                refreshLayout.setRefreshing(false);
                            }
                        }
                        else {
                            alert.alertInfo(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseActivity.RetrofitResponse> call, Throwable t) {
                        alert.alertInfo(t.getMessage());
                    }
                }
        );
    }
}
