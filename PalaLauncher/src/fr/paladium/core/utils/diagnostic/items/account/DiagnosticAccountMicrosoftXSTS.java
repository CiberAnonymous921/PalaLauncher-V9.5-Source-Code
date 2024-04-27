/*    */ package fr.paladium.core.utils.diagnostic.items.account;
/*    */ 
/*    */ import fr.paladium.core.authentication.microsoft.MicrosoftAuthenticator;
/*    */ import fr.paladium.core.authentication.microsoft.response.MicrosoftXSTSAuthorization;
/*    */ import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
/*    */ import fr.paladium.core.controller.AccountController;
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ 
/*    */ 
/*    */ public class DiagnosticAccountMicrosoftXSTS
/*    */   extends DiagnosticItem
/*    */ {
/*    */   public static MicrosoftXSTSAuthorization XSTS_AUTHORIZATION;
/*    */   
/*    */   public DiagnosticAccountMicrosoftXSTS(String name) {
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
/* 27 */       if (DiagnosticAccountMicrosoftUser.USER_AUTHENTICATION == null) {
/* 28 */         return false;
/*    */       }
/*    */       
/* 31 */       XSTS_AUTHORIZATION = MicrosoftAuthenticator.getXSTSAuthorization(DiagnosticAccountMicrosoftUser.USER_AUTHENTICATION.getToken());
/* 32 */       return (XSTS_AUTHORIZATION != null);
/* 33 */     } catch (Exception e) {
/* 34 */       e.printStackTrace();
/* 35 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\account\DiagnosticAccountMicrosoftXSTS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */