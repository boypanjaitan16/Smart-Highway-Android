package main.boy.pjt.etoll.beforelogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.request.RequestLogin;
import main.boy.pjt.etoll.response.ResponseProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    MySession session;
    Button loginBtn;
    EditText usernameField;
    EditText passwordField;
    TextView registerBtn;
    MyAlert alert;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session         = new MySession(this);
        progressDialog  = new ProgressDialog(this);

        if (session.getIS_LOGIN()){
            startActivity(new Intent(LoginActivity.this, CoreActivity.class));
            finish();
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("NEW TOKEN",newToken);
                session.setFbmToken(newToken);

            }
        });

        alert           = new MyAlert(this, false);
        registerBtn     = findViewById(R.id.go_register);
        loginBtn        = findViewById(R.id.btn_login);
        usernameField   = findViewById(R.id.username);
        passwordField   = findViewById(R.id.password);

        loginBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.setMessage(MyConstant.System.loadingMessage);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        doLogin();
                    }
                }
        );

        registerBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    }
                }
        );
    }

    private void doLogin(){
        final String valPass    = passwordField.getText().toString();
        String valUsername      = usernameField.getText().toString();

        RequestLogin body       = new RequestLogin(valUsername, valPass);
        MyRetrofitInterface retrofitInterface   = MyRetrofit.getClient().create(MyRetrofitInterface.class);
        Call<ResponseProfile.RetrofitResponse> call     = retrofitInterface.loginField(valUsername, valPass, session.getFbmToken());

        call.enqueue(
                new Callback<ResponseProfile.RetrofitResponse>() {
                    @Override
                    public void onResponse(Call<ResponseProfile.RetrofitResponse> call, Response<ResponseProfile.RetrofitResponse> response) {
                        progressDialog.dismiss();
                        if (response.body().getStatus().equals(MyConstant.System.responseSuccess)){

                            ResponseProfile.Values values   = response.body().getData();

                            session.setCOSTUMER_ID(values.getUsername());
                            session.setNAME(values.getName());
                            session.setEMAIL(values.getEmail());
                            session.setPHONE(values.getPhone());
                            session.setPASSWORD(values.getPassword());
                            session.setIS_LOGIN(true);

                            Log.d("PROFILE", values.toString());

                            startActivity(new Intent(LoginActivity.this, CoreActivity.class));
                            finish();
                        }
                        else {
                            alert.alertInfoTitle("Gagal Login", response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseProfile.RetrofitResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        alert.alertInfo(t.getMessage());
                        t.printStackTrace();
                    }
                }
        );

    }
}
