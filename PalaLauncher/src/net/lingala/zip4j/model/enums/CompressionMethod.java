/*    */ package net.lingala.zip4j.model.enums;
/*    */ 
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CompressionMethod
/*    */ {
/* 14 */   STORE(0),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   DEFLATE(8),
/*    */ 
/*    */ 
/*    */   
/* 23 */   AES_INTERNAL_ONLY(99);
/*    */   
/*    */   private int code;
/*    */   
/*    */   CompressionMethod(int code) {
/* 28 */     this.code = code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCode() {
/* 36 */     return this.code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CompressionMethod getCompressionMethodFromCode(int code) throws ZipException {
/* 46 */     for (CompressionMethod compressionMethod : values()) {
/* 47 */       if (compressionMethod.getCode() == code) {
/* 48 */         return compressionMethod;
/*    */       }
/*    */     } 
/*    */     
/* 52 */     throw new ZipException("Unknown compression method", ZipException.Type.UNKNOWN_COMPRESSION_METHOD);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\enums\CompressionMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */