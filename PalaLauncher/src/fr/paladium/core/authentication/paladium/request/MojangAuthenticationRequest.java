package fr.paladium.core.authentication.paladium.request;
	
public class MojangAuthenticationRequest {
    private String accessToken;
    private String selectedProfile;
    private String serverId;

    public MojangAuthenticationRequest(String accessToken, String selectedProfile, String serverId) {
        this.accessToken = accessToken;
        this.selectedProfile = selectedProfile;
        this.serverId = serverId;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getSelectedProfile() {
        return this.selectedProfile;
    }

    public String getServerId() {
        return this.serverId;
    }
}