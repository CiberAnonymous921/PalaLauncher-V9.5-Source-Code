/*     */ package net.lingala.zip4j.model;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class ZipModel
/*     */   implements Cloneable
/*     */ {
/*  25 */   private List<LocalFileHeader> localFileHeaders = new ArrayList<>();
/*  26 */   private List<DataDescriptor> dataDescriptors = new ArrayList<>();
/*  27 */   private ArchiveExtraDataRecord archiveExtraDataRecord = new ArchiveExtraDataRecord();
/*  28 */   private CentralDirectory centralDirectory = new CentralDirectory();
/*  29 */   private EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = new EndOfCentralDirectoryRecord();
/*  30 */   private Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator = new Zip64EndOfCentralDirectoryLocator();
/*  31 */   private Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = new Zip64EndOfCentralDirectoryRecord();
/*     */   
/*     */   private boolean splitArchive;
/*     */   private long splitLength;
/*     */   private File zipFile;
/*     */   private boolean isZip64Format = false;
/*     */   private boolean isNestedZipFile;
/*     */   private long start;
/*     */   private long end;
/*     */   
/*     */   public ZipModel() {
/*  42 */     this.splitLength = -1L;
/*     */   }
/*     */   
/*     */   public List<LocalFileHeader> getLocalFileHeaders() {
/*  46 */     return this.localFileHeaders;
/*     */   }
/*     */   
/*     */   public void setLocalFileHeaders(List<LocalFileHeader> localFileHeaderList) {
/*  50 */     this.localFileHeaders = localFileHeaderList;
/*     */   }
/*     */   
/*     */   public List<DataDescriptor> getDataDescriptors() {
/*  54 */     return this.dataDescriptors;
/*     */   }
/*     */   
/*     */   public void setDataDescriptors(List<DataDescriptor> dataDescriptors) {
/*  58 */     this.dataDescriptors = dataDescriptors;
/*     */   }
/*     */   
/*     */   public CentralDirectory getCentralDirectory() {
/*  62 */     return this.centralDirectory;
/*     */   }
/*     */   
/*     */   public void setCentralDirectory(CentralDirectory centralDirectory) {
/*  66 */     this.centralDirectory = centralDirectory;
/*     */   }
/*     */   
/*     */   public EndOfCentralDirectoryRecord getEndOfCentralDirectoryRecord() {
/*  70 */     return this.endOfCentralDirectoryRecord;
/*     */   }
/*     */   
/*     */   public void setEndOfCentralDirectoryRecord(EndOfCentralDirectoryRecord endOfCentralDirectoryRecord) {
/*  74 */     this.endOfCentralDirectoryRecord = endOfCentralDirectoryRecord;
/*     */   }
/*     */   
/*     */   public ArchiveExtraDataRecord getArchiveExtraDataRecord() {
/*  78 */     return this.archiveExtraDataRecord;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setArchiveExtraDataRecord(ArchiveExtraDataRecord archiveExtraDataRecord) {
/*  83 */     this.archiveExtraDataRecord = archiveExtraDataRecord;
/*     */   }
/*     */   
/*     */   public boolean isSplitArchive() {
/*  87 */     return this.splitArchive;
/*     */   }
/*     */   
/*     */   public void setSplitArchive(boolean splitArchive) {
/*  91 */     this.splitArchive = splitArchive;
/*     */   }
/*     */   
/*     */   public File getZipFile() {
/*  95 */     return this.zipFile;
/*     */   }
/*     */   
/*     */   public void setZipFile(File zipFile) {
/*  99 */     this.zipFile = zipFile;
/*     */   }
/*     */   
/*     */   public Zip64EndOfCentralDirectoryLocator getZip64EndOfCentralDirectoryLocator() {
/* 103 */     return this.zip64EndOfCentralDirectoryLocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setZip64EndOfCentralDirectoryLocator(Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator) {
/* 108 */     this.zip64EndOfCentralDirectoryLocator = zip64EndOfCentralDirectoryLocator;
/*     */   }
/*     */   
/*     */   public Zip64EndOfCentralDirectoryRecord getZip64EndOfCentralDirectoryRecord() {
/* 112 */     return this.zip64EndOfCentralDirectoryRecord;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setZip64EndOfCentralDirectoryRecord(Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord) {
/* 117 */     this.zip64EndOfCentralDirectoryRecord = zip64EndOfCentralDirectoryRecord;
/*     */   }
/*     */   
/*     */   public boolean isZip64Format() {
/* 121 */     return this.isZip64Format;
/*     */   }
/*     */   
/*     */   public void setZip64Format(boolean isZip64Format) {
/* 125 */     this.isZip64Format = isZip64Format;
/*     */   }
/*     */   
/*     */   public boolean isNestedZipFile() {
/* 129 */     return this.isNestedZipFile;
/*     */   }
/*     */   
/*     */   public void setNestedZipFile(boolean isNestedZipFile) {
/* 133 */     this.isNestedZipFile = isNestedZipFile;
/*     */   }
/*     */   
/*     */   public long getStart() {
/* 137 */     return this.start;
/*     */   }
/*     */   
/*     */   public void setStart(long start) {
/* 141 */     this.start = start;
/*     */   }
/*     */   
/*     */   public long getEnd() {
/* 145 */     return this.end;
/*     */   }
/*     */   
/*     */   public void setEnd(long end) {
/* 149 */     this.end = end;
/*     */   }
/*     */   
/*     */   public long getSplitLength() {
/* 153 */     return this.splitLength;
/*     */   }
/*     */   
/*     */   public void setSplitLength(long splitLength) {
/* 157 */     this.splitLength = splitLength;
/*     */   }
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 161 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\ZipModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */