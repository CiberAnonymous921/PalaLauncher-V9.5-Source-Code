/*     */ package net.lingala.zip4j.io.inputstream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import net.lingala.zip4j.crypto.Decrypter;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.model.LocalFileHeader;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*     */ import net.lingala.zip4j.util.Zip4jUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class CipherInputStream<T extends Decrypter>
/*     */   extends InputStream
/*     */ {
/*     */   private ZipEntryInputStream zipEntryInputStream;
/*     */   private T decrypter;
/*     */   private byte[] lastReadRawDataCache;
/*  19 */   private byte[] singleByteBuffer = new byte[1];
/*     */   private LocalFileHeader localFileHeader;
/*     */   
/*     */   public CipherInputStream(ZipEntryInputStream zipEntryInputStream, LocalFileHeader localFileHeader, char[] password) throws IOException, ZipException {
/*  23 */     this.zipEntryInputStream = zipEntryInputStream;
/*  24 */     this.decrypter = initializeDecrypter(localFileHeader, password);
/*  25 */     this.localFileHeader = localFileHeader;
/*     */     
/*  27 */     if (getCompressionMethod(localFileHeader) == CompressionMethod.DEFLATE) {
/*  28 */       this.lastReadRawDataCache = new byte[4096];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  34 */     int readLen = read(this.singleByteBuffer);
/*     */     
/*  36 */     if (readLen == -1) {
/*  37 */       return -1;
/*     */     }
/*     */     
/*  40 */     return this.singleByteBuffer[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  45 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  50 */     int readLen = Zip4jUtil.readFully(this.zipEntryInputStream, b, off, len);
/*     */     
/*  52 */     if (readLen > 0) {
/*  53 */       cacheRawData(b, readLen);
/*  54 */       this.decrypter.decryptData(b, off, readLen);
/*     */     } 
/*     */     
/*  57 */     return readLen;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  62 */     this.zipEntryInputStream.close();
/*     */   }
/*     */   
/*     */   public byte[] getLastReadRawDataCache() {
/*  66 */     return this.lastReadRawDataCache;
/*     */   }
/*     */   
/*     */   protected int readRaw(byte[] b) throws IOException {
/*  70 */     return this.zipEntryInputStream.readRawFully(b);
/*     */   }
/*     */   
/*     */   private void cacheRawData(byte[] b, int len) {
/*  74 */     if (this.lastReadRawDataCache != null) {
/*  75 */       System.arraycopy(b, 0, this.lastReadRawDataCache, 0, len);
/*     */     }
/*     */   }
/*     */   
/*     */   private CompressionMethod getCompressionMethod(LocalFileHeader localFileHeader) throws ZipException {
/*  80 */     if (localFileHeader.getCompressionMethod() != CompressionMethod.AES_INTERNAL_ONLY) {
/*  81 */       return localFileHeader.getCompressionMethod();
/*     */     }
/*     */     
/*  84 */     if (localFileHeader.getAesExtraDataRecord() == null) {
/*  85 */       throw new ZipException("AesExtraDataRecord not present in localheader for aes encrypted data");
/*     */     }
/*     */     
/*  88 */     return localFileHeader.getAesExtraDataRecord().getCompressionMethod();
/*     */   }
/*     */   
/*     */   public T getDecrypter() {
/*  92 */     return this.decrypter;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endOfEntryReached(InputStream inputStream) throws IOException {}
/*     */ 
/*     */   
/*     */   protected long getNumberOfBytesReadForThisEntry() {
/* 100 */     return this.zipEntryInputStream.getNumberOfBytesRead();
/*     */   }
/*     */   
/*     */   public LocalFileHeader getLocalFileHeader() {
/* 104 */     return this.localFileHeader;
/*     */   }
/*     */   
/*     */   protected abstract T initializeDecrypter(LocalFileHeader paramLocalFileHeader, char[] paramArrayOfchar) throws IOException, ZipException;
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\CipherInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */