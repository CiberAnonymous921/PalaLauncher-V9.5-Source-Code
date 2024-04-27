package fr.paladium.core.controller;
import fr.paladium.core.authentication.microsoft.MicrosoftAuthenticator;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIHandledException;
import fr.paladium.core.authentication.microsoft.response.MicrosoftMinecraftAccount;
import fr.paladium.core.authentication.microsoft.response.MicrosoftOAuthToken;
import fr.paladium.core.authentication.microsoft.response.MicrosoftUserAuthentication;
import fr.paladium.core.authentication.microsoft.response.MicrosoftXSTSAuthorization;
import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
import java.io.IOException;
public class AuthenticationController {
    public MicrosoftMinecraftAccount getMicrosoftAccount(MicrosoftOAuthToken oAuthToken) throws MicrosoftAPIHandledException, IOException {
        MicrosoftUserAuthentication userAuthentication = MicrosoftAuthenticator.getUserAuthentication(oAuthToken.getAccessToken());
        MicrosoftXSTSAuthorization xstsAuthorization = MicrosoftAuthenticator.getXSTSAuthorization(userAuthentication.getToken());
        MicrosoftMinecraftAccount minecraftAccount = MicrosoftAuthenticator.getMinecraftAccount(xstsAuthorization);
        return minecraftAccount;
    }
    private static AuthenticationController instance;
    public MinecraftAccount getMinecraftAccount(MicrosoftOAuthToken oAuthToken) throws MicrosoftAPIHandledException, IOException {
        MicrosoftUserAuthentication userAuthentication = MicrosoftAuthenticator.getUserAuthentication(oAuthToken.getAccessToken());
        MicrosoftXSTSAuthorization xstsAuthorization = MicrosoftAuthenticator.getXSTSAuthorization(userAuthentication.getToken());
        MicrosoftMinecraftAccount minecraftAccount = MicrosoftAuthenticator.getMinecraftAccount(xstsAuthorization);
        MinecraftAccount minecraftInfos = MicrosoftAuthenticator.getMinecraftInfos(minecraftAccount.getAccessToken());
        minecraftInfos.setAccessToken(minecraftAccount.getAccessToken());
        minecraftInfos.setRefreshToken(oAuthToken.getRefreshToken());
        return minecraftInfos;
    }
    public MinecraftAccount verify(MinecraftAccount account) throws MicrosoftAPIHandledException, IOException {
        MinecraftAccount loggedAccount;
        try {
            loggedAccount = MicrosoftAuthenticator.getMinecraftInfos(account.getAccessToken());
        } catch (MicrosoftAPIHandledException e) {
            loggedAccount = null;
        }
        if (loggedAccount == null) {
            MicrosoftOAuthToken token = MicrosoftAuthenticator.loginWithRefresh(account.getRefreshToken());
            loggedAccount = getInstance().getMinecraftAccount(token);
            AccountController.getInstance().saveAccount(loggedAccount);
        } else {
            loggedAccount.setAccessToken(account.getAccessToken());
            loggedAccount.setRefreshToken(account.getRefreshToken());
        }
        return loggedAccount;
    }
    public static AuthenticationController getInstance() {
        if (instance == null) {
            instance = new AuthenticationController();
        }
        return instance;
    }
}