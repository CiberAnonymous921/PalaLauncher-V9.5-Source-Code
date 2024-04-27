package fr.paladium.core.authentication.microsoft.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class MicrosoftXSTSAuthorization {
	
	@SerializedName("Token")
	private String token;
	
	@SerializedName("DisplayClaims")
	private JsonObject displayClaims;
	
	public String getToken() {
		return this.token;
	}
	
	public JsonObject getDisplayClaims() {
		return this.displayClaims;
	}
	
	public String getUHS() {
		return this.displayClaims.get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();
	}
}
