package fr.paladium.core.authentication.microsoft.response;

import com.google.gson.annotations.SerializedName;

public class MicrosoftUserAuthentication {
	
	@SerializedName("Token")
	private String token;
	
	public String getToken() {
		return this.token;
	}
}
