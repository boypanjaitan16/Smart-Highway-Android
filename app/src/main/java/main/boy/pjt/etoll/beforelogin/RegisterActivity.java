package main.boy.pjt.etoll.beforelogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import main.boy.pjt.etoll.R;
import main.boy.pjt.etoll.afterlogin.CoreActivity;
import main.boy.pjt.etoll.helper.MyAlert;
import main.boy.pjt.etoll.helper.MyConstant;
import main.boy.pjt.etoll.helper.MyRetrofit;
import main.boy.pjt.etoll.helper.MyRetrofitInterface;
import main.boy.pjt.etoll.helper.MySession;
import main.boy.pjt.etoll.values.ValueResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ProgressDialog loading;
    MyAlert alert;
    Button registerBtn;
    EditText usernameField;
    EditText passwordField;
    EditText emailField;
    EditText nameField;
    EditText phoneField;
    MySession session;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loading         = new ProgressDialog(this);
        alert           = new MyAlert(this, false);
        session         = new MySession(this);
        progressDialog  = new ProgressDialog(this);

        usernameField   = findViewById(R.id.username);
        passwordField   = findViewById(R.id.password);
        emailField      = findViewById(R.id.email);
        nameField       = findViewById(R.id.name);
        phoneField      = findViewById(R.id.phone);
        registerBtn     = findViewById(R.id.btn_register);

        loading.setCancelable(false);
        registerBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.setMessage(MyConstant.System.loadingMessage);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        doRegister();
                    }
                }
        );
    }

    private void doRegister(){
        final String valUsername  = usernameField.getText().toString();
        final String valPassword  = passwordField.getText().toString();
        final String valEmail     = emailField.getText().toString();
        final String valName      = nameField.getText().toString();
        final String valPhone     = phoneField.getText().toString();

        MyRetrofitInterface retrofitInterface   = MyRetrofit.getClient().create(MyRetrofitInterface.class);
        Call<ValueResponse> call                = retrofitInterface.register(valUsername, valName, valPassword, valEmail, valPhone);

        call.enqueue(
                new Callback<ValueResponse>() {
                    @Override
                    public void onResponse(Call<ValueResponse> call, Response<ValueResponse> response) {
                        progressDialog.dismiss();
                        if (response.body().getStatus().equals(MyConstant.System.responseSuccess)){
                            session.setCOSTUMER_ID(valUsername);
                            session.setNAME(valName);
                            session.setEMAIL(valEmail);
                            session.setPHONE(valPhone);
                            session.setPASSWORD(valPassword);
                            session.setIS_LOGIN(true);

                            Toast.makeText(RegisterActivity.this, "Sukses mendaftar", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, CoreActivity.class));
                            finish();
                        }
                        else{
                            alert.alertInfoTitle("Gagal Mendaftar", response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ValueResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        alert.alertInfo(t.getMessage());
                    }
                }
        );
    }
}
