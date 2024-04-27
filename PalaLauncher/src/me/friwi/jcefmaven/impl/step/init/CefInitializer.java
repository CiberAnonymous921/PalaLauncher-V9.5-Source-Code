/*    */ package me.friwi.jcefmaven.impl.step.init;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.logging.Logger;
/*    */ import me.friwi.jcefmaven.CefInitializationException;
/*    */ import me.friwi.jcefmaven.EnumPlatform;
/*    */ import me.friwi.jcefmaven.UnsupportedPlatformException;
/*    */ import org.cef.CefApp;
/*    */ import org.cef.CefSettings;
/*    */ import org.cef.SystemBootstrap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CefInitializer
/*    */ {
/* 22 */   private static final Logger LOGGER = Logger.getLogger(CefInitializer.class.getName());
/*    */   
/*    */   private static final String JAVA_LIBRARY_PATH = "java.library.path";
/*    */   
/*    */   public static CefApp initialize(File installDir, List<String> cefArgs, CefSettings cefSettings) throws UnsupportedPlatformException, CefInitializationException {
/* 27 */     Objects.requireNonNull(installDir, "installDir cannot be null");
/* 28 */     Objects.requireNonNull(cefArgs, "cefArgs cannot be null");
/* 29 */     Objects.requireNonNull(cefSettings, "cefSettings cannot be null");
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 34 */       String path = System.getProperty("java.library.path");
/* 35 */       if (!path.endsWith(File.pathSeparator)) path = path + File.pathSeparator; 
/* 36 */       path = path + installDir.getAbsolutePath();
/* 37 */       System.setProperty("java.library.path", path);
/*    */ 
/*    */       
/* 40 */       SystemBootstrap.setLoader(libname -> {
/*    */           
/*    */           });
/*    */       
/*    */       try {
/* 45 */         System.loadLibrary("jawt");
/* 46 */       } catch (UnsatisfiedLinkError e) {
/* 47 */         LOGGER.warning("Error while loading jawt library: " + e.getMessage());
/*    */       } 
/*    */ 
/*    */       
/* 51 */       if (EnumPlatform.getCurrentPlatform().getOs().isWindows()) {
/* 52 */         System.load((new File(installDir, "chrome_elf.dll")).getAbsolutePath());
/* 53 */         System.load((new File(installDir, "libcef.dll")).getAbsolutePath());
/* 54 */         System.load((new File(installDir, "jcef.dll")).getAbsolutePath());
/* 55 */       } else if (EnumPlatform.getCurrentPlatform().getOs().isLinux()) {
/*    */         
/* 57 */         System.load((new File(installDir, "libjcef.so")).getAbsolutePath());
/*    */         
/* 59 */         boolean success = CefApp.startup(cefArgs.<String>toArray(new String[0]));
/* 60 */         if (!success) throw new CefInitializationException("JCef did not initialize correctly!");
/*    */         
/* 62 */         System.load((new File(installDir, "libcef.so")).getAbsolutePath());
/* 63 */       } else if (EnumPlatform.getCurrentPlatform().getOs().isMacOSX()) {
/*    */         
/* 65 */         System.load((new File(installDir, "libjcef.dylib")).getAbsolutePath());
/*    */         
/* 67 */         cefArgs.add(0, "--framework-dir-path=" + installDir.getAbsolutePath() + "/Chromium Embedded Framework.framework");
/* 68 */         cefArgs.add(0, "--main-bundle-path=" + installDir.getAbsolutePath() + "/jcef Helper.app");
/* 69 */         cefArgs.add(0, "--browser-subprocess-path=" + installDir.getAbsolutePath() + "/jcef Helper.app/Contents/MacOS/jcef Helper");
/* 70 */         cefSettings.browser_subprocess_path = installDir.getAbsolutePath() + "/jcef Helper.app/Contents/MacOS/jcef Helper";
/*    */         
/* 72 */         boolean success = CefApp.startup(cefArgs.<String>toArray(new String[0]));
/* 73 */         if (!success) throw new CefInitializationException("JCef did not initialize correctly!");
/*    */       
/*    */       } 
/* 76 */       return CefApp.getInstance(cefArgs.<String>toArray(new String[0]), cefSettings);
/* 77 */     } catch (RuntimeException e) {
/* 78 */       throw new CefInitializationException("Error while initializing JCef", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\impl\step\init\CefInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */