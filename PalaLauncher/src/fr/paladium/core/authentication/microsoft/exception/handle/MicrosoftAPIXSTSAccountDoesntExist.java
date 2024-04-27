package fr.paladium.core.authentication.microsoft.exception.handle;

import com.google.gson.JsonObject;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIException;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIExceptionHandler;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIHandledException;
import fr.paladium.core.utils.desktop.DesktopUtils;
import fr.paladium.core.utils.json.JsonOnlineParser;

public class MicrosoftAPIXSTSAccountDoesntExist extends MicrosoftAPIHandledException {
	
	public MicrosoftAPIXSTSAccountDoesntExist(MicrosoftAPIException exception) {
		super(exception, MicrosoftAPIExceptionHandler.Error.XSTS_ACCOUNT_DOESNT_EXIST);
	}
	
	public String getNotificationMessage() {
		String errorMessage = getException().getError();
		if (errorMessage == null) {
			return super.getNotificationMessage();
		}
		
		JsonObject errorObject = (JsonObject)JsonOnlineParser.GSON.fromJson(errorMessage, JsonObject.class);
		if (errorObject == null) {
			return super.getNotificationMessage();
		}
		
		String errorCode = errorObject.get("XErr").getAsString();
		if (errorCode == null) {
			return super.getNotificationMessage();
		}
		
		if (errorCode.equals("2148916233")) {
			DesktopUtils.openURL("https://www.minecraft.net");
			return "Ce compte Microsoft n'est pas lié à un compte Xbox.";
		} 
		if (errorCode.equals("2148916235")) {
			return "Ce compte Microsoft provient d'un pays où Xbox Live n'est pas disponible."; 
		}
		if (errorCode.equals("2148916236")) {
			DesktopUtils.openURL("https://www.xbox.com");
			return "Ce compte Microsoft nécessite une vérification adulte sur la page Xbox.";
		} 
		if (errorCode.equals("2148916237")) {
			DesktopUtils.openURL("https://www.xbox.com");
			return "Ce compte Microsoft nécessite une vérification d'âge sur la page Xbox.";
		} 
		if (errorCode.equals("2148916238")) {
			DesktopUtils.openURL("https://support.microsoft.com/fr-fr/account-billing/ajouter-des-personnes-%C3%A0-vore-groupe-de-famille-4a07b974-8103-16ad-6ea2-46549ca19e03");
			return "Ce compte Microsoft est un compte mineur ne faisant pas partie d'un groupe famillial.";
		} 
		return super.getNotificationMessage();
	}
}
