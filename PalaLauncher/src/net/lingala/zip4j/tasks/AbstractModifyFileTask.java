/*    */ package net.lingala.zip4j.tasks;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.RandomAccessFile;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.headers.HeaderUtil;
/*    */ import net.lingala.zip4j.model.FileHeader;
/*    */ import net.lingala.zip4j.model.ZipModel;
/*    */ import net.lingala.zip4j.progress.ProgressMonitor;
/*    */ import net.lingala.zip4j.util.FileUtils;
/*    */ 
/*    */ abstract class AbstractModifyFileTask<T>
/*    */   extends AsyncZipTask<T>
/*    */ {
/*    */   AbstractModifyFileTask(AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/* 20 */     super(asyncTaskParameters);
/*    */   }
/*    */   
/*    */   File getTemporaryFile(String zipPathWithName) {
/* 24 */     Random random = new Random();
/* 25 */     File tmpFile = new File(zipPathWithName + random.nextInt(10000));
/*    */     
/* 27 */     while (tmpFile.exists()) {
/* 28 */       tmpFile = new File(zipPathWithName + random.nextInt(10000));
/*    */     }
/*    */     
/* 31 */     return tmpFile;
/*    */   }
/*    */   
/*    */   void updateOffsetsForAllSubsequentFileHeaders(ZipModel zipModel, FileHeader fileHeaderModified, long offsetToAdd) throws ZipException {
/* 35 */     int indexOfFileHeader = HeaderUtil.getIndexOfFileHeader(zipModel, fileHeaderModified);
/*    */     
/* 37 */     if (indexOfFileHeader == -1) {
/* 38 */       throw new ZipException("Could not locate modified file header in zipModel");
/*    */     }
/*    */     
/* 41 */     List<FileHeader> allFileHeaders = zipModel.getCentralDirectory().getFileHeaders();
/*    */     
/* 43 */     for (int i = indexOfFileHeader + 1; i < allFileHeaders.size(); i++) {
/* 44 */       FileHeader fileHeaderToUpdate = allFileHeaders.get(i);
/* 45 */       fileHeaderToUpdate.setOffsetLocalHeader(fileHeaderToUpdate.getOffsetLocalHeader() + offsetToAdd);
/*    */       
/* 47 */       if (zipModel.isZip64Format() && fileHeaderToUpdate
/* 48 */         .getZip64ExtendedInfo() != null && fileHeaderToUpdate
/* 49 */         .getZip64ExtendedInfo().getOffsetLocalHeader() != -1L)
/*    */       {
/* 51 */         fileHeaderToUpdate.getZip64ExtendedInfo().setOffsetLocalHeader(fileHeaderToUpdate
/* 52 */             .getZip64ExtendedInfo().getOffsetLocalHeader() + offsetToAdd);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   void cleanupFile(boolean successFlag, File zipFile, File temporaryZipFile) throws ZipException {
/* 59 */     if (successFlag) {
/* 60 */       restoreFileName(zipFile, temporaryZipFile);
/*    */     }
/* 62 */     else if (!temporaryZipFile.delete()) {
/* 63 */       throw new ZipException("Could not delete temporary file");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   long copyFile(RandomAccessFile randomAccessFile, OutputStream outputStream, long start, long length, ProgressMonitor progressMonitor) throws IOException {
/* 70 */     FileUtils.copyFile(randomAccessFile, outputStream, start, start + length, progressMonitor);
/* 71 */     return length;
/*    */   }
/*    */   
/*    */   private void restoreFileName(File zipFile, File temporaryZipFile) throws ZipException {
/* 75 */     if (zipFile.delete()) {
/* 76 */       if (!temporaryZipFile.renameTo(zipFile)) {
/* 77 */         throw new ZipException("cannot rename modified zip file");
/*    */       }
/*    */     } else {
/* 80 */       throw new ZipException("cannot delete old zip file");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\AbstractModifyFileTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */