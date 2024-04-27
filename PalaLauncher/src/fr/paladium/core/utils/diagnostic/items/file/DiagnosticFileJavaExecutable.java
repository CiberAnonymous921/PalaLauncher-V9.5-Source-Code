/*    */ package fr.paladium.core.utils.diagnostic.items.file;
/*    */ 
/*    */ import fr.paladium.core.distribution.dto.DistributionOS;
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ import fr.paladium.core.utils.io.FileUtils;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ public class DiagnosticFileJavaExecutable
/*    */   extends DiagnosticItem
/*    */ {
/*    */   public DiagnosticFileJavaExecutable(String name) {
/* 14 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 19 */     File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/* 20 */     File javaFile = new File(installDir, "runtime/bin/" + ((OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) ? "java.exe" : "java"));
/*    */     
/* 22 */     if (!javaFile.exists()) {
/* 23 */       return false;
/*    */     }
/*    */     
/*    */     try {
/* 27 */       return (FileUtils.getFileSize(javaFile) > 0L);
/* 28 */     } catch (Exception e) {
/* 29 */       e.printStackTrace();
/* 30 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\file\DiagnosticFileJavaExecutable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */