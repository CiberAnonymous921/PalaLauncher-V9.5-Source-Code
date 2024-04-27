/*     */ package net.lingala.zip4j.headers;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.util.InternalZipConstants;
/*     */ import net.lingala.zip4j.util.Zip4jUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HeaderUtil
/*     */ {
/*     */   public static FileHeader getFileHeader(ZipModel zipModel, String fileName) throws ZipException {
/*  20 */     FileHeader fileHeader = getFileHeaderWithExactMatch(zipModel, fileName);
/*     */     
/*  22 */     if (fileHeader == null) {
/*  23 */       fileName = fileName.replaceAll("\\\\", "/");
/*  24 */       fileHeader = getFileHeaderWithExactMatch(zipModel, fileName);
/*     */       
/*  26 */       if (fileHeader == null) {
/*  27 */         fileName = fileName.replaceAll("/", "\\\\");
/*  28 */         fileHeader = getFileHeaderWithExactMatch(zipModel, fileName);
/*     */       } 
/*     */     } 
/*     */     
/*  32 */     return fileHeader;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getIndexOfFileHeader(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
/*  37 */     if (zipModel == null || fileHeader == null) {
/*  38 */       throw new ZipException("input parameters is null, cannot determine index of file header");
/*     */     }
/*     */     
/*  41 */     if (zipModel.getCentralDirectory() == null || zipModel
/*  42 */       .getCentralDirectory().getFileHeaders() == null || zipModel
/*  43 */       .getCentralDirectory().getFileHeaders().size() <= 0) {
/*  44 */       return -1;
/*     */     }
/*     */     
/*  47 */     String fileName = fileHeader.getFileName();
/*     */     
/*  49 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
/*  50 */       throw new ZipException("file name in file header is empty or null, cannot determine index of file header");
/*     */     }
/*     */     
/*  53 */     List<FileHeader> fileHeadersFromCentralDir = zipModel.getCentralDirectory().getFileHeaders();
/*  54 */     for (int i = 0; i < fileHeadersFromCentralDir.size(); i++) {
/*  55 */       FileHeader fileHeaderFromCentralDir = fileHeadersFromCentralDir.get(i);
/*  56 */       String fileNameForHdr = fileHeaderFromCentralDir.getFileName();
/*  57 */       if (Zip4jUtil.isStringNotNullAndNotEmpty(fileNameForHdr))
/*     */       {
/*     */ 
/*     */         
/*  61 */         if (fileName.equalsIgnoreCase(fileNameForHdr))
/*  62 */           return i; 
/*     */       }
/*     */     } 
/*  65 */     return -1;
/*     */   }
/*     */   
/*     */   public static String decodeStringWithCharset(byte[] data, boolean isUtf8Encoded, Charset charset) {
/*  69 */     if (InternalZipConstants.CHARSET_UTF_8.equals(charset) && !isUtf8Encoded) {
/*     */       try {
/*  71 */         return new String(data, "Cp437");
/*  72 */       } catch (UnsupportedEncodingException e) {
/*  73 */         return new String(data);
/*     */       } 
/*     */     }
/*     */     
/*  77 */     if (charset != null) {
/*  78 */       return new String(data, charset);
/*     */     }
/*     */     
/*  81 */     return new String(data, InternalZipConstants.CHARSET_UTF_8);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getOffsetOfNextEntry(ZipModel zipModel, FileHeader fileHeader) throws ZipException {
/*  86 */     int indexOfFileHeader = getIndexOfFileHeader(zipModel, fileHeader);
/*     */     
/*  88 */     List<FileHeader> fileHeaders = zipModel.getCentralDirectory().getFileHeaders();
/*  89 */     if (indexOfFileHeader == fileHeaders.size() - 1) {
/*  90 */       return getOffsetStartOfCentralDirectory(zipModel);
/*     */     }
/*  92 */     return ((FileHeader)fileHeaders.get(indexOfFileHeader + 1)).getOffsetLocalHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getOffsetStartOfCentralDirectory(ZipModel zipModel) {
/*  97 */     if (zipModel.isZip64Format()) {
/*  98 */       return zipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber();
/*     */     }
/*     */     
/* 101 */     return zipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory();
/*     */   }
/*     */   
/*     */   public static List<FileHeader> getFileHeadersUnderDirectory(List<FileHeader> allFileHeaders, FileHeader rootFileHeader) {
/* 105 */     if (!rootFileHeader.isDirectory()) {
/* 106 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 109 */     return (List<FileHeader>)allFileHeaders.stream().filter(e -> e.getFileName().startsWith(rootFileHeader.getFileName())).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public static long getTotalUncompressedSizeOfAllFileHeaders(List<FileHeader> fileHeaders) {
/* 113 */     long totalUncompressedSize = 0L;
/* 114 */     for (FileHeader fileHeader : fileHeaders) {
/* 115 */       if (fileHeader.getZip64ExtendedInfo() != null && fileHeader
/* 116 */         .getZip64ExtendedInfo().getUncompressedSize() > 0L) {
/* 117 */         totalUncompressedSize += fileHeader.getZip64ExtendedInfo().getUncompressedSize(); continue;
/*     */       } 
/* 119 */       totalUncompressedSize += fileHeader.getUncompressedSize();
/*     */     } 
/*     */     
/* 122 */     return totalUncompressedSize;
/*     */   }
/*     */   
/*     */   private static FileHeader getFileHeaderWithExactMatch(ZipModel zipModel, String fileName) throws ZipException {
/* 126 */     if (zipModel == null) {
/* 127 */       throw new ZipException("zip model is null, cannot determine file header with exact match for fileName: " + fileName);
/*     */     }
/*     */ 
/*     */     
/* 131 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
/* 132 */       throw new ZipException("file name is null, cannot determine file header with exact match for fileName: " + fileName);
/*     */     }
/*     */ 
/*     */     
/* 136 */     if (zipModel.getCentralDirectory() == null) {
/* 137 */       throw new ZipException("central directory is null, cannot determine file header with exact match for fileName: " + fileName);
/*     */     }
/*     */ 
/*     */     
/* 141 */     if (zipModel.getCentralDirectory().getFileHeaders() == null) {
/* 142 */       throw new ZipException("file Headers are null, cannot determine file header with exact match for fileName: " + fileName);
/*     */     }
/*     */ 
/*     */     
/* 146 */     if (zipModel.getCentralDirectory().getFileHeaders().size() == 0) {
/* 147 */       return null;
/*     */     }
/*     */     
/* 150 */     for (FileHeader fileHeader : zipModel.getCentralDirectory().getFileHeaders()) {
/* 151 */       String fileNameForHdr = fileHeader.getFileName();
/* 152 */       if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileNameForHdr)) {
/*     */         continue;
/*     */       }
/*     */       
/* 156 */       if (fileName.equalsIgnoreCase(fileNameForHdr)) {
/* 157 */         return fileHeader;
/*     */       }
/*     */     } 
/*     */     
/* 161 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\headers\HeaderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */