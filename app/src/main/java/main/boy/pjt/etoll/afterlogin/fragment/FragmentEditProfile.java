package main.boy.pjt.etoll.afterlogin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.values.ValueBalance;
import main.boy.pjt.etoll.values.ValueResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Boy Panjaitan on 08/06/2018.
 */

public class FragmentEditProfile extends Fragment {

    EditText profileName;
    EditText profileEmail;
    EditText profilePhone;
    EditText profilePass;
    LinearLayout saveChanges;
    MySession session;
    MyAlert alert;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ((CoreActivity)getActivity()).getSupportActionBar().setTitle("Edit Profile");

        profileName     = view.findViewById(R.id.profile_name);
        profileEmail    = view.findViewById(R.id.profile_email);
        profilePhone    = view.findViewById(R.id.profile_phone);
        profilePass     = view.findViewById(R.id.profile_password);
        saveChanges     = view.findViewById(R.id.simpan_perubahan);
        session         = new MySession(getContext());
        alert           = new MyAlert(getContext(), false);

        saveChanges.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateProfile();
                    }
                }
        );

        profilePhone.setText(session.getPHONE());
        profileEmail.setText(session.getEMAIL());
        profileName.setText(session.getNAME());
        profilePass.setText(session.getPASSWORD());

        return view;
    }

    private void updateProfile(){
        final String phone    = profilePhone.getText().toString();
        final String email    = profileEmail.getText().toString();
        final String name     = profileName.getText().toString();
        final String password = profilePass.getText().toString();

        alert.loaderStart();
        MyRetrofitInterface retrofitInterface   = MyRetrofit.getClient().create(MyRetrofitInterface.class);
        Call<ValueResponse> call     = retrofitInterface.updateProfile(session.getCOSTUMER_ID(), name, password, email, phone);

        call.enqueue(
                new Callback<ValueResponse>() {
                    @Override
                    public void onResponse(Call<ValueResponse> call, Response<ValueResponse> response) {
                        alert.loaderStop();
                        if (response.body().getStatus().equals(MyConstant.System.responseSuccess)){
                            Toast.makeText(getContext(), "Profil berhasil diperbaharui", Toast.LENGTH_LONG).show();

                            session.setEMAIL(email);
                            session.setNAME(name);
                            session.setPASSWORD(password);
                            session.setPHONE(phone);

                            ((CoreActivity)getActivity()).startFragment(new FragmentProfile());
                        }
                        else {
                            alert.alertInfo(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ValueResponse> call, Throwable t) {
                        alert.loaderStop();
                        alert.alertInfo(t.getMessage());
                    }
                }
        );
    }
}
