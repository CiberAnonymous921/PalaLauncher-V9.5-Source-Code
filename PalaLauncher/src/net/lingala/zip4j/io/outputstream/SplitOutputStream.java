/*     */ package net.lingala.zip4j.io.outputstream;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.headers.HeaderSignature;
/*     */ import net.lingala.zip4j.model.enums.RandomAccessFileMode;
/*     */ import net.lingala.zip4j.util.FileUtils;
/*     */ import net.lingala.zip4j.util.RawIO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SplitOutputStream
/*     */   extends OutputStream
/*     */   implements OutputStreamWithSplitZipSupport
/*     */ {
/*     */   private RandomAccessFile raf;
/*     */   private long splitLength;
/*     */   private File zipFile;
/*     */   private int currSplitFileCounter;
/*     */   private long bytesWrittenForThisPart;
/*  40 */   private RawIO rawIO = new RawIO();
/*     */   
/*     */   public SplitOutputStream(File file) throws FileNotFoundException, ZipException {
/*  43 */     this(file, -1L);
/*     */   }
/*     */   
/*     */   public SplitOutputStream(File file, long splitLength) throws FileNotFoundException, ZipException {
/*  47 */     if (splitLength >= 0L && splitLength < 65536L) {
/*  48 */       throw new ZipException("split length less than minimum allowed split length of 65536 Bytes");
/*     */     }
/*     */     
/*  51 */     this.raf = new RandomAccessFile(file, RandomAccessFileMode.WRITE.getValue());
/*  52 */     this.splitLength = splitLength;
/*  53 */     this.zipFile = file;
/*  54 */     this.currSplitFileCounter = 0;
/*  55 */     this.bytesWrittenForThisPart = 0L;
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException {
/*  59 */     write(new byte[] { (byte)b });
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  63 */     write(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  67 */     if (len <= 0) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     if (this.splitLength == -1L) {
/*  72 */       this.raf.write(b, off, len);
/*  73 */       this.bytesWrittenForThisPart += len;
/*     */       
/*     */       return;
/*     */     } 
/*  77 */     if (this.bytesWrittenForThisPart >= this.splitLength) {
/*  78 */       startNextSplitFile();
/*  79 */       this.raf.write(b, off, len);
/*  80 */       this.bytesWrittenForThisPart = len;
/*  81 */     } else if (this.bytesWrittenForThisPart + len > this.splitLength) {
/*  82 */       if (isHeaderData(b)) {
/*  83 */         startNextSplitFile();
/*  84 */         this.raf.write(b, off, len);
/*  85 */         this.bytesWrittenForThisPart = len;
/*     */       } else {
/*  87 */         this.raf.write(b, off, (int)(this.splitLength - this.bytesWrittenForThisPart));
/*  88 */         startNextSplitFile();
/*  89 */         this.raf.write(b, off + (int)(this.splitLength - this.bytesWrittenForThisPart), (int)(len - this.splitLength - this.bytesWrittenForThisPart));
/*     */         
/*  91 */         this.bytesWrittenForThisPart = len - this.splitLength - this.bytesWrittenForThisPart;
/*     */       } 
/*     */     } else {
/*  94 */       this.raf.write(b, off, len);
/*  95 */       this.bytesWrittenForThisPart += len;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startNextSplitFile() throws IOException {
/* 100 */     String zipFileWithoutExt = FileUtils.getZipFileNameWithoutExtension(this.zipFile.getName());
/* 101 */     String zipFileName = this.zipFile.getAbsolutePath();
/*     */     
/* 103 */     String parentPath = (this.zipFile.getParent() == null) ? "" : (this.zipFile.getParent() + System.getProperty("file.separator"));
/*     */     
/* 105 */     String fileExtension = ".z0" + (this.currSplitFileCounter + 1);
/* 106 */     if (this.currSplitFileCounter >= 9) {
/* 107 */       fileExtension = ".z" + (this.currSplitFileCounter + 1);
/*     */     }
/*     */     
/* 110 */     File currSplitFile = new File(parentPath + zipFileWithoutExt + fileExtension);
/*     */     
/* 112 */     this.raf.close();
/*     */     
/* 114 */     if (currSplitFile.exists()) {
/* 115 */       throw new IOException("split file: " + currSplitFile.getName() + " already exists in the current directory, cannot rename this file");
/*     */     }
/*     */ 
/*     */     
/* 119 */     if (!this.zipFile.renameTo(currSplitFile)) {
/* 120 */       throw new IOException("cannot rename newly created split file");
/*     */     }
/*     */     
/* 123 */     this.zipFile = new File(zipFileName);
/* 124 */     this.raf = new RandomAccessFile(this.zipFile, RandomAccessFileMode.WRITE.getValue());
/* 125 */     this.currSplitFileCounter++;
/*     */   }
/*     */   
/*     */   private boolean isHeaderData(byte[] buff) {
/* 129 */     int signature = this.rawIO.readIntLittleEndian(buff);
/* 130 */     for (HeaderSignature headerSignature : HeaderSignature.values()) {
/*     */       
/* 132 */       if (headerSignature != HeaderSignature.SPLIT_ZIP && headerSignature
/* 133 */         .getValue() == signature) {
/* 134 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkBufferSizeAndStartNextSplitFile(int bufferSize) throws ZipException {
/* 150 */     if (bufferSize < 0) {
/* 151 */       throw new ZipException("negative buffersize for checkBufferSizeAndStartNextSplitFile");
/*     */     }
/*     */     
/* 154 */     if (!isBufferSizeFitForCurrSplitFile(bufferSize)) {
/*     */       try {
/* 156 */         startNextSplitFile();
/* 157 */         this.bytesWrittenForThisPart = 0L;
/* 158 */         return true;
/* 159 */       } catch (IOException e) {
/* 160 */         throw new ZipException(e);
/*     */       } 
/*     */     }
/*     */     
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isBufferSizeFitForCurrSplitFile(int bufferSize) {
/* 175 */     if (this.splitLength >= 65536L) {
/* 176 */       return (this.bytesWrittenForThisPart + bufferSize <= this.splitLength);
/*     */     }
/*     */     
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void seek(long pos) throws IOException {
/* 184 */     this.raf.seek(pos);
/*     */   }
/*     */   
/*     */   public int skipBytes(int n) throws IOException {
/* 188 */     return this.raf.skipBytes(n);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 192 */     this.raf.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getFilePointer() throws IOException {
/* 197 */     return this.raf.getFilePointer();
/*     */   }
/*     */   
/*     */   public boolean isSplitZipFile() {
/* 201 */     return (this.splitLength != -1L);
/*     */   }
/*     */   
/*     */   public long getSplitLength() {
/* 205 */     return this.splitLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCurrentSplitFileCounter() {
/* 210 */     return this.currSplitFileCounter;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\SplitOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */