/*    */ package fr.paladium.core.utils.net.dns;
/*    */ 
/*    */ 
/*    */ public class DohQuestion
/*    */ {
/*    */   private final String name;
/*    */   private final int type;
/*    */   
/*    */   public DohQuestion(String name, int type) {
/* 10 */     this.name = name; this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 15 */     return this.name;
/*    */   }
/*    */   public int getType() {
/* 18 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\net\dns\DohQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */