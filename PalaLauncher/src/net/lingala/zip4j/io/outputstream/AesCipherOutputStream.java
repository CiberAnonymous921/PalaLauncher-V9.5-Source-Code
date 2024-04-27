/*    */ package net.lingala.zip4j.io.outputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import net.lingala.zip4j.crypto.AESEncrpyter;
/*    */ import net.lingala.zip4j.crypto.Encrypter;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.model.ZipParameters;
/*    */ 
/*    */ 
/*    */ class AesCipherOutputStream
/*    */   extends CipherOutputStream<AESEncrpyter>
/*    */ {
/* 14 */   private byte[] pendingBuffer = new byte[16];
/* 15 */   private int pendingBufferLength = 0;
/*    */   
/*    */   public AesCipherOutputStream(ZipEntryOutputStream outputStream, ZipParameters zipParameters, char[] password) throws IOException, ZipException {
/* 18 */     super(outputStream, zipParameters, password);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AESEncrpyter initializeEncrypter(OutputStream outputStream, ZipParameters zipParameters, char[] password) throws IOException, ZipException {
/* 23 */     AESEncrpyter encrypter = new AESEncrpyter(password, zipParameters.getAesKeyStrength());
/* 24 */     writeAesEncryptionHeaderData(encrypter);
/* 25 */     return encrypter;
/*    */   }
/*    */   
/*    */   private void writeAesEncryptionHeaderData(AESEncrpyter encrypter) throws IOException {
/* 29 */     writeHeaders(encrypter.getSaltBytes());
/* 30 */     writeHeaders(encrypter.getDerivedPasswordVerifier());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 35 */     write(new byte[] { (byte)b });
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/* 40 */     write(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 45 */     if (len >= 16 - this.pendingBufferLength) {
/* 46 */       System.arraycopy(b, off, this.pendingBuffer, this.pendingBufferLength, 16 - this.pendingBufferLength);
/* 47 */       super.write(this.pendingBuffer, 0, this.pendingBuffer.length);
/* 48 */       off = 16 - this.pendingBufferLength;
/* 49 */       len -= off;
/* 50 */       this.pendingBufferLength = 0;
/*    */     } else {
/* 52 */       System.arraycopy(b, off, this.pendingBuffer, this.pendingBufferLength, len);
/* 53 */       this.pendingBufferLength += len;
/*    */       
/*    */       return;
/*    */     } 
/* 57 */     if (len != 0 && len % 16 != 0) {
/* 58 */       System.arraycopy(b, len + off - len % 16, this.pendingBuffer, 0, len % 16);
/* 59 */       this.pendingBufferLength = len % 16;
/* 60 */       len -= this.pendingBufferLength;
/*    */     } 
/*    */     
/* 63 */     super.write(b, off, len);
/*    */   }
/*    */ 
/*    */   
/*    */   public void closeEntry() throws IOException {
/* 68 */     if (this.pendingBufferLength != 0) {
/* 69 */       super.write(this.pendingBuffer, 0, this.pendingBufferLength);
/* 70 */       this.pendingBufferLength = 0;
/*    */     } 
/*    */     
/* 73 */     writeHeaders(getEncrypter().getFinalMac());
/* 74 */     super.closeEntry();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\AesCipherOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */