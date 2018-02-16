package com.example.android.beritafiras.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.beritafiras.R;
import com.example.android.beritafiras.config.MyConstant;
import com.example.android.beritafiras.helpers.MyFunction;
import com.example.android.beritafiras.helpers.MyValidation;
import com.example.android.beritafiras.helpers.SharedPrefManager;
import com.example.android.beritafiras.models.ResponseBerita;
import com.example.android.beritafiras.network.ApiServices;
import com.example.android.beritafiras.network.InitRetrofit;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrudActivity extends MyFunction {

    SharedPrefManager sharedPrefManager;

    /**
     * Context Variables
     */
    Context mContext;

    /**
     * Views
     */
    View parentView;

    @BindView(R.id.edt_title)
    EditText edtTitle;
    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.btnFile)
    Button btnFile;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.btn_insertdata)
    Button btnInsertdata;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    @BindView(R.id.btnhapus)
    Button btnhapus;
    @BindView(R.id.ln1)
    LinearLayout ln1;

    ProgressDialog progressDialog;

    String mediaPath;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    @BindView(R.id.fileName)
    TextView fileName;
    @BindView(R.id.lnVisible)
    LinearLayout lnVisible;

    File file;

    String id_berita, title, content, foto, msg;

    MyValidation myValidation;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyStoragePermissions(CrudActivity.this);
        setContentView(R.layout.activity_crud);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);

        mContext = this;

        sharedPrefManager = new SharedPrefManager(mContext);

        myValidation = new MyValidation(this);

        /**
         * Parent View
         */
        parentView = findViewById(R.id.ln1);

        Intent terima = getIntent();

        id_berita = terima.getStringExtra("ID");
        title = terima.getStringExtra("TITLE");
        content = terima.getStringExtra("CONTENT");
        foto = terima.getStringExtra("FOTO");

        if(title.toString().equals("")){
            btnInsertdata.setVisibility(View.VISIBLE);
        }else{
            btnInsertdata.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            btnhapus.setVisibility(View.VISIBLE);
            lnVisible.setVisibility(View.VISIBLE);
            fileName.setVisibility(View.VISIBLE);

            String[] split = foto.split("/");
            int cntFoto = split.length;

            edtTitle.setText(title.toString());
            edtContent.setText(content.toString());
            fileName.setText(split[cntFoto-2]+"/"+split[cntFoto-1]);
            Picasso.with(this).load(foto).into(imageView);
        }

    }

    @OnClick({R.id.btnFile, R.id.btn_insertdata, R.id.btnUpdate, R.id.btnhapus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnFile:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
                break;
            case R.id.btn_insertdata:
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();
                String  fn = fileName.getText().toString();

                if (!myValidation.isEmptyField(title)) {
                    edtTitle.setError("Fill news title in here.");
                }else if(!myValidation.isEmptyField(content)){
                    edtContent.setError("Fill news content in here.");
                }else if(!myValidation.isEmptyField(fn)){
                    Snackbar.make(view, "Choose news image first.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }else{
                    insertBerita();
                }
                break;
            case R.id.btnUpdate:
                updateBerita();
                break;
            case R.id.btnhapus:
                deleteBerita();
                break;
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void updateBerita() {
        progressDialog = ProgressDialog.show(mContext, null, "Uploading...", true, false);

        String fn = fileName.getText().toString();

        String[] ctsplit = fn.split("/");
        int cntSplit = ctsplit.length;

        if(cntSplit==2){
            String ttl, ctn;
            ttl = edtTitle.getText().toString();
            ctn = edtContent.getText().toString();

            ApiServices api = InitRetrofit.getInstance();

            Call<ResponseBerita> callUpdate = api.requestUpdate(id_berita, ttl, ctn, "noupload");
            callUpdate.enqueue(new Callback<ResponseBerita>() {
                @Override
                public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            finish();
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        assert response.body() != null;
                        Log.v("Response", response.body().toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBerita> call, Throwable t) {
                    Snackbar.make(parentView, "Error connection, please check your internet.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    progressDialog.dismiss();
                }
            });
        }else{
            // Map is used to multipart the file using okhttp3.RequestBody
            file = new File(mediaPath);

            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            String ttl, ctn;
            ttl = edtTitle.getText().toString();
            ctn = edtContent.getText().toString();

            ApiServices api = InitRetrofit.getInstance();

            Call<ResponseBerita> callUpdate = api.requestUpdateFoto(Integer.parseInt(id_berita), ttl, ctn, fileToUpload, filename);
            callUpdate.enqueue(new Callback<ResponseBerita>() {
                @Override
                public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            finish();
                            progressDialog.dismiss();
                        } else {
                            if (response.body().getCode() == 400) {
                                if(response.body().getMsg().equals("Failed updated.")){
                                    Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    progressDialog.dismiss();
                                }else{
                                    Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    progressDialog.dismiss();
                                }
                            } else {
                                if(response.body().getMsg().equals("Required field missing.")){
                                    Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    progressDialog.dismiss();
                                }else{
                                    Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    } else {
                        assert response.body() != null;
                        progressDialog.dismiss();
                        Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.v("Response", response.body().toString());
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
    }

    private void deleteBerita() {
        progressDialog = ProgressDialog.show(mContext, null, "Loading...", true, false);

        ApiServices api = InitRetrofit.getInstance();

        Call<ResponseBerita> callDelete = api.requestDelete(Integer.parseInt(id_berita));
        callDelete.enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus()){
                        msg = response.body().getMsg();
                        Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        finish();
                        progressDialog.dismiss();
                    }else{
                        if(response.body().getCode()==403){
                            msg = response.body().getMsg();
                            Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            progressDialog.dismiss();
                        }else{
                            if(response.body().getMsg().equals("Data not found.")){
                                msg = response.body().getMsg();
                                Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                progressDialog.dismiss();
                            }else{
                                msg = response.body().getMsg();
                                Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                progressDialog.dismiss();
                            }
                        }
                    }
                }else{
                    assert response.body() != null;
                    progressDialog.dismiss();
                    msg = response.body().getMsg();
                    Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.v("Response", response.body().toString());
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

    private void insertBerita() {
        progressDialog = ProgressDialog.show(mContext, null, "Uploading...", true, false);
        // Map is used to multipart the file using okhttp3.RequestBody
        file = new File(mediaPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        int id_user = Integer.parseInt(sharedPrefManager.getSpIduser().toString());

        ApiServices getResponse = InitRetrofit.getInstance();
        Call<ResponseBerita> call = getResponse.postBerita(title, content, fileToUpload, filename, id_user);
        call.enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        finish();
                        progressDialog.dismiss();
                    } else {
                        if (response.body().getCode() == 400) {
                            Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            progressDialog.dismiss();
                        } else {
                            if (response.body().getMsg().equals("Forbidden.")) {
                                Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                progressDialog.dismiss();
                            } else {
                                Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                progressDialog.dismiss();
                            }
                        }
                    }
                } else {
                    assert response.body() != null;
                    progressDialog.dismiss();
                    Snackbar.make(parentView, response.body().getMsg().toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.v("Response", response.body().toString());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                String[] nameFl = mediaPath.toString().split("/");
                int jum = nameFl.length;
                // Set the Image in ImageView for Previewing the Media
                imageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();
                lnVisible.setVisibility(View.VISIBLE);
//                fileName.setText(mediaPath);
                fileName.setText(nameFl[jum - 1]);
                fileName.setVisibility(View.VISIBLE);

            } else {
                Snackbar.make(parentView, "You haven't picked Image.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } catch (Exception e) {
            Snackbar.make(parentView, "Something went wrong.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }
}
