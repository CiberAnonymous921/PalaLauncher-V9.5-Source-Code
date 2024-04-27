/*    */ package net.lingala.zip4j.model.enums;
/*    */ 
/*    */ public enum RandomAccessFileMode
/*    */ {
/*  5 */   READ("r"),
/*  6 */   WRITE("rw");
/*    */   
/*    */   private String value;
/*    */   
/*    */   RandomAccessFileMode(String value) {
/* 11 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 15 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\enums\RandomAccessFileMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */