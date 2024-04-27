/*    */ package net.lingala.zip4j.model.enums;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum AesVersion
/*    */ {
/* 11 */   ONE(1),
/*    */ 
/*    */ 
/*    */   
/* 15 */   TWO(2);
/*    */   
/*    */   private int versionNumber;
/*    */   
/*    */   AesVersion(int versionNumber) {
/* 20 */     this.versionNumber = versionNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getVersionNumber() {
/* 28 */     return this.versionNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AesVersion getFromVersionNumber(int versionNumber) {
/* 36 */     for (AesVersion aesVersion : values()) {
/* 37 */       if (aesVersion.versionNumber == versionNumber) {
/* 38 */         return aesVersion;
/*    */       }
/*    */     } 
/*    */     
/* 42 */     throw new IllegalArgumentException("Unsupported Aes version");
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\enums\AesVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */