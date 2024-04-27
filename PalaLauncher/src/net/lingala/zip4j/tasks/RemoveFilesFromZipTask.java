/*     */ package net.lingala.zip4j.tasks;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.headers.HeaderUtil;
/*     */ import net.lingala.zip4j.headers.HeaderWriter;
/*     */ import net.lingala.zip4j.io.outputstream.SplitOutputStream;
/*     */ import net.lingala.zip4j.model.EndOfCentralDirectoryRecord;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.model.enums.RandomAccessFileMode;
/*     */ import net.lingala.zip4j.progress.ProgressMonitor;
/*     */ 
/*     */ public class RemoveFilesFromZipTask
/*     */   extends AbstractModifyFileTask<RemoveFilesFromZipTask.RemoveFilesFromZipTaskParameters>
/*     */ {
/*     */   private ZipModel zipModel;
/*     */   private HeaderWriter headerWriter;
/*     */   
/*     */   public RemoveFilesFromZipTask(ZipModel zipModel, HeaderWriter headerWriter, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/*  27 */     super(asyncTaskParameters);
/*  28 */     this.zipModel = zipModel;
/*  29 */     this.headerWriter = headerWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void executeTask(RemoveFilesFromZipTaskParameters taskParameters, ProgressMonitor progressMonitor) throws IOException {
/*  35 */     if (this.zipModel.isSplitArchive()) {
/*  36 */       throw new ZipException("This is a split archive. Zip file format does not allow updating split/spanned files");
/*     */     }
/*     */     
/*  39 */     List<String> entriesToRemove = filterNonExistingEntries(taskParameters.filesToRemove);
/*     */     
/*  41 */     if (entriesToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  45 */     File temporaryZipFile = getTemporaryFile(this.zipModel.getZipFile().getPath());
/*  46 */     boolean successFlag = false;
/*     */     
/*  48 */     try(SplitOutputStream outputStream = new SplitOutputStream(temporaryZipFile); 
/*  49 */         RandomAccessFile inputStream = new RandomAccessFile(this.zipModel.getZipFile(), RandomAccessFileMode.READ.getValue())) {
/*     */       
/*  51 */       long currentFileCopyPointer = 0L;
/*  52 */       List<FileHeader> allUnchangedFileHeaders = new ArrayList<>(this.zipModel.getCentralDirectory().getFileHeaders());
/*     */       
/*  54 */       for (FileHeader fileHeader : allUnchangedFileHeaders) {
/*  55 */         long lengthOfCurrentEntry = HeaderUtil.getOffsetOfNextEntry(this.zipModel, fileHeader) - outputStream.getFilePointer();
/*  56 */         if (shouldEntryBeRemoved(fileHeader, entriesToRemove)) {
/*  57 */           updateHeaders(fileHeader, lengthOfCurrentEntry);
/*     */           
/*  59 */           if (!this.zipModel.getCentralDirectory().getFileHeaders().remove(fileHeader)) {
/*  60 */             throw new ZipException("Could not remove entry from list of central directory headers");
/*     */           }
/*     */           
/*  63 */           currentFileCopyPointer += lengthOfCurrentEntry;
/*     */         } else {
/*     */           
/*  66 */           currentFileCopyPointer += copyFile(inputStream, (OutputStream)outputStream, currentFileCopyPointer, lengthOfCurrentEntry, progressMonitor);
/*     */         } 
/*  68 */         verifyIfTaskIsCancelled();
/*     */       } 
/*     */       
/*  71 */       this.headerWriter.finalizeZipFile(this.zipModel, (OutputStream)outputStream, taskParameters.charset);
/*  72 */       successFlag = true;
/*     */     } finally {
/*  74 */       cleanupFile(successFlag, this.zipModel.getZipFile(), temporaryZipFile);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected long calculateTotalWork(RemoveFilesFromZipTaskParameters taskParameters) {
/*  80 */     return this.zipModel.getZipFile().length();
/*     */   }
/*     */   
/*     */   private List<String> filterNonExistingEntries(List<String> filesToRemove) throws ZipException {
/*  84 */     List<String> filteredFilesToRemove = new ArrayList<>();
/*     */     
/*  86 */     for (String fileToRemove : filesToRemove) {
/*  87 */       if (HeaderUtil.getFileHeader(this.zipModel, fileToRemove) != null) {
/*  88 */         filteredFilesToRemove.add(fileToRemove);
/*     */       }
/*     */     } 
/*     */     
/*  92 */     return filteredFilesToRemove;
/*     */   }
/*     */   
/*     */   private boolean shouldEntryBeRemoved(FileHeader fileHeaderToBeChecked, List<String> fileNamesToBeRemoved) {
/*  96 */     for (String fileNameToBeRemoved : fileNamesToBeRemoved) {
/*  97 */       if (fileHeaderToBeChecked.getFileName().startsWith(fileNameToBeRemoved)) {
/*  98 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   private void updateHeaders(FileHeader fileHeaderThatWasRemoved, long offsetToSubtract) throws ZipException {
/* 106 */     updateOffsetsForAllSubsequentFileHeaders(this.zipModel, fileHeaderThatWasRemoved, negate(offsetToSubtract));
/*     */     
/* 108 */     EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = this.zipModel.getEndOfCentralDirectoryRecord();
/* 109 */     endOfCentralDirectoryRecord.setOffsetOfStartOfCentralDirectory(endOfCentralDirectoryRecord
/* 110 */         .getOffsetOfStartOfCentralDirectory() - offsetToSubtract);
/* 111 */     endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(endOfCentralDirectoryRecord
/* 112 */         .getTotalNumberOfEntriesInCentralDirectory() - 1);
/*     */     
/* 114 */     if (endOfCentralDirectoryRecord.getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() > 0) {
/* 115 */       endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(endOfCentralDirectoryRecord
/* 116 */           .getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() - 1);
/*     */     }
/*     */     
/* 119 */     if (this.zipModel.isZip64Format()) {
/* 120 */       this.zipModel.getZip64EndOfCentralDirectoryRecord().setOffsetStartCentralDirectoryWRTStartDiskNumber(this.zipModel
/* 121 */           .getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber() - offsetToSubtract);
/*     */       
/* 123 */       this.zipModel.getZip64EndOfCentralDirectoryRecord().setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(this.zipModel
/* 124 */           .getZip64EndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory() - 1L);
/*     */       
/* 126 */       this.zipModel.getZip64EndOfCentralDirectoryLocator().setOffsetZip64EndOfCentralDirectoryRecord(this.zipModel
/* 127 */           .getZip64EndOfCentralDirectoryLocator().getOffsetZip64EndOfCentralDirectoryRecord() - offsetToSubtract);
/*     */     } 
/*     */   }
/*     */   
/*     */   private long negate(long val) {
/* 132 */     if (val == Long.MIN_VALUE) {
/* 133 */       throw new ArithmeticException("long overflow");
/*     */     }
/*     */     
/* 136 */     return -val;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ProgressMonitor.Task getTask() {
/* 141 */     return ProgressMonitor.Task.REMOVE_ENTRY;
/*     */   }
/*     */   
/*     */   public static class RemoveFilesFromZipTaskParameters extends AbstractZipTaskParameters {
/*     */     private List<String> filesToRemove;
/*     */     
/*     */     public RemoveFilesFromZipTaskParameters(List<String> filesToRemove, Charset charset) {
/* 148 */       super(charset);
/* 149 */       this.filesToRemove = filesToRemove;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\RemoveFilesFromZipTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */