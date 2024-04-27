/*    */ package net.lingala.zip4j.tasks;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.progress.ProgressMonitor;
/*    */ 
/*    */ 
/*    */ public abstract class AsyncZipTask<T>
/*    */ {
/*    */   private ProgressMonitor progressMonitor;
/*    */   private boolean runInThread;
/*    */   private ExecutorService executorService;
/*    */   
/*    */   public AsyncZipTask(AsyncTaskParameters asyncTaskParameters) {
/* 16 */     this.progressMonitor = asyncTaskParameters.progressMonitor;
/* 17 */     this.runInThread = asyncTaskParameters.runInThread;
/* 18 */     this.executorService = asyncTaskParameters.executorService;
/*    */   }
/*    */   
/*    */   public void execute(T taskParameters) throws ZipException {
/* 22 */     this.progressMonitor.fullReset();
/* 23 */     this.progressMonitor.setState(ProgressMonitor.State.BUSY);
/* 24 */     this.progressMonitor.setCurrentTask(getTask());
/*    */     
/* 26 */     if (this.runInThread) {
/* 27 */       long totalWorkToBeDone = calculateTotalWork(taskParameters);
/* 28 */       this.progressMonitor.setTotalWork(totalWorkToBeDone);
/*    */       
/* 30 */       this.executorService.execute(() -> {
/*    */             try {
/*    */               performTaskWithErrorHandling((T)taskParameters, this.progressMonitor);
/* 33 */             } catch (ZipException zipException) {}
/*    */           
/*    */           });
/*    */     } else {
/*    */       
/* 38 */       performTaskWithErrorHandling(taskParameters, this.progressMonitor);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void performTaskWithErrorHandling(T taskParameters, ProgressMonitor progressMonitor) throws ZipException {
/*    */     try {
/* 44 */       executeTask(taskParameters, progressMonitor);
/* 45 */       progressMonitor.endProgressMonitor();
/* 46 */     } catch (ZipException e) {
/* 47 */       progressMonitor.endProgressMonitor((Exception)e);
/* 48 */       throw e;
/* 49 */     } catch (Exception e) {
/* 50 */       progressMonitor.endProgressMonitor(e);
/* 51 */       throw new ZipException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void verifyIfTaskIsCancelled() throws ZipException {
/* 56 */     if (!this.progressMonitor.isCancelAllTasks()) {
/*    */       return;
/*    */     }
/*    */     
/* 60 */     this.progressMonitor.setResult(ProgressMonitor.Result.CANCELLED);
/* 61 */     this.progressMonitor.setState(ProgressMonitor.State.READY);
/* 62 */     throw new ZipException("Task cancelled", ZipException.Type.TASK_CANCELLED_EXCEPTION);
/*    */   }
/*    */   
/*    */   protected abstract void executeTask(T paramT, ProgressMonitor paramProgressMonitor) throws IOException;
/*    */   
/*    */   protected abstract long calculateTotalWork(T paramT) throws ZipException;
/*    */   
/*    */   protected abstract ProgressMonitor.Task getTask();
/*    */   
/*    */   public static class AsyncTaskParameters {
/*    */     private ProgressMonitor progressMonitor;
/*    */     private boolean runInThread;
/*    */     private ExecutorService executorService;
/*    */     
/*    */     public AsyncTaskParameters(ExecutorService executorService, boolean runInThread, ProgressMonitor progressMonitor) {
/* 77 */       this.executorService = executorService;
/* 78 */       this.runInThread = runInThread;
/* 79 */       this.progressMonitor = progressMonitor;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\AsyncZipTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */