/*    */ package net.lingala.zip4j.crypto.PBKDF2;
/*    */ 
/*    */ import java.security.InvalidKeyException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import javax.crypto.Mac;
/*    */ import javax.crypto.spec.SecretKeySpec;
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
/*    */ public class MacBasedPRF
/*    */   implements PRF
/*    */ {
/*    */   private Mac mac;
/*    */   private int hLen;
/*    */   private String macAlgorithm;
/*    */   
/*    */   public MacBasedPRF(String macAlgorithm) {
/* 35 */     this.macAlgorithm = macAlgorithm;
/*    */     try {
/* 37 */       this.mac = Mac.getInstance(macAlgorithm);
/* 38 */       this.hLen = this.mac.getMacLength();
/* 39 */     } catch (NoSuchAlgorithmException e) {
/* 40 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public byte[] doFinal(byte[] M) {
/* 45 */     return this.mac.doFinal(M);
/*    */   }
/*    */   
/*    */   public byte[] doFinal() {
/* 49 */     return this.mac.doFinal();
/*    */   }
/*    */   
/*    */   public int getHLen() {
/* 53 */     return this.hLen;
/*    */   }
/*    */   
/*    */   public void init(byte[] P) {
/*    */     try {
/* 58 */       this.mac.init(new SecretKeySpec(P, this.macAlgorithm));
/* 59 */     } catch (InvalidKeyException e) {
/* 60 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void update(byte[] U) {
/*    */     try {
/* 66 */       this.mac.update(U);
/* 67 */     } catch (IllegalStateException e) {
/* 68 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void update(byte[] U, int start, int len) {
/*    */     try {
/* 74 */       this.mac.update(U, start, len);
/* 75 */     } catch (IllegalStateException e) {
/* 76 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\PBKDF2\MacBasedPRF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */