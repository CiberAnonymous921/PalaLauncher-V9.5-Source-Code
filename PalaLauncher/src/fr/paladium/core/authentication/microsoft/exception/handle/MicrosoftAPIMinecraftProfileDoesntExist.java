package fr.paladium.core.authentication.microsoft.exception.handle;

import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIException;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIExceptionHandler;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIHandledException;

public class MicrosoftAPIMinecraftProfileDoesntExist extends MicrosoftAPIHandledException {
	public MicrosoftAPIMinecraftProfileDoesntExist(MicrosoftAPIException exception) {
		super(exception, MicrosoftAPIExceptionHandler.Error.MINECRAFT_PROFILE_DOESNT_EXIST);
	}
}