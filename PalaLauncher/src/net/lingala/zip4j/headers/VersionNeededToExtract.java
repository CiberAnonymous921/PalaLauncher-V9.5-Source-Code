/*    */ package net.lingala.zip4j.headers;
/*    */ 
/*    */ public enum VersionNeededToExtract
/*    */ {
/*  5 */   DEFAULT(10),
/*  6 */   DEFLATE_COMPRESSED(20),
/*  7 */   ZIP_64_FORMAT(45),
/*  8 */   AES_ENCRYPTED(51);
/*    */   
/*    */   private int code;
/*    */   
/*    */   VersionNeededToExtract(int code) {
/* 13 */     this.code = code;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 17 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\headers\VersionNeededToExtract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */