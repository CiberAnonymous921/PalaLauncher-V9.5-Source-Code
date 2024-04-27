/*    */ package fr.paladium.core.utils.net.dns;public class DohAnswer {
/*    */   private final String name;
/*    */   private final int type;
/*    */   
/*    */   public DohAnswer(String name, int type, int ttl, String data) {
/*  6 */     this.name = name; this.type = type; this.ttl = ttl; this.data = data;
/*    */   }
/*    */   private final int ttl; private final String data;
/*    */   
/*    */   public String getName() {
/* 11 */     return this.name;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 15 */     return this.type;
/*    */   }
/*    */   public int getTtl() {
/* 18 */     return this.ttl;
/*    */   }
/*    */   public String getData() {
/* 21 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\net\dns\DohAnswer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */