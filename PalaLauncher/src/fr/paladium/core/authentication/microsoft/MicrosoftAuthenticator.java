package fr.paladium.core.authentication.microsoft;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIException;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIExceptionHandler;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIHandledException;
import fr.paladium.core.authentication.microsoft.request.MicrosoftMinecraftXboxRequest;
import fr.paladium.core.authentication.microsoft.request.MicrosoftRPSRequest;
import fr.paladium.core.authentication.microsoft.request.MicrosoftXSTSRequest;
import fr.paladium.core.authentication.microsoft.request.MinecraftCreateProfileRequest;
import fr.paladium.core.authentication.microsoft.response.MicrosoftMinecraftAccount;
import fr.paladium.core.authentication.microsoft.response.MicrosoftOAuthToken;
import fr.paladium.core.authentication.microsoft.response.MicrosoftUserAuthentication;
import fr.paladium.core.authentication.microsoft.response.MicrosoftXSTSAuthorization;
import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
import fr.paladium.core.authentication.microsoft.response.MinecraftProductType;
import fr.paladium.core.authentication.microsoft.response.profile.MinecraftProfile;
import fr.paladium.core.authentication.microsoft.response.profile.MinecraftProfileDetails;
import fr.paladium.core.authentication.microsoft.response.profile.MinecraftProfileDetailsStatus;
import fr.paladium.core.utils.json.JsonOnlineParser;
import fr.paladium.core.utils.net.CustomHttpClient;

public class MicrosoftAuthenticator {
	
	private static final String REDIRECT_URI = "https://login.microsoftonline.com/common/oauth2/nativeclient";
	private static final String CLIENT_ID = "eded06a1-609d-4aa6-b457-b9c3fd1ab692";
	private static final String CODE_PATTERN = "https://login.microsoftonline.com/common/oauth2/nativeclient#code=";
	private static final String TOKEN_PATTERN = "https://www.xbox.com/en-US/auth/msa?action=loggedIn#state=https%3A%2F%2Fwww.xbox.com%2Ffr-FR%2F&accessToken=";
	
	public static String getLoginURL() {
		return "https://login.live.com/oauth20_authorize.srf?client_id=eded06a1-609d-4aa6-b457-b9c3fd1ab692&response_type=code&redirect_uri=https://login.microsoftonline.com/common/oauth2/nativeclient&response_mode=fragment&scope=XboxLive.signin%20offline_access&cobrandid=8058f65d-ce06-4c30-9559-473c9275a65d";
	}
	
	public static String getCodeFromURL(String url) {
		return url.replace("https://login.microsoftonline.com/common/oauth2/nativeclient#code=", "");
	}
	
	public static boolean isCodeURL(String url) {
		return url.contains("https://login.microsoftonline.com/common/oauth2/nativeclient#code=");
	}
	
	public static String getTokenFromURL(String url) {
		return url.replace("https://www.xbox.com/en-US/auth/msa?action=loggedIn#state=https%3A%2F%2Fwww.xbox.com%2Ffr-FR%2F&accessToken=", "");
	}
	
	public static boolean isTokenURL(String url) {
		return url.contains("https://www.xbox.com/en-US/auth/msa?action=loggedIn#state=https%3A%2F%2Fwww.xbox.com%2Ffr-FR%2F&accessToken=");
	}
	
	public static MicrosoftOAuthToken loginWithCode(String code) throws MicrosoftAPIHandledException, IOException {
		HttpPost post = new HttpPost("https://login.live.com/oauth20_token.srf");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setEntity((HttpEntity)new UrlEncodedFormEntity(Arrays.asList(new BasicNameValuePair[] {
			new BasicNameValuePair("client_id", "eded06a1-609d-4aa6-b457-b9c3fd1ab692"),
			new BasicNameValuePair("redirect_uri", "https://login.microsoftonline.com/common/oauth2/nativeclient"),
			new BasicNameValuePair("code", code),
			new BasicNameValuePair("grant_type", "authorization_code")
		})));
		
		CloseableHttpClient client = CustomHttpClient.createClient();
		try {
			CloseableHttpResponse response = client.execute((HttpUriRequest)post);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw MicrosoftAPIExceptionHandler.handle(Request.LOGIN_WITH_CODE, new MicrosoftAPIException(response));
			}
			
			MicrosoftOAuthToken microsoftOAuthToken = (MicrosoftOAuthToken)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), MicrosoftOAuthToken.class);
			if (client != null) client.close();
			return microsoftOAuthToken;
		} catch (Throwable throwable) {
			if (client != null) {
				try {
					client.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
			}
			throw throwable;
		}
	}
	
	public static MicrosoftOAuthToken loginWithRefresh(String refresh) throws MicrosoftAPIHandledException, IOException {
		HttpPost post = new HttpPost("https://login.live.com/oauth20_token.srf");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setEntity((HttpEntity)new UrlEncodedFormEntity(Arrays.asList(new BasicNameValuePair[] {
			new BasicNameValuePair("client_id", "eded06a1-609d-4aa6-b457-b9c3fd1ab692"),
			new BasicNameValuePair("redirect_uri", "https://login.microsoftonline.com/common/oauth2/nativeclient"),
			new BasicNameValuePair("refresh_token", refresh),
			new BasicNameValuePair("grant_type", "refresh_token")
		})));
		
		CloseableHttpClient client = CustomHttpClient.createClient();
		try {
			CloseableHttpResponse response = client.execute((HttpUriRequest)post);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw MicrosoftAPIExceptionHandler.handle(Request.LOGIN_WITH_REFRESH, new MicrosoftAPIException(response));
			}		MicrosoftOAuthToken microsoftOAuthToken = (MicrosoftOAuthToken)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), MicrosoftOAuthToken.class);
			if (client != null) client.close();
			return microsoftOAuthToken;
		} catch (Throwable throwable) {
			if (client != null) {
				try {
					client.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
			}
			throw throwable;
		}
	}

	public static MicrosoftUserAuthentication getUserAuthentication(String token) throws MicrosoftAPIHandledException, IOException {
		HttpPost post = new HttpPost("https://user.auth.xboxlive.com/user/authenticate");
		post.addHeader("Content-Type", "application/json");
		post.setEntity((HttpEntity)new StringEntity(JsonOnlineParser.GSON.toJson(new MicrosoftRPSRequest(token))));
		
		CloseableHttpClient client = CustomHttpClient.createClient();
		try {
			CloseableHttpResponse response = client.execute((HttpUriRequest)post);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw MicrosoftAPIExceptionHandler.handle(Request.GET_USER_AUTHENTICATION, new MicrosoftAPIException(response));
			}
			
			MicrosoftUserAuthentication microsoftUserAuthentication = (MicrosoftUserAuthentication)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), MicrosoftUserAuthentication.class);
			if (client != null) client.close();
			return microsoftUserAuthentication;
		} catch (Throwable throwable) {
			if (client != null) {
				try {
					client.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
			}
			throw throwable;
		}
	}

	public static MicrosoftXSTSAuthorization getXSTSAuthorization(String token) throws MicrosoftAPIHandledException, IOException {
		HttpPost post = new HttpPost("https://xsts.auth.xboxlive.com/xsts/authorize");
		post.addHeader("Content-Type", "application/json");
		post.setEntity((HttpEntity)new StringEntity(JsonOnlineParser.GSON.toJson(new MicrosoftXSTSRequest(token))));
		
		CloseableHttpClient client = CustomHttpClient.createClient();
		try {
			CloseableHttpResponse response = client.execute((HttpUriRequest)post);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw MicrosoftAPIExceptionHandler.handle(Request.GET_XSTS_AUTHORIZATION, new MicrosoftAPIException(response));
			}
			
			MicrosoftXSTSAuthorization microsoftXSTSAuthorization = (MicrosoftXSTSAuthorization)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), MicrosoftXSTSAuthorization.class);
			if (client != null) client.close();
			return microsoftXSTSAuthorization;
		} catch (Throwable throwable) {
			if (client != null) {
				try {
					client.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
			}
			throw throwable;
		}
	}

	public static MicrosoftMinecraftAccount getMinecraftAccount(MicrosoftXSTSAuthorization authorization) throws MicrosoftAPIHandledException, IOException {
		HttpPost post = new HttpPost("https://api.minecraftservices.com/authentication/login_with_xbox");
		post.addHeader("Content-Type", "application/json");
		post.setEntity((HttpEntity)new StringEntity(JsonOnlineParser.GSON.toJson(new MicrosoftMinecraftXboxRequest("XBL3.0 x=" + authorization.getUHS() + ";" + authorization.getToken()))));
		
		CloseableHttpClient client = CustomHttpClient.createClient();
		try {
			CloseableHttpResponse response = client.execute((HttpUriRequest)post);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw MicrosoftAPIExceptionHandler.handle(Request.GET_MINECRAFT_ACCOUNT, new MicrosoftAPIException(response));
			}
			
			MicrosoftMinecraftAccount microsoftMinecraftAccount = (MicrosoftMinecraftAccount)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), MicrosoftMinecraftAccount.class);
			if (client != null) client.close();
			return microsoftMinecraftAccount;
		} catch (Throwable throwable) {
			if (client != null) {
				try {
					client.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
			}
			throw throwable;
		}
	}

	public static MinecraftAccount getMinecraftInfos(String token) throws MicrosoftAPIHandledException, IOException {
		HttpGet get = new HttpGet("https://api.minecraftservices.com/minecraft/profile");
		get.addHeader("Authorization", "Bearer " + token);
		
		CloseableHttpClient client = CustomHttpClient.createClient();
		try {
			CloseableHttpResponse response = client.execute((HttpUriRequest)get);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw MicrosoftAPIExceptionHandler.handle(Request.GET_MINECRAFT_INFOS, new MicrosoftAPIException(response));
			}
			
			MinecraftAccount minecraftAccount = (MinecraftAccount)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), MinecraftAccount.class);
			if (client != null) client.close();
				return minecraftAccount;
		} catch (Throwable throwable) {
			if (client != null) {
			try {
				client.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
			}
			throw throwable;
		}
	}
	
	public static MinecraftProfile createMinecraftProfile(String token, String playerName) throws IOException {
		HttpPost post = new HttpPost("https://api.minecraftservices.com/minecraft/profile");
		post.addHeader("Authorization", "Bearer " + token);
		post.addHeader("Content-Type", "application/json");
		post.setEntity((HttpEntity)new StringEntity(JsonOnlineParser.GSON.toJson(new MinecraftCreateProfileRequest(playerName))));
		
		CloseableHttpClient client = CustomHttpClient.createClient();
		try {
			CloseableHttpResponse response = client.execute((HttpUriRequest)post);
			if (response.getStatusLine().getStatusCode() != 200) {
				MinecraftProfile minecraftProfile1 = (MinecraftProfile)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), MinecraftProfile.class);
				if (client != null) client.close();
				return minecraftProfile1;
			}
			
			MinecraftProfile minecraftProfile = new MinecraftProfile(new MinecraftProfileDetails(MinecraftProfileDetailsStatus.SUCCESS));
			if (client != null) client.close();
			return minecraftProfile;
		} catch (Throwable throwable) {
			if (client != null) {
				try {
					client.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
			}
			throw throwable;
		}
	}

	public static MinecraftProductType getMinecraftProduct(String token) throws IOException {
		HttpGet get = new HttpGet("https://api.minecraftservices.com/entitlements/license?requestId=paladium");
		get.addHeader("Authorization", "Bearer " + token);
		
		CloseableHttpClient client = CustomHttpClient.createClient();
		try {
			CloseableHttpResponse response = client.execute((HttpUriRequest)get);
			if (response.getStatusLine().getStatusCode() != 200) {
				MinecraftProductType minecraftProductType1 = MinecraftProductType.NONE;
				if (client != null) client.close();
				return minecraftProductType1;
			}
			
			JsonObject object = (JsonObject)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), JsonObject.class);
			JsonArray array = object.getAsJsonArray("items");
			for (JsonElement element : array) {
				if (element.getAsJsonObject().get("name").getAsString().equalsIgnoreCase("product_minecraft")) {
					MinecraftProductType minecraftProductType1 = MinecraftProductType.valueOf(element.getAsJsonObject().get("source").getAsString().toUpperCase());
					if (client != null) client.close();
					return minecraftProductType1;
				}
			}
			
			MinecraftProductType minecraftProductType = MinecraftProductType.NONE;
			if (client != null) client.close();
			return minecraftProductType;
		} catch (Throwable throwable) {
			if (client != null) {
				try {
					client.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
			}
			throw throwable;
		}
	}

	public enum Request {
		LOGIN_WITH_CODE,
		LOGIN_WITH_REFRESH,
		GET_USER_AUTHENTICATION,
		GET_XSTS_AUTHORIZATION,
		GET_MINECRAFT_ACCOUNT,
		GET_MINECRAFT_INFOS;
	}

}

