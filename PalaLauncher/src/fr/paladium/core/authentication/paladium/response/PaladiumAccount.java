package fr.paladium.core.authentication.paladium.response;

import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
import fr.paladium.core.authentication.paladium.PaladiumAuthenticator;
import fr.paladium.core.utils.desktop.NotificationHelper;
import fr.paladium.core.utils.net.Telemetry;
import java.io.IOException;

public class PaladiumAccount {
    private MinecraftAccount account;
    private String lastAccessToken;
    private PaladiumJWTResponse jwt;
    private boolean connected;

    public MinecraftAccount getAccount() {
        return this.account;
    }

    public String getLastAccessToken() {
        return this.lastAccessToken;
    }

    public PaladiumJWTResponse getJwt() {
        return this.jwt;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public PaladiumAccount(MinecraftAccount account) throws IOException {
        authenticate(account);
    }

    public PaladiumAccount authenticate(MinecraftAccount account) throws IOException {
        this.account = account;
        this.lastAccessToken = account.getAccessToken();

        PaladiumAuthenticationResponse paladiumAuthenticationResponse = PaladiumAuthenticator.authenticate(account);
        if (paladiumAuthenticationResponse == null) {
            NotificationHelper.sendNotification("Impossible de contacter les serveurs de Paladium Games.");
            Telemetry.collect(Telemetry.Type.ERR_AUTH_PALADIUM);
            this.connected = false;
            return this;
        }

        MojangAuthenticationResponse mojangAuthenticationResponse = PaladiumAuthenticator.sendAuthenticationToMojangServices(paladiumAuthenticationResponse, account);
        this.jwt = PaladiumAuthenticator.validate(mojangAuthenticationResponse);

        this.connected = true;
        return this;
    }

    public boolean verify(MinecraftAccount account) {
        return account.getAccessToken().equals(this.lastAccessToken);
    }
}
