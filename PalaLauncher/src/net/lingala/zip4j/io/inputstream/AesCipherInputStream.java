/*     */ package net.lingala.zip4j.io.inputstream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import net.lingala.zip4j.crypto.AESDecrypter;
/*     */ import net.lingala.zip4j.crypto.Decrypter;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.model.AESExtraDataRecord;
/*     */ import net.lingala.zip4j.model.LocalFileHeader;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*     */ import net.lingala.zip4j.util.Zip4jUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AesCipherInputStream
/*     */   extends CipherInputStream<AESDecrypter>
/*     */ {
/*  20 */   private byte[] singleByteBuffer = new byte[1];
/*  21 */   private byte[] aes16ByteBlock = new byte[16];
/*  22 */   private int aes16ByteBlockPointer = 0;
/*  23 */   private int remainingAes16ByteBlockLength = 0;
/*  24 */   private int lengthToRead = 0;
/*  25 */   private int offsetWithAesBlock = 0;
/*  26 */   private int bytesCopiedInThisIteration = 0;
/*  27 */   private int lengthToCopyInThisIteration = 0;
/*  28 */   private int aes16ByteBlockReadLength = 0;
/*     */ 
/*     */   
/*     */   public AesCipherInputStream(ZipEntryInputStream zipEntryInputStream, LocalFileHeader localFileHeader, char[] password) throws IOException {
/*  32 */     super(zipEntryInputStream, localFileHeader, password);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AESDecrypter initializeDecrypter(LocalFileHeader localFileHeader, char[] password) throws IOException {
/*  37 */     return new AESDecrypter(localFileHeader.getAesExtraDataRecord(), password, getSalt(localFileHeader), getPasswordVerifier());
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  42 */     int readLen = read(this.singleByteBuffer);
/*     */     
/*  44 */     if (readLen == -1) {
/*  45 */       return -1;
/*     */     }
/*     */     
/*  48 */     return this.singleByteBuffer[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  53 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  58 */     this.lengthToRead = len;
/*  59 */     this.offsetWithAesBlock = off;
/*  60 */     this.bytesCopiedInThisIteration = 0;
/*     */     
/*  62 */     if (this.remainingAes16ByteBlockLength != 0) {
/*  63 */       copyBytesFromBuffer(b, this.offsetWithAesBlock);
/*     */       
/*  65 */       if (this.bytesCopiedInThisIteration == len) {
/*  66 */         return this.bytesCopiedInThisIteration;
/*     */       }
/*     */     } 
/*     */     
/*  70 */     if (this.lengthToRead < 16) {
/*  71 */       this.aes16ByteBlockReadLength = super.read(this.aes16ByteBlock, 0, this.aes16ByteBlock.length);
/*  72 */       this.aes16ByteBlockPointer = 0;
/*     */       
/*  74 */       if (this.aes16ByteBlockReadLength == -1) {
/*  75 */         this.remainingAes16ByteBlockLength = 0;
/*     */         
/*  77 */         if (this.bytesCopiedInThisIteration > 0) {
/*  78 */           return this.bytesCopiedInThisIteration;
/*     */         }
/*     */         
/*  81 */         return -1;
/*     */       } 
/*     */       
/*  84 */       this.remainingAes16ByteBlockLength = this.aes16ByteBlockReadLength;
/*     */       
/*  86 */       copyBytesFromBuffer(b, this.offsetWithAesBlock);
/*     */       
/*  88 */       if (this.bytesCopiedInThisIteration == len) {
/*  89 */         return this.bytesCopiedInThisIteration;
/*     */       }
/*     */     } 
/*     */     
/*  93 */     int readLen = super.read(b, this.offsetWithAesBlock, this.lengthToRead - this.lengthToRead % 16);
/*     */     
/*  95 */     if (readLen == -1) {
/*  96 */       if (this.bytesCopiedInThisIteration > 0) {
/*  97 */         return this.bytesCopiedInThisIteration;
/*     */       }
/*  99 */       return -1;
/*     */     } 
/*     */     
/* 102 */     return readLen + this.bytesCopiedInThisIteration;
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyBytesFromBuffer(byte[] b, int off) {
/* 107 */     this.lengthToCopyInThisIteration = (this.lengthToRead < this.remainingAes16ByteBlockLength) ? this.lengthToRead : this.remainingAes16ByteBlockLength;
/* 108 */     System.arraycopy(this.aes16ByteBlock, this.aes16ByteBlockPointer, b, off, this.lengthToCopyInThisIteration);
/*     */     
/* 110 */     incrementAesByteBlockPointer(this.lengthToCopyInThisIteration);
/* 111 */     decrementRemainingAesBytesLength(this.lengthToCopyInThisIteration);
/*     */     
/* 113 */     this.bytesCopiedInThisIteration += this.lengthToCopyInThisIteration;
/*     */     
/* 115 */     this.lengthToRead -= this.lengthToCopyInThisIteration;
/* 116 */     this.offsetWithAesBlock += this.lengthToCopyInThisIteration;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endOfEntryReached(InputStream inputStream) throws IOException {
/* 121 */     verifyContent(readStoredMac(inputStream));
/*     */   }
/*     */   
/*     */   private void verifyContent(byte[] storedMac) throws IOException {
/* 125 */     if (getLocalFileHeader().isDataDescriptorExists() && CompressionMethod.DEFLATE
/* 126 */       .equals(Zip4jUtil.getCompressionMethod(getLocalFileHeader()))) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     byte[] calculatedMac = getDecrypter().getCalculatedAuthenticationBytes();
/* 136 */     byte[] first10BytesOfCalculatedMac = new byte[10];
/* 137 */     System.arraycopy(calculatedMac, 0, first10BytesOfCalculatedMac, 0, 10);
/*     */     
/* 139 */     if (!Arrays.equals(storedMac, first10BytesOfCalculatedMac)) {
/* 140 */       throw new IOException("Reached end of data for this entry, but aes verification failed");
/*     */     }
/*     */   }
/*     */   
/*     */   protected byte[] readStoredMac(InputStream inputStream) throws IOException {
/* 145 */     byte[] storedMac = new byte[10];
/* 146 */     int readLen = Zip4jUtil.readFully(inputStream, storedMac);
/*     */     
/* 148 */     if (readLen != 10) {
/* 149 */       throw new ZipException("Invalid AES Mac bytes. Could not read sufficient data");
/*     */     }
/*     */     
/* 152 */     return storedMac;
/*     */   }
/*     */   
/*     */   private byte[] getSalt(LocalFileHeader localFileHeader) throws IOException {
/* 156 */     if (localFileHeader.getAesExtraDataRecord() == null) {
/* 157 */       throw new IOException("invalid aes extra data record");
/*     */     }
/*     */     
/* 160 */     AESExtraDataRecord aesExtraDataRecord = localFileHeader.getAesExtraDataRecord();
/* 161 */     byte[] saltBytes = new byte[aesExtraDataRecord.getAesKeyStrength().getSaltLength()];
/* 162 */     readRaw(saltBytes);
/* 163 */     return saltBytes;
/*     */   }
/*     */   
/*     */   private byte[] getPasswordVerifier() throws IOException {
/* 167 */     byte[] pvBytes = new byte[2];
/* 168 */     readRaw(pvBytes);
/* 169 */     return pvBytes;
/*     */   }
/*     */   
/*     */   private void incrementAesByteBlockPointer(int incrementBy) {
/* 173 */     this.aes16ByteBlockPointer += incrementBy;
/*     */     
/* 175 */     if (this.aes16ByteBlockPointer >= 15) {
/* 176 */       this.aes16ByteBlockPointer = 15;
/*     */     }
/*     */   }
/*     */   
/*     */   private void decrementRemainingAesBytesLength(int decrementBy) {
/* 181 */     this.remainingAes16ByteBlockLength -= decrementBy;
/*     */     
/* 183 */     if (this.remainingAes16ByteBlockLength <= 0)
/* 184 */       this.remainingAes16ByteBlockLength = 0; 
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\AesCipherInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */