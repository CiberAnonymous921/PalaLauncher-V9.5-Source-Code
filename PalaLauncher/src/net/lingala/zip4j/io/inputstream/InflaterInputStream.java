/*    */ package net.lingala.zip4j.io.inputstream;
/*    */ 
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.PushbackInputStream;
/*    */ import java.util.zip.DataFormatException;
/*    */ import java.util.zip.Inflater;
/*    */ 
/*    */ 
/*    */ public class InflaterInputStream
/*    */   extends DecompressedInputStream
/*    */ {
/*    */   private Inflater inflater;
/*    */   private byte[] buff;
/* 16 */   private byte[] singleByteBuffer = new byte[1];
/*    */   private int len;
/*    */   
/*    */   public InflaterInputStream(CipherInputStream cipherInputStream) {
/* 20 */     super(cipherInputStream);
/* 21 */     this.inflater = new Inflater(true);
/* 22 */     this.buff = new byte[4096];
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 27 */     int readLen = read(this.singleByteBuffer);
/*    */     
/* 29 */     if (readLen == -1) {
/* 30 */       return -1;
/*    */     }
/*    */     
/* 33 */     return this.singleByteBuffer[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b) throws IOException {
/* 38 */     return read(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/*    */     try {
/*    */       int n;
/* 45 */       while ((n = this.inflater.inflate(b, off, len)) == 0) {
/* 46 */         if (this.inflater.finished() || this.inflater.needsDictionary()) {
/* 47 */           return -1;
/*    */         }
/* 49 */         if (this.inflater.needsInput()) {
/* 50 */           fill();
/*    */         }
/*    */       } 
/* 53 */       return n;
/* 54 */     } catch (DataFormatException e) {
/* 55 */       throw new IOException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void endOfEntryReached(InputStream inputStream) throws IOException {
/* 61 */     if (this.inflater != null) {
/* 62 */       this.inflater.end();
/* 63 */       this.inflater = null;
/*    */     } 
/* 65 */     super.endOfEntryReached(inputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public void pushBackInputStreamIfNecessary(PushbackInputStream pushbackInputStream) throws IOException {
/* 70 */     int n = this.inflater.getRemaining();
/* 71 */     if (n > 0) {
/* 72 */       byte[] rawDataCache = getLastReadRawDataCache();
/* 73 */       pushbackInputStream.unread(rawDataCache, this.len - n, n);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 79 */     if (this.inflater != null) {
/* 80 */       this.inflater.end();
/*    */     }
/* 82 */     super.close();
/*    */   }
/*    */   
/*    */   private void fill() throws IOException {
/* 86 */     this.len = super.read(this.buff, 0, this.buff.length);
/* 87 */     if (this.len == -1) {
/* 88 */       throw new EOFException("Unexpected end of input stream");
/*    */     }
/* 90 */     this.inflater.setInput(this.buff, 0, this.len);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\InflaterInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */