/*    */ package me.friwi.jcefmaven.impl.util.macos;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnquarantineUtil
/*    */ {
/* 16 */   private static final Logger LOGGER = Logger.getLogger(UnquarantineUtil.class.getName());
/*    */   
/*    */   public static void unquarantine(File dir) {
/* 19 */     Objects.requireNonNull(dir, "dir cannot be null");
/*    */     try {
/* 21 */       Process p = Runtime.getRuntime().exec(new String[] { "xattr", "-r", "-d", "com.apple.quarantine", dir.getAbsolutePath() });
/*    */       try {
/* 23 */         if (p.waitFor() > 0)
/*    */         {
/* 25 */           LOGGER.log(Level.WARNING, "Failed to update xattr! Command returned non-zero exit code.");
/*    */         }
/* 27 */       } catch (InterruptedException e) {
/* 28 */         LOGGER.log(Level.WARNING, "Failed to update xattr! Command got interrupted.", e);
/*    */       } 
/* 30 */     } catch (IOException e) {
/* 31 */       LOGGER.log(Level.WARNING, "Failed to update xattr! IOException on command execution: " + e.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\imp\\util\macos\UnquarantineUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */