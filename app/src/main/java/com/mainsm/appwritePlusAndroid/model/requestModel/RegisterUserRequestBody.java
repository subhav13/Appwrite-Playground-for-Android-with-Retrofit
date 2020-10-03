package com.mainsm.appwritePlusAndroid.model.requestModel;

import com.google.gson.annotations.SerializedName;

public class RegisterUserRequestBody{

	public RegisterUserRequestBody(String name, String email, String password) {
		this.password = password;
		this.name = name;
		this.email = email;
	}

	@SerializedName("password")
	private String password;

	@SerializedName("name")
	private String name;

	@SerializedName("email")
	private String email;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
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
			"RegisterUserRequestBody{" + 
			"password = '" + password + '\'' + 
			",name = '" + name + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}