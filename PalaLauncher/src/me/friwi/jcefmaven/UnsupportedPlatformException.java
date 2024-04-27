/*    */ package me.friwi.jcefmaven;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnsupportedPlatformException
/*    */   extends Exception
/*    */ {
/*    */   private final String osName;
/*    */   private final String osArch;
/*    */   
/*    */   public UnsupportedPlatformException(String osName, String osArch) {
/* 14 */     super("Could not determine platform for os.name=" + osName + " and " + "os.arch" + "=" + osArch);
/*    */ 
/*    */     
/* 17 */     this.osName = osName;
/* 18 */     this.osArch = osArch;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getOsName() {
/* 27 */     return this.osName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getOsArch() {
/* 36 */     return this.osArch;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\UnsupportedPlatformException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */