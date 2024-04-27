/*     */ package net.lingala.zip4j.tasks;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.headers.HeaderSignature;
/*     */ import net.lingala.zip4j.headers.HeaderWriter;
/*     */ import net.lingala.zip4j.model.EndOfCentralDirectoryRecord;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator;
/*     */ import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.model.enums.RandomAccessFileMode;
/*     */ import net.lingala.zip4j.progress.ProgressMonitor;
/*     */ import net.lingala.zip4j.util.FileUtils;
/*     */ import net.lingala.zip4j.util.RawIO;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MergeSplitZipFileTask
/*     */   extends AsyncZipTask<MergeSplitZipFileTask.MergeSplitZipFileTaskParameters>
/*     */ {
/*     */   private ZipModel zipModel;
/*  30 */   private RawIO rawIO = new RawIO();
/*     */   
/*     */   public MergeSplitZipFileTask(ZipModel zipModel, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/*  33 */     super(asyncTaskParameters);
/*  34 */     this.zipModel = zipModel;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void executeTask(MergeSplitZipFileTaskParameters taskParameters, ProgressMonitor progressMonitor) throws IOException {
/*  39 */     if (!this.zipModel.isSplitArchive()) {
/*  40 */       ZipException e = new ZipException("archive not a split zip file");
/*  41 */       progressMonitor.endProgressMonitor((Exception)e);
/*  42 */       throw e;
/*     */     } 
/*     */     
/*  45 */     try (OutputStream outputStream = new FileOutputStream(taskParameters.outputZipFile)) {
/*  46 */       long totalBytesWritten = 0L;
/*  47 */       int totalNumberOfSplitFiles = this.zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk();
/*  48 */       if (totalNumberOfSplitFiles <= 0) {
/*  49 */         throw new ZipException("zip archive not a split zip file");
/*     */       }
/*     */       
/*  52 */       int splitSignatureOverhead = 0;
/*  53 */       for (int i = 0; i <= totalNumberOfSplitFiles; i++) {
/*  54 */         try (RandomAccessFile randomAccessFile = createSplitZipFileStream(this.zipModel, i)) {
/*  55 */           int start = 0;
/*  56 */           long end = randomAccessFile.length();
/*     */           
/*  58 */           if (i == 0) {
/*  59 */             if (this.rawIO.readIntLittleEndian(randomAccessFile) == HeaderSignature.SPLIT_ZIP.getValue()) {
/*  60 */               splitSignatureOverhead = 4;
/*  61 */               start = 4;
/*     */             } else {
/*  63 */               randomAccessFile.seek(0L);
/*     */             } 
/*     */           }
/*     */           
/*  67 */           if (i == totalNumberOfSplitFiles) {
/*  68 */             end = this.zipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory();
/*     */           }
/*     */           
/*  71 */           FileUtils.copyFile(randomAccessFile, outputStream, start, end, progressMonitor);
/*  72 */           totalBytesWritten += end - start;
/*  73 */           updateFileHeaderOffsetsForIndex(this.zipModel.getCentralDirectory().getFileHeaders(), (i == 0) ? 0L : totalBytesWritten, i, splitSignatureOverhead);
/*     */           
/*  75 */           verifyIfTaskIsCancelled();
/*     */         } 
/*     */       } 
/*  78 */       updateHeadersForMergeSplitFileAction(this.zipModel, totalBytesWritten, outputStream, taskParameters.charset);
/*  79 */       progressMonitor.endProgressMonitor();
/*  80 */     } catch (CloneNotSupportedException e) {
/*  81 */       throw new ZipException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected long calculateTotalWork(MergeSplitZipFileTaskParameters taskParameters) {
/*  87 */     if (!this.zipModel.isSplitArchive()) {
/*  88 */       return 0L;
/*     */     }
/*     */     
/*  91 */     long totalSize = 0L;
/*  92 */     for (int i = 0; i <= this.zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk(); i++) {
/*  93 */       totalSize += getNextSplitZipFile(this.zipModel, i).length();
/*     */     }
/*  95 */     return totalSize;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateFileHeaderOffsetsForIndex(List<FileHeader> fileHeaders, long offsetToAdd, int index, int splitSignatureOverhead) {
/* 100 */     for (FileHeader fileHeader : fileHeaders) {
/* 101 */       if (fileHeader.getDiskNumberStart() == index) {
/* 102 */         fileHeader.setOffsetLocalHeader(fileHeader.getOffsetLocalHeader() + offsetToAdd - splitSignatureOverhead);
/* 103 */         fileHeader.setDiskNumberStart(0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private File getNextSplitZipFile(ZipModel zipModel, int partNumber) {
/* 109 */     if (partNumber == zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk()) {
/* 110 */       return zipModel.getZipFile();
/*     */     }
/*     */     
/* 113 */     String splitZipExtension = ".z0";
/* 114 */     if (partNumber >= 9) {
/* 115 */       splitZipExtension = ".z";
/*     */     }
/* 117 */     String rootZipFile = zipModel.getZipFile().getPath();
/* 118 */     String nextSplitZipFileName = zipModel.getZipFile().getPath().substring(0, rootZipFile.lastIndexOf(".")) + splitZipExtension + (partNumber + 1);
/*     */     
/* 120 */     return new File(nextSplitZipFileName);
/*     */   }
/*     */   
/*     */   private RandomAccessFile createSplitZipFileStream(ZipModel zipModel, int partNumber) throws FileNotFoundException {
/* 124 */     File splitFile = getNextSplitZipFile(zipModel, partNumber);
/* 125 */     return new RandomAccessFile(splitFile, RandomAccessFileMode.READ.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateHeadersForMergeSplitFileAction(ZipModel zipModel, long totalBytesWritten, OutputStream outputStream, Charset charset) throws IOException, CloneNotSupportedException {
/* 132 */     ZipModel newZipModel = (ZipModel)zipModel.clone();
/* 133 */     newZipModel.getEndOfCentralDirectoryRecord().setOffsetOfStartOfCentralDirectory(totalBytesWritten);
/*     */     
/* 135 */     updateSplitZipModel(newZipModel, totalBytesWritten);
/*     */     
/* 137 */     HeaderWriter headerWriter = new HeaderWriter();
/* 138 */     headerWriter.finalizeZipFileWithoutValidations(newZipModel, outputStream, charset);
/*     */   }
/*     */   
/*     */   private void updateSplitZipModel(ZipModel zipModel, long totalFileSize) {
/* 142 */     zipModel.setSplitArchive(false);
/* 143 */     updateSplitEndCentralDirectory(zipModel);
/*     */     
/* 145 */     if (zipModel.isZip64Format()) {
/* 146 */       updateSplitZip64EndCentralDirLocator(zipModel, totalFileSize);
/* 147 */       updateSplitZip64EndCentralDirRec(zipModel, totalFileSize);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateSplitEndCentralDirectory(ZipModel zipModel) {
/* 152 */     int numberOfFileHeaders = zipModel.getCentralDirectory().getFileHeaders().size();
/* 153 */     EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = zipModel.getEndOfCentralDirectoryRecord();
/* 154 */     endOfCentralDirectoryRecord.setNumberOfThisDisk(0);
/* 155 */     endOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDir(0);
/* 156 */     endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(numberOfFileHeaders);
/* 157 */     endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(numberOfFileHeaders);
/*     */   }
/*     */   
/*     */   private void updateSplitZip64EndCentralDirLocator(ZipModel zipModel, long totalFileSize) {
/* 161 */     if (zipModel.getZip64EndOfCentralDirectoryLocator() == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 166 */     Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator = zipModel.getZip64EndOfCentralDirectoryLocator();
/* 167 */     zip64EndOfCentralDirectoryLocator.setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(0);
/* 168 */     zip64EndOfCentralDirectoryLocator.setOffsetZip64EndOfCentralDirectoryRecord(zip64EndOfCentralDirectoryLocator
/* 169 */         .getOffsetZip64EndOfCentralDirectoryRecord() + totalFileSize);
/* 170 */     zip64EndOfCentralDirectoryLocator.setTotalNumberOfDiscs(1);
/*     */   }
/*     */   
/*     */   private void updateSplitZip64EndCentralDirRec(ZipModel zipModel, long totalFileSize) {
/* 174 */     if (zipModel.getZip64EndOfCentralDirectoryRecord() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 178 */     Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = zipModel.getZip64EndOfCentralDirectoryRecord();
/* 179 */     zip64EndOfCentralDirectoryRecord.setNumberOfThisDisk(0);
/* 180 */     zip64EndOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDirectory(0);
/* 181 */     zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(zipModel
/* 182 */         .getEndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory());
/* 183 */     zip64EndOfCentralDirectoryRecord.setOffsetStartCentralDirectoryWRTStartDiskNumber(zip64EndOfCentralDirectoryRecord
/* 184 */         .getOffsetStartCentralDirectoryWRTStartDiskNumber() + totalFileSize);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ProgressMonitor.Task getTask() {
/* 189 */     return ProgressMonitor.Task.MERGE_ZIP_FILES;
/*     */   }
/*     */   
/*     */   public static class MergeSplitZipFileTaskParameters extends AbstractZipTaskParameters {
/*     */     private File outputZipFile;
/*     */     
/*     */     public MergeSplitZipFileTaskParameters(File outputZipFile, Charset charset) {
/* 196 */       super(charset);
/* 197 */       this.outputZipFile = outputZipFile;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\MergeSplitZipFileTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */