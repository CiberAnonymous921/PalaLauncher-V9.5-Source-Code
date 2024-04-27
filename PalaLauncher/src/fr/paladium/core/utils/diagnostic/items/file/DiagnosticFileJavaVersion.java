/*    */ package fr.paladium.core.utils.diagnostic.items.file;
/*    */ 
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import java.io.File;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ public class DiagnosticFileJavaVersion
/*    */   extends DiagnosticItem
/*    */ {
/*    */   public DiagnosticFileJavaVersion(String name) {
/* 13 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 18 */     File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/* 19 */     File javaFile = new File(installDir, "runtime/release");
/*    */     
/* 21 */     if (!javaFile.exists()) {
/* 22 */       return false;
/*    */     }
/*    */     
/*    */     try {
/* 26 */       String javaVersion = FileUtils.readLines(javaFile, StandardCharsets.UTF_8).get(0);
/* 27 */       return javaVersion.contains("1.8.0_202");
/* 28 */     } catch (Exception e) {
/* 29 */       e.printStackTrace();
/* 30 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\file\DiagnosticFileJavaVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */