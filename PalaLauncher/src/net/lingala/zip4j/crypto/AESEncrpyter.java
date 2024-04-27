/*     */ package net.lingala.zip4j.crypto;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.lingala.zip4j.crypto.PBKDF2.MacBasedPRF;
/*     */ import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Engine;
/*     */ import net.lingala.zip4j.crypto.PBKDF2.PBKDF2Parameters;
/*     */ import net.lingala.zip4j.crypto.engine.AESEngine;
/*     */ import net.lingala.zip4j.exception.ZipException;
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
/*     */ 
/*     */ public class AESEncrpyter
/*     */   implements Encrypter
/*     */ {
/*     */   private static final int PASSWORD_VERIFIER_LENGTH = 2;
/*     */   private char[] password;
/*     */   private AesKeyStrength aesKeyStrength;
/*     */   private AESEngine aesEngine;
/*     */   private MacBasedPRF mac;
/*     */   private boolean finished;
/*  42 */   private int nonce = 1;
/*  43 */   private int loopCount = 0;
/*     */   
/*     */   private byte[] iv;
/*     */   private byte[] counterBlock;
/*     */   private byte[] derivedPasswordVerifier;
/*     */   private byte[] saltBytes;
/*     */   
/*     */   public AESEncrpyter(char[] password, AesKeyStrength aesKeyStrength) throws ZipException {
/*  51 */     if (password == null || password.length == 0) {
/*  52 */       throw new ZipException("input password is empty or null");
/*     */     }
/*  54 */     if (aesKeyStrength != AesKeyStrength.KEY_STRENGTH_128 && aesKeyStrength != AesKeyStrength.KEY_STRENGTH_256)
/*     */     {
/*  56 */       throw new ZipException("Invalid AES key strength");
/*     */     }
/*     */     
/*  59 */     this.password = password;
/*  60 */     this.aesKeyStrength = aesKeyStrength;
/*  61 */     this.finished = false;
/*  62 */     this.counterBlock = new byte[16];
/*  63 */     this.iv = new byte[16];
/*  64 */     init();
/*     */   }
/*     */   
/*     */   private void init() throws ZipException {
/*  68 */     int keyLength = this.aesKeyStrength.getKeyLength();
/*  69 */     int macLength = this.aesKeyStrength.getMacLength();
/*  70 */     int saltLength = this.aesKeyStrength.getSaltLength();
/*     */     
/*  72 */     this.saltBytes = generateSalt(saltLength);
/*  73 */     byte[] keyBytes = deriveKey(this.saltBytes, this.password, keyLength, macLength);
/*     */     
/*  75 */     if (keyBytes == null || keyBytes.length != keyLength + macLength + 2) {
/*  76 */       throw new ZipException("invalid key generated, cannot decrypt file");
/*     */     }
/*     */     
/*  79 */     byte[] aesKey = new byte[keyLength];
/*  80 */     byte[] macKey = new byte[macLength];
/*  81 */     this.derivedPasswordVerifier = new byte[2];
/*     */     
/*  83 */     System.arraycopy(keyBytes, 0, aesKey, 0, keyLength);
/*  84 */     System.arraycopy(keyBytes, keyLength, macKey, 0, macLength);
/*  85 */     System.arraycopy(keyBytes, keyLength + macLength, this.derivedPasswordVerifier, 0, 2);
/*     */     
/*  87 */     this.aesEngine = new AESEngine(aesKey);
/*  88 */     this.mac = new MacBasedPRF("HmacSHA1");
/*  89 */     this.mac.init(macKey);
/*     */   }
/*     */   
/*     */   private byte[] deriveKey(byte[] salt, char[] password, int keyLength, int macLength) throws ZipException {
/*     */     try {
/*  94 */       PBKDF2Parameters p = new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", salt, 1000);
/*     */       
/*  96 */       PBKDF2Engine e = new PBKDF2Engine(p);
/*  97 */       byte[] derivedKey = e.deriveKey(password, keyLength + macLength + 2);
/*  98 */       return derivedKey;
/*  99 */     } catch (Exception e) {
/* 100 */       throw new ZipException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int encryptData(byte[] buff) throws ZipException {
/* 106 */     if (buff == null) {
/* 107 */       throw new ZipException("input bytes are null, cannot perform AES encrpytion");
/*     */     }
/* 109 */     return encryptData(buff, 0, buff.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int encryptData(byte[] buff, int start, int len) throws ZipException {
/* 114 */     if (this.finished)
/*     */     {
/*     */ 
/*     */       
/* 118 */       throw new ZipException("AES Encrypter is in finished state (A non 16 byte block has already been passed to encrypter)");
/*     */     }
/*     */     
/* 121 */     if (len % 16 != 0) {
/* 122 */       this.finished = true;
/*     */     }
/*     */     
/* 125 */     for (int j = start; j < start + len; j += 16) {
/* 126 */       this.loopCount = (j + 16 <= start + len) ? 16 : (start + len - j);
/*     */ 
/*     */       
/* 129 */       AesCipherUtil.prepareBuffAESIVBytes(this.iv, this.nonce);
/* 130 */       this.aesEngine.processBlock(this.iv, this.counterBlock);
/*     */       
/* 132 */       for (int k = 0; k < this.loopCount; k++) {
/* 133 */         buff[j + k] = (byte)(buff[j + k] ^ this.counterBlock[k]);
/*     */       }
/*     */       
/* 136 */       this.mac.update(buff, j, this.loopCount);
/* 137 */       this.nonce++;
/*     */     } 
/*     */     
/* 140 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] generateSalt(int size) throws ZipException {
/* 145 */     if (size != 8 && size != 16) {
/* 146 */       throw new ZipException("invalid salt size, cannot generate salt");
/*     */     }
/*     */     
/* 149 */     int rounds = 0;
/*     */     
/* 151 */     if (size == 8)
/* 152 */       rounds = 2; 
/* 153 */     if (size == 16) {
/* 154 */       rounds = 4;
/*     */     }
/* 156 */     byte[] salt = new byte[size];
/* 157 */     for (int j = 0; j < rounds; j++) {
/* 158 */       Random rand = new Random();
/* 159 */       int i = rand.nextInt();
/* 160 */       salt[0 + j * 4] = (byte)(i >> 24);
/* 161 */       salt[1 + j * 4] = (byte)(i >> 16);
/* 162 */       salt[2 + j * 4] = (byte)(i >> 8);
/* 163 */       salt[3 + j * 4] = (byte)i;
/*     */     } 
/* 165 */     return salt;
/*     */   }
/*     */   
/*     */   public byte[] getFinalMac() {
/* 169 */     byte[] rawMacBytes = this.mac.doFinal();
/* 170 */     byte[] macBytes = new byte[10];
/* 171 */     System.arraycopy(rawMacBytes, 0, macBytes, 0, 10);
/* 172 */     return macBytes;
/*     */   }
/*     */   
/*     */   public byte[] getDerivedPasswordVerifier() {
/* 176 */     return this.derivedPasswordVerifier;
/*     */   }
/*     */   
/*     */   public byte[] getSaltBytes() {
/* 180 */     return this.saltBytes;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\AESEncrpyter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */