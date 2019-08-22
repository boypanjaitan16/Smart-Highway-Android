package main.boy.pjt.etoll.afterlogin.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.beforelogin.LoginActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.response.ResponseBalance;
import main.boy.pjt.etoll.response.ResponseDefault;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Boy Panjaitan on 06/06/2018.
 */

public class FragmentProfile extends Fragment {

    MyRetrofitInterface retrofitInterface;
    TextView profileName;
    TextView profileEmail;
    TextView profilePhone;
    TextView saldoValue;
    TextView btnEditProfile;
    LinearLayout saldoRow;
    LinearLayout logout;
    MySession session;
    MyAlert alert;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_profile, container, false);

        ((CoreActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_profile);

        profileName     = view.findViewById(R.id.profile_name);
        profileEmail    = view.findViewById(R.id.profile_email);
        profilePhone    = view.findViewById(R.id.profile_phone);
        saldoValue      = view.findViewById(R.id.saldo_value);
        saldoRow        = view.findViewById(R.id.saldo_row);
        logout          = view.findViewById(R.id.logout);
        btnEditProfile  = view.findViewById(R.id.btn_edit_profile);
        session         = new MySession(getContext());
        alert           = new MyAlert(getContext(), false);
        retrofitInterface   = MyRetrofit.getClient().create(MyRetrofitInterface.class);


        profileName.setText(session.getNAME());
        profilePhone.setText(session.getPHONE());
        profileEmail.setText(session.getEMAIL());

        logout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.alertWithConfirm("Apakah anda yakin ingin keluar ?",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        session.setIS_LOGIN(false);
                                        startActivity(new Intent(getContext(), LoginActivity.class));
                                        ((CoreActivity)getActivity()).finish();
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
        );

        btnEditProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((CoreActivity)getActivity()).startFragment(new FragmentEditProfile());
                    }
                }
        );

        saldoRow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final View layout = getLayoutInflater().inflate(R.layout.layout_topup_balance, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setView(layout)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText balance    = layout.findViewById(R.id.balance);
                                        if (!balance.getText().toString().equals("")) {
                                            alert.loaderStart();
                                            int nominal = Integer.valueOf(balance.getText().toString());

                                            Call<ResponseDefault> call = retrofitInterface.topUpBalance(session.getCOSTUMER_ID(), nominal);
                                            call.enqueue(
                                                    new Callback<ResponseDefault>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                                                            alert.loaderStop();
                                                            if (response.body().getStatus().equals(MyConstant.System.responseSuccess)) {
                                                                alert.alertInfoTitle("Top-up Saldo", "Terimakasih, permintaan anda sedang dalam proses verifikasi dari pihak kami");
                                                            }
                                                            else {
                                                                alert.alertInfo(response.body().getMessage());
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseDefault> call, Throwable t) {
                                                            alert.loaderStop();
                                                            alert.alertInfo(t.getMessage());
                                                        }
                                                    }
                                            );
                                        }
                                        else{
                                            alert.alertInfo("Anda harus mengisikan nominal top-up yang diinginkan");
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
        );

        loadBalance();

        return view;
    }



    private void loadBalance(){
        Call<ResponseBalance.RetrofitResponse> call     = retrofitInterface.getBalance(session.getCOSTUMER_ID());

        call.enqueue(
                new Callback<ResponseBalance.RetrofitResponse>() {
                    @Override
                    public void onResponse(Call<ResponseBalance.RetrofitResponse> call, Response<ResponseBalance.RetrofitResponse> response) {
                        if (response.body().getStatus().equals(MyConstant.System.responseSuccess)){
                            ResponseBalance.Values values = response.body().getData();
                            saldoValue.setText(new StringBuilder().append("Rp ").append(values.getBalance()).toString());
                        }
                        else{
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
}
