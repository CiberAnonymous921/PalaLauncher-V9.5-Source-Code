/*    */ package net.lingala.zip4j.crypto;
/*    */ 
/*    */ import java.util.Random;
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
/*    */ 
/*    */ public class StandardEncrypter
/*    */   implements Encrypter
/*    */ {
/*    */   private ZipCryptoEngine zipCryptoEngine;
/*    */   private byte[] headerBytes;
/*    */   
/*    */   public StandardEncrypter(char[] password, long key) throws ZipException {
/* 32 */     this.zipCryptoEngine = new ZipCryptoEngine();
/*    */     
/* 34 */     this.headerBytes = new byte[12];
/* 35 */     init(password, key);
/*    */   }
/*    */   
/*    */   private void init(char[] password, long key) throws ZipException {
/* 39 */     if (password == null || password.length <= 0) {
/* 40 */       throw new ZipException("input password is null or empty, cannot initialize standard encrypter");
/*    */     }
/* 42 */     this.zipCryptoEngine.initKeys(password);
/* 43 */     this.headerBytes = generateRandomBytes(12);
/*    */     
/* 45 */     this.zipCryptoEngine.initKeys(password);
/*    */     
/* 47 */     this.headerBytes[11] = (byte)(int)(key >>> 24L);
/* 48 */     this.headerBytes[10] = (byte)(int)(key >>> 16L);
/*    */     
/* 50 */     if (this.headerBytes.length < 12) {
/* 51 */       throw new ZipException("invalid header bytes generated, cannot perform standard encryption");
/*    */     }
/*    */     
/* 54 */     encryptData(this.headerBytes);
/*    */   }
/*    */   
/*    */   public int encryptData(byte[] buff) throws ZipException {
/* 58 */     if (buff == null) {
/* 59 */       throw new NullPointerException();
/*    */     }
/* 61 */     return encryptData(buff, 0, buff.length);
/*    */   }
/*    */   
/*    */   public int encryptData(byte[] buff, int start, int len) throws ZipException {
/* 65 */     if (len < 0) {
/* 66 */       throw new ZipException("invalid length specified to decrpyt data");
/*    */     }
/*    */     
/* 69 */     for (int i = start; i < start + len; i++) {
/* 70 */       buff[i] = encryptByte(buff[i]);
/*    */     }
/* 72 */     return len;
/*    */   }
/*    */   
/*    */   protected byte encryptByte(byte val) {
/* 76 */     byte temp_val = (byte)(val ^ this.zipCryptoEngine.decryptByte() & 0xFF);
/* 77 */     this.zipCryptoEngine.updateKeys(val);
/* 78 */     return temp_val;
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] generateRandomBytes(int size) throws ZipException {
/* 83 */     if (size <= 0) {
/* 84 */       throw new ZipException("size is either 0 or less than 0, cannot generate header for standard encryptor");
/*    */     }
/*    */     
/* 87 */     byte[] buff = new byte[size];
/*    */     
/* 89 */     Random rand = new Random();
/*    */     
/* 91 */     for (int i = 0; i < buff.length; i++)
/*    */     {
/* 93 */       buff[i] = encryptByte((byte)rand.nextInt(256));
/*    */     }
/* 95 */     return buff;
/*    */   }
/*    */   
/*    */   public byte[] getHeaderBytes() {
/* 99 */     return this.headerBytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\StandardEncrypter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */