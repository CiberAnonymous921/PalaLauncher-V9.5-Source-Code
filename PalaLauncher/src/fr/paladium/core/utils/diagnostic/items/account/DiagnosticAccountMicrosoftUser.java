/*    */ package fr.paladium.core.utils.diagnostic.items.account;
/*    */ 
/*    */ import fr.paladium.core.authentication.microsoft.MicrosoftAuthenticator;
/*    */ import fr.paladium.core.authentication.microsoft.response.MicrosoftOAuthToken;
/*    */ import fr.paladium.core.authentication.microsoft.response.MicrosoftUserAuthentication;
/*    */ import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
/*    */ import fr.paladium.core.controller.AccountController;
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ 
/*    */ public class DiagnosticAccountMicrosoftUser
/*    */   extends DiagnosticItem {
/*    */   public static MicrosoftOAuthToken OAUTH_TOKEN;
/*    */   public static MicrosoftUserAuthentication USER_AUTHENTICATION;
/*    */   
/*    */   public DiagnosticAccountMicrosoftUser(String name) {
/* 16 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 21 */     MinecraftAccount account = AccountController.getInstance().getCurrentAccount();
/* 22 */     if (account == null) {
/* 23 */       return false;
/*    */     }
/*    */     
/*    */     try {
/* 27 */       OAUTH_TOKEN = MicrosoftAuthenticator.loginWithRefresh(account.getRefreshToken());
/* 28 */       if (OAUTH_TOKEN == null) {
/* 29 */         return false;
/*    */       }
/*    */       
/* 32 */       USER_AUTHENTICATION = MicrosoftAuthenticator.getUserAuthentication(OAUTH_TOKEN.getAccessToken());
/* 33 */       return (USER_AUTHENTICATION != null);
/* 34 */     } catch (Exception e) {
/* 35 */       e.printStackTrace();
/* 36 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\account\DiagnosticAccountMicrosoftUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */