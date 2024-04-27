/*    */ package net.lingala.zip4j.io.outputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.zip.Deflater;
/*    */ import net.lingala.zip4j.model.enums.CompressionLevel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DeflaterOutputStream
/*    */   extends CompressedOutputStream
/*    */ {
/* 28 */   private byte[] buff = new byte[4096];
/*    */   protected Deflater deflater;
/*    */   
/*    */   public DeflaterOutputStream(CipherOutputStream cipherOutputStream, CompressionLevel compressionLevel) {
/* 32 */     super(cipherOutputStream);
/* 33 */     this.deflater = new Deflater(compressionLevel.getLevel(), true);
/*    */   }
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/* 37 */     write(b, 0, b.length);
/*    */   }
/*    */   
/*    */   public void write(int bval) throws IOException {
/* 41 */     byte[] b = new byte[1];
/* 42 */     b[0] = (byte)bval;
/* 43 */     write(b, 0, 1);
/*    */   }
/*    */   
/*    */   public void write(byte[] buf, int off, int len) throws IOException {
/* 47 */     this.deflater.setInput(buf, off, len);
/* 48 */     while (!this.deflater.needsInput()) {
/* 49 */       deflate();
/*    */     }
/*    */   }
/*    */   
/*    */   private void deflate() throws IOException {
/* 54 */     int len = this.deflater.deflate(this.buff, 0, this.buff.length);
/* 55 */     if (len > 0) {
/* 56 */       super.write(this.buff, 0, len);
/*    */     }
/*    */   }
/*    */   
/*    */   public void closeEntry() throws IOException {
/* 61 */     if (!this.deflater.finished()) {
/* 62 */       this.deflater.finish();
/* 63 */       while (!this.deflater.finished()) {
/* 64 */         deflate();
/*    */       }
/*    */     } 
/* 67 */     this.deflater.end();
/* 68 */     super.closeEntry();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\DeflaterOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */