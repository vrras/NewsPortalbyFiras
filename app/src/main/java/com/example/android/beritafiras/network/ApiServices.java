package com.example.android.beritafiras.network;

import com.example.android.beritafiras.models.ResponseBerita;
import com.example.android.beritafiras.models.ResponseLogin;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Firas Luthfi on 2/12/2018.
 */

public interface ApiServices {

    @GET("get.php")
    Call<ResponseBerita>requestBerita();

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseLogin> requestLogin(@Field("username") String username,
                                     @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseLogin> requestRegister(@Field("fullname") String fullname,
                                       @Field("username") String username,
                                       @Field("password") String password);

//    @FormUrlEncoded
//    @POST("insert.php")
//    Call<ResponseBerita> postBerita(@Field("title") String title,
//                                    @Field("content") String content,
//                                    @Field("id_user") int id_user);

    @Multipart
    @POST("insert.php")
    Call<ResponseBerita> postBerita(@Part("title") String title,
                                    @Part("content") String content,
                                    @Part MultipartBody.Part file,
                                    @Part("file") RequestBody name,
                                    @Part("id_user") int id_user);

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseBerita> requestDelete(@Field("id_berita") int id_berita);

    @Multipart
    @POST("update.php")
    Call<ResponseBerita> requestUpdateFoto(@Part("id_berita") int id_berita,
                                       @Part("title") String title,
                                       @Part("content") String content,
                                       @Part MultipartBody.Part file,
                                       @Part("file") RequestBody name);

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseBerita> requestUpdate(@Field("id_berita") String id_berita,
                                       @Field("title") String title,
                                       @Field("content") String content,
                                       @Field("flag") String flag);

    @FormUrlEncoded
    @POST("updateUser.php")
    Call<ResponseBerita> requestChange(@Field("username") String username,
                                       @Field("password") String password);
}
