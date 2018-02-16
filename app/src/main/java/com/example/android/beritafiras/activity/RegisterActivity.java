package com.example.android.beritafiras.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.beritafiras.R;
import com.example.android.beritafiras.helpers.MyFunction;
import com.example.android.beritafiras.helpers.MyValidation;
import com.example.android.beritafiras.models.ResponseBerita;
import com.example.android.beritafiras.models.ResponseLogin;
import com.example.android.beritafiras.network.ApiServices;
import com.example.android.beritafiras.network.InitRetrofit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends MyFunction {

    @BindView(R.id.etNama)
    EditText etNama;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRePassword)
    EditText etRePassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    MyValidation myValidation;
    ProgressDialog loading;
    Context mContext;
    View parentView;
    @BindView(R.id.rlLayout)
    RelativeLayout rlLayout;

    String userId, flag;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnChange)
    Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        myValidation = new MyValidation(this);
        parentView = rlLayout;

        Intent get = getIntent();
        userId = get.getStringExtra("USER").toString();
        flag = get.getStringExtra("FLAG").toString();

        if (flag.toString().equals("")) {
            etNama.setVisibility(View.VISIBLE);
            etUsername.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            etRePassword.setVisibility(View.VISIBLE);
        } else {
            etNama.setVisibility(View.GONE);
            etUsername.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            etRePassword.setVisibility(View.VISIBLE);

            tvTitle.setText("Change Password");
            etUsername.setText(userId);
            etUsername.setEnabled(false);
            etPassword.setHint("New Password");
            btnRegister.setVisibility(View.GONE);
            btnChange.setVisibility(View.VISIBLE);
        }

    }

    private void requestChange() {
        loading = ProgressDialog.show(this, null, "Loading...", true, false);

        String usrc = etUsername.getText().toString();
        String psdc = etPassword.getText().toString();

        ApiServices api = InitRetrofit.getInstance();

        Call<ResponseBerita> changeCall = api.requestChange(usrc, psdc);
        changeCall.enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        loading.dismiss();
                        Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        finish();
                    } else {
                        if (response.body().getCode() == 400) {
                            Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            loading.dismiss();
                        } else {
                            Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            loading.dismiss();
                        }
                    }
                } else {
                    Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {
                loading.dismiss();
                Snackbar.make(parentView, "Error connection, please check your internet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void requestRegister() {
        loading = ProgressDialog.show(this, null, "Loading...", true, false);
        String nama, user, password;

        nama = etNama.getText().toString();
        user = etUsername.getText().toString();
        password = etPassword.getText().toString();

        ApiServices api = InitRetrofit.getInstance();

        Call<ResponseLogin> registerCall = api.requestRegister(nama, user, password);
        registerCall.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful()) {
                    boolean status = response.body().isStatus();
                    // Kalau response status nya = true
                    if (status) {
                        String username = response.body().getUser().get(0).getUsername();

                        String msg = response.body().getMsg().toString();
                        Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        Intent kirim = new Intent(RegisterActivity.this, LoginActivity.class);
                        kirim.putExtra("USER", username);
                        startActivity(kirim);
                        finish();
                        loading.dismiss();
                    } else {

                        int code = response.body().getCode();
                        if (code != 403) {

                            String msg = response.body().getMsg().toString();
                            if (msg.equals("Username already exist.")) {
                                etUsername.setError(msg);
                                loading.dismiss();
                            } else if (msg.equals("Failed to registered.")) {
                                Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                loading.dismiss();
                            }

                        } else {
                            String msg = response.body().getMsg().toString();
                            Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            loading.dismiss();
                        }

                    }
                } else {
                    String msg = response.body().getMsg().toString();
                    Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Snackbar.make(parentView, "Error connection, please check your internet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                loading.dismiss();
            }
        });
    }

    @OnClick({R.id.btnRegister, R.id.btnChange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                String nama, user, password, repassword;

                nama = etNama.getText().toString();
                user = etUsername.getText().toString();
                password = etPassword.getText().toString();
                repassword = etRePassword.getText().toString();

                if (!myValidation.isEmptyField(nama)) {
                    etNama.setError("Fill your fullname in here.");
                } else if (!myValidation.isEmptyField(user)) {
                    etUsername.setError("Fill your username in here.");
                } else if (!myValidation.isEmptyField(password)) {
                    etPassword.setError("Fill your password in here.");
                } else if (!myValidation.isEmptyField(repassword)) {
                    etRePassword.setError("Re-type password in here.");
                } else if (!myValidation.isMatch(password, repassword)) {
                    etRePassword.setError("Password not match. Try again.");
                } else {
                    requestRegister();
                }
                break;
            case R.id.btnChange:
                String psd = etPassword.getText().toString();
                String rpsd= etRePassword.getText().toString();

                if (!myValidation.isEmptyField(psd)) {
                    etPassword.setError("Fill your new password in here.");
                } else if (!myValidation.isEmptyField(rpsd)) {
                    etRePassword.setError("Re-type password in here.");
                } else if (!myValidation.isMatch(psd, rpsd)) {
                    etRePassword.setError("Password not match. Try again.");
                } else {
                    requestChange();
                }
                break;
        }
    }
}
