package fr.paladium.core.authentication.microsoft.request;

public class MinecraftCreateProfileRequest
{
	private String profileName;
	
	public MinecraftCreateProfileRequest(String profileName) {
		this.profileName = profileName;
	}
	public String getProfileName() {
		return this.profileName;
	}
}