/*    */ package net.lingala.zip4j.exception;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class ZipException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 25 */   private Type type = Type.UNKNOWN;
/*    */   
/*    */   public ZipException(String message) {
/* 28 */     super(message);
/*    */   }
/*    */   
/*    */   public ZipException(Exception rootException) {
/* 32 */     super(rootException);
/*    */   }
/*    */   
/*    */   public ZipException(String message, Exception rootException) {
/* 36 */     super(message, rootException);
/*    */   }
/*    */   
/*    */   public ZipException(String message, Type type) {
/* 40 */     super(message);
/* 41 */     this.type = type;
/*    */   }
/*    */   
/*    */   public ZipException(String message, Throwable throwable, Type type) {
/* 45 */     super(message, throwable);
/* 46 */     this.type = type;
/*    */   }
/*    */   
/*    */   public Type getType() {
/* 50 */     return this.type;
/*    */   }
/*    */   
/*    */   public enum Type {
/* 54 */     WRONG_PASSWORD,
/* 55 */     TASK_CANCELLED_EXCEPTION,
/* 56 */     CHECKSUM_MISMATCH,
/* 57 */     UNKNOWN_COMPRESSION_METHOD,
/* 58 */     FILE_NOT_FOUND,
/* 59 */     UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\exception\ZipException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */