/*    */ package net.lingala.zip4j.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.zip.CRC32;
/*    */ import net.lingala.zip4j.exception.ZipException;
/*    */ import net.lingala.zip4j.progress.ProgressMonitor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CrcUtil
/*    */ {
/*    */   private static final int BUF_SIZE = 16384;
/*    */   
/*    */   public static long computeFileCrc(File inputFile, ProgressMonitor progressMonitor) throws IOException {
/* 34 */     if (inputFile == null || !inputFile.exists() || !inputFile.canRead()) {
/* 35 */       throw new ZipException("input file is null or does not exist or cannot read. Cannot calculate CRC for the file");
/*    */     }
/*    */ 
/*    */     
/* 39 */     byte[] buff = new byte[16384];
/* 40 */     CRC32 crc32 = new CRC32();
/*    */     
/* 42 */     try (InputStream inputStream = new FileInputStream(inputFile)) {
/*    */       int readLen;
/* 44 */       while ((readLen = inputStream.read(buff)) != -1) {
/* 45 */         crc32.update(buff, 0, readLen);
/*    */         
/* 47 */         if (progressMonitor != null) {
/* 48 */           progressMonitor.updateWorkCompleted(readLen);
/* 49 */           if (progressMonitor.isCancelAllTasks()) {
/* 50 */             progressMonitor.setResult(ProgressMonitor.Result.CANCELLED);
/* 51 */             progressMonitor.setState(ProgressMonitor.State.READY);
/* 52 */             return 0L;
/*    */           } 
/*    */         } 
/*    */       } 
/* 56 */       return crc32.getValue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4\\util\CrcUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */