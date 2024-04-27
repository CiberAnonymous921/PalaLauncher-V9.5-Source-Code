/*    */ package net.lingala.zip4j.io.inputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.PushbackInputStream;
/*    */ 
/*    */ abstract class DecompressedInputStream
/*    */   extends InputStream {
/*    */   private CipherInputStream cipherInputStream;
/* 10 */   protected byte[] oneByteBuffer = new byte[1];
/*    */   
/*    */   public DecompressedInputStream(CipherInputStream cipherInputStream) {
/* 13 */     this.cipherInputStream = cipherInputStream;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 18 */     int readLen = read(this.oneByteBuffer);
/*    */     
/* 20 */     if (readLen == -1) {
/* 21 */       return -1;
/*    */     }
/*    */     
/* 24 */     return this.oneByteBuffer[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b) throws IOException {
/* 29 */     return read(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 34 */     return this.cipherInputStream.read(b, off, len);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 39 */     this.cipherInputStream.close();
/*    */   }
/*    */   
/*    */   public void endOfEntryReached(InputStream inputStream) throws IOException {
/* 43 */     this.cipherInputStream.endOfEntryReached(inputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public void pushBackInputStreamIfNecessary(PushbackInputStream pushbackInputStream) throws IOException {}
/*    */ 
/*    */   
/*    */   protected byte[] getLastReadRawDataCache() {
/* 51 */     return this.cipherInputStream.getLastReadRawDataCache();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\DecompressedInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */