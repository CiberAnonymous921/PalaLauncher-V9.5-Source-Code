/*   */ package fr.paladium.core.utils.diagnostic;
/*   */ 
/*   */ public abstract class DiagnosticItem
/*   */ {
/*   */   public String name;
/*   */   
/*   */   public DiagnosticItem(String name) {
/* 8 */     this.name = name;
/*   */   }
/*   */   
/*   */   public abstract boolean test();
/*   */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\DiagnosticItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */