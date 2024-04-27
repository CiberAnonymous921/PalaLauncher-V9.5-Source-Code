/*     */ package net.lingala.zip4j.tasks;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.headers.HeaderUtil;
/*     */ import net.lingala.zip4j.headers.HeaderWriter;
/*     */ import net.lingala.zip4j.io.outputstream.SplitOutputStream;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.model.enums.RandomAccessFileMode;
/*     */ import net.lingala.zip4j.progress.ProgressMonitor;
/*     */ import net.lingala.zip4j.util.RawIO;
/*     */ import net.lingala.zip4j.util.Zip4jUtil;
/*     */ 
/*     */ 
/*     */ public class RenameFilesTask
/*     */   extends AbstractModifyFileTask<RenameFilesTask.RenameFilesTaskParameters>
/*     */ {
/*     */   private ZipModel zipModel;
/*     */   private HeaderWriter headerWriter;
/*     */   private RawIO rawIO;
/*     */   private Charset charset;
/*     */   
/*     */   public RenameFilesTask(ZipModel zipModel, HeaderWriter headerWriter, RawIO rawIO, Charset charset, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/*  33 */     super(asyncTaskParameters);
/*  34 */     this.zipModel = zipModel;
/*  35 */     this.headerWriter = headerWriter;
/*  36 */     this.rawIO = rawIO;
/*  37 */     this.charset = charset;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void executeTask(RenameFilesTaskParameters taskParameters, ProgressMonitor progressMonitor) throws IOException {
/*  42 */     Map<String, String> fileNamesMap = filterNonExistingEntriesAndAddSeparatorIfNeeded(taskParameters.fileNamesMap);
/*  43 */     if (fileNamesMap.size() == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  47 */     File temporaryFile = getTemporaryFile(this.zipModel.getZipFile().getPath());
/*  48 */     boolean successFlag = false;
/*  49 */     try(RandomAccessFile inputStream = new RandomAccessFile(this.zipModel.getZipFile(), RandomAccessFileMode.WRITE.getValue()); 
/*  50 */         SplitOutputStream outputStream = new SplitOutputStream(temporaryFile)) {
/*     */       
/*  52 */       long currentFileCopyPointer = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  59 */       List<FileHeader> allUnchangedFileHeaders = new ArrayList<>(this.zipModel.getCentralDirectory().getFileHeaders());
/*     */       
/*  61 */       for (FileHeader fileHeader : allUnchangedFileHeaders) {
/*  62 */         Map.Entry<String, String> fileNameMapForThisEntry = getCorrespondingEntryFromMap(fileHeader, fileNamesMap);
/*  63 */         progressMonitor.setFileName(fileHeader.getFileName());
/*     */         
/*  65 */         long lengthToCopy = HeaderUtil.getOffsetOfNextEntry(this.zipModel, fileHeader) - outputStream.getFilePointer();
/*  66 */         if (fileNameMapForThisEntry == null) {
/*     */           
/*  68 */           currentFileCopyPointer += copyFile(inputStream, (OutputStream)outputStream, currentFileCopyPointer, lengthToCopy, progressMonitor);
/*     */         } else {
/*  70 */           String newFileName = getNewFileName(fileNameMapForThisEntry.getValue(), fileNameMapForThisEntry.getKey(), fileHeader.getFileName());
/*  71 */           byte[] newFileNameBytes = newFileName.getBytes(this.charset);
/*  72 */           int headersOffset = newFileNameBytes.length - fileHeader.getFileNameLength();
/*     */           
/*  74 */           currentFileCopyPointer = copyEntryAndChangeFileName(newFileNameBytes, fileHeader, currentFileCopyPointer, lengthToCopy, inputStream, (OutputStream)outputStream, progressMonitor);
/*     */ 
/*     */           
/*  77 */           updateHeadersInZipModel(fileHeader, newFileName, newFileNameBytes, headersOffset);
/*     */         } 
/*     */         
/*  80 */         verifyIfTaskIsCancelled();
/*     */       } 
/*     */       
/*  83 */       this.headerWriter.finalizeZipFile(this.zipModel, (OutputStream)outputStream, this.charset);
/*  84 */       successFlag = true;
/*     */     } finally {
/*  86 */       cleanupFile(successFlag, this.zipModel.getZipFile(), temporaryFile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected long calculateTotalWork(RenameFilesTaskParameters taskParameters) {
/*  93 */     return this.zipModel.getZipFile().length();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ProgressMonitor.Task getTask() {
/*  98 */     return ProgressMonitor.Task.RENAME_FILE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long copyEntryAndChangeFileName(byte[] newFileNameBytes, FileHeader fileHeader, long start, long totalLengthOfEntry, RandomAccessFile inputStream, OutputStream outputStream, ProgressMonitor progressMonitor) throws IOException {
/* 104 */     long currentFileCopyPointer = start;
/*     */     
/* 106 */     currentFileCopyPointer += copyFile(inputStream, outputStream, currentFileCopyPointer, 26L, progressMonitor);
/*     */     
/* 108 */     this.rawIO.writeShortLittleEndian(outputStream, newFileNameBytes.length);
/*     */     
/* 110 */     currentFileCopyPointer += 2L;
/* 111 */     currentFileCopyPointer += copyFile(inputStream, outputStream, currentFileCopyPointer, 2L, progressMonitor);
/*     */     
/* 113 */     outputStream.write(newFileNameBytes);
/* 114 */     currentFileCopyPointer += fileHeader.getFileNameLength();
/*     */     
/* 116 */     long remainingLengthToCopy = totalLengthOfEntry - currentFileCopyPointer - start;
/*     */     
/* 118 */     currentFileCopyPointer += copyFile(inputStream, outputStream, currentFileCopyPointer, remainingLengthToCopy, progressMonitor);
/*     */ 
/*     */     
/* 121 */     return currentFileCopyPointer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map.Entry<String, String> getCorrespondingEntryFromMap(FileHeader fileHeaderToBeChecked, Map<String, String> fileNamesMap) {
/* 127 */     for (Map.Entry<String, String> fileHeaderToBeRenamed : fileNamesMap.entrySet()) {
/* 128 */       if (fileHeaderToBeChecked.getFileName().startsWith(fileHeaderToBeRenamed.getKey())) {
/* 129 */         return fileHeaderToBeRenamed;
/*     */       }
/*     */     } 
/*     */     
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateHeadersInZipModel(FileHeader fileHeader, String newFileName, byte[] newFileNameBytes, int headersOffset) throws ZipException {
/* 139 */     FileHeader fileHeaderToBeChanged = HeaderUtil.getFileHeader(this.zipModel, fileHeader.getFileName());
/*     */     
/* 141 */     if (fileHeaderToBeChanged == null)
/*     */     {
/*     */       
/* 144 */       throw new ZipException("could not find any header with name: " + fileHeader.getFileName());
/*     */     }
/*     */     
/* 147 */     fileHeaderToBeChanged.setFileName(newFileName);
/* 148 */     fileHeaderToBeChanged.setFileNameLength(newFileNameBytes.length);
/*     */     
/* 150 */     updateOffsetsForAllSubsequentFileHeaders(this.zipModel, fileHeaderToBeChanged, headersOffset);
/*     */     
/* 152 */     this.zipModel.getEndOfCentralDirectoryRecord().setOffsetOfStartOfCentralDirectory(this.zipModel
/* 153 */         .getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory() + headersOffset);
/*     */     
/* 155 */     if (this.zipModel.isZip64Format()) {
/* 156 */       this.zipModel.getZip64EndOfCentralDirectoryRecord().setOffsetStartCentralDirectoryWRTStartDiskNumber(this.zipModel
/* 157 */           .getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber() + headersOffset);
/*     */ 
/*     */       
/* 160 */       this.zipModel.getZip64EndOfCentralDirectoryLocator().setOffsetZip64EndOfCentralDirectoryRecord(this.zipModel
/* 161 */           .getZip64EndOfCentralDirectoryLocator().getOffsetZip64EndOfCentralDirectoryRecord() + headersOffset);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, String> filterNonExistingEntriesAndAddSeparatorIfNeeded(Map<String, String> inputFileNamesMap) throws ZipException {
/* 167 */     Map<String, String> fileNamesMapToBeChanged = new HashMap<>();
/* 168 */     for (Map.Entry<String, String> allNamesToBeChanged : inputFileNamesMap.entrySet()) {
/* 169 */       if (!Zip4jUtil.isStringNotNullAndNotEmpty(allNamesToBeChanged.getKey())) {
/*     */         continue;
/*     */       }
/*     */       
/* 173 */       FileHeader fileHeaderToBeChanged = HeaderUtil.getFileHeader(this.zipModel, allNamesToBeChanged.getKey());
/* 174 */       if (fileHeaderToBeChanged != null) {
/* 175 */         if (fileHeaderToBeChanged.isDirectory() && !((String)allNamesToBeChanged.getValue()).endsWith("/")) {
/* 176 */           fileNamesMapToBeChanged.put(allNamesToBeChanged.getKey(), (String)allNamesToBeChanged.getValue() + "/"); continue;
/*     */         } 
/* 178 */         fileNamesMapToBeChanged.put(allNamesToBeChanged.getKey(), allNamesToBeChanged.getValue());
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     return fileNamesMapToBeChanged;
/*     */   }
/*     */   
/*     */   private String getNewFileName(String newFileName, String oldFileName, String fileNameFromHeaderToBeChanged) throws ZipException {
/* 186 */     if (fileNameFromHeaderToBeChanged.equals(oldFileName))
/* 187 */       return newFileName; 
/* 188 */     if (fileNameFromHeaderToBeChanged.startsWith(oldFileName)) {
/* 189 */       String fileNameWithoutOldName = fileNameFromHeaderToBeChanged.substring(oldFileName.length());
/* 190 */       return newFileName + fileNameWithoutOldName;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     throw new ZipException("old file name was neither an exact match nor a partial match");
/*     */   }
/*     */   
/*     */   public static class RenameFilesTaskParameters {
/*     */     private Map<String, String> fileNamesMap;
/*     */     
/*     */     public RenameFilesTaskParameters(Map<String, String> fileNamesMap) {
/* 203 */       this.fileNamesMap = fileNamesMap;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\RenameFilesTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */