/*    */ package net.lingala.zip4j.io.inputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ class ZipEntryInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private static final int MAX_RAW_READ_FULLY_RETRY_ATTEMPTS = 15;
/*    */   private InputStream inputStream;
/* 11 */   private long numberOfBytesRead = 0L;
/* 12 */   private byte[] singleByteArray = new byte[1];
/*    */   private long compressedSize;
/*    */   
/*    */   public ZipEntryInputStream(InputStream inputStream, long compressedSize) {
/* 16 */     this.inputStream = inputStream;
/* 17 */     this.compressedSize = compressedSize;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 22 */     int readLen = read(this.singleByteArray);
/* 23 */     if (readLen == -1) {
/* 24 */       return -1;
/*    */     }
/*    */     
/* 27 */     return this.singleByteArray[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b) throws IOException {
/* 32 */     return read(b, 0, b.length);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 38 */     if (this.compressedSize != -1L) {
/* 39 */       if (this.numberOfBytesRead >= this.compressedSize) {
/* 40 */         return -1;
/*    */       }
/*    */       
/* 43 */       if (len > this.compressedSize - this.numberOfBytesRead) {
/* 44 */         len = (int)(this.compressedSize - this.numberOfBytesRead);
/*    */       }
/*    */     } 
/*    */     
/* 48 */     int readLen = this.inputStream.read(b, off, len);
/*    */     
/* 50 */     if (readLen > 0) {
/* 51 */       this.numberOfBytesRead += readLen;
/*    */     }
/*    */     
/* 54 */     return readLen;
/*    */   }
/*    */ 
/*    */   
/*    */   public int readRawFully(byte[] b) throws IOException {
/* 59 */     int readLen = this.inputStream.read(b);
/*    */     
/* 61 */     if (readLen != b.length) {
/* 62 */       readLen = readUntilBufferIsFull(b, readLen);
/*    */       
/* 64 */       if (readLen != b.length) {
/* 65 */         throw new IOException("Cannot read fully into byte buffer");
/*    */       }
/*    */     } 
/*    */     
/* 69 */     return readLen;
/*    */   }
/*    */   
/*    */   private int readUntilBufferIsFull(byte[] b, int readLength) throws IOException {
/* 73 */     int remainingLength = b.length - readLength;
/* 74 */     int loopReadLength = 0;
/* 75 */     int retryAttempt = 0;
/*    */     
/* 77 */     while (readLength < b.length && loopReadLength != -1 && retryAttempt < 15) {
/* 78 */       loopReadLength += this.inputStream.read(b, readLength, remainingLength);
/*    */       
/* 80 */       if (loopReadLength > 0) {
/* 81 */         readLength += loopReadLength;
/* 82 */         remainingLength -= loopReadLength;
/*    */       } 
/*    */       
/* 85 */       retryAttempt++;
/*    */     } 
/*    */     
/* 88 */     return readLength;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 93 */     this.inputStream.close();
/*    */   }
/*    */   
/*    */   public long getNumberOfBytesRead() {
/* 97 */     return this.numberOfBytesRead;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\ZipEntryInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */