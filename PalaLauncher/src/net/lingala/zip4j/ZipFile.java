/*      */ package net.lingala.zip4j;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import net.lingala.zip4j.exception.ZipException;
/*      */ import net.lingala.zip4j.headers.HeaderReader;
/*      */ import net.lingala.zip4j.headers.HeaderUtil;
/*      */ import net.lingala.zip4j.headers.HeaderWriter;
/*      */ import net.lingala.zip4j.io.inputstream.NumberedSplitRandomAccessFile;
/*      */ import net.lingala.zip4j.io.inputstream.ZipInputStream;
/*      */ import net.lingala.zip4j.model.FileHeader;
/*      */ import net.lingala.zip4j.model.ZipModel;
/*      */ import net.lingala.zip4j.model.ZipParameters;
/*      */ import net.lingala.zip4j.model.enums.RandomAccessFileMode;
/*      */ import net.lingala.zip4j.progress.ProgressMonitor;
/*      */ import net.lingala.zip4j.tasks.AddFilesToZipTask;
/*      */ import net.lingala.zip4j.tasks.AddFolderToZipTask;
/*      */ import net.lingala.zip4j.tasks.AddStreamToZipTask;
/*      */ import net.lingala.zip4j.tasks.AsyncZipTask;
/*      */ import net.lingala.zip4j.tasks.ExtractAllFilesTask;
/*      */ import net.lingala.zip4j.tasks.ExtractFileTask;
/*      */ import net.lingala.zip4j.tasks.MergeSplitZipFileTask;
/*      */ import net.lingala.zip4j.tasks.RemoveFilesFromZipTask;
/*      */ import net.lingala.zip4j.tasks.RenameFilesTask;
/*      */ import net.lingala.zip4j.tasks.SetCommentTask;
/*      */ import net.lingala.zip4j.util.FileUtils;
/*      */ import net.lingala.zip4j.util.InternalZipConstants;
/*      */ import net.lingala.zip4j.util.RawIO;
/*      */ import net.lingala.zip4j.util.UnzipUtil;
/*      */ import net.lingala.zip4j.util.Zip4jUtil;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ZipFile
/*      */ {
/*      */   private File zipFile;
/*      */   private ZipModel zipModel;
/*      */   private boolean isEncrypted;
/*      */   private ProgressMonitor progressMonitor;
/*      */   private boolean runInThread;
/*      */   private char[] password;
/*   90 */   private HeaderWriter headerWriter = new HeaderWriter();
/*   91 */   private Charset charset = InternalZipConstants.CHARSET_UTF_8;
/*      */ 
/*      */   
/*      */   private ThreadFactory threadFactory;
/*      */ 
/*      */   
/*      */   private ExecutorService executorService;
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(String zipFile) {
/*  102 */     this(new File(zipFile), (char[])null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(String zipFile, char[] password) {
/*  113 */     this(new File(zipFile), password);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(File zipFile) {
/*  123 */     this(zipFile, (char[])null);
/*      */   }
/*      */   
/*      */   public ZipFile(File zipFile, char[] password) {
/*  127 */     this.zipFile = zipFile;
/*  128 */     this.password = password;
/*  129 */     this.runInThread = false;
/*  130 */     this.progressMonitor = new ProgressMonitor();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void createSplitZipFile(List<File> filesToAdd, ZipParameters parameters, boolean splitArchive, long splitLength) throws ZipException {
/*  150 */     if (this.zipFile.exists()) {
/*  151 */       throw new ZipException("zip file: " + this.zipFile + " already exists. To add files to existing zip file use addFile method");
/*      */     }
/*      */ 
/*      */     
/*  155 */     if (filesToAdd == null || filesToAdd.size() == 0) {
/*  156 */       throw new ZipException("input file List is null, cannot create zip file");
/*      */     }
/*      */     
/*  159 */     createNewZipModel();
/*  160 */     this.zipModel.setSplitArchive(splitArchive);
/*  161 */     this.zipModel.setSplitLength(splitLength);
/*      */     
/*  163 */     (new AddFilesToZipTask(this.zipModel, this.password, this.headerWriter, buildAsyncParameters())).execute(new AddFilesToZipTask.AddFilesToZipTaskParameters(filesToAdd, parameters, this.charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void createSplitZipFileFromFolder(File folderToAdd, ZipParameters parameters, boolean splitArchive, long splitLength) throws ZipException {
/*  183 */     if (folderToAdd == null) {
/*  184 */       throw new ZipException("folderToAdd is null, cannot create zip file from folder");
/*      */     }
/*      */     
/*  187 */     if (parameters == null) {
/*  188 */       throw new ZipException("input parameters are null, cannot create zip file from folder");
/*      */     }
/*      */     
/*  191 */     if (this.zipFile.exists()) {
/*  192 */       throw new ZipException("zip file: " + this.zipFile + " already exists. To add files to existing zip file use addFolder method");
/*      */     }
/*      */ 
/*      */     
/*  196 */     createNewZipModel();
/*  197 */     this.zipModel.setSplitArchive(splitArchive);
/*      */     
/*  199 */     if (splitArchive) {
/*  200 */       this.zipModel.setSplitLength(splitLength);
/*      */     }
/*      */     
/*  203 */     addFolder(folderToAdd, parameters, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFile(String fileToAdd) throws ZipException {
/*  214 */     addFile(fileToAdd, new ZipParameters());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFile(String fileToAdd, ZipParameters zipParameters) throws ZipException {
/*  226 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileToAdd)) {
/*  227 */       throw new ZipException("file to add is null or empty");
/*      */     }
/*      */     
/*  230 */     addFiles(Collections.singletonList(new File(fileToAdd)), zipParameters);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFile(File fileToAdd) throws ZipException {
/*  241 */     addFiles(Collections.singletonList(fileToAdd), new ZipParameters());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFile(File fileToAdd, ZipParameters parameters) throws ZipException {
/*  254 */     addFiles(Collections.singletonList(fileToAdd), parameters);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFiles(List<File> filesToAdd) throws ZipException {
/*  265 */     addFiles(filesToAdd, new ZipParameters());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFiles(List<File> filesToAdd, ZipParameters parameters) throws ZipException {
/*  279 */     if (filesToAdd == null || filesToAdd.size() == 0) {
/*  280 */       throw new ZipException("input file List is null or empty");
/*      */     }
/*      */     
/*  283 */     if (parameters == null) {
/*  284 */       throw new ZipException("input parameters are null");
/*      */     }
/*      */     
/*  287 */     if (this.progressMonitor.getState() == ProgressMonitor.State.BUSY) {
/*  288 */       throw new ZipException("invalid operation - Zip4j is in busy state");
/*      */     }
/*      */     
/*  291 */     readZipInfo();
/*      */     
/*  293 */     if (this.zipModel == null) {
/*  294 */       throw new ZipException("internal error: zip model is null");
/*      */     }
/*      */     
/*  297 */     if (this.zipFile.exists() && this.zipModel.isSplitArchive()) {
/*  298 */       throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files");
/*      */     }
/*      */     
/*  301 */     (new AddFilesToZipTask(this.zipModel, this.password, this.headerWriter, buildAsyncParameters())).execute(new AddFilesToZipTask.AddFilesToZipTaskParameters(filesToAdd, parameters, this.charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFolder(File folderToAdd) throws ZipException {
/*  314 */     addFolder(folderToAdd, new ZipParameters());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFolder(File folderToAdd, ZipParameters zipParameters) throws ZipException {
/*  328 */     if (folderToAdd == null) {
/*  329 */       throw new ZipException("input path is null, cannot add folder to zip file");
/*      */     }
/*      */     
/*  332 */     if (!folderToAdd.exists()) {
/*  333 */       throw new ZipException("folder does not exist");
/*      */     }
/*      */     
/*  336 */     if (!folderToAdd.isDirectory()) {
/*  337 */       throw new ZipException("input folder is not a directory");
/*      */     }
/*      */     
/*  340 */     if (!folderToAdd.canRead()) {
/*  341 */       throw new ZipException("cannot read input folder");
/*      */     }
/*      */     
/*  344 */     if (zipParameters == null) {
/*  345 */       throw new ZipException("input parameters are null, cannot add folder to zip file");
/*      */     }
/*      */     
/*  348 */     addFolder(folderToAdd, zipParameters, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addFolder(File folderToAdd, ZipParameters zipParameters, boolean checkSplitArchive) throws ZipException {
/*  361 */     readZipInfo();
/*      */     
/*  363 */     if (this.zipModel == null) {
/*  364 */       throw new ZipException("internal error: zip model is null");
/*      */     }
/*      */     
/*  367 */     if (checkSplitArchive && 
/*  368 */       this.zipModel.isSplitArchive()) {
/*  369 */       throw new ZipException("This is a split archive. Zip file format does not allow updating split/spanned files");
/*      */     }
/*      */ 
/*      */     
/*  373 */     (new AddFolderToZipTask(this.zipModel, this.password, this.headerWriter, buildAsyncParameters())).execute(new AddFolderToZipTask.AddFolderToZipTaskParameters(folderToAdd, zipParameters, this.charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addStream(InputStream inputStream, ZipParameters parameters) throws ZipException {
/*  390 */     if (inputStream == null) {
/*  391 */       throw new ZipException("inputstream is null, cannot add file to zip");
/*      */     }
/*      */     
/*  394 */     if (parameters == null) {
/*  395 */       throw new ZipException("zip parameters are null");
/*      */     }
/*      */     
/*  398 */     setRunInThread(false);
/*      */     
/*  400 */     readZipInfo();
/*      */     
/*  402 */     if (this.zipModel == null) {
/*  403 */       throw new ZipException("internal error: zip model is null");
/*      */     }
/*      */     
/*  406 */     if (this.zipFile.exists() && this.zipModel.isSplitArchive()) {
/*  407 */       throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files");
/*      */     }
/*      */     
/*  410 */     (new AddStreamToZipTask(this.zipModel, this.password, this.headerWriter, buildAsyncParameters())).execute(new AddStreamToZipTask.AddStreamToZipTaskParameters(inputStream, parameters, this.charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void extractAll(String destinationPath) throws ZipException {
/*  424 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(destinationPath)) {
/*  425 */       throw new ZipException("output path is null or invalid");
/*      */     }
/*      */     
/*  428 */     if (!Zip4jUtil.createDirectoryIfNotExists(new File(destinationPath))) {
/*  429 */       throw new ZipException("invalid output path");
/*      */     }
/*      */     
/*  432 */     if (this.zipModel == null) {
/*  433 */       readZipInfo();
/*      */     }
/*      */ 
/*      */     
/*  437 */     if (this.zipModel == null) {
/*  438 */       throw new ZipException("Internal error occurred when extracting zip file");
/*      */     }
/*      */     
/*  441 */     if (this.progressMonitor.getState() == ProgressMonitor.State.BUSY) {
/*  442 */       throw new ZipException("invalid operation - Zip4j is in busy state");
/*      */     }
/*      */     
/*  445 */     (new ExtractAllFilesTask(this.zipModel, this.password, buildAsyncParameters())).execute(new ExtractAllFilesTask.ExtractAllFilesTaskParameters(destinationPath, this.charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void extractFile(FileHeader fileHeader, String destinationPath) throws ZipException {
/*  460 */     extractFile(fileHeader, destinationPath, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void extractFile(FileHeader fileHeader, String destinationPath, String newFileName) throws ZipException {
/*  480 */     if (fileHeader == null) {
/*  481 */       throw new ZipException("input file header is null, cannot extract file");
/*      */     }
/*      */     
/*  484 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(destinationPath)) {
/*  485 */       throw new ZipException("destination path is empty or null, cannot extract file");
/*      */     }
/*      */     
/*  488 */     if (this.progressMonitor.getState() == ProgressMonitor.State.BUSY) {
/*  489 */       throw new ZipException("invalid operation - Zip4j is in busy state");
/*      */     }
/*      */     
/*  492 */     readZipInfo();
/*      */     
/*  494 */     (new ExtractFileTask(this.zipModel, this.password, buildAsyncParameters())).execute(new ExtractFileTask.ExtractFileTaskParameters(destinationPath, fileHeader, newFileName, this.charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void extractFile(String fileName, String destinationPath) throws ZipException {
/*  517 */     extractFile(fileName, destinationPath, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void extractFile(String fileName, String destinationPath, String newFileName) throws ZipException {
/*  546 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
/*  547 */       throw new ZipException("file to extract is null or empty, cannot extract file");
/*      */     }
/*      */     
/*  550 */     readZipInfo();
/*      */     
/*  552 */     FileHeader fileHeader = HeaderUtil.getFileHeader(this.zipModel, fileName);
/*      */     
/*  554 */     if (fileHeader == null) {
/*  555 */       throw new ZipException("No file found with name " + fileName + " in zip file", ZipException.Type.FILE_NOT_FOUND);
/*      */     }
/*      */     
/*  558 */     extractFile(fileHeader, destinationPath, newFileName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<FileHeader> getFileHeaders() throws ZipException {
/*  568 */     readZipInfo();
/*  569 */     if (this.zipModel == null || this.zipModel.getCentralDirectory() == null) {
/*  570 */       return Collections.emptyList();
/*      */     }
/*  572 */     return this.zipModel.getCentralDirectory().getFileHeaders();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FileHeader getFileHeader(String fileName) throws ZipException {
/*  584 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
/*  585 */       throw new ZipException("input file name is emtpy or null, cannot get FileHeader");
/*      */     }
/*      */     
/*  588 */     readZipInfo();
/*  589 */     if (this.zipModel == null || this.zipModel.getCentralDirectory() == null) {
/*  590 */       return null;
/*      */     }
/*      */     
/*  593 */     return HeaderUtil.getFileHeader(this.zipModel, fileName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEncrypted() throws ZipException {
/*  603 */     if (this.zipModel == null) {
/*  604 */       readZipInfo();
/*  605 */       if (this.zipModel == null) {
/*  606 */         throw new ZipException("Zip Model is null");
/*      */       }
/*      */     } 
/*      */     
/*  610 */     if (this.zipModel.getCentralDirectory() == null || this.zipModel.getCentralDirectory().getFileHeaders() == null) {
/*  611 */       throw new ZipException("invalid zip file");
/*      */     }
/*      */     
/*  614 */     for (FileHeader fileHeader : this.zipModel.getCentralDirectory().getFileHeaders()) {
/*  615 */       if (fileHeader != null && 
/*  616 */         fileHeader.isEncrypted()) {
/*  617 */         this.isEncrypted = true;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  623 */     return this.isEncrypted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSplitArchive() throws ZipException {
/*  634 */     if (this.zipModel == null) {
/*  635 */       readZipInfo();
/*  636 */       if (this.zipModel == null) {
/*  637 */         throw new ZipException("Zip Model is null");
/*      */       }
/*      */     } 
/*      */     
/*  641 */     return this.zipModel.isSplitArchive();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeFile(FileHeader fileHeader) throws ZipException {
/*  657 */     if (fileHeader == null) {
/*  658 */       throw new ZipException("input file header is null, cannot remove file");
/*      */     }
/*      */     
/*  661 */     removeFile(fileHeader.getFileName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeFile(String fileName) throws ZipException {
/*  680 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
/*  681 */       throw new ZipException("file name is empty or null, cannot remove file");
/*      */     }
/*      */     
/*  684 */     removeFiles(Collections.singletonList(fileName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeFiles(List<String> fileNames) throws ZipException {
/*  700 */     if (fileNames == null) {
/*  701 */       throw new ZipException("fileNames list is null");
/*      */     }
/*      */     
/*  704 */     if (fileNames.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  708 */     if (this.zipModel == null) {
/*  709 */       readZipInfo();
/*      */     }
/*      */     
/*  712 */     if (this.zipModel.isSplitArchive()) {
/*  713 */       throw new ZipException("Zip file format does not allow updating split/spanned files");
/*      */     }
/*      */     
/*  716 */     (new RemoveFilesFromZipTask(this.zipModel, this.headerWriter, buildAsyncParameters())).execute(new RemoveFilesFromZipTask.RemoveFilesFromZipTaskParameters(fileNames, this.charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renameFile(FileHeader fileHeader, String newFileName) throws ZipException {
/*  734 */     if (fileHeader == null) {
/*  735 */       throw new ZipException("File header is null");
/*      */     }
/*      */     
/*  738 */     renameFile(fileHeader.getFileName(), newFileName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renameFile(String fileNameToRename, String newFileName) throws ZipException {
/*  758 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileNameToRename)) {
/*  759 */       throw new ZipException("file name to be changed is null or empty");
/*      */     }
/*      */     
/*  762 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(newFileName)) {
/*  763 */       throw new ZipException("newFileName is null or empty");
/*      */     }
/*      */     
/*  766 */     renameFiles(Collections.singletonMap(fileNameToRename, newFileName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renameFiles(Map<String, String> fileNamesMap) throws ZipException {
/*  783 */     if (fileNamesMap == null) {
/*  784 */       throw new ZipException("fileNamesMap is null");
/*      */     }
/*      */     
/*  787 */     if (fileNamesMap.size() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  791 */     readZipInfo();
/*      */     
/*  793 */     if (this.zipModel.isSplitArchive()) {
/*  794 */       throw new ZipException("Zip file format does not allow updating split/spanned files");
/*      */     }
/*      */     
/*  797 */     AsyncZipTask.AsyncTaskParameters asyncTaskParameters = buildAsyncParameters();
/*  798 */     (new RenameFilesTask(this.zipModel, this.headerWriter, new RawIO(), this.charset, asyncTaskParameters)).execute(new RenameFilesTask.RenameFilesTaskParameters(fileNamesMap));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void mergeSplitFiles(File outputZipFile) throws ZipException {
/*  810 */     if (outputZipFile == null) {
/*  811 */       throw new ZipException("outputZipFile is null, cannot merge split files");
/*      */     }
/*      */     
/*  814 */     if (outputZipFile.exists()) {
/*  815 */       throw new ZipException("output Zip File already exists");
/*      */     }
/*      */     
/*  818 */     readZipInfo();
/*      */     
/*  820 */     if (this.zipModel == null) {
/*  821 */       throw new ZipException("zip model is null, corrupt zip file?");
/*      */     }
/*      */     
/*  824 */     (new MergeSplitZipFileTask(this.zipModel, buildAsyncParameters())).execute(new MergeSplitZipFileTask.MergeSplitZipFileTaskParameters(outputZipFile, this.charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComment(String comment) throws ZipException {
/*  835 */     if (comment == null) {
/*  836 */       throw new ZipException("input comment is null, cannot update zip file");
/*      */     }
/*      */     
/*  839 */     if (!this.zipFile.exists()) {
/*  840 */       throw new ZipException("zip file does not exist, cannot set comment for zip file");
/*      */     }
/*      */     
/*  843 */     readZipInfo();
/*      */     
/*  845 */     if (this.zipModel == null) {
/*  846 */       throw new ZipException("zipModel is null, cannot update zip file");
/*      */     }
/*      */     
/*  849 */     if (this.zipModel.getEndOfCentralDirectoryRecord() == null) {
/*  850 */       throw new ZipException("end of central directory is null, cannot set comment");
/*      */     }
/*      */     
/*  853 */     (new SetCommentTask(this.zipModel, buildAsyncParameters())).execute(new SetCommentTask.SetCommentTaskTaskParameters(comment, this.charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getComment() throws ZipException {
/*  864 */     if (!this.zipFile.exists()) {
/*  865 */       throw new ZipException("zip file does not exist, cannot read comment");
/*      */     }
/*      */     
/*  868 */     readZipInfo();
/*      */     
/*  870 */     if (this.zipModel == null) {
/*  871 */       throw new ZipException("zip model is null, cannot read comment");
/*      */     }
/*      */     
/*  874 */     if (this.zipModel.getEndOfCentralDirectoryRecord() == null) {
/*  875 */       throw new ZipException("end of central directory record is null, cannot read comment");
/*      */     }
/*      */     
/*  878 */     return this.zipModel.getEndOfCentralDirectoryRecord().getComment();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipInputStream getInputStream(FileHeader fileHeader) throws IOException {
/*  891 */     if (fileHeader == null) {
/*  892 */       throw new ZipException("FileHeader is null, cannot get InputStream");
/*      */     }
/*      */     
/*  895 */     readZipInfo();
/*      */     
/*  897 */     if (this.zipModel == null) {
/*  898 */       throw new ZipException("zip model is null, cannot get inputstream");
/*      */     }
/*      */     
/*  901 */     return UnzipUtil.createZipInputStream(this.zipModel, fileHeader, this.password);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isValidZipFile() {
/*  913 */     if (!this.zipFile.exists()) {
/*  914 */       return false;
/*      */     }
/*      */     
/*      */     try {
/*  918 */       readZipInfo();
/*  919 */       return true;
/*  920 */     } catch (Exception e) {
/*  921 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<File> getSplitZipFiles() throws ZipException {
/*  935 */     readZipInfo();
/*  936 */     return FileUtils.getSplitZipFiles(this.zipModel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPassword(char[] password) {
/*  944 */     this.password = password;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readZipInfo() throws ZipException {
/*  955 */     if (this.zipModel != null) {
/*      */       return;
/*      */     }
/*      */     
/*  959 */     if (!this.zipFile.exists()) {
/*  960 */       createNewZipModel();
/*      */       
/*      */       return;
/*      */     } 
/*  964 */     if (!this.zipFile.canRead()) {
/*  965 */       throw new ZipException("no read access for the input zip file");
/*      */     }
/*      */     
/*  968 */     try (RandomAccessFile randomAccessFile = initializeRandomAccessFileForHeaderReading()) {
/*  969 */       HeaderReader headerReader = new HeaderReader();
/*  970 */       this.zipModel = headerReader.readAllHeaders(randomAccessFile, this.charset);
/*  971 */       this.zipModel.setZipFile(this.zipFile);
/*  972 */     } catch (ZipException e) {
/*  973 */       throw e;
/*  974 */     } catch (IOException e) {
/*  975 */       throw new ZipException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createNewZipModel() {
/*  985 */     this.zipModel = new ZipModel();
/*  986 */     this.zipModel.setZipFile(this.zipFile);
/*      */   }
/*      */   
/*      */   private RandomAccessFile initializeRandomAccessFileForHeaderReading() throws IOException {
/*  990 */     if (FileUtils.isNumberedSplitFile(this.zipFile)) {
/*  991 */       File[] allSplitFiles = FileUtils.getAllSortedNumberedSplitFiles(this.zipFile);
/*      */       
/*  993 */       NumberedSplitRandomAccessFile numberedSplitRandomAccessFile = new NumberedSplitRandomAccessFile(this.zipFile, RandomAccessFileMode.READ.getValue(), allSplitFiles);
/*  994 */       numberedSplitRandomAccessFile.openLastSplitFileForReading();
/*  995 */       return (RandomAccessFile)numberedSplitRandomAccessFile;
/*      */     } 
/*      */     
/*  998 */     return new RandomAccessFile(this.zipFile, RandomAccessFileMode.READ.getValue());
/*      */   }
/*      */   
/*      */   private AsyncZipTask.AsyncTaskParameters buildAsyncParameters() {
/* 1002 */     if (this.runInThread) {
/* 1003 */       if (this.threadFactory == null) {
/* 1004 */         this.threadFactory = Executors.defaultThreadFactory();
/*      */       }
/* 1006 */       this.executorService = Executors.newSingleThreadExecutor(this.threadFactory);
/*      */     } 
/*      */     
/* 1009 */     return new AsyncZipTask.AsyncTaskParameters(this.executorService, this.runInThread, this.progressMonitor);
/*      */   }
/*      */   
/*      */   public ProgressMonitor getProgressMonitor() {
/* 1013 */     return this.progressMonitor;
/*      */   }
/*      */   
/*      */   public boolean isRunInThread() {
/* 1017 */     return this.runInThread;
/*      */   }
/*      */   
/*      */   public void setRunInThread(boolean runInThread) {
/* 1021 */     this.runInThread = runInThread;
/*      */   }
/*      */   
/*      */   public File getFile() {
/* 1025 */     return this.zipFile;
/*      */   }
/*      */   
/*      */   public Charset getCharset() {
/* 1029 */     return this.charset;
/*      */   }
/*      */   
/*      */   public void setCharset(Charset charset) throws IllegalArgumentException {
/* 1033 */     if (charset == null) {
/* 1034 */       throw new IllegalArgumentException("charset cannot be null");
/*      */     }
/* 1036 */     this.charset = charset;
/*      */   }
/*      */   
/*      */   public void setThreadFactory(ThreadFactory threadFactory) {
/* 1040 */     this.threadFactory = threadFactory;
/*      */   }
/*      */   
/*      */   public ExecutorService getExecutorService() {
/* 1044 */     return this.executorService;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1049 */     return this.zipFile.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\ZipFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */