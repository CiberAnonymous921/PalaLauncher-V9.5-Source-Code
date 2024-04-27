/*    */ package net.lingala.zip4j.tasks;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.headers.HeaderWriter;
/*    */ import net.lingala.zip4j.io.outputstream.SplitOutputStream;
/*    */ import net.lingala.zip4j.model.EndOfCentralDirectoryRecord;
/*    */ import net.lingala.zip4j.model.ZipModel;
/*    */ import net.lingala.zip4j.progress.ProgressMonitor;
/*    */ 
/*    */ public class SetCommentTask
/*    */   extends AsyncZipTask<SetCommentTask.SetCommentTaskTaskParameters>
/*    */ {
/*    */   private ZipModel zipModel;
/*    */   
/*    */   public SetCommentTask(ZipModel zipModel, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/* 19 */     super(asyncTaskParameters);
/* 20 */     this.zipModel = zipModel;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void executeTask(SetCommentTaskTaskParameters taskParameters, ProgressMonitor progressMonitor) throws IOException {
/* 25 */     if (taskParameters.comment == null) {
/* 26 */       throw new ZipException("comment is null, cannot update Zip file with comment");
/*    */     }
/*    */     
/* 29 */     EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = this.zipModel.getEndOfCentralDirectoryRecord();
/* 30 */     endOfCentralDirectoryRecord.setComment(taskParameters.comment);
/*    */     
/* 32 */     try (SplitOutputStream outputStream = new SplitOutputStream(this.zipModel.getZipFile())) {
/* 33 */       if (this.zipModel.isZip64Format()) {
/* 34 */         outputStream.seek(this.zipModel.getZip64EndOfCentralDirectoryRecord()
/* 35 */             .getOffsetStartCentralDirectoryWRTStartDiskNumber());
/*    */       } else {
/* 37 */         outputStream.seek(endOfCentralDirectoryRecord.getOffsetOfStartOfCentralDirectory());
/*    */       } 
/*    */       
/* 40 */       HeaderWriter headerWriter = new HeaderWriter();
/* 41 */       headerWriter.finalizeZipFileWithoutValidations(this.zipModel, (OutputStream)outputStream, taskParameters.charset);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected long calculateTotalWork(SetCommentTaskTaskParameters taskParameters) {
/* 47 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ProgressMonitor.Task getTask() {
/* 52 */     return ProgressMonitor.Task.SET_COMMENT;
/*    */   }
/*    */   
/*    */   public static class SetCommentTaskTaskParameters extends AbstractZipTaskParameters {
/*    */     private String comment;
/*    */     
/*    */     public SetCommentTaskTaskParameters(String comment, Charset charset) {
/* 59 */       super(charset);
/* 60 */       this.comment = comment;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\SetCommentTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */