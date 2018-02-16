package com.example.android.beritafiras.models;

import com.google.gson.annotations.SerializedName;

public class BeritaItem{

	@SerializedName("foto")
	private String foto;

	@SerializedName("publisher")
	private String publisher;

	@SerializedName("title")
	private String title;

	@SerializedName("id_berita")
	private String idBerita;

	@SerializedName("publish_date")
	private String publishDate;

	@SerializedName("content")
	private String content;

	@SerializedName("update_date")
	private String updateDate;

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setPublisher(String publisher){
		this.publisher = publisher;
	}

	public String getPublisher(){
		return publisher;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setIdBerita(String idBerita){
		this.idBerita = idBerita;
	}

	public String getIdBerita(){
		return idBerita;
	}

	public void setPublishDate(String publishDate){
		this.publishDate = publishDate;
	}

	public String getPublishDate(){
		return publishDate;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setUpdateDate(String updateDate){
		this.updateDate = updateDate;
	}

	public String getUpdateDate(){
		return updateDate;
	}

	@Override
 	public String toString(){
		return 
			"BeritaItem{" + 
			"foto = '" + foto + '\'' + 
			",publisher = '" + publisher + '\'' + 
			",title = '" + title + '\'' + 
			",id_berita = '" + idBerita + '\'' + 
			",publish_date = '" + publishDate + '\'' + 
			",content = '" + content + '\'' + 
			",update_date = '" + updateDate + '\'' + 
			"}";
		}
}