/*     */ package net.lingala.zip4j.crypto.PBKDF2;
/*     */ 
/*     */ import net.lingala.zip4j.util.Zip4jUtil;
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
/*     */ public class PBKDF2Engine
/*     */ {
/*     */   private PBKDF2Parameters parameters;
/*     */   private PRF prf;
/*     */   
/*     */   public PBKDF2Engine(PBKDF2Parameters parameters) {
/*  32 */     this(parameters, null);
/*     */   }
/*     */   
/*     */   public PBKDF2Engine(PBKDF2Parameters parameters, PRF prf) {
/*  36 */     this.parameters = parameters;
/*  37 */     this.prf = prf;
/*     */   }
/*     */   
/*     */   public byte[] deriveKey(char[] inputPassword) {
/*  41 */     return deriveKey(inputPassword, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] deriveKey(char[] inputPassword, int dkLen) {
/*  46 */     if (inputPassword == null) {
/*  47 */       throw new NullPointerException();
/*     */     }
/*     */     
/*  50 */     byte[] p = Zip4jUtil.convertCharArrayToByteArray(inputPassword);
/*     */     
/*  52 */     assertPRF(p);
/*  53 */     if (dkLen == 0) {
/*  54 */       dkLen = this.prf.getHLen();
/*     */     }
/*  56 */     return PBKDF2(this.prf, this.parameters.getSalt(), this.parameters.getIterationCount(), dkLen);
/*     */   }
/*     */   
/*     */   public boolean verifyKey(char[] inputPassword) {
/*  60 */     byte[] referenceKey = getParameters().getDerivedKey();
/*  61 */     if (referenceKey == null || referenceKey.length == 0) {
/*  62 */       return false;
/*     */     }
/*  64 */     byte[] inputKey = deriveKey(inputPassword, referenceKey.length);
/*     */     
/*  66 */     if (inputKey == null || inputKey.length != referenceKey.length) {
/*  67 */       return false;
/*     */     }
/*  69 */     for (int i = 0; i < inputKey.length; i++) {
/*  70 */       if (inputKey[i] != referenceKey[i]) {
/*  71 */         return false;
/*     */       }
/*     */     } 
/*  74 */     return true;
/*     */   }
/*     */   
/*     */   private void assertPRF(byte[] P) {
/*  78 */     if (this.prf == null) {
/*  79 */       this.prf = new MacBasedPRF(this.parameters.getHashAlgorithm());
/*     */     }
/*  81 */     this.prf.init(P);
/*     */   }
/*     */   
/*     */   public PRF getPseudoRandomFunction() {
/*  85 */     return this.prf;
/*     */   }
/*     */   
/*     */   private byte[] PBKDF2(PRF prf, byte[] S, int c, int dkLen) {
/*  89 */     if (S == null) {
/*  90 */       S = new byte[0];
/*     */     }
/*  92 */     int hLen = prf.getHLen();
/*  93 */     int l = ceil(dkLen, hLen);
/*  94 */     int r = dkLen - (l - 1) * hLen;
/*  95 */     byte[] T = new byte[l * hLen];
/*  96 */     int ti_offset = 0;
/*  97 */     for (int i = 1; i <= l; i++) {
/*  98 */       _F(T, ti_offset, prf, S, c, i);
/*  99 */       ti_offset += hLen;
/*     */     } 
/* 101 */     if (r < hLen) {
/*     */       
/* 103 */       byte[] DK = new byte[dkLen];
/* 104 */       System.arraycopy(T, 0, DK, 0, dkLen);
/* 105 */       return DK;
/*     */     } 
/* 107 */     return T;
/*     */   }
/*     */   
/*     */   private int ceil(int a, int b) {
/* 111 */     int m = 0;
/* 112 */     if (a % b > 0) {
/* 113 */       m = 1;
/*     */     }
/* 115 */     return a / b + m;
/*     */   }
/*     */ 
/*     */   
/*     */   private void _F(byte[] dest, int offset, PRF prf, byte[] S, int c, int blockIndex) {
/* 120 */     int hLen = prf.getHLen();
/* 121 */     byte[] U_r = new byte[hLen];
/*     */ 
/*     */     
/* 124 */     byte[] U_i = new byte[S.length + 4];
/* 125 */     System.arraycopy(S, 0, U_i, 0, S.length);
/* 126 */     INT(U_i, S.length, blockIndex);
/*     */     
/* 128 */     for (int i = 0; i < c; i++) {
/* 129 */       U_i = prf.doFinal(U_i);
/* 130 */       xor(U_r, U_i);
/*     */     } 
/* 132 */     System.arraycopy(U_r, 0, dest, offset, hLen);
/*     */   }
/*     */   
/*     */   private void xor(byte[] dest, byte[] src) {
/* 136 */     for (int i = 0; i < dest.length; i++) {
/* 137 */       dest[i] = (byte)(dest[i] ^ src[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void INT(byte[] dest, int offset, int i) {
/* 142 */     dest[offset] = (byte)(i / 16777216);
/* 143 */     dest[offset + 1] = (byte)(i / 65536);
/* 144 */     dest[offset + 2] = (byte)(i / 256);
/* 145 */     dest[offset + 3] = (byte)i;
/*     */   }
/*     */   
/*     */   public PBKDF2Parameters getParameters() {
/* 149 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void setParameters(PBKDF2Parameters parameters) {
/* 153 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public void setPseudoRandomFunction(PRF prf) {
/* 157 */     this.prf = prf;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\PBKDF2\PBKDF2Engine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */