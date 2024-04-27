/*    */ package net.lingala.zip4j.crypto.engine;
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
/*    */ public class ZipCryptoEngine
/*    */ {
/* 21 */   private final int[] keys = new int[3];
/* 22 */   private static final int[] CRC_TABLE = new int[256];
/*    */   
/*    */   static {
/* 25 */     for (int i = 0; i < 256; i++) {
/* 26 */       int r = i;
/* 27 */       for (int j = 0; j < 8; j++) {
/* 28 */         if ((r & 0x1) == 1) {
/* 29 */           r = r >>> 1 ^ 0xEDB88320;
/*    */         } else {
/* 31 */           r >>>= 1;
/*    */         } 
/*    */       } 
/* 34 */       CRC_TABLE[i] = r;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void initKeys(char[] password) {
/* 39 */     this.keys[0] = 305419896;
/* 40 */     this.keys[1] = 591751049;
/* 41 */     this.keys[2] = 878082192;
/* 42 */     for (int i = 0; i < password.length; i++) {
/* 43 */       updateKeys((byte)(password[i] & 0xFF));
/*    */     }
/*    */   }
/*    */   
/*    */   public void updateKeys(byte charAt) {
/* 48 */     this.keys[0] = crc32(this.keys[0], charAt);
/* 49 */     this.keys[1] = this.keys[1] + (this.keys[0] & 0xFF);
/* 50 */     this.keys[1] = this.keys[1] * 134775813 + 1;
/* 51 */     this.keys[2] = crc32(this.keys[2], (byte)(this.keys[1] >> 24));
/*    */   }
/*    */   
/*    */   private int crc32(int oldCrc, byte charAt) {
/* 55 */     return oldCrc >>> 8 ^ CRC_TABLE[(oldCrc ^ charAt) & 0xFF];
/*    */   }
/*    */   
/*    */   public byte decryptByte() {
/* 59 */     int temp = this.keys[2] | 0x2;
/* 60 */     return (byte)(temp * (temp ^ 0x1) >>> 8);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\engine\ZipCryptoEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */