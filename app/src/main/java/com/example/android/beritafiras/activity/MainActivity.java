package com.example.android.beritafiras.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.android.beritafiras.R;
import com.example.android.beritafiras.adapter.AdapterBerita;
import com.example.android.beritafiras.helpers.MyFunction;
import com.example.android.beritafiras.helpers.SharedPrefManager;
import com.example.android.beritafiras.models.BeritaItem;
import com.example.android.beritafiras.models.ResponseBerita;
import com.example.android.beritafiras.network.ApiServices;
import com.example.android.beritafiras.network.InitRetrofit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends MyFunction {

    // Deklarasi LOG TAG
    private final static String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.rvListBerita)
    RecyclerView rvListBerita;
    @BindView(R.id.swlayout)
    SwipeRefreshLayout swlayout;
    @BindView(R.id.llLayout)
    LinearLayout llLayout;
    // Deklarasi Widget
    private RecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;
    Context mContext;
    View parentView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mContext = this;
        sharedPrefManager = new SharedPrefManager(this);
        parentView = llLayout;

        // Inisialisasi Widget
        recyclerView = (RecyclerView) findViewById(R.id.rvListBerita);
        // RecyclerView harus pakai Layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Eksekusi method
        showBerita();

        // Mengeset properti warna yang berputar pada SwipeRefreshLayout
        swlayout.setColorSchemeResources(R.color.accent, R.color.primary);

        // Mengeset listener yang akan dijalankan saat layar di refresh/swipe
        swlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        showBerita();
                        // Berhenti berputar/refreshing
                        swlayout.setRefreshing(false);

                    }
                }, 4000);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){

            new AlertDialog.Builder(this)
                    .setTitle("Berita Firas")
                    .setMessage("Anda yakin ingin keluar?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//                            sharedPrefManager = new SharedPrefManager(getApplicationContext());
//                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ALREADY_LOGINREADER, false);
//                            sharedPrefManager.saveSPString(SharedPrefManager.SP_IDUSER, "");
//                            sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, "");
//                            sharedPrefManager.saveSPString(SharedPrefManager.SP_FULLNAME, "");

//                            simpleIntent(LoginActivity.class);
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
        showBerita();
        progressDialog = ProgressDialog.show(mContext, null, "Loading...", true, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private void showBerita() {

        ApiServices api = InitRetrofit.getInstance();
        // Siapkan request
        Call<ResponseBerita> beritaCall = api.requestBerita();
        // Kirim request
        beritaCall.enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {

                // Pastikan response Sukses
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());
                    // tampung data response body ke variable
                    List<BeritaItem> data_berita = response.body().getBerita();
                    boolean status = response.body().getStatus();
                    // Kalau response status nya = true
                    if (status) {
                        // Buat Adapter untuk recycler view
                        AdapterBerita adapter = new AdapterBerita(MainActivity.this, data_berita);
                        recyclerView.setAdapter(adapter);
                        Snackbar.make(parentView, "News up to date.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        progressDialog.dismiss();
                    } else {
                        // kalau tidak true
                        Snackbar.make(parentView, "Not found news now.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        progressDialog.dismiss();
                    }
                }else{
                    progressDialog.dismiss();
                    Snackbar.make(parentView, "Not found news now.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {
                Snackbar.make(parentView, "Error connection, please check your internet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            sharedPrefManager = new SharedPrefManager(getApplicationContext());
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ALREADY_LOGINREADER, false);
            sharedPrefManager.saveSPString(SharedPrefManager.SP_IDUSER, "");
            sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, "");
            sharedPrefManager.saveSPString(SharedPrefManager.SP_FULLNAME, "");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            Snackbar.make(parentView, "Logged out.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
