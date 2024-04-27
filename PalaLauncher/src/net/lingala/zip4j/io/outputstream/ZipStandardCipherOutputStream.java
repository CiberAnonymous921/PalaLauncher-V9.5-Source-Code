/*    */ package net.lingala.zip4j.io.outputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import net.lingala.zip4j.crypto.Encrypter;
/*    */ import net.lingala.zip4j.crypto.StandardEncrypter;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.model.ZipParameters;
/*    */ import net.lingala.zip4j.util.Zip4jUtil;
/*    */ 
/*    */ class ZipStandardCipherOutputStream
/*    */   extends CipherOutputStream<StandardEncrypter> {
/*    */   public ZipStandardCipherOutputStream(ZipEntryOutputStream outputStream, ZipParameters zipParameters, char[] password) throws IOException, ZipException {
/* 14 */     super(outputStream, zipParameters, password);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StandardEncrypter initializeEncrypter(OutputStream outputStream, ZipParameters zipParameters, char[] password) throws IOException, ZipException {
/* 19 */     long key = getEncryptionKey(zipParameters);
/* 20 */     StandardEncrypter encrypter = new StandardEncrypter(password, key);
/* 21 */     writeHeaders(encrypter.getHeaderBytes());
/* 22 */     return encrypter;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 27 */     write(new byte[] { (byte)b });
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/* 32 */     write(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 37 */     super.write(b, off, len);
/*    */   }
/*    */   
/*    */   private long getEncryptionKey(ZipParameters zipParameters) {
/* 41 */     if (zipParameters.isWriteExtendedLocalFileHeader()) {
/* 42 */       long dosTime = Zip4jUtil.javaToDosTime(zipParameters.getLastModifiedFileTime());
/* 43 */       return (dosTime & 0xFFFFL) << 16L;
/*    */     } 
/*    */     
/* 46 */     return zipParameters.getEntryCRC();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\ZipStandardCipherOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */