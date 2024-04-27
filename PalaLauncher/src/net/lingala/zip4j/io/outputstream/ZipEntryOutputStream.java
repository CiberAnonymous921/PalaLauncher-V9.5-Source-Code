/*    */ package net.lingala.zip4j.io.outputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ class ZipEntryOutputStream
/*    */   extends OutputStream {
/*  8 */   private long numberOfBytesWrittenForThisEntry = 0L;
/*    */   private OutputStream outputStream;
/*    */   private boolean entryClosed;
/*    */   
/*    */   public ZipEntryOutputStream(OutputStream outputStream) {
/* 13 */     this.outputStream = outputStream;
/* 14 */     this.entryClosed = false;
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
/* 29 */     if (this.entryClosed) {
/* 30 */       throw new IllegalStateException("ZipEntryOutputStream is closed");
/*    */     }
/*    */     
/* 33 */     this.outputStream.write(b, off, len);
/* 34 */     this.numberOfBytesWrittenForThisEntry += len;
/*    */   }
/*    */   
/*    */   public void closeEntry() throws IOException {
/* 38 */     this.entryClosed = true;
/*    */   }
/*    */   
/*    */   public long getNumberOfBytesWrittenForThisEntry() {
/* 42 */     return this.numberOfBytesWrittenForThisEntry;
/*    */   }
/*    */   
/*    */   public void close() throws IOException {}
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\ZipEntryOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */