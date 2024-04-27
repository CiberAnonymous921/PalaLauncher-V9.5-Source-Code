/*     */ package net.lingala.zip4j.model;
/*     */ 
/*     */ import net.lingala.zip4j.model.enums.AesKeyStrength;
/*     */ import net.lingala.zip4j.model.enums.AesVersion;
/*     */ import net.lingala.zip4j.model.enums.CompressionLevel;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*     */ import net.lingala.zip4j.model.enums.EncryptionMethod;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZipParameters
/*     */ {
/*     */   public enum SymbolicLinkAction
/*     */   {
/*  37 */     INCLUDE_LINK_ONLY,
/*     */ 
/*     */ 
/*     */     
/*  41 */     INCLUDE_LINKED_FILE_ONLY,
/*     */ 
/*     */ 
/*     */     
/*  45 */     INCLUDE_LINK_AND_LINKED_FILE;
/*     */   }
/*     */   
/*  48 */   private CompressionMethod compressionMethod = CompressionMethod.DEFLATE;
/*  49 */   private CompressionLevel compressionLevel = CompressionLevel.NORMAL;
/*     */   private boolean encryptFiles = false;
/*  51 */   private EncryptionMethod encryptionMethod = EncryptionMethod.NONE;
/*     */   private boolean readHiddenFiles = true;
/*     */   private boolean readHiddenFolders = true;
/*  54 */   private AesKeyStrength aesKeyStrength = AesKeyStrength.KEY_STRENGTH_256;
/*  55 */   private AesVersion aesVersion = AesVersion.TWO;
/*     */   private boolean includeRootFolder = true;
/*     */   private long entryCRC;
/*     */   private String defaultFolderPath;
/*     */   private String fileNameInZip;
/*  60 */   private long lastModifiedFileTime = System.currentTimeMillis();
/*  61 */   private long entrySize = -1L;
/*     */   private boolean writeExtendedLocalFileHeader = true;
/*     */   private boolean overrideExistingFilesInZip = true;
/*     */   private String rootFolderNameInZip;
/*     */   private String fileComment;
/*  66 */   private SymbolicLinkAction symbolicLinkAction = SymbolicLinkAction.INCLUDE_LINKED_FILE_ONLY;
/*     */ 
/*     */ 
/*     */   
/*     */   private ExcludeFileFilter excludeFileFilter;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean unixMode;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipParameters() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipParameters(ZipParameters zipParameters) {
/*  85 */     this.compressionMethod = zipParameters.getCompressionMethod();
/*  86 */     this.compressionLevel = zipParameters.getCompressionLevel();
/*  87 */     this.encryptFiles = zipParameters.isEncryptFiles();
/*  88 */     this.encryptionMethod = zipParameters.getEncryptionMethod();
/*  89 */     this.readHiddenFiles = zipParameters.isReadHiddenFiles();
/*  90 */     this.readHiddenFolders = zipParameters.isReadHiddenFolders();
/*  91 */     this.aesKeyStrength = zipParameters.getAesKeyStrength();
/*  92 */     this.aesVersion = zipParameters.getAesVersion();
/*  93 */     this.includeRootFolder = zipParameters.isIncludeRootFolder();
/*  94 */     this.entryCRC = zipParameters.getEntryCRC();
/*  95 */     this.defaultFolderPath = zipParameters.getDefaultFolderPath();
/*  96 */     this.fileNameInZip = zipParameters.getFileNameInZip();
/*  97 */     this.lastModifiedFileTime = zipParameters.getLastModifiedFileTime();
/*  98 */     this.entrySize = zipParameters.getEntrySize();
/*  99 */     this.writeExtendedLocalFileHeader = zipParameters.isWriteExtendedLocalFileHeader();
/* 100 */     this.overrideExistingFilesInZip = zipParameters.isOverrideExistingFilesInZip();
/* 101 */     this.rootFolderNameInZip = zipParameters.getRootFolderNameInZip();
/* 102 */     this.fileComment = zipParameters.getFileComment();
/* 103 */     this.symbolicLinkAction = zipParameters.getSymbolicLinkAction();
/* 104 */     this.excludeFileFilter = zipParameters.getExcludeFileFilter();
/* 105 */     this.unixMode = zipParameters.isUnixMode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompressionMethod getCompressionMethod() {
/* 113 */     return this.compressionMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompressionMethod(CompressionMethod compressionMethod) {
/* 121 */     this.compressionMethod = compressionMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEncryptFiles() {
/* 129 */     return this.encryptFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncryptFiles(boolean encryptFiles) {
/* 137 */     this.encryptFiles = encryptFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EncryptionMethod getEncryptionMethod() {
/* 145 */     return this.encryptionMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncryptionMethod(EncryptionMethod encryptionMethod) {
/* 153 */     this.encryptionMethod = encryptionMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompressionLevel getCompressionLevel() {
/* 161 */     return this.compressionLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompressionLevel(CompressionLevel compressionLevel) {
/* 169 */     this.compressionLevel = compressionLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadHiddenFiles() {
/* 178 */     return this.readHiddenFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadHiddenFiles(boolean readHiddenFiles) {
/* 187 */     this.readHiddenFiles = readHiddenFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadHiddenFolders() {
/* 196 */     return this.readHiddenFolders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadHiddenFolders(boolean readHiddenFolders) {
/* 204 */     this.readHiddenFolders = readHiddenFolders;
/*     */   }
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 208 */     return super.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AesKeyStrength getAesKeyStrength() {
/* 216 */     return this.aesKeyStrength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAesKeyStrength(AesKeyStrength aesKeyStrength) {
/* 224 */     this.aesKeyStrength = aesKeyStrength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AesVersion getAesVersion() {
/* 232 */     return this.aesVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAesVersion(AesVersion aesVersion) {
/* 240 */     this.aesVersion = aesVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeRootFolder() {
/* 248 */     return this.includeRootFolder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeRootFolder(boolean includeRootFolder) {
/* 256 */     this.includeRootFolder = includeRootFolder;
/*     */   }
/*     */   
/*     */   public long getEntryCRC() {
/* 260 */     return this.entryCRC;
/*     */   }
/*     */   
/*     */   public void setEntryCRC(long entryCRC) {
/* 264 */     this.entryCRC = entryCRC;
/*     */   }
/*     */   
/*     */   public String getDefaultFolderPath() {
/* 268 */     return this.defaultFolderPath;
/*     */   }
/*     */   
/*     */   public void setDefaultFolderPath(String defaultFolderPath) {
/* 272 */     this.defaultFolderPath = defaultFolderPath;
/*     */   }
/*     */   
/*     */   public String getFileNameInZip() {
/* 276 */     return this.fileNameInZip;
/*     */   }
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
/*     */   public void setFileNameInZip(String fileNameInZip) {
/* 289 */     this.fileNameInZip = fileNameInZip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastModifiedFileTime() {
/* 297 */     return this.lastModifiedFileTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastModifiedFileTime(long lastModifiedFileTime) {
/* 306 */     if (lastModifiedFileTime <= 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 310 */     this.lastModifiedFileTime = lastModifiedFileTime;
/*     */   }
/*     */   
/*     */   public long getEntrySize() {
/* 314 */     return this.entrySize;
/*     */   }
/*     */   
/*     */   public void setEntrySize(long entrySize) {
/* 318 */     this.entrySize = entrySize;
/*     */   }
/*     */   
/*     */   public boolean isWriteExtendedLocalFileHeader() {
/* 322 */     return this.writeExtendedLocalFileHeader;
/*     */   }
/*     */   
/*     */   public void setWriteExtendedLocalFileHeader(boolean writeExtendedLocalFileHeader) {
/* 326 */     this.writeExtendedLocalFileHeader = writeExtendedLocalFileHeader;
/*     */   }
/*     */   
/*     */   public boolean isOverrideExistingFilesInZip() {
/* 330 */     return this.overrideExistingFilesInZip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverrideExistingFilesInZip(boolean overrideExistingFilesInZip) {
/* 338 */     this.overrideExistingFilesInZip = overrideExistingFilesInZip;
/*     */   }
/*     */   
/*     */   public String getRootFolderNameInZip() {
/* 342 */     return this.rootFolderNameInZip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRootFolderNameInZip(String rootFolderNameInZip) {
/* 353 */     this.rootFolderNameInZip = rootFolderNameInZip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileComment() {
/* 361 */     return this.fileComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFileComment(String fileComment) {
/* 369 */     this.fileComment = fileComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SymbolicLinkAction getSymbolicLinkAction() {
/* 377 */     return this.symbolicLinkAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSymbolicLinkAction(SymbolicLinkAction symbolicLinkAction) {
/* 385 */     this.symbolicLinkAction = symbolicLinkAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExcludeFileFilter getExcludeFileFilter() {
/* 393 */     return this.excludeFileFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludeFileFilter(ExcludeFileFilter excludeFileFilter) {
/* 401 */     this.excludeFileFilter = excludeFileFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnixMode() {
/* 409 */     return this.unixMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnixMode(boolean unixMode) {
/* 417 */     this.unixMode = unixMode;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\ZipParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */