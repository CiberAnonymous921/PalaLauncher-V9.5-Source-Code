/*     */ package net.lingala.zip4j.tasks;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.headers.HeaderUtil;
/*     */ import net.lingala.zip4j.headers.HeaderWriter;
/*     */ import net.lingala.zip4j.io.outputstream.SplitOutputStream;
/*     */ import net.lingala.zip4j.io.outputstream.ZipOutputStream;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.model.ZipParameters;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*     */ import net.lingala.zip4j.model.enums.EncryptionMethod;
/*     */ import net.lingala.zip4j.progress.ProgressMonitor;
/*     */ import net.lingala.zip4j.util.BitUtils;
/*     */ import net.lingala.zip4j.util.CrcUtil;
/*     */ import net.lingala.zip4j.util.FileUtils;
/*     */ import net.lingala.zip4j.util.Zip4jUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAddFileToZipTask<T>
/*     */   extends AsyncZipTask<T>
/*     */ {
/*     */   private ZipModel zipModel;
/*     */   private char[] password;
/*     */   private HeaderWriter headerWriter;
/*  50 */   private byte[] readBuff = new byte[4096];
/*  51 */   private int readLen = -1;
/*     */ 
/*     */   
/*     */   AbstractAddFileToZipTask(ZipModel zipModel, char[] password, HeaderWriter headerWriter, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/*  55 */     super(asyncTaskParameters);
/*  56 */     this.zipModel = zipModel;
/*  57 */     this.password = password;
/*  58 */     this.headerWriter = headerWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void addFilesToZip(List<File> filesToAdd, ProgressMonitor progressMonitor, ZipParameters zipParameters, Charset charset) throws IOException {
/*  64 */     FileUtils.assertFilesExist(filesToAdd, zipParameters.getSymbolicLinkAction());
/*     */     
/*  66 */     List<File> updatedFilesToAdd = removeFilesIfExists(filesToAdd, zipParameters, progressMonitor, charset);
/*     */     
/*  68 */     try(SplitOutputStream splitOutputStream = new SplitOutputStream(this.zipModel.getZipFile(), this.zipModel.getSplitLength()); 
/*  69 */         ZipOutputStream zipOutputStream = initializeOutputStream(splitOutputStream, charset)) {
/*     */       
/*  71 */       for (File fileToAdd : updatedFilesToAdd) {
/*  72 */         verifyIfTaskIsCancelled();
/*  73 */         ZipParameters clonedZipParameters = cloneAndAdjustZipParameters(zipParameters, fileToAdd, progressMonitor);
/*  74 */         progressMonitor.setFileName(fileToAdd.getAbsolutePath());
/*     */         
/*  76 */         if (FileUtils.isSymbolicLink(fileToAdd) && 
/*  77 */           addSymlink(clonedZipParameters)) {
/*  78 */           addSymlinkToZip(fileToAdd, zipOutputStream, clonedZipParameters, splitOutputStream);
/*     */           
/*  80 */           if (ZipParameters.SymbolicLinkAction.INCLUDE_LINK_ONLY.equals(clonedZipParameters.getSymbolicLinkAction())) {
/*     */             continue;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  86 */         addFileToZip(fileToAdd, zipOutputStream, clonedZipParameters, splitOutputStream, progressMonitor);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addSymlinkToZip(File fileToAdd, ZipOutputStream zipOutputStream, ZipParameters zipParameters, SplitOutputStream splitOutputStream) throws IOException {
/*  94 */     ZipParameters clonedZipParameters = new ZipParameters(zipParameters);
/*  95 */     clonedZipParameters.setFileNameInZip(replaceFileNameInZip(zipParameters.getFileNameInZip(), fileToAdd.getName()));
/*  96 */     clonedZipParameters.setEncryptFiles(false);
/*  97 */     clonedZipParameters.setCompressionMethod(CompressionMethod.STORE);
/*     */     
/*  99 */     zipOutputStream.putNextEntry(clonedZipParameters);
/*     */     
/* 101 */     String symLinkTarget = FileUtils.readSymbolicLink(fileToAdd);
/* 102 */     zipOutputStream.write(symLinkTarget.getBytes());
/*     */     
/* 104 */     closeEntry(zipOutputStream, splitOutputStream, fileToAdd, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addFileToZip(File fileToAdd, ZipOutputStream zipOutputStream, ZipParameters zipParameters, SplitOutputStream splitOutputStream, ProgressMonitor progressMonitor) throws IOException {
/* 110 */     zipOutputStream.putNextEntry(zipParameters);
/*     */     
/* 112 */     if (!fileToAdd.isDirectory()) {
/* 113 */       try (InputStream inputStream = new FileInputStream(fileToAdd)) {
/* 114 */         while ((this.readLen = inputStream.read(this.readBuff)) != -1) {
/* 115 */           zipOutputStream.write(this.readBuff, 0, this.readLen);
/* 116 */           progressMonitor.updateWorkCompleted(this.readLen);
/* 117 */           verifyIfTaskIsCancelled();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 122 */     closeEntry(zipOutputStream, splitOutputStream, fileToAdd, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private void closeEntry(ZipOutputStream zipOutputStream, SplitOutputStream splitOutputStream, File fileToAdd, boolean isSymlink) throws IOException {
/* 127 */     FileHeader fileHeader = zipOutputStream.closeEntry();
/* 128 */     byte[] fileAttributes = FileUtils.getFileAttributes(fileToAdd);
/*     */     
/* 130 */     if (!isSymlink)
/*     */     {
/* 132 */       fileAttributes[3] = BitUtils.unsetBit(fileAttributes[3], 5);
/*     */     }
/*     */     
/* 135 */     fileHeader.setExternalFileAttributes(fileAttributes);
/*     */     
/* 137 */     updateLocalFileHeader(fileHeader, splitOutputStream);
/*     */   }
/*     */   
/*     */   long calculateWorkForFiles(List<File> filesToAdd, ZipParameters zipParameters) throws ZipException {
/* 141 */     long totalWork = 0L;
/*     */     
/* 143 */     for (File fileToAdd : filesToAdd) {
/* 144 */       if (!fileToAdd.exists()) {
/*     */         continue;
/*     */       }
/*     */       
/* 148 */       if (zipParameters.isEncryptFiles() && zipParameters.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD) {
/* 149 */         totalWork += fileToAdd.length() * 2L;
/*     */       } else {
/* 151 */         totalWork += fileToAdd.length();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 156 */       String relativeFileName = FileUtils.getRelativeFileName(fileToAdd, zipParameters);
/* 157 */       FileHeader fileHeader = HeaderUtil.getFileHeader(getZipModel(), relativeFileName);
/* 158 */       if (fileHeader != null) {
/* 159 */         totalWork += getZipModel().getZipFile().length() - fileHeader.getCompressedSize();
/*     */       }
/*     */     } 
/*     */     
/* 163 */     return totalWork;
/*     */   }
/*     */   
/*     */   ZipOutputStream initializeOutputStream(SplitOutputStream splitOutputStream, Charset charset) throws IOException {
/* 167 */     if (this.zipModel.getZipFile().exists()) {
/* 168 */       splitOutputStream.seek(HeaderUtil.getOffsetStartOfCentralDirectory(this.zipModel));
/*     */     }
/*     */     
/* 171 */     return new ZipOutputStream((OutputStream)splitOutputStream, this.password, charset, this.zipModel);
/*     */   }
/*     */   
/*     */   void verifyZipParameters(ZipParameters parameters) throws ZipException {
/* 175 */     if (parameters == null) {
/* 176 */       throw new ZipException("cannot validate zip parameters");
/*     */     }
/*     */     
/* 179 */     if (parameters.getCompressionMethod() != CompressionMethod.STORE && parameters.getCompressionMethod() != CompressionMethod.DEFLATE) {
/* 180 */       throw new ZipException("unsupported compression type");
/*     */     }
/*     */     
/* 183 */     if (parameters.isEncryptFiles()) {
/* 184 */       if (parameters.getEncryptionMethod() == EncryptionMethod.NONE) {
/* 185 */         throw new ZipException("Encryption method has to be set, when encrypt files flag is set");
/*     */       }
/*     */       
/* 188 */       if (this.password == null || this.password.length <= 0) {
/* 189 */         throw new ZipException("input password is empty or null");
/*     */       }
/*     */     } else {
/* 192 */       parameters.setEncryptionMethod(EncryptionMethod.NONE);
/*     */     } 
/*     */   }
/*     */   
/*     */   void updateLocalFileHeader(FileHeader fileHeader, SplitOutputStream splitOutputStream) throws IOException {
/* 197 */     this.headerWriter.updateLocalFileHeader(fileHeader, getZipModel(), splitOutputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   private ZipParameters cloneAndAdjustZipParameters(ZipParameters zipParameters, File fileToAdd, ProgressMonitor progressMonitor) throws IOException {
/* 202 */     ZipParameters clonedZipParameters = new ZipParameters(zipParameters);
/* 203 */     clonedZipParameters.setLastModifiedFileTime(Zip4jUtil.javaToDosTime(fileToAdd.lastModified()));
/*     */     
/* 205 */     if (fileToAdd.isDirectory()) {
/* 206 */       clonedZipParameters.setEntrySize(0L);
/*     */     } else {
/* 208 */       clonedZipParameters.setEntrySize(fileToAdd.length());
/*     */     } 
/*     */     
/* 211 */     clonedZipParameters.setWriteExtendedLocalFileHeader(false);
/* 212 */     clonedZipParameters.setLastModifiedFileTime(fileToAdd.lastModified());
/*     */     
/* 214 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(zipParameters.getFileNameInZip())) {
/* 215 */       String relativeFileName = FileUtils.getRelativeFileName(fileToAdd, zipParameters);
/* 216 */       clonedZipParameters.setFileNameInZip(relativeFileName);
/*     */     } 
/*     */     
/* 219 */     if (fileToAdd.isDirectory()) {
/* 220 */       clonedZipParameters.setCompressionMethod(CompressionMethod.STORE);
/* 221 */       clonedZipParameters.setEncryptionMethod(EncryptionMethod.NONE);
/* 222 */       clonedZipParameters.setEncryptFiles(false);
/*     */     } else {
/* 224 */       if (clonedZipParameters.isEncryptFiles() && clonedZipParameters.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD) {
/* 225 */         progressMonitor.setCurrentTask(ProgressMonitor.Task.CALCULATE_CRC);
/* 226 */         clonedZipParameters.setEntryCRC(CrcUtil.computeFileCrc(fileToAdd, progressMonitor));
/* 227 */         progressMonitor.setCurrentTask(ProgressMonitor.Task.ADD_ENTRY);
/*     */       } 
/*     */       
/* 230 */       if (fileToAdd.length() == 0L) {
/* 231 */         clonedZipParameters.setCompressionMethod(CompressionMethod.STORE);
/*     */       }
/*     */     } 
/*     */     
/* 235 */     return clonedZipParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<File> removeFilesIfExists(List<File> files, ZipParameters zipParameters, ProgressMonitor progressMonitor, Charset charset) throws ZipException {
/* 241 */     List<File> filesToAdd = new ArrayList<>(files);
/* 242 */     if (!this.zipModel.getZipFile().exists()) {
/* 243 */       return filesToAdd;
/*     */     }
/*     */     
/* 246 */     for (File file : files) {
/* 247 */       String fileName = FileUtils.getRelativeFileName(file, zipParameters);
/*     */       
/* 249 */       FileHeader fileHeader = HeaderUtil.getFileHeader(this.zipModel, fileName);
/* 250 */       if (fileHeader != null) {
/* 251 */         if (zipParameters.isOverrideExistingFilesInZip()) {
/* 252 */           progressMonitor.setCurrentTask(ProgressMonitor.Task.REMOVE_ENTRY);
/* 253 */           removeFile(fileHeader, progressMonitor, charset);
/* 254 */           verifyIfTaskIsCancelled();
/* 255 */           progressMonitor.setCurrentTask(ProgressMonitor.Task.ADD_ENTRY); continue;
/*     */         } 
/* 257 */         filesToAdd.remove(file);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 262 */     return filesToAdd;
/*     */   }
/*     */   
/*     */   void removeFile(FileHeader fileHeader, ProgressMonitor progressMonitor, Charset charset) throws ZipException {
/* 266 */     AsyncZipTask.AsyncTaskParameters asyncTaskParameters = new AsyncZipTask.AsyncTaskParameters(null, false, progressMonitor);
/* 267 */     RemoveFilesFromZipTask removeFilesFromZipTask = new RemoveFilesFromZipTask(this.zipModel, this.headerWriter, asyncTaskParameters);
/* 268 */     removeFilesFromZipTask.execute(new RemoveFilesFromZipTask.RemoveFilesFromZipTaskParameters(Collections.singletonList(fileHeader.getFileName()), charset));
/*     */   }
/*     */   
/*     */   private String replaceFileNameInZip(String fileInZipWithPath, String newFileName) {
/* 272 */     if (fileInZipWithPath.contains("/")) {
/* 273 */       return fileInZipWithPath.substring(0, fileInZipWithPath.lastIndexOf("/") + 1) + newFileName;
/*     */     }
/*     */     
/* 276 */     return newFileName;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean addSymlink(ZipParameters zipParameters) {
/* 281 */     return (ZipParameters.SymbolicLinkAction.INCLUDE_LINK_ONLY.equals(zipParameters.getSymbolicLinkAction()) || ZipParameters.SymbolicLinkAction.INCLUDE_LINK_AND_LINKED_FILE
/* 282 */       .equals(zipParameters.getSymbolicLinkAction()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ProgressMonitor.Task getTask() {
/* 287 */     return ProgressMonitor.Task.ADD_ENTRY;
/*     */   }
/*     */   
/*     */   protected ZipModel getZipModel() {
/* 291 */     return this.zipModel;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\AbstractAddFileToZipTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */