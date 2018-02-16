package com.example.android.beritafiras.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.beritafiras.R;
import com.example.android.beritafiras.helpers.SharedPrefManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailAdminActivity extends AppCompatActivity {

    String idBerita, title, content, publisher, publishdate, foto;
    @BindView(R.id.ivGambarBerita)
    ImageView ivGambarBerita;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tvTglTerbit)
    TextView tvTglTerbit;
    @BindView(R.id.tvPenulis)
    TextView tvPenulis;
    @BindView(R.id.wvKontenBerita)
    WebView wvKontenBerita;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    Context mContext;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_admin);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //enable button up
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        sharedPrefManager = new SharedPrefManager(this);
        mContext = this;

        addFormData();
    }

    private void addFormData() {
        Intent terima = getIntent();

        idBerita = terima.getStringExtra("ID");
        title = terima.getStringExtra("TITLE");
        content = terima.getStringExtra("CONTENT");
        publisher = terima.getStringExtra("PUBLISHER");
        publishdate = terima.getStringExtra("TGL");
        foto = terima.getStringExtra("FOTO");

        // Set judul actionbar / toolbar
        getSupportActionBar().setTitle(title);

        tvPenulis.setText("Oleh : " + publisher);
        tvTglTerbit.setText(publishdate);
        Picasso.with(DetailAdminActivity.this).load(foto).into(ivGambarBerita);
        // Set isi berita sebagai html ke WebView
        wvKontenBerita.getSettings().setJavaScriptEnabled(true);
        wvKontenBerita.loadData(content, "text/html; charset=utf-8", "UTF-8");
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        Intent kirim = new Intent(DetailAdminActivity.this, CrudActivity.class);

        kirim.putExtra("ID", idBerita.toString());
        kirim.putExtra("TITLE", title.toString());
        kirim.putExtra("CONTENT", content.toString());
        kirim.putExtra("FOTO", foto.toString());

        startActivity(kirim);
        finish();
    }
}
