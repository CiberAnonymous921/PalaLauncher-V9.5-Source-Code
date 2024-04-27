/*    */ package net.lingala.zip4j.util;
/*    */ 
/*    */ import net.lingala.zip4j.headers.VersionMadeBy;
/*    */ import net.lingala.zip4j.headers.VersionNeededToExtract;
/*    */ import net.lingala.zip4j.model.ZipParameters;
/*    */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*    */ import net.lingala.zip4j.model.enums.EncryptionMethod;
/*    */ 
/*    */ public class ZipVersionUtils
/*    */ {
/*    */   public static int determineVersionMadeBy(ZipParameters zipParameters, RawIO rawIO) {
/* 12 */     byte[] versionMadeBy = new byte[2];
/* 13 */     versionMadeBy[0] = VersionMadeBy.SPECIFICATION_VERSION.getCode();
/* 14 */     versionMadeBy[1] = VersionMadeBy.UNIX.getCode();
/* 15 */     if (FileUtils.isWindows() && !zipParameters.isUnixMode()) {
/* 16 */       versionMadeBy[1] = VersionMadeBy.WINDOWS.getCode();
/*    */     }
/*    */     
/* 19 */     return rawIO.readShortLittleEndian(versionMadeBy, 0);
/*    */   }
/*    */   
/*    */   public static VersionNeededToExtract determineVersionNeededToExtract(ZipParameters zipParameters) {
/* 23 */     VersionNeededToExtract versionRequired = VersionNeededToExtract.DEFAULT;
/*    */     
/* 25 */     if (zipParameters.getCompressionMethod() == CompressionMethod.DEFLATE) {
/* 26 */       versionRequired = VersionNeededToExtract.DEFLATE_COMPRESSED;
/*    */     }
/*    */     
/* 29 */     if (zipParameters.getEntrySize() > 4294967295L) {
/* 30 */       versionRequired = VersionNeededToExtract.ZIP_64_FORMAT;
/*    */     }
/*    */     
/* 33 */     if (zipParameters.isEncryptFiles() && zipParameters.getEncryptionMethod().equals(EncryptionMethod.AES)) {
/* 34 */       versionRequired = VersionNeededToExtract.AES_ENCRYPTED;
/*    */     }
/*    */     
/* 37 */     return versionRequired;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4\\util\ZipVersionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */