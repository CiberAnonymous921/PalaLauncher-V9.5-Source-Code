/*    */ package net.lingala.zip4j.headers;
/*    */ 
/*    */ public enum VersionMadeBy
/*    */ {
/*  5 */   SPECIFICATION_VERSION((byte)51),
/*  6 */   WINDOWS((byte)0),
/*  7 */   UNIX((byte)3);
/*    */   
/*    */   private byte code;
/*    */   
/*    */   VersionMadeBy(byte code) {
/* 12 */     this.code = code;
/*    */   }
/*    */   
/*    */   public byte getCode() {
/* 16 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\headers\VersionMadeBy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */