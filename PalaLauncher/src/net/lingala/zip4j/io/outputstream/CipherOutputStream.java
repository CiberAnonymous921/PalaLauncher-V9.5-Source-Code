/*    */ package net.lingala.zip4j.io.outputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import net.lingala.zip4j.crypto.Encrypter;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.model.ZipParameters;
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
/*    */ abstract class CipherOutputStream<T extends Encrypter>
/*    */   extends OutputStream
/*    */ {
/*    */   private ZipEntryOutputStream zipEntryOutputStream;
/*    */   private T encrypter;
/*    */   
/*    */   public CipherOutputStream(ZipEntryOutputStream zipEntryOutputStream, ZipParameters zipParameters, char[] password) throws IOException, ZipException {
/* 33 */     this.zipEntryOutputStream = zipEntryOutputStream;
/* 34 */     this.encrypter = initializeEncrypter(zipEntryOutputStream, zipParameters, password);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 39 */     this.zipEntryOutputStream.write(b);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/* 44 */     this.zipEntryOutputStream.write(b);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 49 */     this.encrypter.encryptData(b, off, len);
/* 50 */     this.zipEntryOutputStream.write(b, off, len);
/*    */   }
/*    */   
/*    */   public void writeHeaders(byte[] b) throws IOException {
/* 54 */     this.zipEntryOutputStream.write(b);
/*    */   }
/*    */   
/*    */   public void closeEntry() throws IOException {
/* 58 */     this.zipEntryOutputStream.closeEntry();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 63 */     this.zipEntryOutputStream.close();
/*    */   }
/*    */   
/*    */   public long getNumberOfBytesWrittenForThisEntry() {
/* 67 */     return this.zipEntryOutputStream.getNumberOfBytesWrittenForThisEntry();
/*    */   }
/*    */   
/*    */   protected T getEncrypter() {
/* 71 */     return this.encrypter;
/*    */   }
/*    */   
/*    */   protected abstract T initializeEncrypter(OutputStream paramOutputStream, ZipParameters paramZipParameters, char[] paramArrayOfchar) throws IOException, ZipException;
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\CipherOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */