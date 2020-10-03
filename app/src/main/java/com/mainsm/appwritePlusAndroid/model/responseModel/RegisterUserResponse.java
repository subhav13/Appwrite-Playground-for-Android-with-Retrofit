package com.mainsm.appwritePlusAndroid.model.responseModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class RegisterUserResponse{

	@SerializedName("roles")
	private List<String> roles;

	@SerializedName("name")
	private String name;

	@SerializedName("registration")
	private int registration;

	@SerializedName("email")
	private String email;

	@SerializedName("$id")
	private String id;

	public List<String> getRoles(){
		return roles;
	}

	public String getName(){
		return name;
	}

	public int getRegistration(){
		return registration;
	}

	public String getEmail(){
		return email;
	}

	public String getId(){
		return id;
	}

	@NotNull
	@Override
 	public String toString(){
		return 
			"RegisterUserResponse{" + 
			"roles = '" + roles + '\'' + 
			",name = '" + name + '\'' + 
			",registration = '" + registration + '\'' + 
			",email = '" + email + '\'' + 
			",$id = '" + id + '\'' + 
			"}";
		}
}