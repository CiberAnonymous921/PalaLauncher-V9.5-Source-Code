/*    */ package net.lingala.zip4j.tasks;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.List;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.headers.HeaderWriter;
/*    */ import net.lingala.zip4j.model.ZipModel;
/*    */ import net.lingala.zip4j.model.ZipParameters;
/*    */ import net.lingala.zip4j.progress.ProgressMonitor;
/*    */ 
/*    */ 
/*    */ public class AddFilesToZipTask
/*    */   extends AbstractAddFileToZipTask<AddFilesToZipTask.AddFilesToZipTaskParameters>
/*    */ {
/*    */   public AddFilesToZipTask(ZipModel zipModel, char[] password, HeaderWriter headerWriter, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/* 18 */     super(zipModel, password, headerWriter, asyncTaskParameters);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void executeTask(AddFilesToZipTaskParameters taskParameters, ProgressMonitor progressMonitor) throws IOException {
/* 25 */     verifyZipParameters(taskParameters.zipParameters);
/* 26 */     addFilesToZip(taskParameters.filesToAdd, progressMonitor, taskParameters.zipParameters, taskParameters.charset);
/*    */   }
/*    */ 
/*    */   
/*    */   protected long calculateTotalWork(AddFilesToZipTaskParameters taskParameters) throws ZipException {
/* 31 */     return calculateWorkForFiles(taskParameters.filesToAdd, taskParameters.zipParameters);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ProgressMonitor.Task getTask() {
/* 36 */     return super.getTask();
/*    */   }
/*    */   
/*    */   public static class AddFilesToZipTaskParameters extends AbstractZipTaskParameters {
/*    */     private List<File> filesToAdd;
/*    */     private ZipParameters zipParameters;
/*    */     
/*    */     public AddFilesToZipTaskParameters(List<File> filesToAdd, ZipParameters zipParameters, Charset charset) {
/* 44 */       super(charset);
/* 45 */       this.filesToAdd = filesToAdd;
/* 46 */       this.zipParameters = zipParameters;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\AddFilesToZipTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */