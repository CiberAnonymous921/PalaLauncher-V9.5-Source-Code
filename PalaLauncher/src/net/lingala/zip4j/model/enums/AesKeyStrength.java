/*    */ package net.lingala.zip4j.model.enums;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum AesKeyStrength
/*    */ {
/* 12 */   KEY_STRENGTH_128(1, 8, 16, 16),
/*    */ 
/*    */ 
/*    */   
/* 16 */   KEY_STRENGTH_192(2, 12, 24, 24),
/*    */ 
/*    */ 
/*    */   
/* 20 */   KEY_STRENGTH_256(3, 16, 32, 32);
/*    */   
/*    */   private int rawCode;
/*    */   private int saltLength;
/*    */   private int macLength;
/*    */   private int keyLength;
/*    */   
/*    */   AesKeyStrength(int rawCode, int saltLength, int macLength, int keyLength) {
/* 28 */     this.rawCode = rawCode;
/* 29 */     this.saltLength = saltLength;
/* 30 */     this.macLength = macLength;
/* 31 */     this.keyLength = keyLength;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawCode() {
/* 39 */     return this.rawCode;
/*    */   }
/*    */   
/*    */   public int getSaltLength() {
/* 43 */     return this.saltLength;
/*    */   }
/*    */   
/*    */   public int getMacLength() {
/* 47 */     return this.macLength;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getKeyLength() {
/* 54 */     return this.keyLength;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AesKeyStrength getAesKeyStrengthFromRawCode(int code) {
/* 63 */     for (AesKeyStrength aesKeyStrength : values()) {
/* 64 */       if (aesKeyStrength.getRawCode() == code) {
/* 65 */         return aesKeyStrength;
/*    */       }
/*    */     } 
/*    */     
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\enums\AesKeyStrength.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */