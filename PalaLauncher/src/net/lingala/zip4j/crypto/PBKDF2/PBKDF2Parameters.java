/*    */ package net.lingala.zip4j.crypto.PBKDF2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PBKDF2Parameters
/*    */ {
/*    */   protected byte[] salt;
/*    */   protected int iterationCount;
/*    */   protected String hashAlgorithm;
/*    */   protected String hashCharset;
/*    */   protected byte[] derivedKey;
/*    */   
/*    */   public PBKDF2Parameters() {
/* 32 */     this.hashAlgorithm = null;
/* 33 */     this.hashCharset = "UTF-8";
/* 34 */     this.salt = null;
/* 35 */     this.iterationCount = 1000;
/* 36 */     this.derivedKey = null;
/*    */   }
/*    */   
/*    */   public PBKDF2Parameters(String hashAlgorithm, String hashCharset, byte[] salt, int iterationCount) {
/* 40 */     this(hashAlgorithm, hashCharset, salt, iterationCount, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public PBKDF2Parameters(String hashAlgorithm, String hashCharset, byte[] salt, int iterationCount, byte[] derivedKey) {
/* 45 */     this.hashAlgorithm = hashAlgorithm;
/* 46 */     this.hashCharset = hashCharset;
/* 47 */     this.salt = salt;
/* 48 */     this.iterationCount = iterationCount;
/* 49 */     this.derivedKey = derivedKey;
/*    */   }
/*    */   
/*    */   public int getIterationCount() {
/* 53 */     return this.iterationCount;
/*    */   }
/*    */   
/*    */   public void setIterationCount(int iterationCount) {
/* 57 */     this.iterationCount = iterationCount;
/*    */   }
/*    */   
/*    */   public byte[] getSalt() {
/* 61 */     return this.salt;
/*    */   }
/*    */   
/*    */   public void setSalt(byte[] salt) {
/* 65 */     this.salt = salt;
/*    */   }
/*    */   
/*    */   public byte[] getDerivedKey() {
/* 69 */     return this.derivedKey;
/*    */   }
/*    */   
/*    */   public void setDerivedKey(byte[] derivedKey) {
/* 73 */     this.derivedKey = derivedKey;
/*    */   }
/*    */   
/*    */   public String getHashAlgorithm() {
/* 77 */     return this.hashAlgorithm;
/*    */   }
/*    */   
/*    */   public void setHashAlgorithm(String hashAlgorithm) {
/* 81 */     this.hashAlgorithm = hashAlgorithm;
/*    */   }
/*    */   
/*    */   public String getHashCharset() {
/* 85 */     return this.hashCharset;
/*    */   }
/*    */   
/*    */   public void setHashCharset(String hashCharset) {
/* 89 */     this.hashCharset = hashCharset;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\PBKDF2\PBKDF2Parameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */