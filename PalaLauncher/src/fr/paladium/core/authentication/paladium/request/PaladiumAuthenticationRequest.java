package fr.paladium.core.authentication.paladium.request;

public class PaladiumAuthenticationRequest {
    private String username;

    public PaladiumAuthenticationRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
