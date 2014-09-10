package com.sinotrans.samsung.activity;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
/*
 * 获得登陆ID
 */
public class GetLogNameActivity extends Application{
	
	public 	 String username;
	public     String position;
	public     String versioncode;
	public     String user;
	@Override
	 public void onCreate() {
	 // TODO Auto-generated method stub
	 super.onCreate();
//	 setNum("5042050");
	 user="5042050";
	 versioncode="0.1";
	 position="";
	 username="管理员";
	 }
	 public String getNum() {
	 return user;
	 }

	 public void setNum(String user) {
	 this.user = user;
	 }
		public String getVersioncode() {
			return versioncode;
		}
		public void setVersioncode(String versioncode) {
			this.versioncode = versioncode;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
	 
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}

}

