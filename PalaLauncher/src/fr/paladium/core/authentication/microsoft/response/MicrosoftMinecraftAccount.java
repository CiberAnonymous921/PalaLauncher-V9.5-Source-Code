package fr.paladium.core.authentication.microsoft.response;

import com.google.gson.annotations.SerializedName;

public class MicrosoftMinecraftAccount {
	
	@SerializedName("access_token")
	private String accessToken;
	
	public String getAccessToken() {
		return this.accessToken;
	}
}
