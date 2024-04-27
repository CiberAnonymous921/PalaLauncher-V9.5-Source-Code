/*    */ package net.lingala.zip4j.tasks;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.headers.HeaderUtil;
/*    */ import net.lingala.zip4j.io.inputstream.SplitInputStream;
/*    */ import net.lingala.zip4j.io.inputstream.ZipInputStream;
/*    */ import net.lingala.zip4j.model.FileHeader;
/*    */ import net.lingala.zip4j.model.ZipModel;
/*    */ import net.lingala.zip4j.progress.ProgressMonitor;
/*    */ import net.lingala.zip4j.util.UnzipUtil;
/*    */ 
/*    */ public class ExtractAllFilesTask
/*    */   extends AbstractExtractFileTask<ExtractAllFilesTask.ExtractAllFilesTaskParameters>
/*    */ {
/*    */   private char[] password;
/*    */   private SplitInputStream splitInputStream;
/*    */   
/*    */   public ExtractAllFilesTask(ZipModel zipModel, char[] password, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/* 22 */     super(zipModel, asyncTaskParameters);
/* 23 */     this.password = password;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void executeTask(ExtractAllFilesTaskParameters taskParameters, ProgressMonitor progressMonitor) throws IOException {
/* 29 */     try (ZipInputStream zipInputStream = prepareZipInputStream(taskParameters.charset)) {
/* 30 */       for (FileHeader fileHeader : getZipModel().getCentralDirectory().getFileHeaders()) {
/* 31 */         if (fileHeader.getFileName().startsWith("__MACOSX")) {
/* 32 */           progressMonitor.updateWorkCompleted(fileHeader.getUncompressedSize());
/*    */           
/*    */           continue;
/*    */         } 
/* 36 */         this.splitInputStream.prepareExtractionForFileHeader(fileHeader);
/*    */         
/* 38 */         extractFile(zipInputStream, fileHeader, taskParameters.outputPath, (String)null, progressMonitor);
/* 39 */         verifyIfTaskIsCancelled();
/*    */       } 
/*    */     } finally {
/* 42 */       if (this.splitInputStream != null) {
/* 43 */         this.splitInputStream.close();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected long calculateTotalWork(ExtractAllFilesTaskParameters taskParameters) {
/* 50 */     return HeaderUtil.getTotalUncompressedSizeOfAllFileHeaders(getZipModel().getCentralDirectory().getFileHeaders());
/*    */   }
/*    */   
/*    */   private ZipInputStream prepareZipInputStream(Charset charset) throws IOException {
/* 54 */     this.splitInputStream = UnzipUtil.createSplitInputStream(getZipModel());
/*    */     
/* 56 */     FileHeader fileHeader = getFirstFileHeader(getZipModel());
/* 57 */     if (fileHeader != null) {
/* 58 */       this.splitInputStream.prepareExtractionForFileHeader(fileHeader);
/*    */     }
/*    */     
/* 61 */     return new ZipInputStream((InputStream)this.splitInputStream, this.password, charset);
/*    */   }
/*    */   
/*    */   private FileHeader getFirstFileHeader(ZipModel zipModel) {
/* 65 */     if (zipModel.getCentralDirectory() == null || zipModel
/* 66 */       .getCentralDirectory().getFileHeaders() == null || zipModel
/* 67 */       .getCentralDirectory().getFileHeaders().size() == 0) {
/* 68 */       return null;
/*    */     }
/*    */     
/* 71 */     return zipModel.getCentralDirectory().getFileHeaders().get(0);
/*    */   }
/*    */   
/*    */   public static class ExtractAllFilesTaskParameters extends AbstractZipTaskParameters {
/*    */     private String outputPath;
/*    */     
/*    */     public ExtractAllFilesTaskParameters(String outputPath, Charset charset) {
/* 78 */       super(charset);
/* 79 */       this.outputPath = outputPath;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\ExtractAllFilesTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */