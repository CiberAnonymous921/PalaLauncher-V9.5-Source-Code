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
/*    */ public class ArchiveExtraDataRecord
/*    */   extends ZipHeader
/*    */ {
/*    */   private int extraFieldLength;
/*    */   private String extraFieldData;
/*    */   
/*    */   public int getExtraFieldLength() {
/* 25 */     return this.extraFieldLength;
/*    */   }
/*    */   
/*    */   public void setExtraFieldLength(int extraFieldLength) {
/* 29 */     this.extraFieldLength = extraFieldLength;
/*    */   }
/*    */   
/*    */   public String getExtraFieldData() {
/* 33 */     return this.extraFieldData;
/*    */   }
/*    */   
/*    */   public void setExtraFieldData(String extraFieldData) {
/* 37 */     this.extraFieldData = extraFieldData;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\ArchiveExtraDataRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */