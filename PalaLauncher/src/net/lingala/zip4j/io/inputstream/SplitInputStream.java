/*    */ package net.lingala.zip4j.io.inputstream;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.RandomAccessFile;
/*    */ import net.lingala.zip4j.model.FileHeader;
/*    */ import net.lingala.zip4j.model.enums.RandomAccessFileMode;
/*    */ 
/*    */ 
/*    */ public abstract class SplitInputStream
/*    */   extends InputStream
/*    */ {
/*    */   protected RandomAccessFile randomAccessFile;
/*    */   protected File zipFile;
/*    */   private boolean isSplitZipArchive;
/* 18 */   private int currentSplitFileCounter = 0;
/* 19 */   private byte[] singleByteArray = new byte[1];
/*    */   
/*    */   public SplitInputStream(File zipFile, boolean isSplitZipArchive, int lastSplitZipFileNumber) throws FileNotFoundException {
/* 22 */     this.randomAccessFile = new RandomAccessFile(zipFile, RandomAccessFileMode.READ.getValue());
/* 23 */     this.zipFile = zipFile;
/* 24 */     this.isSplitZipArchive = isSplitZipArchive;
/*    */ 
/*    */     
/* 27 */     if (isSplitZipArchive) {
/* 28 */       this.currentSplitFileCounter = lastSplitZipFileNumber;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 34 */     int readLen = read(this.singleByteArray);
/* 35 */     if (readLen == -1) {
/* 36 */       return -1;
/*    */     }
/*    */     
/* 39 */     return this.singleByteArray[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b) throws IOException {
/* 44 */     return read(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 49 */     int readLen = this.randomAccessFile.read(b, off, len);
/*    */     
/* 51 */     if ((readLen != len || readLen == -1) && this.isSplitZipArchive) {
/* 52 */       openRandomAccessFileForIndex(this.currentSplitFileCounter + 1);
/* 53 */       this.currentSplitFileCounter++;
/*    */       
/* 55 */       if (readLen < 0) readLen = 0; 
/* 56 */       int newlyRead = this.randomAccessFile.read(b, readLen, len - readLen);
/* 57 */       if (newlyRead > 0) readLen += newlyRead;
/*    */     
/*    */     } 
/* 60 */     return readLen;
/*    */   }
/*    */ 
/*    */   
/*    */   public void prepareExtractionForFileHeader(FileHeader fileHeader) throws IOException {
/* 65 */     if (this.isSplitZipArchive && this.currentSplitFileCounter != fileHeader.getDiskNumberStart()) {
/* 66 */       openRandomAccessFileForIndex(fileHeader.getDiskNumberStart());
/* 67 */       this.currentSplitFileCounter = fileHeader.getDiskNumberStart();
/*    */     } 
/*    */     
/* 70 */     this.randomAccessFile.seek(fileHeader.getOffsetLocalHeader());
/*    */   }
/*    */   
/*    */   protected void openRandomAccessFileForIndex(int zipFileIndex) throws IOException {
/* 74 */     File nextSplitFile = getNextSplitFile(zipFileIndex);
/* 75 */     if (!nextSplitFile.exists()) {
/* 76 */       throw new FileNotFoundException("zip split file does not exist: " + nextSplitFile);
/*    */     }
/* 78 */     this.randomAccessFile.close();
/* 79 */     this.randomAccessFile = new RandomAccessFile(nextSplitFile, RandomAccessFileMode.READ.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract File getNextSplitFile(int paramInt) throws IOException;
/*    */   
/*    */   public void close() throws IOException {
/* 86 */     if (this.randomAccessFile != null)
/* 87 */       this.randomAccessFile.close(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\SplitInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */