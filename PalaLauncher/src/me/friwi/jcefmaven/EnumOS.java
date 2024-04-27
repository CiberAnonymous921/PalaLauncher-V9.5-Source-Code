/*    */ package me.friwi.jcefmaven;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum EnumOS
/*    */ {
/* 10 */   MACOSX,
/* 11 */   LINUX,
/* 12 */   WINDOWS;
/*    */   
/*    */   public boolean isMacOSX() {
/* 15 */     return (this == MACOSX);
/*    */   }
/*    */   
/*    */   public boolean isLinux() {
/* 19 */     return (this == LINUX);
/*    */   }
/*    */   
/*    */   public boolean isWindows() {
/* 23 */     return (this == WINDOWS);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\EnumOS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */