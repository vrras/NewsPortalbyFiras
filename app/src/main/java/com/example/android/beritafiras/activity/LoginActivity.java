package com.example.android.beritafiras.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.beritafiras.R;
import com.example.android.beritafiras.config.MyConstant;
import com.example.android.beritafiras.helpers.MyFunction;
import com.example.android.beritafiras.helpers.MyValidation;
import com.example.android.beritafiras.helpers.SharedPrefManager;
import com.example.android.beritafiras.models.ResponseLogin;
import com.example.android.beritafiras.network.ApiServices;
import com.example.android.beritafiras.network.InitRetrofit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MyFunction {

    //TAG
    private final static String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    ProgressDialog loading;
    @BindView(R.id.etUsername)
    EditText etUsername;
    Context mContext;
    String username, password;
    SharedPrefManager sharedPrefManager;
    MyValidation myValidation;
    @BindView(R.id.tvPass)
    TextView tvPass;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    View parentView;
    @BindView(R.id.rlLayout)
    RelativeLayout rlLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mContext = this;
        sharedPrefManager = new SharedPrefManager(this);
        myValidation = new MyValidation(this);

        /**
         * Parent View
         */
        parentView = rlLayout;

        Intent terima = getIntent();
        if (terima != null) {
            String usernameBaru = terima.getStringExtra("USER");
            etUsername.setText(usernameBaru);
        }

        tvVersion.setText(MyConstant.VERSION);

        Log.d("d", "onCreate");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setTitle("Berita Firas")
                    .setMessage("Apa anda yakin ingin keluar dari aplikasi?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("d", "onResume");
        // Code berikut berfungsi untuk mengecek session, Jika session true ( sudah login )
        // maka langsung memulai MainActivity.
        if (sharedPrefManager.getSpAlreadyLoginAdmin() == true) {

            if (sharedPrefManager.getSpAlreadyLoginReader() == true) {
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ALREADY_LOGINADMIN, false);
                startActivity(new Intent(mContext, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            } else {
                startActivity(new Intent(mContext, AdminActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        } else {
            if (sharedPrefManager.getSpAlreadyLoginReader() == true) {
                startActivity(new Intent(mContext, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("d", "onPause");
    }

    @OnClick({R.id.btnLogin, R.id.btnRegister, R.id.tvPass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                String user = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (!myValidation.isEmptyField(user)) {
                    etUsername.setError("Fill your username in here.");
                } else if (!myValidation.isEmptyField(password)) {
                    etPassword.setError("Fill your password in here.");
                } else {
                    loading = ProgressDialog.show(mContext, null, "Logging in...", true, false);
                    requestLogin();
                }
                break;
            case R.id.btnRegister:
                Intent kirimReg = new Intent(mContext, RegisterActivity.class);
                kirimReg.putExtra("USER", "");
                kirimReg.putExtra("FLAG", "");
                startActivity(kirimReg);
                break;
            case R.id.tvPass:
                String usr = etUsername.getText().toString();

                if(!myValidation.isEmptyField(usr)){
                    etUsername.setError("Fill your username in here, than try again to click forgot password.");
                }else{
                    Intent kirim = new Intent(LoginActivity.this, RegisterActivity.class);
                    kirim.putExtra("USER", etUsername.getText().toString());
                    kirim.putExtra("FLAG", "password");
                    startActivity(kirim);
                }
                break;
        }
    }

    private void requestLogin() {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        ApiServices api = InitRetrofit.getInstance();

        Call<ResponseLogin> loginCall = api.requestLogin(username, password);
        loginCall.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());

                    boolean status = response.body().isStatus();
                    // Kalau response status nya = true
                    if (status) {
                        String iduser = response.body().getUser().get(0).getIdUser();
                        String nama = response.body().getUser().get(0).getFullname();
                        String usernama = response.body().getUser().get(0).getUsername();
                        String access = response.body().getUser().get(0).getAccess();
                        //Login Success
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_IDUSER, iduser);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_FULLNAME, nama);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, usernama);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ACCESS, access);
                        // Shared Pref ini berfungsi untuk menjadi trigger session login
                        loading.dismiss();

                        if (sharedPrefManager.getSpAccess().equals("admin")) {
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ALREADY_LOGINADMIN, true);
                            startActivity(new Intent(mContext, AdminActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } else {
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ALREADY_LOGINREADER, true);
                            startActivity(new Intent(mContext, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }

                    } else {

                        int code = response.body().getCode();
                        if (code != 403) {

                            String msg = response.body().getMsg().toString();
                            if (msg.equals("Login failed.")) {
                                loading.dismiss();
                                Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }else if(msg.equals("Fill your username in here.")) {
                                loading.dismiss();
                                Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                etUsername.setError(msg);
                            } else{
                                loading.dismiss();
                                Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                etPassword.setError(msg);
                            }

                        } else {
                            loading.dismiss();
                            String msg = response.body().getMsg().toString();
                            Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    }
                } else {
                    Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    loading.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                loading.dismiss();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Snackbar.make(parentView, "Error connection, please check your internet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
