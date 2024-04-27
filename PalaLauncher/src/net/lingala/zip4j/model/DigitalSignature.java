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
/*    */ public class DigitalSignature
/*    */   extends ZipHeader
/*    */ {
/*    */   private int sizeOfData;
/*    */   private String signatureData;
/*    */   
/*    */   public int getSizeOfData() {
/* 25 */     return this.sizeOfData;
/*    */   }
/*    */   
/*    */   public void setSizeOfData(int sizeOfData) {
/* 29 */     this.sizeOfData = sizeOfData;
/*    */   }
/*    */   
/*    */   public String getSignatureData() {
/* 33 */     return this.signatureData;
/*    */   }
/*    */   
/*    */   public void setSignatureData(String signatureData) {
/* 37 */     this.signatureData = signatureData;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\DigitalSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */