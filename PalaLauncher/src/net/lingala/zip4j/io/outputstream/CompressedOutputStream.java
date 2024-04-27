/*    */ package net.lingala.zip4j.io.outputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ abstract class CompressedOutputStream
/*    */   extends OutputStream {
/*    */   private CipherOutputStream cipherOutputStream;
/*    */   
/*    */   public CompressedOutputStream(CipherOutputStream cipherOutputStream) {
/* 11 */     this.cipherOutputStream = cipherOutputStream;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 16 */     write(new byte[] { (byte)b });
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/* 21 */     write(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 26 */     this.cipherOutputStream.write(b, off, len);
/*    */   }
/*    */   
/*    */   protected void closeEntry() throws IOException {
/* 30 */     this.cipherOutputStream.closeEntry();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 35 */     this.cipherOutputStream.close();
/*    */   }
/*    */   
/*    */   public long getCompressedSize() {
/* 39 */     return this.cipherOutputStream.getNumberOfBytesWrittenForThisEntry();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\CompressedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */