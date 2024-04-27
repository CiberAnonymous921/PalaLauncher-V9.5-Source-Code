package fr.paladium.core.authentication.microsoft.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class MicrosoftRPSRequest {
	
	@SerializedName("Properties")
	private JsonObject properties;
	
	@SerializedName("RelyingParty")
	private String relyingParty;
	
	@SerializedName("TokenType")
	private String tokenType;
	
	public JsonObject getProperties() {
		return this.properties;
	}
	
	public String getRelyingParty() {
		return this.relyingParty;
	}
	
	public String getTokenType() {
		return this.tokenType;
	}
	
	public MicrosoftRPSRequest(String token) {
		this.properties = new JsonObject();
		this.properties.addProperty("AuthMethod", "RPS");
		this.properties.addProperty("SiteName", "user.auth.xboxlive.com");
		this.properties.addProperty("RpsTicket", "d=" + token);
		
		this.relyingParty = "http://auth.xboxlive.com";
		this.tokenType = "JWT";
	}
}
