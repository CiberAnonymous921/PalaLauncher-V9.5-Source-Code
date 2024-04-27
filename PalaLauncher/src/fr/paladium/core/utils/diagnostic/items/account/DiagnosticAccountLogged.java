/*    */ package fr.paladium.core.utils.diagnostic.items.account;
/*    */ 
/*    */ import fr.paladium.core.controller.AccountController;
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ 
/*    */ public class DiagnosticAccountLogged
/*    */   extends DiagnosticItem {
/*    */   public DiagnosticAccountLogged(String name) {
/*  9 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 14 */     return (AccountController.getInstance().getCurrentAccount() != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\account\DiagnosticAccountLogged.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */