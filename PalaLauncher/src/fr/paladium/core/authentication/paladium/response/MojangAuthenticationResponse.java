package fr.paladium.core.authentication.paladium.response;

public class MojangAuthenticationResponse {
    private String secret;
    private String token;

    public MojangAuthenticationResponse(String secret, String token) {
        this.secret = secret;
        this.token = token;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getToken() {
        return this.token;
    }
}
