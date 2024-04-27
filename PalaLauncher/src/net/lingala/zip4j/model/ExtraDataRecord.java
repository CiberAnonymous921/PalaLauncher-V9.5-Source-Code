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
/*    */ public class ExtraDataRecord
/*    */   extends ZipHeader
/*    */ {
/*    */   private long header;
/*    */   private int sizeOfData;
/*    */   private byte[] data;
/*    */   
/*    */   public long getHeader() {
/* 26 */     return this.header;
/*    */   }
/*    */   
/*    */   public void setHeader(long header) {
/* 30 */     this.header = header;
/*    */   }
/*    */   
/*    */   public int getSizeOfData() {
/* 34 */     return this.sizeOfData;
/*    */   }
/*    */   
/*    */   public void setSizeOfData(int sizeOfData) {
/* 38 */     this.sizeOfData = sizeOfData;
/*    */   }
/*    */   
/*    */   public byte[] getData() {
/* 42 */     return this.data;
/*    */   }
/*    */   
/*    */   public void setData(byte[] data) {
/* 46 */     this.data = data;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\ExtraDataRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */