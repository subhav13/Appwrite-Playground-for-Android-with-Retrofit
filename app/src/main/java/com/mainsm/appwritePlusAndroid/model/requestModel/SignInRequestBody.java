package com.mainsm.appwritePlusAndroid.model.requestModel;

import com.google.gson.annotations.SerializedName;

public class SignInRequestBody{

	public SignInRequestBody(String email, String password) {
		this.password = password;
		this.email = email;
	}

	@SerializedName("password")
	private String password;

	@SerializedName("email")
	private String email;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"SignInRequestBody{" + 
			"password = '" + password + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}