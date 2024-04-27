/*    */ package net.lingala.zip4j.model;
/*    */ 
/*    */ import net.lingala.zip4j.headers.HeaderSignature;
/*    */ 
/*    */ public abstract class ZipHeader
/*    */ {
/*    */   private HeaderSignature signature;
/*    */   
/*    */   public HeaderSignature getSignature() {
/* 10 */     return this.signature;
/*    */   }
/*    */   
/*    */   public void setSignature(HeaderSignature signature) {
/* 14 */     this.signature = signature;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\ZipHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */