/*    */ package net.lingala.zip4j.io.inputstream;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZipStandardSplitInputStream
/*    */   extends SplitInputStream
/*    */ {
/*    */   private int lastSplitZipFileNumber;
/*    */   
/*    */   public ZipStandardSplitInputStream(File zipFile, boolean isSplitZipArchive, int lastSplitZipFileNumber) throws FileNotFoundException {
/* 15 */     super(zipFile, isSplitZipArchive, lastSplitZipFileNumber);
/* 16 */     this.lastSplitZipFileNumber = lastSplitZipFileNumber;
/*    */   }
/*    */ 
/*    */   
/*    */   protected File getNextSplitFile(int zipFileIndex) throws IOException {
/* 21 */     if (zipFileIndex == this.lastSplitZipFileNumber) {
/* 22 */       return this.zipFile;
/*    */     }
/*    */     
/* 25 */     String currZipFileNameWithPath = this.zipFile.getCanonicalPath();
/* 26 */     String extensionSubString = ".z0";
/* 27 */     if (zipFileIndex >= 9) {
/* 28 */       extensionSubString = ".z";
/*    */     }
/*    */     
/* 31 */     return new File(currZipFileNameWithPath.substring(0, currZipFileNameWithPath
/* 32 */           .lastIndexOf(".")) + extensionSubString + (zipFileIndex + 1));
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\ZipStandardSplitInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */