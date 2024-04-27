package fr.paladium.core.authentication.microsoft.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class MicrosoftXSTSRequest {
	@SerializedName("Properties")
	private JsonObject properties;
   
	public JsonObject getProperties() {
		return this.properties;
	}
	@SerializedName("RelyingParty")
	private String relyingParty;
	@SerializedName("TokenType")
	private String tokenType; public String getRelyingParty() { return this.relyingParty; } public String getTokenType() {
		return this.tokenType;
	}
	public MicrosoftXSTSRequest(String token) {
		this.properties = new JsonObject();
		this.properties.addProperty("SandboxId", "RETAIL");
		JsonArray userTokens = new JsonArray();
		userTokens.add(token);
		this.properties.add("UserTokens", (JsonElement)userTokens);
	
		this.relyingParty = "rp://api.minecraftservices.com/";
		this.tokenType = "JWT";
	}
}
