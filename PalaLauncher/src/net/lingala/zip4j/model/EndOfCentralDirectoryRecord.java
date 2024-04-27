/*    */ package net.lingala.zip4j.model;
/*    */ 
/*    */ import net.lingala.zip4j.headers.HeaderSignature;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EndOfCentralDirectoryRecord
/*    */   extends ZipHeader
/*    */ {
/*    */   private int numberOfThisDisk;
/*    */   private int numberOfThisDiskStartOfCentralDir;
/*    */   private int totalNumberOfEntriesInCentralDirectoryOnThisDisk;
/*    */   private int totalNumberOfEntriesInCentralDirectory;
/*    */   private int sizeOfCentralDirectory;
/*    */   private long offsetOfStartOfCentralDirectory;
/*    */   private long offsetOfEndOfCentralDirectory;
/* 30 */   private String comment = "";
/*    */   
/*    */   public EndOfCentralDirectoryRecord() {
/* 33 */     setSignature(HeaderSignature.END_OF_CENTRAL_DIRECTORY);
/*    */   }
/*    */   
/*    */   public int getNumberOfThisDisk() {
/* 37 */     return this.numberOfThisDisk;
/*    */   }
/*    */   
/*    */   public void setNumberOfThisDisk(int numberOfThisDisk) {
/* 41 */     this.numberOfThisDisk = numberOfThisDisk;
/*    */   }
/*    */   
/*    */   public int getNumberOfThisDiskStartOfCentralDir() {
/* 45 */     return this.numberOfThisDiskStartOfCentralDir;
/*    */   }
/*    */   
/*    */   public void setNumberOfThisDiskStartOfCentralDir(int numberOfThisDiskStartOfCentralDir) {
/* 49 */     this.numberOfThisDiskStartOfCentralDir = numberOfThisDiskStartOfCentralDir;
/*    */   }
/*    */   
/*    */   public int getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() {
/* 53 */     return this.totalNumberOfEntriesInCentralDirectoryOnThisDisk;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(int totalNumberOfEntriesInCentralDirectoryOnThisDisk) {
/* 58 */     this.totalNumberOfEntriesInCentralDirectoryOnThisDisk = totalNumberOfEntriesInCentralDirectoryOnThisDisk;
/*    */   }
/*    */   
/*    */   public int getTotalNumberOfEntriesInCentralDirectory() {
/* 62 */     return this.totalNumberOfEntriesInCentralDirectory;
/*    */   }
/*    */   
/*    */   public void setTotalNumberOfEntriesInCentralDirectory(int totNoOfEntrisInCentralDir) {
/* 66 */     this.totalNumberOfEntriesInCentralDirectory = totNoOfEntrisInCentralDir;
/*    */   }
/*    */   
/*    */   public int getSizeOfCentralDirectory() {
/* 70 */     return this.sizeOfCentralDirectory;
/*    */   }
/*    */   
/*    */   public void setSizeOfCentralDirectory(int sizeOfCentralDirectory) {
/* 74 */     this.sizeOfCentralDirectory = sizeOfCentralDirectory;
/*    */   }
/*    */   
/*    */   public long getOffsetOfStartOfCentralDirectory() {
/* 78 */     return this.offsetOfStartOfCentralDirectory;
/*    */   }
/*    */   
/*    */   public void setOffsetOfStartOfCentralDirectory(long offSetOfStartOfCentralDir) {
/* 82 */     this.offsetOfStartOfCentralDirectory = offSetOfStartOfCentralDir;
/*    */   }
/*    */   
/*    */   public long getOffsetOfEndOfCentralDirectory() {
/* 86 */     return this.offsetOfEndOfCentralDirectory;
/*    */   }
/*    */   
/*    */   public void setOffsetOfEndOfCentralDirectory(long offsetOfEndOfCentralDirectory) {
/* 90 */     this.offsetOfEndOfCentralDirectory = offsetOfEndOfCentralDirectory;
/*    */   }
/*    */   
/*    */   public String getComment() {
/* 94 */     return this.comment;
/*    */   }
/*    */   
/*    */   public void setComment(String comment) {
/* 98 */     if (comment != null)
/* 99 */       this.comment = comment; 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\EndOfCentralDirectoryRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */