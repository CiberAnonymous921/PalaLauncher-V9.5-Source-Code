/*    */ package me.friwi.jcefmaven.impl.step.check;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import me.friwi.jcefmaven.CefBuildInfo;
/*    */ import me.friwi.jcefmaven.EnumPlatform;
/*    */ import me.friwi.jcefmaven.UnsupportedPlatformException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CefInstallationChecker
/*    */ {
/* 19 */   private static final Logger LOGGER = Logger.getLogger(CefInstallationChecker.class.getName());
/*    */   public static boolean checkInstallation(File installDir) throws UnsupportedPlatformException {
/*    */     CefBuildInfo installed, required;
/* 22 */     Objects.requireNonNull(installDir, "installDir cannot be null");
/* 23 */     File buildInfo = new File(installDir, "build_meta.json");
/* 24 */     if (!(new File(installDir, "install.lock")).exists()) return false; 
/* 25 */     if (!buildInfo.exists()) return false;
/*    */     
/*    */     try {
/* 28 */       installed = CefBuildInfo.fromFile(buildInfo);
/* 29 */     } catch (IOException e) {
/* 30 */       LOGGER.log(Level.WARNING, "Error while parsing existing installation. Reinstalling.", e);
/* 31 */       return false;
/*    */     } 
/*    */     
/*    */     try {
/* 35 */       required = CefBuildInfo.fromClasspath();
/* 36 */     } catch (IOException e) {
/* 37 */       LOGGER.log(Level.WARNING, "Error while parsing existing installation. Reinstalling.", e);
/* 38 */       return false;
/*    */     } 
/*    */     
/* 41 */     return (required.getReleaseTag().equals(installed.getReleaseTag()) && installed
/* 42 */       .getPlatform().equals(EnumPlatform.getCurrentPlatform().getIdentifier()));
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\impl\step\check\CefInstallationChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */