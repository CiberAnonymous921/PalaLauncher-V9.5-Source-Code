/*    */ package net.lingala.zip4j.io.outputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ 
/*    */ public class CountingOutputStream
/*    */   extends OutputStream
/*    */   implements OutputStreamWithSplitZipSupport {
/*    */   private OutputStream outputStream;
/* 11 */   private long numberOfBytesWritten = 0L;
/*    */   
/*    */   public CountingOutputStream(OutputStream outputStream) {
/* 14 */     this.outputStream = outputStream;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 19 */     write(new byte[] { (byte)b });
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/* 24 */     write(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 29 */     this.outputStream.write(b, off, len);
/* 30 */     this.numberOfBytesWritten += len;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCurrentSplitFileCounter() {
/* 35 */     if (isSplitZipFile()) {
/* 36 */       return ((SplitOutputStream)this.outputStream).getCurrentSplitFileCounter();
/*    */     }
/*    */     
/* 39 */     return 0;
/*    */   }
/*    */   
/*    */   public long getOffsetForNextEntry() throws IOException {
/* 43 */     if (this.outputStream instanceof SplitOutputStream) {
/* 44 */       return ((SplitOutputStream)this.outputStream).getFilePointer();
/*    */     }
/*    */     
/* 47 */     return this.numberOfBytesWritten;
/*    */   }
/*    */   
/*    */   public long getSplitLength() {
/* 51 */     if (isSplitZipFile()) {
/* 52 */       return ((SplitOutputStream)this.outputStream).getSplitLength();
/*    */     }
/*    */     
/* 55 */     return 0L;
/*    */   }
/*    */   
/*    */   public boolean isSplitZipFile() {
/* 59 */     return (this.outputStream instanceof SplitOutputStream && ((SplitOutputStream)this.outputStream)
/* 60 */       .isSplitZipFile());
/*    */   }
/*    */   
/*    */   public long getNumberOfBytesWritten() throws IOException {
/* 64 */     if (this.outputStream instanceof SplitOutputStream) {
/* 65 */       return ((SplitOutputStream)this.outputStream).getFilePointer();
/*    */     }
/*    */     
/* 68 */     return this.numberOfBytesWritten;
/*    */   }
/*    */   
/*    */   public boolean checkBuffSizeAndStartNextSplitFile(int bufferSize) throws ZipException {
/* 72 */     if (!isSplitZipFile()) {
/* 73 */       return false;
/*    */     }
/*    */     
/* 76 */     return ((SplitOutputStream)this.outputStream).checkBufferSizeAndStartNextSplitFile(bufferSize);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getFilePointer() throws IOException {
/* 81 */     if (this.outputStream instanceof SplitOutputStream) {
/* 82 */       return ((SplitOutputStream)this.outputStream).getFilePointer();
/*    */     }
/*    */     
/* 85 */     return this.numberOfBytesWritten;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 90 */     this.outputStream.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\CountingOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */