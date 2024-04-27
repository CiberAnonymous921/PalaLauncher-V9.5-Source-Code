package fr.paladium.core.authentication.paladium.response;

public class PaladiumAuthenticationResponse {
    private String publicKey;
    private String serverId;
    private String token;

    public String getPublicKey() {
        return this.publicKey;
    }

    public String getServerId() {
        return this.serverId;
    }

    public String getToken() {
        return this.token;
    }
}
