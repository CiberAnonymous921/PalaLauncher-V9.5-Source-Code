/*    */ package fr.paladium.core.utils.io;
/*    */ 
/*    */ import fr.paladium.core.distribution.dto.DistributionOS;
/*    */ import java.io.File;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ public class OsCheck
/*    */ {
/*    */   private static DistributionOS detectedOS;
/*    */   
/*    */   public static DistributionOS getOperatingSystemType() {
/* 13 */     if (detectedOS == null) {
/* 14 */       String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
/* 15 */       if (OS.contains("mac") || OS.contains("darwin")) {
/* 16 */         detectedOS = DistributionOS.MACOS;
/* 17 */       } else if (OS.contains("win")) {
/* 18 */         detectedOS = DistributionOS.WINDOWS;
/* 19 */       } else if (OS.contains("nux")) {
/* 20 */         detectedOS = DistributionOS.LINUX;
/*    */       } else {
/* 22 */         detectedOS = DistributionOS.WINDOWS;
/*    */       } 
/*    */     } 
/*    */     
/* 26 */     return detectedOS;
/*    */   }
/*    */   
/*    */   public static File getAppData() {
/* 30 */     DistributionOS os = getOperatingSystemType();
/*    */     
/* 32 */     if (os == DistributionOS.WINDOWS)
/* 33 */       return new File(System.getenv("APPDATA")); 
/* 34 */     if (os == DistributionOS.MACOS) {
/* 35 */       return new File(System.getProperty("user.home") + "/Library/Application Support");
/*    */     }
/* 37 */     return new File(System.getProperty("user.home"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\io\OsCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */