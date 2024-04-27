/*    */ package fr.paladium.core.utils.diagnostic.items.account;
/*    */ 
/*    */ import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
/*    */ import fr.paladium.core.authentication.paladium.response.PaladiumAccount;
/*    */ import fr.paladium.core.controller.AccountController;
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ 
/*    */ public class DiagnosticAccountPaladium
/*    */   extends DiagnosticItem
/*    */ {
/*    */   public DiagnosticAccountPaladium(String name) {
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
/* 23 */       PaladiumAccount paladiumAccount = new PaladiumAccount(account);
/* 24 */       return (paladiumAccount != null && paladiumAccount.getJwt() != null);
/* 25 */     } catch (Exception e) {
/* 26 */       e.printStackTrace();
/* 27 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\account\DiagnosticAccountPaladium.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */