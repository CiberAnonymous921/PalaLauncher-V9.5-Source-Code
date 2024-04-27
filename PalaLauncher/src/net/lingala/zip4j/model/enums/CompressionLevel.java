/*    */ package net.lingala.zip4j.model.enums;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CompressionLevel
/*    */ {
/* 13 */   FASTEST(1),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   FAST(3),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   NORMAL(5),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   MAXIMUM(7),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   ULTRA(9);
/*    */   
/*    */   private int level;
/*    */   
/*    */   CompressionLevel(int level) {
/* 38 */     this.level = level;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLevel() {
/* 46 */     return this.level;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\enums\CompressionLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */