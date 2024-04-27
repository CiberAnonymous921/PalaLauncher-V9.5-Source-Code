/*     */ package net.lingala.zip4j.headers;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.model.AESExtraDataRecord;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.LocalFileHeader;
/*     */ import net.lingala.zip4j.model.ZipParameters;
/*     */ import net.lingala.zip4j.model.enums.AesKeyStrength;
/*     */ import net.lingala.zip4j.model.enums.CompressionLevel;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*     */ import net.lingala.zip4j.model.enums.EncryptionMethod;
/*     */ import net.lingala.zip4j.util.BitUtils;
/*     */ import net.lingala.zip4j.util.FileUtils;
/*     */ import net.lingala.zip4j.util.InternalZipConstants;
/*     */ import net.lingala.zip4j.util.RawIO;
/*     */ import net.lingala.zip4j.util.Zip4jUtil;
/*     */ import net.lingala.zip4j.util.ZipVersionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileHeaderFactory
/*     */ {
/*     */   public FileHeader generateFileHeader(ZipParameters zipParameters, boolean isSplitZip, int currentDiskNumberStart, Charset charset, RawIO rawIO) throws ZipException {
/*  30 */     FileHeader fileHeader = new FileHeader();
/*  31 */     fileHeader.setSignature(HeaderSignature.CENTRAL_DIRECTORY);
/*  32 */     fileHeader.setVersionMadeBy(ZipVersionUtils.determineVersionMadeBy(zipParameters, rawIO));
/*  33 */     fileHeader.setVersionNeededToExtract(ZipVersionUtils.determineVersionNeededToExtract(zipParameters).getCode());
/*     */     
/*  35 */     if (zipParameters.isEncryptFiles() && zipParameters.getEncryptionMethod() == EncryptionMethod.AES) {
/*  36 */       fileHeader.setCompressionMethod(CompressionMethod.AES_INTERNAL_ONLY);
/*  37 */       fileHeader.setAesExtraDataRecord(generateAESExtraDataRecord(zipParameters));
/*  38 */       fileHeader.setExtraFieldLength(fileHeader.getExtraFieldLength() + 11);
/*     */     } else {
/*  40 */       fileHeader.setCompressionMethod(zipParameters.getCompressionMethod());
/*     */     } 
/*     */     
/*  43 */     if (zipParameters.isEncryptFiles()) {
/*  44 */       if (zipParameters.getEncryptionMethod() == null || zipParameters.getEncryptionMethod() == EncryptionMethod.NONE) {
/*  45 */         throw new ZipException("Encryption method has to be set when encryptFiles flag is set in zip parameters");
/*     */       }
/*     */       
/*  48 */       fileHeader.setEncrypted(true);
/*  49 */       fileHeader.setEncryptionMethod(zipParameters.getEncryptionMethod());
/*     */     } 
/*     */     
/*  52 */     String fileName = validateAndGetFileName(zipParameters.getFileNameInZip());
/*  53 */     fileHeader.setFileName(fileName);
/*  54 */     fileHeader.setFileNameLength(determineFileNameLength(fileName, charset));
/*  55 */     fileHeader.setDiskNumberStart(isSplitZip ? currentDiskNumberStart : 0);
/*     */     
/*  57 */     if (zipParameters.getLastModifiedFileTime() > 0L) {
/*  58 */       fileHeader.setLastModifiedTime(Zip4jUtil.javaToDosTime(zipParameters.getLastModifiedFileTime()));
/*     */     } else {
/*  60 */       fileHeader.setLastModifiedTime(Zip4jUtil.javaToDosTime(System.currentTimeMillis()));
/*     */     } 
/*     */ 
/*     */     
/*  64 */     fileHeader.setExternalFileAttributes(new byte[4]);
/*  65 */     fileHeader.setDirectory(FileUtils.isZipEntryDirectory(fileName));
/*     */     
/*  67 */     if (zipParameters.isWriteExtendedLocalFileHeader() && zipParameters.getEntrySize() == -1L) {
/*  68 */       fileHeader.setUncompressedSize(0L);
/*     */     } else {
/*  70 */       fileHeader.setUncompressedSize(zipParameters.getEntrySize());
/*     */     } 
/*     */     
/*  73 */     if (zipParameters.isEncryptFiles() && zipParameters.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD) {
/*  74 */       fileHeader.setCrc(zipParameters.getEntryCRC());
/*     */     }
/*     */     
/*  77 */     fileHeader.setGeneralPurposeFlag(determineGeneralPurposeBitFlag(fileHeader.isEncrypted(), zipParameters, charset));
/*  78 */     fileHeader.setDataDescriptorExists(zipParameters.isWriteExtendedLocalFileHeader());
/*  79 */     fileHeader.setFileComment(zipParameters.getFileComment());
/*  80 */     return fileHeader;
/*     */   }
/*     */   
/*     */   public LocalFileHeader generateLocalFileHeader(FileHeader fileHeader) {
/*  84 */     LocalFileHeader localFileHeader = new LocalFileHeader();
/*  85 */     localFileHeader.setSignature(HeaderSignature.LOCAL_FILE_HEADER);
/*  86 */     localFileHeader.setVersionNeededToExtract(fileHeader.getVersionNeededToExtract());
/*  87 */     localFileHeader.setCompressionMethod(fileHeader.getCompressionMethod());
/*  88 */     localFileHeader.setLastModifiedTime(fileHeader.getLastModifiedTime());
/*  89 */     localFileHeader.setUncompressedSize(fileHeader.getUncompressedSize());
/*  90 */     localFileHeader.setFileNameLength(fileHeader.getFileNameLength());
/*  91 */     localFileHeader.setFileName(fileHeader.getFileName());
/*  92 */     localFileHeader.setEncrypted(fileHeader.isEncrypted());
/*  93 */     localFileHeader.setEncryptionMethod(fileHeader.getEncryptionMethod());
/*  94 */     localFileHeader.setAesExtraDataRecord(fileHeader.getAesExtraDataRecord());
/*  95 */     localFileHeader.setCrc(fileHeader.getCrc());
/*  96 */     localFileHeader.setCompressedSize(fileHeader.getCompressedSize());
/*  97 */     localFileHeader.setGeneralPurposeFlag((byte[])fileHeader.getGeneralPurposeFlag().clone());
/*  98 */     localFileHeader.setDataDescriptorExists(fileHeader.isDataDescriptorExists());
/*  99 */     localFileHeader.setExtraFieldLength(fileHeader.getExtraFieldLength());
/* 100 */     return localFileHeader;
/*     */   }
/*     */   
/*     */   private byte[] determineGeneralPurposeBitFlag(boolean isEncrypted, ZipParameters zipParameters, Charset charset) {
/* 104 */     byte[] generalPurposeBitFlag = new byte[2];
/* 105 */     generalPurposeBitFlag[0] = generateFirstGeneralPurposeByte(isEncrypted, zipParameters);
/* 106 */     if (charset.equals(InternalZipConstants.CHARSET_UTF_8)) {
/* 107 */       generalPurposeBitFlag[1] = BitUtils.setBit(generalPurposeBitFlag[1], 3);
/*     */     }
/* 109 */     return generalPurposeBitFlag;
/*     */   }
/*     */ 
/*     */   
/*     */   private byte generateFirstGeneralPurposeByte(boolean isEncrypted, ZipParameters zipParameters) {
/* 114 */     byte firstByte = 0;
/*     */     
/* 116 */     if (isEncrypted) {
/* 117 */       firstByte = BitUtils.setBit(firstByte, 0);
/*     */     }
/*     */     
/* 120 */     if (CompressionMethod.DEFLATE.equals(zipParameters.getCompressionMethod())) {
/* 121 */       if (CompressionLevel.NORMAL.equals(zipParameters.getCompressionLevel())) {
/* 122 */         firstByte = BitUtils.unsetBit(firstByte, 1);
/* 123 */         firstByte = BitUtils.unsetBit(firstByte, 2);
/* 124 */       } else if (CompressionLevel.MAXIMUM.equals(zipParameters.getCompressionLevel())) {
/* 125 */         firstByte = BitUtils.setBit(firstByte, 1);
/* 126 */         firstByte = BitUtils.unsetBit(firstByte, 2);
/* 127 */       } else if (CompressionLevel.FAST.equals(zipParameters.getCompressionLevel())) {
/* 128 */         firstByte = BitUtils.unsetBit(firstByte, 1);
/* 129 */         firstByte = BitUtils.setBit(firstByte, 2);
/* 130 */       } else if (CompressionLevel.FASTEST.equals(zipParameters.getCompressionLevel()) || CompressionLevel.ULTRA
/* 131 */         .equals(zipParameters.getCompressionLevel())) {
/* 132 */         firstByte = BitUtils.setBit(firstByte, 1);
/* 133 */         firstByte = BitUtils.setBit(firstByte, 2);
/*     */       } 
/*     */     }
/*     */     
/* 137 */     if (zipParameters.isWriteExtendedLocalFileHeader()) {
/* 138 */       firstByte = BitUtils.setBit(firstByte, 3);
/*     */     }
/*     */     
/* 141 */     return firstByte;
/*     */   }
/*     */   
/*     */   private String validateAndGetFileName(String fileNameInZip) throws ZipException {
/* 145 */     if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileNameInZip)) {
/* 146 */       throw new ZipException("fileNameInZip is null or empty");
/*     */     }
/* 148 */     return fileNameInZip;
/*     */   }
/*     */   
/*     */   private AESExtraDataRecord generateAESExtraDataRecord(ZipParameters parameters) throws ZipException {
/* 152 */     AESExtraDataRecord aesExtraDataRecord = new AESExtraDataRecord();
/*     */     
/* 154 */     if (parameters.getAesVersion() != null) {
/* 155 */       aesExtraDataRecord.setAesVersion(parameters.getAesVersion());
/*     */     }
/*     */     
/* 158 */     if (parameters.getAesKeyStrength() == AesKeyStrength.KEY_STRENGTH_128) {
/* 159 */       aesExtraDataRecord.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_128);
/* 160 */     } else if (parameters.getAesKeyStrength() == AesKeyStrength.KEY_STRENGTH_192) {
/* 161 */       aesExtraDataRecord.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_192);
/* 162 */     } else if (parameters.getAesKeyStrength() == AesKeyStrength.KEY_STRENGTH_256) {
/* 163 */       aesExtraDataRecord.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
/*     */     } else {
/* 165 */       throw new ZipException("invalid AES key strength");
/*     */     } 
/*     */     
/* 168 */     aesExtraDataRecord.setCompressionMethod(parameters.getCompressionMethod());
/* 169 */     return aesExtraDataRecord;
/*     */   }
/*     */   
/*     */   private int determineFileNameLength(String fileName, Charset charset) {
/* 173 */     return (fileName.getBytes(charset)).length;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\headers\FileHeaderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */