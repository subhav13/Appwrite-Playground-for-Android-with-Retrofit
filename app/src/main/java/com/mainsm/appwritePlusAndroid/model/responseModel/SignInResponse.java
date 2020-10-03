package com.mainsm.appwritePlusAndroid.model.responseModel;

import com.google.gson.annotations.SerializedName;

public class SignInResponse{

	@SerializedName("expire")
	private int expire;

	@SerializedName("type")
	private int type;

	@SerializedName("$id")
	private String id;

	public int getExpire(){
		return expire;
	}

	public int getType(){
		return type;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"SignInResponse{" + 
			"expire = '" + expire + '\'' + 
			",type = '" + type + '\'' + 
			",$id = '" + id + '\'' + 
			"}";
		}
}