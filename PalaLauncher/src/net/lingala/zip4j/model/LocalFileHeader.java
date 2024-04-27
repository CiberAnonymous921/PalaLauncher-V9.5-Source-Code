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
/*    */ public class LocalFileHeader
/*    */   extends AbstractFileHeader
/*    */ {
/*    */   private byte[] extraField;
/*    */   private long offsetStartOfData;
/*    */   private boolean writeCompressedSizeInZip64ExtraRecord;
/*    */   
/*    */   public LocalFileHeader() {
/* 28 */     setSignature(HeaderSignature.LOCAL_FILE_HEADER);
/*    */   }
/*    */   
/*    */   public byte[] getExtraField() {
/* 32 */     return this.extraField;
/*    */   }
/*    */   
/*    */   public void setExtraField(byte[] extraField) {
/* 36 */     this.extraField = extraField;
/*    */   }
/*    */   
/*    */   public long getOffsetStartOfData() {
/* 40 */     return this.offsetStartOfData;
/*    */   }
/*    */   
/*    */   public void setOffsetStartOfData(long offsetStartOfData) {
/* 44 */     this.offsetStartOfData = offsetStartOfData;
/*    */   }
/*    */   
/*    */   public boolean isWriteCompressedSizeInZip64ExtraRecord() {
/* 48 */     return this.writeCompressedSizeInZip64ExtraRecord;
/*    */   }
/*    */   
/*    */   public void setWriteCompressedSizeInZip64ExtraRecord(boolean writeCompressedSizeInZip64ExtraRecord) {
/* 52 */     this.writeCompressedSizeInZip64ExtraRecord = writeCompressedSizeInZip64ExtraRecord;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\LocalFileHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */