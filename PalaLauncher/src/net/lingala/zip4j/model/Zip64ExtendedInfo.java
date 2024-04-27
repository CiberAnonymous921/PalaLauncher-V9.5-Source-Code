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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Zip64ExtendedInfo
/*    */   extends ZipHeader
/*    */ {
/*    */   private int size;
/* 28 */   private long compressedSize = -1L;
/* 29 */   private long uncompressedSize = -1L;
/* 30 */   private long offsetLocalHeader = -1L;
/* 31 */   private int diskNumberStart = -1;
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 35 */     return this.size;
/*    */   }
/*    */   
/*    */   public void setSize(int size) {
/* 39 */     this.size = size;
/*    */   }
/*    */   
/*    */   public long getCompressedSize() {
/* 43 */     return this.compressedSize;
/*    */   }
/*    */   
/*    */   public void setCompressedSize(long compressedSize) {
/* 47 */     this.compressedSize = compressedSize;
/*    */   }
/*    */   
/*    */   public long getUncompressedSize() {
/* 51 */     return this.uncompressedSize;
/*    */   }
/*    */   
/*    */   public void setUncompressedSize(long uncompressedSize) {
/* 55 */     this.uncompressedSize = uncompressedSize;
/*    */   }
/*    */   
/*    */   public long getOffsetLocalHeader() {
/* 59 */     return this.offsetLocalHeader;
/*    */   }
/*    */   
/*    */   public void setOffsetLocalHeader(long offsetLocalHeader) {
/* 63 */     this.offsetLocalHeader = offsetLocalHeader;
/*    */   }
/*    */   
/*    */   public int getDiskNumberStart() {
/* 67 */     return this.diskNumberStart;
/*    */   }
/*    */   
/*    */   public void setDiskNumberStart(int diskNumberStart) {
/* 71 */     this.diskNumberStart = diskNumberStart;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\Zip64ExtendedInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */