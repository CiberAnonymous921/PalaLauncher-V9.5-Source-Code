package fr.paladium.core.authentication.microsoft.exception.handle;

import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIException;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIExceptionHandler;
import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIHandledException;

public class MicrosoftAPIBadLoginCodeException extends MicrosoftAPIHandledException {
	public MicrosoftAPIBadLoginCodeException(MicrosoftAPIException exception) {
		super(exception, MicrosoftAPIExceptionHandler.Error.BAD_LOGIN_CODE);
	}
}