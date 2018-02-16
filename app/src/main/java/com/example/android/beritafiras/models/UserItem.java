package com.example.android.beritafiras.models;

import com.google.gson.annotations.SerializedName;

public class UserItem{

	@SerializedName("access")
	private String access;

	@SerializedName("insert_date")
	private String insertDate;

	@SerializedName("last_active_date")
	private String lastActiveDate;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("fullname")
	private String fullname;

	@SerializedName("update_date")
	private String updateDate;

	@SerializedName("username")
	private String username;

	public void setAccess(String access){
		this.access = access;
	}

	public String getAccess(){
		return access;
	}

	public void setInsertDate(String insertDate){
		this.insertDate = insertDate;
	}

	public String getInsertDate(){
		return insertDate;
	}

	public void setLastActiveDate(String lastActiveDate){
		this.lastActiveDate = lastActiveDate;
	}

	public String getLastActiveDate(){
		return lastActiveDate;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setFullname(String fullname){
		this.fullname = fullname;
	}

	public String getFullname(){
		return fullname;
	}

	public void setUpdateDate(String updateDate){
		this.updateDate = updateDate;
	}

	public String getUpdateDate(){
		return updateDate;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"UserItem{" + 
			"access = '" + access + '\'' + 
			",insert_date = '" + insertDate + '\'' + 
			",last_active_date = '" + lastActiveDate + '\'' + 
			",id_user = '" + idUser + '\'' + 
			",fullname = '" + fullname + '\'' + 
			",update_date = '" + updateDate + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}