/*     */ package net.lingala.zip4j.model;
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
/*     */ public class Zip64EndOfCentralDirectoryRecord
/*     */   extends ZipHeader
/*     */ {
/*     */   private long sizeOfZip64EndCentralDirectoryRecord;
/*     */   private int versionMadeBy;
/*     */   private int versionNeededToExtract;
/*     */   private int numberOfThisDisk;
/*     */   private int numberOfThisDiskStartOfCentralDirectory;
/*     */   private long totalNumberOfEntriesInCentralDirectoryOnThisDisk;
/*     */   private long totalNumberOfEntriesInCentralDirectory;
/*     */   private long sizeOfCentralDirectory;
/*  29 */   private long offsetStartCentralDirectoryWRTStartDiskNumber = -1L;
/*     */   private byte[] extensibleDataSector;
/*     */   
/*     */   public long getSizeOfZip64EndCentralDirectoryRecord() {
/*  33 */     return this.sizeOfZip64EndCentralDirectoryRecord;
/*     */   }
/*     */   
/*     */   public void setSizeOfZip64EndCentralDirectoryRecord(long sizeOfZip64EndCentralDirectoryRecord) {
/*  37 */     this.sizeOfZip64EndCentralDirectoryRecord = sizeOfZip64EndCentralDirectoryRecord;
/*     */   }
/*     */   
/*     */   public int getVersionMadeBy() {
/*  41 */     return this.versionMadeBy;
/*     */   }
/*     */   
/*     */   public void setVersionMadeBy(int versionMadeBy) {
/*  45 */     this.versionMadeBy = versionMadeBy;
/*     */   }
/*     */   
/*     */   public int getVersionNeededToExtract() {
/*  49 */     return this.versionNeededToExtract;
/*     */   }
/*     */   
/*     */   public void setVersionNeededToExtract(int versionNeededToExtract) {
/*  53 */     this.versionNeededToExtract = versionNeededToExtract;
/*     */   }
/*     */   
/*     */   public int getNumberOfThisDisk() {
/*  57 */     return this.numberOfThisDisk;
/*     */   }
/*     */   
/*     */   public void setNumberOfThisDisk(int numberOfThisDisk) {
/*  61 */     this.numberOfThisDisk = numberOfThisDisk;
/*     */   }
/*     */   
/*     */   public int getNumberOfThisDiskStartOfCentralDirectory() {
/*  65 */     return this.numberOfThisDiskStartOfCentralDirectory;
/*     */   }
/*     */   
/*     */   public void setNumberOfThisDiskStartOfCentralDirectory(int numberOfThisDiskStartOfCentralDirectory) {
/*  69 */     this.numberOfThisDiskStartOfCentralDirectory = numberOfThisDiskStartOfCentralDirectory;
/*     */   }
/*     */   
/*     */   public long getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() {
/*  73 */     return this.totalNumberOfEntriesInCentralDirectoryOnThisDisk;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(long totalNumberOfEntriesInCentralDirectoryOnThisDisk) {
/*  78 */     this.totalNumberOfEntriesInCentralDirectoryOnThisDisk = totalNumberOfEntriesInCentralDirectoryOnThisDisk;
/*     */   }
/*     */   
/*     */   public long getTotalNumberOfEntriesInCentralDirectory() {
/*  82 */     return this.totalNumberOfEntriesInCentralDirectory;
/*     */   }
/*     */   
/*     */   public void setTotalNumberOfEntriesInCentralDirectory(long totalNumberOfEntriesInCentralDirectory) {
/*  86 */     this.totalNumberOfEntriesInCentralDirectory = totalNumberOfEntriesInCentralDirectory;
/*     */   }
/*     */   
/*     */   public long getSizeOfCentralDirectory() {
/*  90 */     return this.sizeOfCentralDirectory;
/*     */   }
/*     */   
/*     */   public void setSizeOfCentralDirectory(long sizeOfCentralDirectory) {
/*  94 */     this.sizeOfCentralDirectory = sizeOfCentralDirectory;
/*     */   }
/*     */   
/*     */   public long getOffsetStartCentralDirectoryWRTStartDiskNumber() {
/*  98 */     return this.offsetStartCentralDirectoryWRTStartDiskNumber;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOffsetStartCentralDirectoryWRTStartDiskNumber(long offsetStartCentralDirectoryWRTStartDiskNumber) {
/* 103 */     this.offsetStartCentralDirectoryWRTStartDiskNumber = offsetStartCentralDirectoryWRTStartDiskNumber;
/*     */   }
/*     */   
/*     */   public byte[] getExtensibleDataSector() {
/* 107 */     return this.extensibleDataSector;
/*     */   }
/*     */   
/*     */   public void setExtensibleDataSector(byte[] extensibleDataSector) {
/* 111 */     this.extensibleDataSector = extensibleDataSector;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\Zip64EndOfCentralDirectoryRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */