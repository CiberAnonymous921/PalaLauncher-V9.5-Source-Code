/*     */ package net.lingala.zip4j.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.DosFileAttributeView;
/*     */ import java.nio.file.attribute.DosFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.nio.file.attribute.PosixFileAttributeView;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.model.ExcludeFileFilter;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.model.ZipParameters;
/*     */ import net.lingala.zip4j.progress.ProgressMonitor;
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
/*     */ public class FileUtils
/*     */ {
/*     */   public static void setFileAttributes(Path file, byte[] fileAttributes) {
/*  45 */     if (fileAttributes == null || fileAttributes.length == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  49 */     String os = System.getProperty("os.name").toLowerCase();
/*  50 */     if (isWindows(os)) {
/*  51 */       applyWindowsFileAttributes(file, fileAttributes);
/*  52 */     } else if (isMac(os) || isUnix(os)) {
/*  53 */       applyPosixFileAttributes(file, fileAttributes);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setFileLastModifiedTime(Path file, long lastModifiedTime) {
/*  58 */     if (lastModifiedTime <= 0L || !Files.exists(file, new LinkOption[0])) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  63 */       Files.setLastModifiedTime(file, FileTime.fromMillis(Zip4jUtil.dosToJavaTme(lastModifiedTime)));
/*  64 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setFileLastModifiedTimeWithoutNio(File file, long lastModifiedTime) {
/*  70 */     file.setLastModified(Zip4jUtil.dosToJavaTme(lastModifiedTime));
/*     */   }
/*     */   
/*     */   public static byte[] getFileAttributes(File file) {
/*     */     try {
/*  75 */       if (file == null || (!Files.isSymbolicLink(file.toPath()) && !file.exists())) {
/*  76 */         return new byte[4];
/*     */       }
/*     */       
/*  79 */       Path path = file.toPath();
/*     */       
/*  81 */       String os = System.getProperty("os.name").toLowerCase();
/*  82 */       if (isWindows(os))
/*  83 */         return getWindowsFileAttributes(path); 
/*  84 */       if (isMac(os) || isUnix(os)) {
/*  85 */         return getPosixFileAttributes(path);
/*     */       }
/*  87 */       return new byte[4];
/*     */     }
/*  89 */     catch (NoSuchMethodError e) {
/*  90 */       return new byte[4];
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List<File> getFilesInDirectoryRecursive(File path, boolean readHiddenFiles, boolean readHiddenFolders) throws ZipException {
/*  95 */     return getFilesInDirectoryRecursive(path, readHiddenFiles, readHiddenFolders, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<File> getFilesInDirectoryRecursive(File path, boolean readHiddenFiles, boolean readHiddenFolders, ExcludeFileFilter excludedFiles) throws ZipException {
/* 101 */     if (path == null) {
/* 102 */       throw new ZipException("input path is null, cannot read files in the directory");
/*     */     }
/*     */     
/* 105 */     List<File> result = new ArrayList<>();
/* 106 */     File[] filesAndDirs = path.listFiles();
/*     */     
/* 108 */     if (!path.isDirectory() || !path.canRead() || filesAndDirs == null) {
/* 109 */       return result;
/*     */     }
/*     */     
/* 112 */     for (File file : filesAndDirs) {
/* 113 */       if (excludedFiles == null || !excludedFiles.isExcluded(file))
/*     */       {
/*     */ 
/*     */         
/* 117 */         if (!file.isHidden() || (
/* 118 */           file.isDirectory() ? 
/* 119 */           !readHiddenFolders : 
/*     */ 
/*     */           
/* 122 */           !readHiddenFiles)) {
/*     */ 
/*     */ 
/*     */           
/* 126 */           result.add(file);
/* 127 */           if (file.isDirectory())
/* 128 */             result.addAll(getFilesInDirectoryRecursive(file, readHiddenFiles, readHiddenFolders, excludedFiles)); 
/*     */         } 
/*     */       }
/*     */     } 
/* 132 */     return result;
/*     */   }
/*     */   
/*     */   public static String getFileNameWithoutExtension(String fileName) {
/* 136 */     int pos = fileName.lastIndexOf(".");
/* 137 */     if (pos == -1) {
/* 138 */       return fileName;
/*     */     }
/*     */     
/* 141 */     return fileName.substring(0, pos);
/*     */   }
/*     */   
/*     */   public static String getZipFileNameWithoutExtension(String zipFile) throws ZipException {
/* 145 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(zipFile)) {
/* 146 */       throw new ZipException("zip file name is empty or null, cannot determine zip file name");
/*     */     }
/* 148 */     String tmpFileName = zipFile;
/* 149 */     if (zipFile.contains(System.getProperty("file.separator"))) {
/* 150 */       tmpFileName = zipFile.substring(zipFile.lastIndexOf(System.getProperty("file.separator")) + 1);
/*     */     }
/*     */     
/* 153 */     if (tmpFileName.endsWith(".zip")) {
/* 154 */       tmpFileName = tmpFileName.substring(0, tmpFileName.lastIndexOf("."));
/*     */     }
/* 156 */     return tmpFileName;
/*     */   }
/*     */   
/*     */   public static List<File> getSplitZipFiles(ZipModel zipModel) throws ZipException {
/* 160 */     if (zipModel == null) {
/* 161 */       throw new ZipException("cannot get split zip files: zipmodel is null");
/*     */     }
/*     */     
/* 164 */     if (zipModel.getEndOfCentralDirectoryRecord() == null) {
/* 165 */       return null;
/*     */     }
/*     */     
/* 168 */     if (!zipModel.getZipFile().exists()) {
/* 169 */       throw new ZipException("zip file does not exist");
/*     */     }
/*     */     
/* 172 */     List<File> splitZipFiles = new ArrayList<>();
/* 173 */     File currZipFile = zipModel.getZipFile();
/*     */ 
/*     */     
/* 176 */     if (!zipModel.isSplitArchive()) {
/* 177 */       splitZipFiles.add(currZipFile);
/* 178 */       return splitZipFiles;
/*     */     } 
/*     */     
/* 181 */     int numberOfThisDisk = zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk();
/*     */     
/* 183 */     if (numberOfThisDisk == 0) {
/* 184 */       splitZipFiles.add(currZipFile);
/* 185 */       return splitZipFiles;
/*     */     } 
/* 187 */     for (int i = 0; i <= numberOfThisDisk; i++) {
/* 188 */       if (i == numberOfThisDisk) {
/* 189 */         splitZipFiles.add(zipModel.getZipFile());
/*     */       } else {
/* 191 */         String fileExt = ".z0";
/* 192 */         if (i >= 9) {
/* 193 */           fileExt = ".z";
/*     */         }
/*     */         
/* 196 */         String partFile = currZipFile.getName().contains(".") ? currZipFile.getPath().substring(0, currZipFile.getPath().lastIndexOf(".")) : currZipFile.getPath();
/* 197 */         partFile = partFile + fileExt + (i + 1);
/* 198 */         splitZipFiles.add(new File(partFile));
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     return splitZipFiles;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getRelativeFileName(File fileToAdd, ZipParameters zipParameters) throws ZipException {
/*     */     String fileName;
/*     */     try {
/* 209 */       String fileCanonicalPath = fileToAdd.getCanonicalPath();
/* 210 */       if (Zip4jUtil.isStringNotNullAndNotEmpty(zipParameters.getDefaultFolderPath())) {
/* 211 */         String tmpFileName; File rootFolderFile = new File(zipParameters.getDefaultFolderPath());
/* 212 */         String rootFolderFileRef = rootFolderFile.getCanonicalPath();
/*     */         
/* 214 */         if (!rootFolderFileRef.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
/* 215 */           rootFolderFileRef = rootFolderFileRef + InternalZipConstants.FILE_SEPARATOR;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 220 */         if (isSymbolicLink(fileToAdd)) {
/* 221 */           String rootPath = (new File(fileToAdd.getParentFile().getCanonicalFile().getPath() + File.separator + fileToAdd.getCanonicalFile().getName())).getPath();
/* 222 */           tmpFileName = rootPath.substring(rootFolderFileRef.length());
/*     */         } else {
/* 224 */           tmpFileName = fileCanonicalPath.substring(rootFolderFileRef.length());
/*     */         } 
/*     */         
/* 227 */         if (tmpFileName.startsWith(System.getProperty("file.separator"))) {
/* 228 */           tmpFileName = tmpFileName.substring(1);
/*     */         }
/*     */         
/* 231 */         File tmpFile = new File(fileCanonicalPath);
/*     */         
/* 233 */         if (tmpFile.isDirectory()) {
/* 234 */           tmpFileName = tmpFileName.replaceAll("\\\\", "/");
/* 235 */           tmpFileName = tmpFileName + "/";
/*     */         } else {
/* 237 */           String bkFileName = tmpFileName.substring(0, tmpFileName.lastIndexOf(tmpFile.getName()));
/* 238 */           bkFileName = bkFileName.replaceAll("\\\\", "/");
/* 239 */           tmpFileName = bkFileName + getNameOfFileInZip(tmpFile, zipParameters.getFileNameInZip());
/*     */         } 
/*     */         
/* 242 */         fileName = tmpFileName;
/*     */       } else {
/* 244 */         File relFile = new File(fileCanonicalPath);
/* 245 */         fileName = getNameOfFileInZip(relFile, zipParameters.getFileNameInZip());
/* 246 */         if (relFile.isDirectory()) {
/* 247 */           fileName = fileName + "/";
/*     */         }
/*     */       } 
/* 250 */     } catch (IOException e) {
/* 251 */       throw new ZipException(e);
/*     */     } 
/*     */     
/* 254 */     String rootFolderNameInZip = zipParameters.getRootFolderNameInZip();
/* 255 */     if (Zip4jUtil.isStringNotNullAndNotEmpty(rootFolderNameInZip)) {
/* 256 */       if (!rootFolderNameInZip.endsWith("\\") && !rootFolderNameInZip.endsWith("/")) {
/* 257 */         rootFolderNameInZip = rootFolderNameInZip + InternalZipConstants.FILE_SEPARATOR;
/*     */       }
/*     */       
/* 260 */       rootFolderNameInZip = rootFolderNameInZip.replaceAll("\\\\", "/");
/* 261 */       fileName = rootFolderNameInZip + fileName;
/*     */     } 
/*     */     
/* 264 */     return fileName;
/*     */   }
/*     */   
/*     */   private static String getNameOfFileInZip(File fileToAdd, String fileNameInZip) throws IOException {
/* 268 */     if (Zip4jUtil.isStringNotNullAndNotEmpty(fileNameInZip)) {
/* 269 */       return fileNameInZip;
/*     */     }
/*     */     
/* 272 */     if (isSymbolicLink(fileToAdd)) {
/* 273 */       return fileToAdd.toPath().toRealPath(new LinkOption[] { LinkOption.NOFOLLOW_LINKS }).getFileName().toString();
/*     */     }
/*     */     
/* 276 */     return fileToAdd.getName();
/*     */   }
/*     */   
/*     */   public static boolean isZipEntryDirectory(String fileNameInZip) {
/* 280 */     return (fileNameInZip.endsWith("/") || fileNameInZip.endsWith("\\"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyFile(RandomAccessFile randomAccessFile, OutputStream outputStream, long start, long end, ProgressMonitor progressMonitor) throws ZipException {
/* 286 */     if (start < 0L || end < 0L || start > end) {
/* 287 */       throw new ZipException("invalid offsets");
/*     */     }
/*     */     
/* 290 */     if (start == end) {
/*     */       return;
/*     */     }
/*     */     try {
/*     */       byte[] buff;
/* 295 */       randomAccessFile.seek(start);
/*     */ 
/*     */ 
/*     */       
/* 299 */       long bytesRead = 0L;
/* 300 */       long bytesToRead = end - start;
/*     */       
/* 302 */       if (end - start < 4096L) {
/* 303 */         buff = new byte[(int)bytesToRead];
/*     */       } else {
/* 305 */         buff = new byte[4096];
/*     */       } 
/*     */       int readLen;
/* 308 */       while ((readLen = randomAccessFile.read(buff)) != -1) {
/* 309 */         outputStream.write(buff, 0, readLen);
/*     */         
/* 311 */         progressMonitor.updateWorkCompleted(readLen);
/* 312 */         if (progressMonitor.isCancelAllTasks()) {
/* 313 */           progressMonitor.setResult(ProgressMonitor.Result.CANCELLED);
/*     */           
/*     */           return;
/*     */         } 
/* 317 */         bytesRead += readLen;
/*     */         
/* 319 */         if (bytesRead == bytesToRead)
/*     */           break; 
/* 321 */         if (bytesRead + buff.length > bytesToRead) {
/* 322 */           buff = new byte[(int)(bytesToRead - bytesRead)];
/*     */         }
/*     */       }
/*     */     
/* 326 */     } catch (IOException e) {
/* 327 */       throw new ZipException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void assertFilesExist(List<File> files, ZipParameters.SymbolicLinkAction symLinkAction) throws ZipException {
/* 332 */     for (File file : files) {
/* 333 */       if (isSymbolicLink(file)) {
/*     */ 
/*     */         
/* 336 */         if (symLinkAction.equals(ZipParameters.SymbolicLinkAction.INCLUDE_LINK_AND_LINKED_FILE) || symLinkAction
/* 337 */           .equals(ZipParameters.SymbolicLinkAction.INCLUDE_LINKED_FILE_ONLY))
/* 338 */           assertSymbolicLinkTargetExists(file); 
/*     */         continue;
/*     */       } 
/* 341 */       assertFileExists(file);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isNumberedSplitFile(File file) {
/* 347 */     return file.getName().endsWith(".zip.001");
/*     */   }
/*     */   
/*     */   public static String getFileExtension(File file) {
/* 351 */     String fileName = file.getName();
/*     */     
/* 353 */     if (!fileName.contains(".")) {
/* 354 */       return "";
/*     */     }
/*     */     
/* 357 */     return fileName.substring(fileName.lastIndexOf(".") + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File[] getAllSortedNumberedSplitFiles(File firstNumberedFile) {
/* 367 */     String zipFileNameWithoutExtension = getFileNameWithoutExtension(firstNumberedFile.getName());
/* 368 */     File[] allSplitFiles = firstNumberedFile.getParentFile().listFiles((dir, name) -> name.startsWith(zipFileNameWithoutExtension + "."));
/*     */     
/* 370 */     if (allSplitFiles == null) {
/* 371 */       return new File[0];
/*     */     }
/*     */     
/* 374 */     Arrays.sort((Object[])allSplitFiles);
/*     */     
/* 376 */     return allSplitFiles;
/*     */   }
/*     */   
/*     */   public static String getNextNumberedSplitFileCounterAsExtension(int index) {
/* 380 */     return "." + getExtensionZerosPrefix(index) + (index + 1);
/*     */   }
/*     */   
/*     */   public static boolean isSymbolicLink(File file) {
/*     */     try {
/* 385 */       return Files.isSymbolicLink(file.toPath());
/* 386 */     } catch (Exception|Error e) {
/* 387 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String readSymbolicLink(File file) {
/*     */     try {
/* 393 */       return Files.readSymbolicLink(file.toPath()).toString();
/* 394 */     } catch (Exception|Error e) {
/* 395 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String getExtensionZerosPrefix(int index) {
/* 400 */     if (index < 9)
/* 401 */       return "00"; 
/* 402 */     if (index < 99) {
/* 403 */       return "0";
/*     */     }
/* 405 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   private static void applyWindowsFileAttributes(Path file, byte[] fileAttributes) {
/* 410 */     if (fileAttributes[0] == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 415 */     DosFileAttributeView fileAttributeView = Files.<DosFileAttributeView>getFileAttributeView(file, DosFileAttributeView.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*     */     try {
/* 417 */       fileAttributeView.setReadOnly(BitUtils.isBitSet(fileAttributes[0], 0));
/* 418 */       fileAttributeView.setHidden(BitUtils.isBitSet(fileAttributes[0], 1));
/* 419 */       fileAttributeView.setSystem(BitUtils.isBitSet(fileAttributes[0], 2));
/* 420 */       fileAttributeView.setArchive(BitUtils.isBitSet(fileAttributes[0], 5));
/* 421 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void applyPosixFileAttributes(Path file, byte[] fileAttributes) {
/* 427 */     if (fileAttributes[2] == 0 && fileAttributes[3] == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 433 */       Set<PosixFilePermission> posixFilePermissions = new HashSet<>();
/* 434 */       addIfBitSet(fileAttributes[3], 0, posixFilePermissions, PosixFilePermission.OWNER_READ);
/* 435 */       addIfBitSet(fileAttributes[2], 7, posixFilePermissions, PosixFilePermission.OWNER_WRITE);
/* 436 */       addIfBitSet(fileAttributes[2], 6, posixFilePermissions, PosixFilePermission.OWNER_EXECUTE);
/* 437 */       addIfBitSet(fileAttributes[2], 5, posixFilePermissions, PosixFilePermission.GROUP_READ);
/* 438 */       addIfBitSet(fileAttributes[2], 4, posixFilePermissions, PosixFilePermission.GROUP_WRITE);
/* 439 */       addIfBitSet(fileAttributes[2], 3, posixFilePermissions, PosixFilePermission.GROUP_EXECUTE);
/* 440 */       addIfBitSet(fileAttributes[2], 2, posixFilePermissions, PosixFilePermission.OTHERS_READ);
/* 441 */       addIfBitSet(fileAttributes[2], 1, posixFilePermissions, PosixFilePermission.OTHERS_WRITE);
/* 442 */       addIfBitSet(fileAttributes[2], 0, posixFilePermissions, PosixFilePermission.OTHERS_EXECUTE);
/* 443 */       Files.setPosixFilePermissions(file, posixFilePermissions);
/* 444 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] getWindowsFileAttributes(Path file) {
/* 450 */     byte[] fileAttributes = new byte[4];
/*     */     
/*     */     try {
/* 453 */       DosFileAttributeView dosFileAttributeView = Files.<DosFileAttributeView>getFileAttributeView(file, DosFileAttributeView.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*     */       
/* 455 */       DosFileAttributes dosFileAttributes = dosFileAttributeView.readAttributes();
/*     */       
/* 457 */       byte windowsAttribute = 0;
/*     */       
/* 459 */       windowsAttribute = setBitIfApplicable(dosFileAttributes.isReadOnly(), windowsAttribute, 0);
/* 460 */       windowsAttribute = setBitIfApplicable(dosFileAttributes.isHidden(), windowsAttribute, 1);
/* 461 */       windowsAttribute = setBitIfApplicable(dosFileAttributes.isSystem(), windowsAttribute, 2);
/* 462 */       windowsAttribute = setBitIfApplicable(dosFileAttributes.isArchive(), windowsAttribute, 5);
/* 463 */       fileAttributes[0] = windowsAttribute;
/* 464 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */     
/* 468 */     return fileAttributes;
/*     */   }
/*     */   
/*     */   private static void assertFileExists(File file) throws ZipException {
/* 472 */     if (!file.exists()) {
/* 473 */       throw new ZipException("File does not exist: " + file);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void assertSymbolicLinkTargetExists(File file) throws ZipException {
/* 478 */     if (!file.exists()) {
/* 479 */       throw new ZipException("Symlink target '" + readSymbolicLink(file) + "' does not exist for link '" + file + "'");
/*     */     }
/*     */   }
/*     */   
/*     */   private static byte[] getPosixFileAttributes(Path file) {
/* 484 */     byte[] fileAttributes = new byte[4];
/*     */     
/*     */     try {
/* 487 */       PosixFileAttributeView posixFileAttributeView = Files.<PosixFileAttributeView>getFileAttributeView(file, PosixFileAttributeView.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*     */       
/* 489 */       Set<PosixFilePermission> posixFilePermissions = posixFileAttributeView.readAttributes().permissions();
/*     */       
/* 491 */       fileAttributes[3] = setBitIfApplicable(Files.isRegularFile(file, new LinkOption[0]), fileAttributes[3], 7);
/* 492 */       fileAttributes[3] = setBitIfApplicable(Files.isDirectory(file, new LinkOption[0]), fileAttributes[3], 6);
/* 493 */       fileAttributes[3] = setBitIfApplicable(Files.isSymbolicLink(file), fileAttributes[3], 5);
/* 494 */       fileAttributes[3] = setBitIfApplicable(posixFilePermissions.contains(PosixFilePermission.OWNER_READ), fileAttributes[3], 0);
/* 495 */       fileAttributes[2] = setBitIfApplicable(posixFilePermissions.contains(PosixFilePermission.OWNER_WRITE), fileAttributes[2], 7);
/* 496 */       fileAttributes[2] = setBitIfApplicable(posixFilePermissions.contains(PosixFilePermission.OWNER_EXECUTE), fileAttributes[2], 6);
/* 497 */       fileAttributes[2] = setBitIfApplicable(posixFilePermissions.contains(PosixFilePermission.GROUP_READ), fileAttributes[2], 5);
/* 498 */       fileAttributes[2] = setBitIfApplicable(posixFilePermissions.contains(PosixFilePermission.GROUP_WRITE), fileAttributes[2], 4);
/* 499 */       fileAttributes[2] = setBitIfApplicable(posixFilePermissions.contains(PosixFilePermission.GROUP_EXECUTE), fileAttributes[2], 3);
/* 500 */       fileAttributes[2] = setBitIfApplicable(posixFilePermissions.contains(PosixFilePermission.OTHERS_READ), fileAttributes[2], 2);
/* 501 */       fileAttributes[2] = setBitIfApplicable(posixFilePermissions.contains(PosixFilePermission.OTHERS_WRITE), fileAttributes[2], 1);
/* 502 */       fileAttributes[2] = setBitIfApplicable(posixFilePermissions.contains(PosixFilePermission.OTHERS_EXECUTE), fileAttributes[2], 0);
/* 503 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */     
/* 507 */     return fileAttributes;
/*     */   }
/*     */   
/*     */   private static byte setBitIfApplicable(boolean applicable, byte b, int pos) {
/* 511 */     if (applicable) {
/* 512 */       b = BitUtils.setBit(b, pos);
/*     */     }
/*     */     
/* 515 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addIfBitSet(byte b, int pos, Set<PosixFilePermission> posixFilePermissions, PosixFilePermission posixFilePermissionToAdd) {
/* 520 */     if (BitUtils.isBitSet(b, pos)) {
/* 521 */       posixFilePermissions.add(posixFilePermissionToAdd);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isWindows() {
/* 526 */     String os = System.getProperty("os.name").toLowerCase();
/* 527 */     return isWindows(os);
/*     */   }
/*     */   
/*     */   private static boolean isWindows(String os) {
/* 531 */     return os.contains("win");
/*     */   }
/*     */   
/*     */   private static boolean isMac(String os) {
/* 535 */     return os.contains("mac");
/*     */   }
/*     */   
/*     */   private static boolean isUnix(String os) {
/* 539 */     return os.contains("nux");
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4\\util\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */