package fr.paladium.core.authentication.microsoft.response;

import com.google.gson.annotations.SerializedName;

public class MicrosoftOAuthToken {
	@SerializedName("access_token")
private String accessToken;  
	public MicrosoftOAuthToken(String accessToken, String refreshToken) {
		this.accessToken = accessToken; this.refreshToken = refreshToken;
	}
	@SerializedName("refresh_token")
	private String refreshToken; public String getAccessToken() {
		return this.accessToken; } public String getRefreshToken() {
		return this.refreshToken;
	}
}