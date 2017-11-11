package com.example.shoujibao;

import android.graphics.Bitmap;

public class News {
	public String id;
	public String title;
	public String content;
	public String mark;
	public Bitmap image;
	
	public News(String id, String title, String content, Bitmap image,String mark) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.title = title;
		this.content = content;
		this.image = image;
		this.mark = mark;
	}

}
