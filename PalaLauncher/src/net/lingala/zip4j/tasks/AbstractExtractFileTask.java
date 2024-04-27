/*     */ package net.lingala.zip4j.tasks;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.regex.Matcher;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.io.inputstream.ZipInputStream;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.LocalFileHeader;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.progress.ProgressMonitor;
/*     */ import net.lingala.zip4j.util.BitUtils;
/*     */ import net.lingala.zip4j.util.InternalZipConstants;
/*     */ import net.lingala.zip4j.util.UnzipUtil;
/*     */ import net.lingala.zip4j.util.Zip4jUtil;
/*     */ 
/*     */ 
/*     */ public abstract class AbstractExtractFileTask<T>
/*     */   extends AsyncZipTask<T>
/*     */ {
/*     */   private ZipModel zipModel;
/*  28 */   private byte[] buff = new byte[4096];
/*     */   
/*     */   public AbstractExtractFileTask(ZipModel zipModel, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
/*  31 */     super(asyncTaskParameters);
/*  32 */     this.zipModel = zipModel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extractFile(ZipInputStream zipInputStream, FileHeader fileHeader, String outputPath, String newFileName, ProgressMonitor progressMonitor) throws IOException {
/*  38 */     if (!outputPath.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
/*  39 */       outputPath = outputPath + InternalZipConstants.FILE_SEPARATOR;
/*     */     }
/*     */     
/*  42 */     File outputFile = determineOutputFile(fileHeader, outputPath, newFileName);
/*  43 */     progressMonitor.setFileName(outputFile.getAbsolutePath());
/*     */ 
/*     */     
/*  46 */     String outputCanonicalPath = (new File(outputPath)).getCanonicalPath() + File.separator;
/*  47 */     if (!outputFile.getCanonicalPath().startsWith(outputCanonicalPath)) {
/*  48 */       throw new ZipException("illegal file name that breaks out of the target directory: " + fileHeader
/*  49 */           .getFileName());
/*     */     }
/*     */     
/*  52 */     verifyNextEntry(zipInputStream, fileHeader);
/*     */     
/*  54 */     if (fileHeader.isDirectory()) {
/*  55 */       if (!outputFile.exists() && 
/*  56 */         !outputFile.mkdirs()) {
/*  57 */         throw new ZipException("Could not create directory: " + outputFile);
/*     */       }
/*     */     }
/*  60 */     else if (isSymbolicLink(fileHeader)) {
/*  61 */       createSymLink(zipInputStream, fileHeader, outputFile, progressMonitor);
/*     */     } else {
/*  63 */       checkOutputDirectoryStructure(outputFile);
/*  64 */       unzipFile(zipInputStream, fileHeader, outputFile, progressMonitor);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isSymbolicLink(FileHeader fileHeader) {
/*  69 */     byte[] externalFileAttributes = fileHeader.getExternalFileAttributes();
/*     */     
/*  71 */     if (externalFileAttributes == null || externalFileAttributes.length < 4) {
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     return BitUtils.isBitSet(externalFileAttributes[3], 5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void unzipFile(ZipInputStream inputStream, FileHeader fileHeader, File outputFile, ProgressMonitor progressMonitor) throws IOException {
/*  81 */     try (OutputStream outputStream = new FileOutputStream(outputFile)) {
/*  82 */       int readLength; while ((readLength = inputStream.read(this.buff)) != -1) {
/*  83 */         outputStream.write(this.buff, 0, readLength);
/*  84 */         progressMonitor.updateWorkCompleted(readLength);
/*  85 */         verifyIfTaskIsCancelled();
/*     */       } 
/*  87 */     } catch (Exception e) {
/*  88 */       if (outputFile.exists()) {
/*  89 */         outputFile.delete();
/*     */       }
/*  91 */       throw e;
/*     */     } 
/*     */     
/*  94 */     UnzipUtil.applyFileAttributes(fileHeader, outputFile);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createSymLink(ZipInputStream zipInputStream, FileHeader fileHeader, File outputFile, ProgressMonitor progressMonitor) throws IOException {
/* 100 */     String symLinkPath = new String(readCompleteEntry(zipInputStream, fileHeader, progressMonitor));
/*     */     
/* 102 */     if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
/* 103 */       throw new ZipException("Could not create parent directories");
/*     */     }
/*     */     
/*     */     try {
/* 107 */       Path linkTarget = Paths.get(symLinkPath, new String[0]);
/* 108 */       Files.createSymbolicLink(outputFile.toPath(), linkTarget, (FileAttribute<?>[])new FileAttribute[0]);
/* 109 */       UnzipUtil.applyFileAttributes(fileHeader, outputFile);
/* 110 */     } catch (NoSuchMethodError error) {
/* 111 */       try (OutputStream outputStream = new FileOutputStream(outputFile)) {
/* 112 */         outputStream.write(symLinkPath.getBytes());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] readCompleteEntry(ZipInputStream zipInputStream, FileHeader fileHeader, ProgressMonitor progressMonitor) throws IOException {
/* 119 */     byte[] b = new byte[(int)fileHeader.getUncompressedSize()];
/* 120 */     int readLength = zipInputStream.read(b);
/*     */     
/* 122 */     if (readLength != b.length) {
/* 123 */       throw new ZipException("Could not read complete entry");
/*     */     }
/*     */     
/* 126 */     progressMonitor.updateWorkCompleted(b.length);
/* 127 */     return b;
/*     */   }
/*     */   
/*     */   private void verifyNextEntry(ZipInputStream zipInputStream, FileHeader fileHeader) throws IOException {
/* 131 */     LocalFileHeader localFileHeader = zipInputStream.getNextEntry(fileHeader);
/*     */     
/* 133 */     if (localFileHeader == null) {
/* 134 */       throw new ZipException("Could not read corresponding local file header for file header: " + fileHeader
/* 135 */           .getFileName());
/*     */     }
/*     */     
/* 138 */     if (!fileHeader.getFileName().equals(localFileHeader.getFileName())) {
/* 139 */       throw new ZipException("File header and local file header mismatch");
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkOutputDirectoryStructure(File outputFile) throws ZipException {
/* 144 */     if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
/* 145 */       throw new ZipException("Unable to create parent directories: " + outputFile.getParentFile());
/*     */     }
/*     */   }
/*     */   
/*     */   private File determineOutputFile(FileHeader fileHeader, String outputPath, String newFileName) {
/*     */     String outputFileName;
/* 151 */     if (Zip4jUtil.isStringNotNullAndNotEmpty(newFileName)) {
/* 152 */       outputFileName = newFileName;
/*     */     } else {
/* 154 */       outputFileName = getFileNameWithSystemFileSeparators(fileHeader.getFileName());
/*     */     } 
/*     */     
/* 157 */     return new File(outputPath + InternalZipConstants.FILE_SEPARATOR + outputFileName);
/*     */   }
/*     */   
/*     */   private String getFileNameWithSystemFileSeparators(String fileNameToReplace) {
/* 161 */     return fileNameToReplace.replaceAll("[/\\\\]", Matcher.quoteReplacement(InternalZipConstants.FILE_SEPARATOR));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ProgressMonitor.Task getTask() {
/* 166 */     return ProgressMonitor.Task.EXTRACT_ENTRY;
/*     */   }
/*     */   
/*     */   public ZipModel getZipModel() {
/* 170 */     return this.zipModel;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\AbstractExtractFileTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */