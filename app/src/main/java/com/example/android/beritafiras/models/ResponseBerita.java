package com.example.android.beritafiras.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBerita{

	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private int code;

	@SerializedName("berita")
	private List<BeritaItem> berita;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setBerita(List<BeritaItem> berita){
		this.berita = berita;
	}

	public List<BeritaItem> getBerita(){
		return berita;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponseBerita{" + 
			"msg = '" + msg + '\'' + 
			",code = '" + code + '\'' + 
			",berita = '" + berita + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}