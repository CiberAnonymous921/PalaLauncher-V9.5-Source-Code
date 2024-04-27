/*    */ package net.lingala.zip4j.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.Path;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.io.inputstream.NumberedSplitInputStream;
/*    */ import net.lingala.zip4j.io.inputstream.SplitInputStream;
/*    */ import net.lingala.zip4j.io.inputstream.ZipInputStream;
/*    */ import net.lingala.zip4j.io.inputstream.ZipStandardSplitInputStream;
/*    */ import net.lingala.zip4j.model.FileHeader;
/*    */ import net.lingala.zip4j.model.ZipModel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnzipUtil
/*    */ {
/*    */   public static ZipInputStream createZipInputStream(ZipModel zipModel, FileHeader fileHeader, char[] password) throws IOException {
/* 24 */     SplitInputStream splitInputStream = null;
/*    */     try {
/* 26 */       splitInputStream = createSplitInputStream(zipModel);
/* 27 */       splitInputStream.prepareExtractionForFileHeader(fileHeader);
/*    */       
/* 29 */       ZipInputStream zipInputStream = new ZipInputStream((InputStream)splitInputStream, password);
/* 30 */       if (zipInputStream.getNextEntry(fileHeader) == null) {
/* 31 */         throw new ZipException("Could not locate local file header for corresponding file header");
/*    */       }
/*    */       
/* 34 */       return zipInputStream;
/* 35 */     } catch (IOException e) {
/* 36 */       if (splitInputStream != null) {
/* 37 */         splitInputStream.close();
/*    */       }
/* 39 */       throw e;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void applyFileAttributes(FileHeader fileHeader, File file) {
/*    */     try {
/* 46 */       Path path = file.toPath();
/* 47 */       FileUtils.setFileAttributes(path, fileHeader.getExternalFileAttributes());
/* 48 */       FileUtils.setFileLastModifiedTime(path, fileHeader.getLastModifiedTime());
/* 49 */     } catch (NoSuchMethodError e) {
/* 50 */       FileUtils.setFileLastModifiedTimeWithoutNio(file, fileHeader.getLastModifiedTime());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static SplitInputStream createSplitInputStream(ZipModel zipModel) throws IOException {
/* 55 */     File zipFile = zipModel.getZipFile();
/*    */     
/* 57 */     if (zipFile.getName().endsWith(".zip.001")) {
/* 58 */       return (SplitInputStream)new NumberedSplitInputStream(zipModel.getZipFile(), true, zipModel
/* 59 */           .getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
/*    */     }
/*    */     
/* 62 */     return (SplitInputStream)new ZipStandardSplitInputStream(zipModel.getZipFile(), zipModel.isSplitArchive(), zipModel
/* 63 */         .getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4\\util\UnzipUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */