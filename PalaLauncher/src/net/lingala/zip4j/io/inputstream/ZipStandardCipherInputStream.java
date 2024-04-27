/*    */ package net.lingala.zip4j.io.inputstream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.lingala.zip4j.crypto.Decrypter;
/*    */ import net.lingala.zip4j.crypto.StandardDecrypter;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.model.LocalFileHeader;
/*    */ 
/*    */ 
/*    */ class ZipStandardCipherInputStream
/*    */   extends CipherInputStream<StandardDecrypter>
/*    */ {
/*    */   public ZipStandardCipherInputStream(ZipEntryInputStream zipEntryInputStream, LocalFileHeader localFileHeader, char[] password) throws IOException, ZipException {
/* 14 */     super(zipEntryInputStream, localFileHeader, password);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StandardDecrypter initializeDecrypter(LocalFileHeader localFileHeader, char[] password) throws ZipException, IOException {
/* 19 */     return new StandardDecrypter(password, localFileHeader.getCrcRawData(), getStandardDecrypterHeaderBytes());
/*    */   }
/*    */   
/*    */   private byte[] getStandardDecrypterHeaderBytes() throws IOException {
/* 23 */     byte[] headerBytes = new byte[12];
/* 24 */     readRaw(headerBytes);
/* 25 */     return headerBytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\ZipStandardCipherInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */