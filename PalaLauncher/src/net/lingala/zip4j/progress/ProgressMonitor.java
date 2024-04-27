/*     */ package net.lingala.zip4j.progress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProgressMonitor
/*     */ {
/*     */   private State state;
/*     */   private long totalWork;
/*     */   private long workCompleted;
/*     */   private int percentDone;
/*     */   private Task currentTask;
/*     */   private String fileName;
/*     */   private Result result;
/*     */   private Exception exception;
/*     */   private boolean cancelAllTasks;
/*     */   private boolean pause;
/*     */   
/*     */   public enum State
/*     */   {
/*  24 */     READY, BUSY; }
/*  25 */   public enum Result { SUCCESS, WORK_IN_PROGRESS, ERROR, CANCELLED; }
/*  26 */   public enum Task { NONE, ADD_ENTRY, REMOVE_ENTRY, CALCULATE_CRC, EXTRACT_ENTRY, MERGE_ZIP_FILES, SET_COMMENT, RENAME_FILE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProgressMonitor() {
/*  40 */     reset();
/*     */   }
/*     */   
/*     */   public void updateWorkCompleted(long workCompleted) {
/*  44 */     this.workCompleted += workCompleted;
/*     */     
/*  46 */     if (this.totalWork > 0L) {
/*  47 */       this.percentDone = (int)(this.workCompleted * 100L / this.totalWork);
/*  48 */       if (this.percentDone > 100) {
/*  49 */         this.percentDone = 100;
/*     */       }
/*     */     } 
/*     */     
/*  53 */     while (this.pause) {
/*     */       try {
/*  55 */         Thread.sleep(150L);
/*  56 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endProgressMonitor() {
/*  63 */     this.result = Result.SUCCESS;
/*  64 */     this.percentDone = 100;
/*  65 */     reset();
/*     */   }
/*     */   
/*     */   public void endProgressMonitor(Exception e) {
/*  69 */     this.result = Result.ERROR;
/*  70 */     this.exception = e;
/*  71 */     reset();
/*     */   }
/*     */   
/*     */   public void fullReset() {
/*  75 */     reset();
/*  76 */     this.fileName = null;
/*  77 */     this.totalWork = 0L;
/*  78 */     this.workCompleted = 0L;
/*  79 */     this.percentDone = 0;
/*     */   }
/*     */   
/*     */   private void reset() {
/*  83 */     this.currentTask = Task.NONE;
/*  84 */     this.state = State.READY;
/*     */   }
/*     */   
/*     */   public State getState() {
/*  88 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(State state) {
/*  92 */     this.state = state;
/*     */   }
/*     */   
/*     */   public long getTotalWork() {
/*  96 */     return this.totalWork;
/*     */   }
/*     */   
/*     */   public void setTotalWork(long totalWork) {
/* 100 */     this.totalWork = totalWork;
/*     */   }
/*     */   
/*     */   public long getWorkCompleted() {
/* 104 */     return this.workCompleted;
/*     */   }
/*     */   
/*     */   public int getPercentDone() {
/* 108 */     return this.percentDone;
/*     */   }
/*     */   
/*     */   public void setPercentDone(int percentDone) {
/* 112 */     this.percentDone = percentDone;
/*     */   }
/*     */   
/*     */   public Task getCurrentTask() {
/* 116 */     return this.currentTask;
/*     */   }
/*     */   
/*     */   public void setCurrentTask(Task currentTask) {
/* 120 */     this.currentTask = currentTask;
/*     */   }
/*     */   
/*     */   public String getFileName() {
/* 124 */     return this.fileName;
/*     */   }
/*     */   
/*     */   public void setFileName(String fileName) {
/* 128 */     this.fileName = fileName;
/*     */   }
/*     */   
/*     */   public Result getResult() {
/* 132 */     return this.result;
/*     */   }
/*     */   
/*     */   public void setResult(Result result) {
/* 136 */     this.result = result;
/*     */   }
/*     */   
/*     */   public Exception getException() {
/* 140 */     return this.exception;
/*     */   }
/*     */   
/*     */   public void setException(Exception exception) {
/* 144 */     this.exception = exception;
/*     */   }
/*     */   
/*     */   public boolean isCancelAllTasks() {
/* 148 */     return this.cancelAllTasks;
/*     */   }
/*     */   
/*     */   public void setCancelAllTasks(boolean cancelAllTasks) {
/* 152 */     this.cancelAllTasks = cancelAllTasks;
/*     */   }
/*     */   
/*     */   public boolean isPause() {
/* 156 */     return this.pause;
/*     */   }
/*     */   
/*     */   public void setPause(boolean pause) {
/* 160 */     this.pause = pause;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\progress\ProgressMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */