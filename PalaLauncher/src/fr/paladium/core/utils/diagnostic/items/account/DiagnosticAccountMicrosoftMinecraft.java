/*    */ package fr.paladium.core.utils.diagnostic.items.account;
/*    */ 
/*    */ import fr.paladium.core.authentication.microsoft.MicrosoftAuthenticator;
/*    */ import fr.paladium.core.authentication.microsoft.response.MicrosoftMinecraftAccount;
/*    */ import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
/*    */ import fr.paladium.core.controller.AccountController;
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ 
/*    */ public class DiagnosticAccountMicrosoftMinecraft
/*    */   extends DiagnosticItem {
/*    */   public DiagnosticAccountMicrosoftMinecraft(String name) {
/* 12 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 17 */     MinecraftAccount account = AccountController.getInstance().getCurrentAccount();
/* 18 */     if (account == null) {
/* 19 */       return false;
/*    */     }
/*    */     
/*    */     try {
/* 23 */       if (DiagnosticAccountMicrosoftXSTS.XSTS_AUTHORIZATION == null) {
/* 24 */         return false;
/*    */       }
/*    */       
/* 27 */       MicrosoftMinecraftAccount minecraftAccount = MicrosoftAuthenticator.getMinecraftAccount(DiagnosticAccountMicrosoftXSTS.XSTS_AUTHORIZATION);
/* 28 */       return (minecraftAccount != null);
/* 29 */     } catch (Exception e) {
/* 30 */       e.printStackTrace();
/* 31 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\account\DiagnosticAccountMicrosoftMinecraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */