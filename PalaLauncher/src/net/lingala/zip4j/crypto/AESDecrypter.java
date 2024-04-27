/*     */ package net.lingala.zip4j.crypto;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
/*     */ import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
/*     */ import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
/*     */ import net.lingala.zip4j.crypto.engine.AESEngine;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.model.AESExtraDataRecord;
/*     */ import net.lingala.zip4j.model.enums.AesKeyStrength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AESDecrypter
/*     */   implements Decrypter
/*     */ {
/*     */   public static final int PASSWORD_VERIFIER_LENGTH = 2;
/*     */   private AESExtraDataRecord aesExtraDataRecord;
/*     */   private char[] password;
/*     */   private AESEngine aesEngine;
/*     */   private MacBasedPRF mac;
/*  41 */   private int nonce = 1;
/*     */   private byte[] iv;
/*     */   private byte[] counterBlock;
/*     */   
/*     */   public AESDecrypter(AESExtraDataRecord aesExtraDataRecord, char[] password, byte[] salt, byte[] passwordVerifier) throws ZipException {
/*  46 */     this.aesExtraDataRecord = aesExtraDataRecord;
/*  47 */     this.password = password;
/*  48 */     this.iv = new byte[16];
/*  49 */     this.counterBlock = new byte[16];
/*  50 */     init(salt, passwordVerifier);
/*     */   }
/*     */   
/*     */   private void init(byte[] salt, byte[] passwordVerifier) throws ZipException {
/*  54 */     AesKeyStrength aesKeyStrength = this.aesExtraDataRecord.getAesKeyStrength();
/*     */     
/*  56 */     if (this.password == null || this.password.length <= 0) {
/*  57 */       throw new ZipException("empty or null password provided for AES Decryptor");
/*     */     }
/*     */     
/*  60 */     byte[] derivedKey = deriveKey(salt, this.password, aesKeyStrength.getKeyLength(), aesKeyStrength.getMacLength());
/*  61 */     if (derivedKey == null || derivedKey.length != aesKeyStrength.getKeyLength() + aesKeyStrength.getMacLength() + 2)
/*     */     {
/*  63 */       throw new ZipException("invalid derived key");
/*     */     }
/*     */     
/*  66 */     byte[] aesKey = new byte[aesKeyStrength.getKeyLength()];
/*  67 */     byte[] macKey = new byte[aesKeyStrength.getMacLength()];
/*  68 */     byte[] derivedPasswordVerifier = new byte[2];
/*     */     
/*  70 */     System.arraycopy(derivedKey, 0, aesKey, 0, aesKeyStrength.getKeyLength());
/*  71 */     System.arraycopy(derivedKey, aesKeyStrength.getKeyLength(), macKey, 0, aesKeyStrength.getMacLength());
/*  72 */     System.arraycopy(derivedKey, aesKeyStrength.getKeyLength() + aesKeyStrength.getMacLength(), derivedPasswordVerifier, 0, 2);
/*     */ 
/*     */     
/*  75 */     if (!Arrays.equals(passwordVerifier, derivedPasswordVerifier)) {
/*  76 */       throw new ZipException("Wrong Password", ZipException.Type.WRONG_PASSWORD);
/*     */     }
/*     */     
/*  79 */     this.aesEngine = new AESEngine(aesKey);
/*  80 */     this.mac = new MacBasedPRF("HmacSHA1");
/*  81 */     this.mac.init(macKey);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int decryptData(byte[] buff, int start, int len) throws ZipException {
/*  87 */     for (int j = start; j < start + len; j += 16) {
/*  88 */       int loopCount = (j + 16 <= start + len) ? 16 : (start + len - j);
/*     */ 
/*     */       
/*  91 */       this.mac.update(buff, j, loopCount);
/*  92 */       AesCipherUtil.prepareBuffAESIVBytes(this.iv, this.nonce);
/*  93 */       this.aesEngine.processBlock(this.iv, this.counterBlock);
/*     */       
/*  95 */       for (int k = 0; k < loopCount; k++) {
/*  96 */         buff[j + k] = (byte)(buff[j + k] ^ this.counterBlock[k]);
/*     */       }
/*     */       
/*  99 */       this.nonce++;
/*     */     } 
/*     */     
/* 102 */     return len;
/*     */   }
/*     */   
/*     */   private byte[] deriveKey(byte[] salt, char[] password, int keyLength, int macLength) {
/* 106 */     PBKDF2Parameters p = new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", salt, 1000);
/* 107 */     PBKDF2Engine e = new PBKDF2Engine(p);
/* 108 */     return e.deriveKey(password, keyLength + macLength + 2);
/*     */   }
/*     */   
/*     */   public byte[] getCalculatedAuthenticationBytes() {
/* 112 */     return this.mac.doFinal();
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\AESDecrypter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */