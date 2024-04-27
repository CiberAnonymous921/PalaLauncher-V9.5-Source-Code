/*    */ package me.friwi.jcefmaven.impl.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Objects;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileUtils
/*    */ {
/* 14 */   private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());
/*    */   
/*    */   public static void deleteDir(File dir) {
/* 17 */     Objects.requireNonNull(dir, "dir cannot be null");
/* 18 */     if (!dir.exists())
/* 19 */       return;  if (dir.isDirectory()) {
/* 20 */       for (File f : (File[])Objects.<File[]>requireNonNull(dir.listFiles(), "Could not read contents of " + dir.getAbsolutePath())) {
/* 21 */         deleteDir(f);
/*    */       }
/*    */     }
/* 24 */     if (!dir.delete())
/* 25 */       LOGGER.log(Level.WARNING, "Could not delete " + dir.getAbsolutePath()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\imp\\util\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */