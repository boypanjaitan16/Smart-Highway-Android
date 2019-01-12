package main.boy.pjt.etoll.afterlogin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.values.ValueBalance;
import main.boy.pjt.etoll.values.ValueResponse;
import main.boy.pjt.etoll.values.ValueRoad;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Boy Panjaitan on 10/06/2018.
 */

public class FragmentRoadDetail extends Fragment {

    ValueRoad.Values roadValue;
    TextView namaJalan;
    TextView panjangJalan;
    TextView tarifRoda4;
    TextView tarifRoda6;
    RadioGroup tarifGroup;
    RadioButton selectedButton;
    LinearLayout btnLanjutkan;
    MyAlert alert;
    MySession session;
    MyRetrofitInterface retrofitInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mainView   = inflater.inflate(R.layout.fragment_detail_road, container, false);

        roadValue   = (ValueRoad.Values) getArguments().getSerializable("value");
        namaJalan   = mainView.findViewById(R.id.nama_jalan);
        panjangJalan    = mainView.findViewById(R.id.panjang_jalan);
        tarifRoda4  = mainView.findViewById(R.id.roda4);
        tarifRoda6  = mainView.findViewById(R.id.roda6);
        tarifGroup  = mainView.findViewById(R.id.radioTarif);
        btnLanjutkan    = mainView.findViewById(R.id.lanjutkan);
        alert       = new MyAlert(getContext(), false);
        session     = new MySession(getContext());
        retrofitInterface   = MyRetrofit.getClient().create(MyRetrofitInterface.class);

        namaJalan.setText(roadValue.getName());
        panjangJalan.setText(roadValue.getDistance()+" KM");
        tarifRoda4.setText("Rp. "+roadValue.getPrice4());
        tarifRoda6.setText("Rp. "+roadValue.getPrice6());

        btnLanjutkan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int checkedId   = tarifGroup.getCheckedRadioButtonId();
                        if (checkedId != -1){
                            alert.loaderStart();
                            selectedButton  = mainView.findViewById(checkedId);

                            try {
                                Call<ValueResponse> call       = retrofitInterface.checkBalance(session.getCOSTUMER_ID(), roadValue.getId(), selectedButton.getText().toString());

                                call.enqueue(
                                        new Callback<ValueResponse>() {
                                            @Override
                                            public void onResponse(Call<ValueResponse> call, Response<ValueResponse> response) {
                                                alert.loaderStop();
                                                if (response.body().getStatus().equals(MyConstant.System.responseSuccess)){
                                                    String tipeRoda     = selectedButton.getText().toString();
                                                    final int jlhRoda         = Integer.valueOf(tipeRoda.replaceAll("\\D", ""));
                                                    Call<ValueBalance.RetrofitResponse> callPlaceOrder  = retrofitInterface.placeRoadOrder(session.getCOSTUMER_ID(), roadValue.getId(), tipeRoda);

                                                    callPlaceOrder.enqueue(
                                                            new Callback<ValueBalance.RetrofitResponse>() {
                                                                @Override
                                                                public void onResponse(Call<ValueBalance.RetrofitResponse> call, Response<ValueBalance.RetrofitResponse> response) {
                                                                    if (response.body().getStatus().equals(MyConstant.System.responseSuccess)){
                                                                        int balance = response.body().getData().getBalance();
                                                                        //this is the one who did the show
                                                                        try {
                                                                            ((CoreActivity)getActivity()).moveGate(1, balance, jlhRoda);
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                            alert.alertInfo(e.getMessage());
                                                                        }
                                                                    }
                                                                    else {
                                                                        alert.alertInfo(response.body().getMessage());
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<ValueBalance.RetrofitResponse> call, Throwable t) {
                                                                    alert.alertInfo(t.getMessage());
                                                                }
                                                            }
                                                    );
                                                }
                                                else {
                                                    alert.alertInfo(response.body().getMessage());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ValueResponse> call, Throwable t) {
                                                alert.alertInfo(t.getMessage());
                                            }
                                        }
                                );
                            }
                            catch (NullPointerException e){
                                alert.alertInfo(e.getMessage());
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Anda belum memilih jenis kendaraan anda", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        return mainView;
    }
}
