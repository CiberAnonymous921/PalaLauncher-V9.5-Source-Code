/*    */ package me.friwi.jcefmaven.impl.step.extract;
/*    */ 
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Objects;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/*    */ import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
/*    */ import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TarGzExtractor
/*    */ {
/*    */   private static final int BUFFER_SIZE = 4096;
/* 20 */   private static final Logger LOGGER = Logger.getLogger(TarGzExtractor.class.getName());
/*    */   
/*    */   public static void extractTarGZ(File installDir, InputStream in) throws IOException {
/* 23 */     Objects.requireNonNull(installDir, "installDir cannot be null");
/* 24 */     Objects.requireNonNull(in, "in cannot be null");
/* 25 */     GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(in);
/* 26 */     TarArchiveInputStream tarIn = new TarArchiveInputStream((InputStream)gzipIn);
/*    */     try {
/*    */       TarArchiveEntry entry;
/* 29 */       while ((entry = (TarArchiveEntry)tarIn.getNextEntry()) != null) {
/* 30 */         File f = new File(installDir, entry.getName());
/* 31 */         if (entry.isDirectory()) {
/* 32 */           boolean created = f.mkdir();
/* 33 */           if (!created) {
/* 34 */             LOGGER.log(Level.SEVERE, "Unable to create directory '%s', during extraction of archive contents.\n", f
/* 35 */                 .getAbsolutePath()); continue;
/*    */           } 
/* 37 */           if ((entry.getMode() & 0x49) != 0 && !f.setExecutable(true, false)) {
/* 38 */             LOGGER.log(Level.SEVERE, "Unable to mark directory '%s' executable, during extraction of archive contents.\n", f
/* 39 */                 .getAbsolutePath());
/*    */           }
/*    */           
/*    */           continue;
/*    */         } 
/* 44 */         byte[] data = new byte[4096];
/* 45 */         BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(f, false), 4096); 
/*    */         try { int count;
/* 47 */           while ((count = tarIn.read(data, 0, 4096)) != -1) {
/* 48 */             dest.write(data, 0, count);
/*    */           }
/* 50 */           dest.close(); } catch (Throwable throwable) { try { dest.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 51 */          if ((entry.getMode() & 0x49) != 0 && !f.setExecutable(true, false)) {
/* 52 */           LOGGER.log(Level.SEVERE, "Unable to mark file '%s' executable, during extraction of archive contents.\n", f
/* 53 */               .getAbsolutePath());
/*    */         }
/*    */       } 
/*    */       
/* 57 */       tarIn.close();
/*    */     } catch (Throwable throwable) {
/*    */       try {
/*    */         tarIn.close();
/*    */       } catch (Throwable throwable1) {
/*    */         throwable.addSuppressed(throwable1);
/*    */       } 
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\impl\step\extract\TarGzExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */