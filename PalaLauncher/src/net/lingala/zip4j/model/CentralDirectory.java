/*    */ package net.lingala.zip4j.model;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class CentralDirectory
/*    */ {
/* 24 */   private List<FileHeader> fileHeaders = new ArrayList<>();
/* 25 */   private DigitalSignature digitalSignature = new DigitalSignature();
/*    */   
/*    */   public List<FileHeader> getFileHeaders() {
/* 28 */     return this.fileHeaders;
/*    */   }
/*    */   
/*    */   public void setFileHeaders(List<FileHeader> fileHeaders) {
/* 32 */     this.fileHeaders = fileHeaders;
/*    */   }
/*    */   
/*    */   public DigitalSignature getDigitalSignature() {
/* 36 */     return this.digitalSignature;
/*    */   }
/*    */   
/*    */   public void setDigitalSignature(DigitalSignature digitalSignature) {
/* 40 */     this.digitalSignature = digitalSignature;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\CentralDirectory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */