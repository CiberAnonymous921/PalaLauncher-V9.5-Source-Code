/*    */ package net.lingala.zip4j.io.inputstream;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import net.lingala.zip4j.util.FileUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NumberedSplitInputStream
/*    */   extends SplitInputStream
/*    */ {
/*    */   public NumberedSplitInputStream(File zipFile, boolean isSplitZipArchive, int lastSplitZipFileNumber) throws FileNotFoundException {
/* 16 */     super(zipFile, isSplitZipArchive, lastSplitZipFileNumber);
/*    */   }
/*    */ 
/*    */   
/*    */   protected File getNextSplitFile(int zipFileIndex) throws IOException {
/* 21 */     String currZipFileNameWithPath = this.zipFile.getCanonicalPath();
/* 22 */     String fileNameWithPathAndWithoutExtension = currZipFileNameWithPath.substring(0, currZipFileNameWithPath
/* 23 */         .lastIndexOf("."));
/* 24 */     return new File(fileNameWithPathAndWithoutExtension + FileUtils.getNextNumberedSplitFileCounterAsExtension(zipFileIndex));
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\NumberedSplitInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */