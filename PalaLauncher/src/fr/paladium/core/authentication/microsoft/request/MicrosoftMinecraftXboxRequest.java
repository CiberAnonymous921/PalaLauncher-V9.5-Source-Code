package fr.paladium.core.authentication.microsoft.request;

public class MicrosoftMinecraftXboxRequest
{
	public MicrosoftMinecraftXboxRequest(String identityToken) {
		this.identityToken = identityToken;
	}
	private String identityToken;
	
	public String getIdentityToken() {
		return this.identityToken;
	}
}