package com.example.android.beritafiras.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.beritafiras.R;
import com.example.android.beritafiras.adapter.AdapterAdmin;
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

public class AdminActivity extends MyFunction {

    // Deklarasi LOG TAG
    private final static String TAG = AdminActivity.class.getSimpleName();
    @BindView(R.id.rvListBerita)
    RecyclerView rvListBerita;
    @BindView(R.id.swlayout)
    SwipeRefreshLayout swlayout;
    @BindView(R.id.rlLayout)
    RelativeLayout rlLayout;
    // Deklarasi Widget
    private RecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;
    Context mContext;
    ProgressDialog pg;
    View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);

        mContext = this;
        parentView = rlLayout;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent kirim = new Intent(AdminActivity.this, CrudActivity.class);

                kirim.putExtra("TITLE", "");

                startActivity(kirim);
//                finish();
            }
        });

        sharedPrefManager = new SharedPrefManager(this);

        // Inisialisasi Widget
        recyclerView = (RecyclerView) findViewById(R.id.rvListBerita);
        // RecyclerView harus pakai Layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Eksekusi method
//        showBerita();

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
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setTitle("Berita Firas")
                    .setMessage("Anda yakin ingin keluar?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sharedPrefManager = new SharedPrefManager(getApplicationContext());
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ALREADY_LOGINADMIN, false);
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_IDUSER, "");
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, "");
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_FULLNAME, "");

                            simpleIntent(LoginActivity.class);
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
        pg = ProgressDialog.show(mContext, null, "Loading...", true, false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("SIKLUS", "onPause()");

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("SIKLUS", "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("SIKLUS", "onDestroy()");

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
                        AdapterAdmin adapter = new AdapterAdmin(AdminActivity.this, data_berita);
                        recyclerView.setAdapter(adapter);
                        Snackbar.make(parentView, "News up to date.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        pg.dismiss();
                    } else {
                        pg.dismiss();
                        // kalau tidak true
                        Snackbar.make(parentView, "Not found news now.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(parentView, "Not found news now.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    pg.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {
                Snackbar.make(parentView, "Error connection, please check your internet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                pg.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logouta) {
            sharedPrefManager = new SharedPrefManager(getApplicationContext());
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_ALREADY_LOGINADMIN, false);
            sharedPrefManager.saveSPString(SharedPrefManager.SP_IDUSER, "");
            sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, "");
            sharedPrefManager.saveSPString(SharedPrefManager.SP_FULLNAME, "");
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            Snackbar.make(parentView, "Logged out.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
