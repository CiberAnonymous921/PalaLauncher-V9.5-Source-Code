/*    */ package fr.paladium.core.authentication.microsoft.exception;
/*    */ 
/*    */ import fr.paladium.core.authentication.microsoft.MicrosoftAuthenticator;
/*    */ import fr.paladium.core.authentication.microsoft.exception.handle.MicrosoftAPIBadLoginCodeException;
/*    */ import fr.paladium.core.authentication.microsoft.exception.handle.MicrosoftAPIMinecraftProfileDoesntExist;
/*    */ import fr.paladium.core.authentication.microsoft.exception.handle.MicrosoftAPIXSTSAccountDoesntExist;
/*    */ import fr.paladium.router.handler.CefRouteHandler;
/*    */ 
/*    */ public class MicrosoftAPIExceptionHandler
/*    */ {
/*    */   public static MicrosoftAPIHandledException handle(MicrosoftAuthenticator.Request request, MicrosoftAPIException exception) {
/* 12 */     if (exception.getCode() == 429) {
/* 13 */       return new MicrosoftAPIHandledException(exception, Error.TOO_MANY_REQUESTS);
/*    */     }
/*    */     
/* 16 */     switch (request) {
/*    */       case LOGIN_WITH_CODE:
/* 18 */         if (exception.getCode() == 400) {
/* 19 */           return (MicrosoftAPIHandledException)new MicrosoftAPIBadLoginCodeException(exception);
/*    */         }
/*    */         break;
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       case GET_XSTS_AUTHORIZATION:
/* 27 */         if (exception.getCode() == 401) {
/* 28 */           return (MicrosoftAPIHandledException)new MicrosoftAPIXSTSAccountDoesntExist(exception);
/*    */         }
/*    */         break;
/*    */       case GET_MINECRAFT_INFOS:
/* 32 */         if (exception.getCode() == CefRouteHandler.StatusCodes.NOT_FOUND.getCode()) {
/* 33 */           return (MicrosoftAPIHandledException)new MicrosoftAPIMinecraftProfileDoesntExist(exception);
/*    */         }
/*    */         break;
/*    */     } 
/* 37 */     return new MicrosoftAPIHandledException(exception, Error.UNKNOWN_ERROR);
/*    */   }
/*    */   
/*    */   public enum Error
/*    */   {
/* 42 */     TOO_MANY_REQUESTS("Trop de requêtes ont été effectuées."),
/* 43 */     UNKNOWN_ERROR("Une erreur inconnue est survenue."),
/* 44 */     BAD_LOGIN_CODE("Le code de connexion est invalide."),
/* 45 */     XSTS_ACCOUNT_DOESNT_EXIST("Ce compte Microsoft n'est pas lié à un compte Xbox Live."),
/* 46 */     MINECRAFT_PROFILE_DOESNT_EXIST("Vous n'avez pas de compte Minecraft de créé.");
/*    */     
/*    */     private final String message;
/*    */     
/*    */     Error(String message) {
/* 51 */       this.message = message;
/*    */     }
/*    */     
/*    */     public String getMessage() {
/* 55 */       return this.message;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\authentication\microsoft\exception\MicrosoftAPIExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */