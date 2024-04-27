/*    */ package fr.paladium.core.utils.diagnostic.items.account;
/*    */ import fr.paladium.core.authentication.microsoft.MicrosoftAuthenticator;
/*    */ import fr.paladium.core.authentication.microsoft.response.MicrosoftMinecraftAccount;
/*    */ import fr.paladium.core.authentication.microsoft.response.MicrosoftOAuthToken;
/*    */ import fr.paladium.core.authentication.microsoft.response.MicrosoftUserAuthentication;
/*    */ import fr.paladium.core.authentication.microsoft.response.MicrosoftXSTSAuthorization;
/*    */ import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
import fr.paladium.core.controller.AccountController;
import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ 
/*    */ public class DiagnosticAccountMinecraft extends DiagnosticItem {
/*    */   public DiagnosticAccountMinecraft(String name) {
/* 11 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 16 */     MinecraftAccount account = AccountController.getInstance().getCurrentAccount();
/* 17 */     if (account == null) {
/* 18 */       return false;
/*    */     }
/*    */     
/*    */     try {
/* 22 */       MicrosoftOAuthToken token = MicrosoftAuthenticator.loginWithRefresh(account.getRefreshToken());
/* 23 */       if (token == null) {
/* 24 */         return false;
/*    */       }
/*    */       
/* 27 */       MicrosoftUserAuthentication userAuthentication = MicrosoftAuthenticator.getUserAuthentication(token.getAccessToken());
/* 28 */       MicrosoftXSTSAuthorization xstsAuthorization = MicrosoftAuthenticator.getXSTSAuthorization(userAuthentication.getToken());
/* 29 */       MicrosoftMinecraftAccount minecraftAccount = MicrosoftAuthenticator.getMinecraftAccount(xstsAuthorization);
/* 30 */       MinecraftAccount minecraftInfos = MicrosoftAuthenticator.getMinecraftInfos(minecraftAccount.getAccessToken());
/* 31 */       return (minecraftInfos != null);
/* 32 */     } catch (Exception e) {
/* 33 */       e.printStackTrace();
/* 34 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\account\DiagnosticAccountMinecraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */