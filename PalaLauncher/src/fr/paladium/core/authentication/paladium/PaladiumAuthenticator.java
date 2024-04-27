package fr.paladium.core.authentication.paladium;

import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
import fr.paladium.core.authentication.paladium.request.MojangAuthenticationRequest;
import fr.paladium.core.authentication.paladium.request.PaladiumAuthenticationRequest;
import fr.paladium.core.authentication.paladium.response.MojangAuthenticationResponse;
import fr.paladium.core.authentication.paladium.response.PaladiumAuthenticationResponse;
import fr.paladium.core.authentication.paladium.response.PaladiumJWTResponse;
import fr.paladium.core.authentication.paladium.utils.CryptManager;
import fr.paladium.core.utils.format.UUIDUtils;
import fr.paladium.core.utils.json.JsonOnlineParser;
import fr.paladium.core.utils.net.CustomHttpClient;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

public class PaladiumAuthenticator {
    private static final String AUTH_URL = "https://palagames-launcher-front.pages.dev/api/auth/start";
    private static final String VALIDATE_URL = "https://palagames-launcher-front.pages.dev/api/auth/validate";
    private static final String MOJANG_AUTH_URL = "https://sessionserver.mojang.com/session/minecraft/join";

    public static PaladiumAuthenticationResponse authenticate(MinecraftAccount account) throws IOException {
        HttpPost post = new HttpPost("https://palagames-launcher-front.pages.dev/api/auth/start");
        post.addHeader("Content-Type", "application/json");
        post.setEntity((HttpEntity)new StringEntity(JsonOnlineParser.GSON.toJson(new PaladiumAuthenticationRequest(account.getName()))));

        CloseableHttpClient client = CustomHttpClient.createClient(); 
        try { 
            CloseableHttpResponse response = client.execute((HttpUriRequest)post);
            PaladiumAuthenticationResponse paladiumAuthenticationResponse = (PaladiumAuthenticationResponse)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), PaladiumAuthenticationResponse.class);
            if (client != null) 
                client.close();  
            return paladiumAuthenticationResponse; 
        } catch (Throwable throwable) { 
            if (client != null)
                try { client.close(); }
                catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
                    throw throwable; 
        }
    } 

    public static MojangAuthenticationResponse sendAuthenticationToMojangServices(PaladiumAuthenticationResponse authenticationResponse, MinecraftAccount account) throws IOException { 
        String serverId = new String(Base64.getDecoder().decode(authenticationResponse.getServerId()));
        PublicKey publicKey = CryptManager.decodePublicKey(Base64.getDecoder().decode(authenticationResponse.getPublicKey()));
        byte[] token = Base64.getDecoder().decode(authenticationResponse.getToken());

        SecretKey secretKey = CryptManager.createNewSharedKey();
        String finalServerId = (new BigInteger(CryptManager.getServerIdHash(serverId, publicKey, secretKey))).toString(16);

        HttpPost post = new HttpPost("https://sessionserver.mojang.com/session/minecraft/join");
        post.addHeader("Content-Type", "application/json");
        post.setEntity((HttpEntity)new StringEntity(JsonOnlineParser.GSON.toJson(new MojangAuthenticationRequest(account.getAccessToken(), UUIDUtils.fromWithoutDashes(account.getId()).toString(), finalServerId))));

        CloseableHttpClient client = CustomHttpClient.createClient(); 
        try { 
            CloseableHttpResponse response = client.execute((HttpUriRequest)post);

            MojangAuthenticationResponse mojangAuthenticationResponse = new MojangAuthenticationResponse(new String(Base64.getEncoder().encode(CryptManager.encryptData(publicKey, secretKey.getEncoded()))), new String(Base64.getEncoder().encode(CryptManager.encryptData(publicKey, token))));

            if (client != null) 
                client.close();  
            return mojangAuthenticationResponse; 
        } catch (Throwable throwable) { 
            if (client != null)
                try { client.close(); }
                catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
                    throw throwable; 
        }
    } 

    public static PaladiumJWTResponse validate(MojangAuthenticationResponse authenticationResponse) throws IOException { 
        HttpPost post = new HttpPost("https://palagames-launcher-front.pages.dev/api/auth/validate");
        post.addHeader("Content-Type", "application/json");
        post.setEntity((HttpEntity)new StringEntity(JsonOnlineParser.GSON.toJson(authenticationResponse)));

        CloseableHttpClient client = CustomHttpClient.createClient(); 
        try {
            CloseableHttpResponse response = client.execute((HttpUriRequest)post);
            PaladiumJWTResponse paladiumJWTResponse = (PaladiumJWTResponse)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), PaladiumJWTResponse.class);
            if (client != null) 
                client.close(); 
            return paladiumJWTResponse; 
        } catch (Throwable throwable) { 
            if (client != null)
                try { client.close(); }
                catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
                    throw throwable; 
        }
    }
}
