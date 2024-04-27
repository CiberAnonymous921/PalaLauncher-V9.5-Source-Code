/*    */ package net.lingala.zip4j.model;
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
/*    */ public class Zip64EndOfCentralDirectoryLocator
/*    */   extends ZipHeader
/*    */ {
/*    */   private int numberOfDiskStartOfZip64EndOfCentralDirectoryRecord;
/*    */   private long offsetZip64EndOfCentralDirectoryRecord;
/*    */   private int totalNumberOfDiscs;
/*    */   
/*    */   public int getNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord() {
/* 26 */     return this.numberOfDiskStartOfZip64EndOfCentralDirectoryRecord;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(int noOfDiskStartOfZip64EndOfCentralDirRec) {
/* 31 */     this.numberOfDiskStartOfZip64EndOfCentralDirectoryRecord = noOfDiskStartOfZip64EndOfCentralDirRec;
/*    */   }
/*    */   
/*    */   public long getOffsetZip64EndOfCentralDirectoryRecord() {
/* 35 */     return this.offsetZip64EndOfCentralDirectoryRecord;
/*    */   }
/*    */   
/*    */   public void setOffsetZip64EndOfCentralDirectoryRecord(long offsetZip64EndOfCentralDirectoryRecord) {
/* 39 */     this.offsetZip64EndOfCentralDirectoryRecord = offsetZip64EndOfCentralDirectoryRecord;
/*    */   }
/*    */   
/*    */   public int getTotalNumberOfDiscs() {
/* 43 */     return this.totalNumberOfDiscs;
/*    */   }
/*    */   
/*    */   public void setTotalNumberOfDiscs(int totNumberOfDiscs) {
/* 47 */     this.totalNumberOfDiscs = totNumberOfDiscs;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\Zip64EndOfCentralDirectoryLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */