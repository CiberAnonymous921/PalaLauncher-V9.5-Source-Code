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
/*    */ import net.lingala.zip4j.util.FileUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AddFolderToZipTask
/*    */   extends AbstractAddFileToZipTask<AddFolderToZipTask.AddFolderToZipTaskParameters>
/*    */ {
/*    */   public AddFolderToZipTask(ZipModel zipModel, char[] password, HeaderWriter headerWriter, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/* 20 */     super(zipModel, password, headerWriter, asyncTaskParameters);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void executeTask(AddFolderToZipTaskParameters taskParameters, ProgressMonitor progressMonitor) throws IOException {
/* 26 */     List<File> filesToAdd = getFilesToAdd(taskParameters);
/* 27 */     setDefaultFolderPath(taskParameters);
/* 28 */     addFilesToZip(filesToAdd, progressMonitor, taskParameters.zipParameters, taskParameters.charset);
/*    */   }
/*    */ 
/*    */   
/*    */   protected long calculateTotalWork(AddFolderToZipTaskParameters taskParameters) throws ZipException {
/* 33 */     List<File> filesToAdd = FileUtils.getFilesInDirectoryRecursive(taskParameters.folderToAdd, taskParameters
/* 34 */         .zipParameters.isReadHiddenFiles(), taskParameters
/* 35 */         .zipParameters.isReadHiddenFolders(), taskParameters
/* 36 */         .zipParameters.getExcludeFileFilter());
/*    */     
/* 38 */     if (taskParameters.zipParameters.isIncludeRootFolder()) {
/* 39 */       filesToAdd.add(taskParameters.folderToAdd);
/*    */     }
/*    */     
/* 42 */     return calculateWorkForFiles(filesToAdd, taskParameters.zipParameters);
/*    */   }
/*    */   
/*    */   private void setDefaultFolderPath(AddFolderToZipTaskParameters taskParameters) throws IOException {
/*    */     String rootFolderPath;
/* 47 */     File folderToAdd = taskParameters.folderToAdd;
/* 48 */     if (taskParameters.zipParameters.isIncludeRootFolder()) {
/* 49 */       File parentFile = folderToAdd.getCanonicalFile().getParentFile();
/* 50 */       if (parentFile == null) {
/* 51 */         rootFolderPath = folderToAdd.getCanonicalPath();
/*    */       } else {
/* 53 */         rootFolderPath = folderToAdd.getCanonicalFile().getParentFile().getCanonicalPath();
/*    */       } 
/*    */     } else {
/* 56 */       rootFolderPath = folderToAdd.getCanonicalPath();
/*    */     } 
/*    */     
/* 59 */     taskParameters.zipParameters.setDefaultFolderPath(rootFolderPath);
/*    */   }
/*    */   
/*    */   private List<File> getFilesToAdd(AddFolderToZipTaskParameters taskParameters) throws ZipException {
/* 63 */     List<File> filesToAdd = FileUtils.getFilesInDirectoryRecursive(taskParameters.folderToAdd, taskParameters
/* 64 */         .zipParameters.isReadHiddenFiles(), taskParameters
/* 65 */         .zipParameters.isReadHiddenFolders(), taskParameters
/* 66 */         .zipParameters.getExcludeFileFilter());
/*    */     
/* 68 */     if (taskParameters.zipParameters.isIncludeRootFolder()) {
/* 69 */       filesToAdd.add(taskParameters.folderToAdd);
/*    */     }
/*    */     
/* 72 */     return filesToAdd;
/*    */   }
/*    */   
/*    */   public static class AddFolderToZipTaskParameters extends AbstractZipTaskParameters {
/*    */     private File folderToAdd;
/*    */     private ZipParameters zipParameters;
/*    */     
/*    */     public AddFolderToZipTaskParameters(File folderToAdd, ZipParameters zipParameters, Charset charset) {
/* 80 */       super(charset);
/* 81 */       this.folderToAdd = folderToAdd;
/* 82 */       this.zipParameters = zipParameters;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\AddFolderToZipTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */