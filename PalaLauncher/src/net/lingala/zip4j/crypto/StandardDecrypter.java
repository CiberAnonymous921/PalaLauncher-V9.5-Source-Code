/*    */ package net.lingala.zip4j.crypto;
/*    */ 
/*    */ import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
/*    */ import net.lingala.zip4j.exception.ZipException;
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
/*    */ public class StandardDecrypter
/*    */   implements Decrypter
/*    */ {
/*    */   private char[] password;
/*    */   private byte[] crcBytes;
/* 28 */   private byte[] crc = new byte[4];
/*    */   private ZipCryptoEngine zipCryptoEngine;
/*    */   
/*    */   public StandardDecrypter(char[] password, byte[] crcBytes, byte[] headerBytes) throws ZipException {
/* 32 */     this.password = password;
/* 33 */     this.crcBytes = crcBytes;
/* 34 */     this.zipCryptoEngine = new ZipCryptoEngine();
/* 35 */     init(headerBytes);
/*    */   }
/*    */   
/*    */   public int decryptData(byte[] buff, int start, int len) throws ZipException {
/* 39 */     if (start < 0 || len < 0) {
/* 40 */       throw new ZipException("one of the input parameters were null in standard decrypt data");
/*    */     }
/*    */     
/* 43 */     for (int i = start; i < start + len; i++) {
/* 44 */       int val = buff[i] & 0xFF;
/* 45 */       val = (val ^ this.zipCryptoEngine.decryptByte()) & 0xFF;
/* 46 */       this.zipCryptoEngine.updateKeys((byte)val);
/* 47 */       buff[i] = (byte)val;
/*    */     } 
/*    */     
/* 50 */     return len;
/*    */   }
/*    */   
/*    */   private void init(byte[] headerBytes) throws ZipException {
/* 54 */     this.crc[3] = (byte)(this.crcBytes[3] & 0xFF);
/* 55 */     this.crc[2] = (byte)(this.crcBytes[3] >> 8 & 0xFF);
/* 56 */     this.crc[1] = (byte)(this.crcBytes[3] >> 16 & 0xFF);
/* 57 */     this.crc[0] = (byte)(this.crcBytes[3] >> 24 & 0xFF);
/*    */     
/* 59 */     if (this.crc[2] > 0 || this.crc[1] > 0 || this.crc[0] > 0) {
/* 60 */       throw new IllegalStateException("Invalid CRC in File Header");
/*    */     }
/* 62 */     if (this.password == null || this.password.length <= 0) {
/* 63 */       throw new ZipException("Wrong password!", ZipException.Type.WRONG_PASSWORD);
/*    */     }
/*    */     
/* 66 */     this.zipCryptoEngine.initKeys(this.password);
/*    */     
/* 68 */     int result = headerBytes[0];
/* 69 */     for (int i = 0; i < 12; i++) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 76 */       this.zipCryptoEngine.updateKeys((byte)(result ^ this.zipCryptoEngine.decryptByte()));
/* 77 */       if (i + 1 != 12)
/* 78 */         result = headerBytes[i + 1]; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\StandardDecrypter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */