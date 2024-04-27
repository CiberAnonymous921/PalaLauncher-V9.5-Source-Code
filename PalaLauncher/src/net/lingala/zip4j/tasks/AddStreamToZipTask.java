/*    */ package net.lingala.zip4j.tasks;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.headers.HeaderUtil;
/*    */ import net.lingala.zip4j.headers.HeaderWriter;
/*    */ import net.lingala.zip4j.io.outputstream.SplitOutputStream;
/*    */ import net.lingala.zip4j.io.outputstream.ZipOutputStream;
/*    */ import net.lingala.zip4j.model.FileHeader;
/*    */ import net.lingala.zip4j.model.ZipModel;
/*    */ import net.lingala.zip4j.model.ZipParameters;
/*    */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*    */ import net.lingala.zip4j.progress.ProgressMonitor;
/*    */ import net.lingala.zip4j.util.Zip4jUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AddStreamToZipTask
/*    */   extends AbstractAddFileToZipTask<AddStreamToZipTask.AddStreamToZipTaskParameters>
/*    */ {
/*    */   public AddStreamToZipTask(ZipModel zipModel, char[] password, HeaderWriter headerWriter, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/* 25 */     super(zipModel, password, headerWriter, asyncTaskParameters);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void executeTask(AddStreamToZipTaskParameters taskParameters, ProgressMonitor progressMonitor) throws IOException {
/* 32 */     verifyZipParameters(taskParameters.zipParameters);
/*    */     
/* 34 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(taskParameters.zipParameters.getFileNameInZip())) {
/* 35 */       throw new ZipException("fileNameInZip has to be set in zipParameters when adding stream");
/*    */     }
/*    */     
/* 38 */     removeFileIfExists(getZipModel(), taskParameters.charset, taskParameters.zipParameters.getFileNameInZip(), progressMonitor);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 44 */     taskParameters.zipParameters.setWriteExtendedLocalFileHeader(true);
/*    */     
/* 46 */     if (taskParameters.zipParameters.getCompressionMethod().equals(CompressionMethod.STORE))
/*    */     {
/* 48 */       taskParameters.zipParameters.setEntrySize(0L);
/*    */     }
/*    */     
/* 51 */     try(SplitOutputStream splitOutputStream = new SplitOutputStream(getZipModel().getZipFile(), getZipModel().getSplitLength()); 
/* 52 */         ZipOutputStream zipOutputStream = initializeOutputStream(splitOutputStream, taskParameters.charset)) {
/*    */       
/* 54 */       byte[] readBuff = new byte[4096];
/* 55 */       int readLen = -1;
/*    */       
/* 57 */       ZipParameters zipParameters = taskParameters.zipParameters;
/* 58 */       zipOutputStream.putNextEntry(zipParameters);
/*    */       
/* 60 */       if (!zipParameters.getFileNameInZip().endsWith("/") && 
/* 61 */         !zipParameters.getFileNameInZip().endsWith("\\")) {
/* 62 */         while ((readLen = taskParameters.inputStream.read(readBuff)) != -1) {
/* 63 */           zipOutputStream.write(readBuff, 0, readLen);
/*    */         }
/*    */       }
/*    */       
/* 67 */       FileHeader fileHeader = zipOutputStream.closeEntry();
/*    */       
/* 69 */       if (fileHeader.getCompressionMethod().equals(CompressionMethod.STORE)) {
/* 70 */         updateLocalFileHeader(fileHeader, splitOutputStream);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected long calculateTotalWork(AddStreamToZipTaskParameters taskParameters) {
/* 77 */     return 0L;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void removeFileIfExists(ZipModel zipModel, Charset charset, String fileNameInZip, ProgressMonitor progressMonitor) throws ZipException {
/* 83 */     FileHeader fileHeader = HeaderUtil.getFileHeader(zipModel, fileNameInZip);
/* 84 */     if (fileHeader != null)
/* 85 */       removeFile(fileHeader, progressMonitor, charset); 
/*    */   }
/*    */   
/*    */   public static class AddStreamToZipTaskParameters
/*    */     extends AbstractZipTaskParameters {
/*    */     private InputStream inputStream;
/*    */     private ZipParameters zipParameters;
/*    */     
/*    */     public AddStreamToZipTaskParameters(InputStream inputStream, ZipParameters zipParameters, Charset charset) {
/* 94 */       super(charset);
/* 95 */       this.inputStream = inputStream;
/* 96 */       this.zipParameters = zipParameters;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\AddStreamToZipTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */