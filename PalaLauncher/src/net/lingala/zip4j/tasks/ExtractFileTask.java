/*    */ package net.lingala.zip4j.tasks;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.headers.HeaderUtil;
/*    */ import net.lingala.zip4j.io.inputstream.SplitInputStream;
/*    */ import net.lingala.zip4j.io.inputstream.ZipInputStream;
/*    */ import net.lingala.zip4j.model.FileHeader;
/*    */ import net.lingala.zip4j.model.ZipModel;
/*    */ import net.lingala.zip4j.progress.ProgressMonitor;
/*    */ import net.lingala.zip4j.util.UnzipUtil;
/*    */ import net.lingala.zip4j.util.Zip4jUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtractFileTask
/*    */   extends AbstractExtractFileTask<ExtractFileTask.ExtractFileTaskParameters>
/*    */ {
/*    */   private char[] password;
/*    */   private SplitInputStream splitInputStream;
/*    */   
/*    */   public ExtractFileTask(ZipModel zipModel, char[] password, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/* 27 */     super(zipModel, asyncTaskParameters);
/* 28 */     this.password = password;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void executeTask(ExtractFileTaskParameters taskParameters, ProgressMonitor progressMonitor) throws IOException {
/* 35 */     try (ZipInputStream zipInputStream = createZipInputStream(taskParameters.fileHeader, taskParameters.charset)) {
/* 36 */       List<FileHeader> fileHeadersUnderDirectory = getFileHeadersToExtract(taskParameters.fileHeader);
/* 37 */       for (FileHeader fileHeader : fileHeadersUnderDirectory) {
/* 38 */         String newFileName = determineNewFileName(taskParameters.newFileName, taskParameters.fileHeader, fileHeader);
/* 39 */         extractFile(zipInputStream, fileHeader, taskParameters.outputPath, newFileName, progressMonitor);
/*    */       } 
/*    */     } finally {
/* 42 */       if (this.splitInputStream != null) {
/* 43 */         this.splitInputStream.close();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected long calculateTotalWork(ExtractFileTaskParameters taskParameters) {
/* 50 */     List<FileHeader> fileHeadersUnderDirectory = getFileHeadersToExtract(taskParameters.fileHeader);
/* 51 */     return HeaderUtil.getTotalUncompressedSizeOfAllFileHeaders(fileHeadersUnderDirectory);
/*    */   }
/*    */   
/*    */   private List<FileHeader> getFileHeadersToExtract(FileHeader rootFileHeader) {
/* 55 */     if (!rootFileHeader.isDirectory()) {
/* 56 */       return Collections.singletonList(rootFileHeader);
/*    */     }
/*    */     
/* 59 */     return HeaderUtil.getFileHeadersUnderDirectory(
/* 60 */         getZipModel().getCentralDirectory().getFileHeaders(), rootFileHeader);
/*    */   }
/*    */   
/*    */   private ZipInputStream createZipInputStream(FileHeader fileHeader, Charset charset) throws IOException {
/* 64 */     this.splitInputStream = UnzipUtil.createSplitInputStream(getZipModel());
/* 65 */     this.splitInputStream.prepareExtractionForFileHeader(fileHeader);
/* 66 */     return new ZipInputStream((InputStream)this.splitInputStream, this.password, charset);
/*    */   }
/*    */   
/*    */   private String determineNewFileName(String newFileName, FileHeader fileHeaderToExtract, FileHeader fileHeaderBeingExtracted) {
/* 70 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(newFileName)) {
/* 71 */       return newFileName;
/*    */     }
/*    */     
/* 74 */     if (!fileHeaderToExtract.isDirectory()) {
/* 75 */       return newFileName;
/*    */     }
/*    */     
/* 78 */     String fileSeparator = "/";
/* 79 */     if (newFileName.endsWith("/")) {
/* 80 */       fileSeparator = "";
/*    */     }
/*    */     
/* 83 */     return fileHeaderBeingExtracted.getFileName().replaceFirst(fileHeaderToExtract.getFileName(), newFileName + fileSeparator);
/*    */   }
/*    */   
/*    */   public static class ExtractFileTaskParameters
/*    */     extends AbstractZipTaskParameters {
/*    */     private String outputPath;
/*    */     private FileHeader fileHeader;
/*    */     private String newFileName;
/*    */     
/*    */     public ExtractFileTaskParameters(String outputPath, FileHeader fileHeader, String newFileName, Charset charset) {
/* 93 */       super(charset);
/* 94 */       this.outputPath = outputPath;
/* 95 */       this.fileHeader = fileHeader;
/* 96 */       this.newFileName = newFileName;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\ExtractFileTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */