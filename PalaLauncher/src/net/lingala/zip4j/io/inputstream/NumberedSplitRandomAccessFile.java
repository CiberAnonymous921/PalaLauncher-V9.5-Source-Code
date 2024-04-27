/*     */ package net.lingala.zip4j.io.inputstream;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import net.lingala.zip4j.model.enums.RandomAccessFileMode;
/*     */ import net.lingala.zip4j.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NumberedSplitRandomAccessFile
/*     */   extends RandomAccessFile
/*     */ {
/*     */   private long splitLength;
/*     */   private File[] allSortedSplitFiles;
/*     */   private RandomAccessFile randomAccessFile;
/*  19 */   private byte[] singleByteBuffer = new byte[1];
/*  20 */   private int currentOpenSplitFileCounter = 0;
/*     */   private String rwMode;
/*     */   
/*     */   public NumberedSplitRandomAccessFile(String name, String mode) throws IOException {
/*  24 */     this(new File(name), mode);
/*     */   }
/*     */   
/*     */   public NumberedSplitRandomAccessFile(File file, String mode) throws IOException {
/*  28 */     this(file, mode, FileUtils.getAllSortedNumberedSplitFiles(file));
/*     */   }
/*     */   
/*     */   public NumberedSplitRandomAccessFile(File file, String mode, File[] allSortedSplitFiles) throws IOException {
/*  32 */     super(file, mode);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     close();
/*     */     
/*  43 */     if (RandomAccessFileMode.WRITE.getValue().equals(mode)) {
/*  44 */       throw new IllegalArgumentException("write mode is not allowed for NumberedSplitRandomAccessFile");
/*     */     }
/*     */     
/*  47 */     assertAllSplitFilesExist(allSortedSplitFiles);
/*     */     
/*  49 */     this.randomAccessFile = new RandomAccessFile(file, mode);
/*  50 */     this.allSortedSplitFiles = allSortedSplitFiles;
/*  51 */     this.splitLength = file.length();
/*  52 */     this.rwMode = mode;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  57 */     int readLen = read(this.singleByteBuffer);
/*     */     
/*  59 */     if (readLen == -1) {
/*  60 */       return -1;
/*     */     }
/*     */     
/*  63 */     return this.singleByteBuffer[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  68 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  73 */     int readLen = this.randomAccessFile.read(b, off, len);
/*     */     
/*  75 */     if (readLen == -1) {
/*  76 */       if (this.currentOpenSplitFileCounter == this.allSortedSplitFiles.length - 1) {
/*  77 */         return -1;
/*     */       }
/*  79 */       openRandomAccessFileForIndex(this.currentOpenSplitFileCounter + 1);
/*  80 */       return read(b, off, len);
/*     */     } 
/*     */     
/*  83 */     return readLen;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*  88 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  93 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  98 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void seek(long pos) throws IOException {
/* 103 */     int splitPartOfPosition = (int)(pos / this.splitLength);
/*     */     
/* 105 */     if (splitPartOfPosition != this.currentOpenSplitFileCounter) {
/* 106 */       openRandomAccessFileForIndex(splitPartOfPosition);
/*     */     }
/*     */     
/* 109 */     this.randomAccessFile.seek(pos - splitPartOfPosition * this.splitLength);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getFilePointer() throws IOException {
/* 114 */     return this.randomAccessFile.getFilePointer();
/*     */   }
/*     */ 
/*     */   
/*     */   public long length() throws IOException {
/* 119 */     return this.randomAccessFile.length();
/*     */   }
/*     */   
/*     */   public void seekInCurrentPart(long pos) throws IOException {
/* 123 */     this.randomAccessFile.seek(pos);
/*     */   }
/*     */   
/*     */   public void openLastSplitFileForReading() throws IOException {
/* 127 */     openRandomAccessFileForIndex(this.allSortedSplitFiles.length - 1);
/*     */   }
/*     */   
/*     */   private void openRandomAccessFileForIndex(int splitCounter) throws IOException {
/* 131 */     if (this.currentOpenSplitFileCounter == splitCounter) {
/*     */       return;
/*     */     }
/*     */     
/* 135 */     if (splitCounter > this.allSortedSplitFiles.length - 1) {
/* 136 */       throw new IOException("split counter greater than number of split files");
/*     */     }
/*     */     
/* 139 */     if (this.randomAccessFile != null) {
/* 140 */       this.randomAccessFile.close();
/*     */     }
/*     */     
/* 143 */     this.randomAccessFile = new RandomAccessFile(this.allSortedSplitFiles[splitCounter], this.rwMode);
/* 144 */     this.currentOpenSplitFileCounter = splitCounter;
/*     */   }
/*     */   
/*     */   private void assertAllSplitFilesExist(File[] allSortedSplitFiles) throws IOException {
/* 148 */     int splitCounter = 1;
/* 149 */     for (File splitFile : allSortedSplitFiles) {
/* 150 */       String fileExtension = FileUtils.getFileExtension(splitFile);
/*     */       try {
/* 152 */         if (splitCounter != Integer.parseInt(fileExtension)) {
/* 153 */           throw new IOException("Split file number " + splitCounter + " does not exist");
/*     */         }
/* 155 */         splitCounter++;
/* 156 */       } catch (NumberFormatException e) {
/* 157 */         throw new IOException("Split file extension not in expected format. Found: " + fileExtension + " expected of format: .001, .002, etc");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\NumberedSplitRandomAccessFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */