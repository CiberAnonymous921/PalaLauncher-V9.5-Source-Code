/*    */ package net.lingala.zip4j.io.outputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import net.lingala.zip4j.crypto.Encrypter;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.model.ZipParameters;
/*    */ 
/*    */ class NoCipherOutputStream
/*    */   extends CipherOutputStream<NoCipherOutputStream.NoEncrypter>
/*    */ {
/*    */   public NoCipherOutputStream(ZipEntryOutputStream zipEntryOutputStream, ZipParameters zipParameters, char[] password) throws IOException, ZipException {
/* 13 */     super(zipEntryOutputStream, zipParameters, password);
/*    */   }
/*    */ 
/*    */   
/*    */   protected NoEncrypter initializeEncrypter(OutputStream outputStream, ZipParameters zipParameters, char[] password) {
/* 18 */     return new NoEncrypter();
/*    */   }
/*    */   
/*    */   static class NoEncrypter
/*    */     implements Encrypter
/*    */   {
/*    */     public int encryptData(byte[] buff) {
/* 25 */       return encryptData(buff, 0, buff.length);
/*    */     }
/*    */ 
/*    */     
/*    */     public int encryptData(byte[] buff, int start, int len) {
/* 30 */       return len;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\NoCipherOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */