/*    */ package net.lingala.zip4j.io.inputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.lingala.zip4j.crypto.Decrypter;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.model.LocalFileHeader;
/*    */ 
/*    */ class NoCipherInputStream
/*    */   extends CipherInputStream
/*    */ {
/*    */   public NoCipherInputStream(ZipEntryInputStream zipEntryInputStream, LocalFileHeader localFileHeader, char[] password) throws IOException, ZipException {
/* 12 */     super(zipEntryInputStream, localFileHeader, password);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Decrypter initializeDecrypter(LocalFileHeader localFileHeader, char[] password) {
/* 17 */     return new NoDecrypter();
/*    */   }
/*    */   
/*    */   static class NoDecrypter
/*    */     implements Decrypter
/*    */   {
/*    */     public int decryptData(byte[] buff, int start, int len) {
/* 24 */       return len;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\NoCipherInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */