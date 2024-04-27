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
/*    */ public class DataDescriptor
/*    */   extends ZipHeader
/*    */ {
/*    */   private long crc;
/*    */   private long compressedSize;
/*    */   private long uncompressedSize;
/*    */   
/*    */   public long getCrc() {
/* 26 */     return this.crc;
/*    */   }
/*    */   
/*    */   public void setCrc(long crc) {
/* 30 */     this.crc = crc;
/*    */   }
/*    */   
/*    */   public long getCompressedSize() {
/* 34 */     return this.compressedSize;
/*    */   }
/*    */   
/*    */   public void setCompressedSize(long compressedSize) {
/* 38 */     this.compressedSize = compressedSize;
/*    */   }
/*    */   
/*    */   public long getUncompressedSize() {
/* 42 */     return this.uncompressedSize;
/*    */   }
/*    */   
/*    */   public void setUncompressedSize(long uncompressedSize) {
/* 46 */     this.uncompressedSize = uncompressedSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\DataDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */