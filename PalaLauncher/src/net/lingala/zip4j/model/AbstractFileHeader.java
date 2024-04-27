/*     */ package net.lingala.zip4j.model;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*     */ import net.lingala.zip4j.model.enums.EncryptionMethod;
/*     */ 
/*     */ public abstract class AbstractFileHeader
/*     */   extends ZipHeader
/*     */ {
/*     */   private int versionNeededToExtract;
/*     */   private byte[] generalPurposeFlag;
/*     */   private CompressionMethod compressionMethod;
/*     */   private long lastModifiedTime;
/*  14 */   private long crc = 0L;
/*     */   private byte[] crcRawData;
/*  16 */   private long compressedSize = 0L;
/*  17 */   private long uncompressedSize = 0L;
/*     */   private int fileNameLength;
/*     */   private int extraFieldLength;
/*     */   private String fileName;
/*     */   private boolean isEncrypted;
/*  22 */   private EncryptionMethod encryptionMethod = EncryptionMethod.NONE;
/*     */   private boolean dataDescriptorExists;
/*     */   private Zip64ExtendedInfo zip64ExtendedInfo;
/*     */   private AESExtraDataRecord aesExtraDataRecord;
/*     */   private boolean fileNameUTF8Encoded;
/*     */   private List<ExtraDataRecord> extraDataRecords;
/*     */   private boolean isDirectory;
/*     */   
/*     */   public int getVersionNeededToExtract() {
/*  31 */     return this.versionNeededToExtract;
/*     */   }
/*     */   
/*     */   public void setVersionNeededToExtract(int versionNeededToExtract) {
/*  35 */     this.versionNeededToExtract = versionNeededToExtract;
/*     */   }
/*     */   
/*     */   public byte[] getGeneralPurposeFlag() {
/*  39 */     return this.generalPurposeFlag;
/*     */   }
/*     */   
/*     */   public void setGeneralPurposeFlag(byte[] generalPurposeFlag) {
/*  43 */     this.generalPurposeFlag = generalPurposeFlag;
/*     */   }
/*     */   
/*     */   public CompressionMethod getCompressionMethod() {
/*  47 */     return this.compressionMethod;
/*     */   }
/*     */   
/*     */   public void setCompressionMethod(CompressionMethod compressionMethod) {
/*  51 */     this.compressionMethod = compressionMethod;
/*     */   }
/*     */   
/*     */   public long getLastModifiedTime() {
/*  55 */     return this.lastModifiedTime;
/*     */   }
/*     */   
/*     */   public void setLastModifiedTime(long lastModifiedTime) {
/*  59 */     this.lastModifiedTime = lastModifiedTime;
/*     */   }
/*     */   
/*     */   public long getCrc() {
/*  63 */     return this.crc;
/*     */   }
/*     */   
/*     */   public void setCrc(long crc) {
/*  67 */     this.crc = crc;
/*     */   }
/*     */   
/*     */   public byte[] getCrcRawData() {
/*  71 */     return this.crcRawData;
/*     */   }
/*     */   
/*     */   public void setCrcRawData(byte[] crcRawData) {
/*  75 */     this.crcRawData = crcRawData;
/*     */   }
/*     */   
/*     */   public long getCompressedSize() {
/*  79 */     return this.compressedSize;
/*     */   }
/*     */   
/*     */   public void setCompressedSize(long compressedSize) {
/*  83 */     this.compressedSize = compressedSize;
/*     */   }
/*     */   
/*     */   public long getUncompressedSize() {
/*  87 */     return this.uncompressedSize;
/*     */   }
/*     */   
/*     */   public void setUncompressedSize(long uncompressedSize) {
/*  91 */     this.uncompressedSize = uncompressedSize;
/*     */   }
/*     */   
/*     */   public int getFileNameLength() {
/*  95 */     return this.fileNameLength;
/*     */   }
/*     */   
/*     */   public void setFileNameLength(int fileNameLength) {
/*  99 */     this.fileNameLength = fileNameLength;
/*     */   }
/*     */   
/*     */   public int getExtraFieldLength() {
/* 103 */     return this.extraFieldLength;
/*     */   }
/*     */   
/*     */   public void setExtraFieldLength(int extraFieldLength) {
/* 107 */     this.extraFieldLength = extraFieldLength;
/*     */   }
/*     */   
/*     */   public String getFileName() {
/* 111 */     return this.fileName;
/*     */   }
/*     */   
/*     */   public void setFileName(String fileName) {
/* 115 */     this.fileName = fileName;
/*     */   }
/*     */   
/*     */   public boolean isEncrypted() {
/* 119 */     return this.isEncrypted;
/*     */   }
/*     */   
/*     */   public void setEncrypted(boolean encrypted) {
/* 123 */     this.isEncrypted = encrypted;
/*     */   }
/*     */   
/*     */   public EncryptionMethod getEncryptionMethod() {
/* 127 */     return this.encryptionMethod;
/*     */   }
/*     */   
/*     */   public void setEncryptionMethod(EncryptionMethod encryptionMethod) {
/* 131 */     this.encryptionMethod = encryptionMethod;
/*     */   }
/*     */   
/*     */   public boolean isDataDescriptorExists() {
/* 135 */     return this.dataDescriptorExists;
/*     */   }
/*     */   
/*     */   public void setDataDescriptorExists(boolean dataDescriptorExists) {
/* 139 */     this.dataDescriptorExists = dataDescriptorExists;
/*     */   }
/*     */   
/*     */   public Zip64ExtendedInfo getZip64ExtendedInfo() {
/* 143 */     return this.zip64ExtendedInfo;
/*     */   }
/*     */   
/*     */   public void setZip64ExtendedInfo(Zip64ExtendedInfo zip64ExtendedInfo) {
/* 147 */     this.zip64ExtendedInfo = zip64ExtendedInfo;
/*     */   }
/*     */   
/*     */   public AESExtraDataRecord getAesExtraDataRecord() {
/* 151 */     return this.aesExtraDataRecord;
/*     */   }
/*     */   
/*     */   public void setAesExtraDataRecord(AESExtraDataRecord aesExtraDataRecord) {
/* 155 */     this.aesExtraDataRecord = aesExtraDataRecord;
/*     */   }
/*     */   
/*     */   public boolean isFileNameUTF8Encoded() {
/* 159 */     return this.fileNameUTF8Encoded;
/*     */   }
/*     */   
/*     */   public void setFileNameUTF8Encoded(boolean fileNameUTF8Encoded) {
/* 163 */     this.fileNameUTF8Encoded = fileNameUTF8Encoded;
/*     */   }
/*     */   
/*     */   public List<ExtraDataRecord> getExtraDataRecords() {
/* 167 */     return this.extraDataRecords;
/*     */   }
/*     */   
/*     */   public void setExtraDataRecords(List<ExtraDataRecord> extraDataRecords) {
/* 171 */     this.extraDataRecords = extraDataRecords;
/*     */   }
/*     */   
/*     */   public boolean isDirectory() {
/* 175 */     return this.isDirectory;
/*     */   }
/*     */   
/*     */   public void setDirectory(boolean directory) {
/* 179 */     this.isDirectory = directory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 184 */     if (obj == null) {
/* 185 */       return false;
/*     */     }
/*     */     
/* 188 */     if (!(obj instanceof AbstractFileHeader)) {
/* 189 */       return false;
/*     */     }
/*     */     
/* 192 */     return getFileName().equals(((AbstractFileHeader)obj).getFileName());
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\AbstractFileHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */