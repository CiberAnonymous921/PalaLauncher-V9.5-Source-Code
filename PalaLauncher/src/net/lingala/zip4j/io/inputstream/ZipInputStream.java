/*     */ package net.lingala.zip4j.io.inputstream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
/*     */ import java.util.zip.CRC32;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.headers.HeaderReader;
/*     */ import net.lingala.zip4j.headers.HeaderSignature;
/*     */ import net.lingala.zip4j.model.DataDescriptor;
/*     */ import net.lingala.zip4j.model.ExtraDataRecord;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.LocalFileHeader;
/*     */ import net.lingala.zip4j.model.enums.AesVersion;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*     */ import net.lingala.zip4j.model.enums.EncryptionMethod;
/*     */ import net.lingala.zip4j.util.InternalZipConstants;
/*     */ import net.lingala.zip4j.util.Zip4jUtil;
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
/*     */ 
/*     */ 
/*     */ public class ZipInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private PushbackInputStream inputStream;
/*     */   private DecompressedInputStream decompressedInputStream;
/*  46 */   private HeaderReader headerReader = new HeaderReader();
/*     */   private char[] password;
/*     */   private LocalFileHeader localFileHeader;
/*  49 */   private CRC32 crc32 = new CRC32();
/*     */   private byte[] endOfEntryBuffer;
/*     */   private boolean canSkipExtendedLocalFileHeader = false;
/*     */   private Charset charset;
/*     */   
/*     */   public ZipInputStream(InputStream inputStream) {
/*  55 */     this(inputStream, null, InternalZipConstants.CHARSET_UTF_8);
/*     */   }
/*     */   
/*     */   public ZipInputStream(InputStream inputStream, Charset charset) {
/*  59 */     this(inputStream, null, charset);
/*     */   }
/*     */   
/*     */   public ZipInputStream(InputStream inputStream, char[] password) {
/*  63 */     this(inputStream, password, InternalZipConstants.CHARSET_UTF_8);
/*     */   }
/*     */   
/*     */   public ZipInputStream(InputStream inputStream, char[] password, Charset charset) {
/*  67 */     if (charset == null) {
/*  68 */       charset = InternalZipConstants.CHARSET_UTF_8;
/*     */     }
/*     */     
/*  71 */     this.inputStream = new PushbackInputStream(inputStream, 4096);
/*  72 */     this.password = password;
/*  73 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public LocalFileHeader getNextEntry() throws IOException {
/*  77 */     return getNextEntry(null);
/*     */   }
/*     */   
/*     */   public LocalFileHeader getNextEntry(FileHeader fileHeader) throws IOException {
/*  81 */     if (this.localFileHeader != null) {
/*  82 */       readUntilEndOfEntry();
/*     */     }
/*     */     
/*  85 */     this.localFileHeader = this.headerReader.readLocalFileHeader(this.inputStream, this.charset);
/*     */     
/*  87 */     if (this.localFileHeader == null) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     verifyLocalFileHeader(this.localFileHeader);
/*  92 */     this.crc32.reset();
/*     */     
/*  94 */     if (fileHeader != null) {
/*  95 */       this.localFileHeader.setCrc(fileHeader.getCrc());
/*  96 */       this.localFileHeader.setCompressedSize(fileHeader.getCompressedSize());
/*  97 */       this.localFileHeader.setUncompressedSize(fileHeader.getUncompressedSize());
/*  98 */       this.canSkipExtendedLocalFileHeader = true;
/*     */     } else {
/* 100 */       this.canSkipExtendedLocalFileHeader = false;
/*     */     } 
/*     */     
/* 103 */     this.decompressedInputStream = initializeEntryInputStream(this.localFileHeader);
/* 104 */     return this.localFileHeader;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 109 */     byte[] b = new byte[1];
/* 110 */     int readLen = read(b);
/*     */     
/* 112 */     if (readLen == -1) {
/* 113 */       return -1;
/*     */     }
/*     */     
/* 116 */     return b[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 121 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 126 */     if (len < 0) {
/* 127 */       throw new IllegalArgumentException("Negative read length");
/*     */     }
/*     */     
/* 130 */     if (len == 0) {
/* 131 */       return 0;
/*     */     }
/*     */     
/* 134 */     if (this.localFileHeader == null)
/*     */     {
/*     */       
/* 137 */       return -1;
/*     */     }
/*     */     
/*     */     try {
/* 141 */       int readLen = this.decompressedInputStream.read(b, off, len);
/*     */       
/* 143 */       if (readLen == -1) {
/* 144 */         endOfCompressedDataReached();
/*     */       } else {
/* 146 */         this.crc32.update(b, off, readLen);
/*     */       } 
/*     */       
/* 149 */       return readLen;
/* 150 */     } catch (IOException e) {
/* 151 */       if (e.getCause() != null && e.getCause() instanceof java.util.zip.DataFormatException && 
/* 152 */         isEncryptionMethodZipStandard(this.localFileHeader)) {
/* 153 */         throw new ZipException(e.getMessage(), e.getCause(), ZipException.Type.WRONG_PASSWORD);
/*     */       }
/*     */       
/* 156 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 162 */     if (this.decompressedInputStream != null) {
/* 163 */       this.decompressedInputStream.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getAvailableBytesInPushBackInputStream() throws IOException {
/* 168 */     return this.inputStream.available();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void endOfCompressedDataReached() throws IOException {
/* 174 */     this.decompressedInputStream.pushBackInputStreamIfNecessary(this.inputStream);
/*     */ 
/*     */     
/* 177 */     this.decompressedInputStream.endOfEntryReached(this.inputStream);
/*     */     
/* 179 */     readExtendedLocalFileHeaderIfPresent();
/* 180 */     verifyCrc();
/* 181 */     resetFields();
/*     */   }
/*     */   
/*     */   private DecompressedInputStream initializeEntryInputStream(LocalFileHeader localFileHeader) throws IOException {
/* 185 */     ZipEntryInputStream zipEntryInputStream = new ZipEntryInputStream(this.inputStream, getCompressedSize(localFileHeader));
/* 186 */     CipherInputStream cipherInputStream = initializeCipherInputStream(zipEntryInputStream, localFileHeader);
/* 187 */     return initializeDecompressorForThisEntry(cipherInputStream, localFileHeader);
/*     */   }
/*     */   
/*     */   private CipherInputStream initializeCipherInputStream(ZipEntryInputStream zipEntryInputStream, LocalFileHeader localFileHeader) throws IOException {
/* 191 */     if (!localFileHeader.isEncrypted()) {
/* 192 */       return new NoCipherInputStream(zipEntryInputStream, localFileHeader, this.password);
/*     */     }
/*     */     
/* 195 */     if (localFileHeader.getEncryptionMethod() == EncryptionMethod.AES) {
/* 196 */       return new AesCipherInputStream(zipEntryInputStream, localFileHeader, this.password);
/*     */     }
/* 198 */     return new ZipStandardCipherInputStream(zipEntryInputStream, localFileHeader, this.password);
/*     */   }
/*     */ 
/*     */   
/*     */   private DecompressedInputStream initializeDecompressorForThisEntry(CipherInputStream cipherInputStream, LocalFileHeader localFileHeader) {
/* 203 */     CompressionMethod compressionMethod = Zip4jUtil.getCompressionMethod(localFileHeader);
/*     */     
/* 205 */     if (compressionMethod == CompressionMethod.DEFLATE) {
/* 206 */       return new InflaterInputStream(cipherInputStream);
/*     */     }
/*     */     
/* 209 */     return new StoreInputStream(cipherInputStream);
/*     */   }
/*     */   
/*     */   private void readExtendedLocalFileHeaderIfPresent() throws IOException {
/* 213 */     if (!this.localFileHeader.isDataDescriptorExists() || this.canSkipExtendedLocalFileHeader) {
/*     */       return;
/*     */     }
/*     */     
/* 217 */     DataDescriptor dataDescriptor = this.headerReader.readDataDescriptor(this.inputStream, 
/* 218 */         checkIfZip64ExtraDataRecordPresentInLFH(this.localFileHeader.getExtraDataRecords()));
/* 219 */     this.localFileHeader.setCompressedSize(dataDescriptor.getCompressedSize());
/* 220 */     this.localFileHeader.setUncompressedSize(dataDescriptor.getUncompressedSize());
/* 221 */     this.localFileHeader.setCrc(dataDescriptor.getCrc());
/*     */   }
/*     */   
/*     */   private void verifyLocalFileHeader(LocalFileHeader localFileHeader) throws IOException {
/* 225 */     if (!isEntryDirectory(localFileHeader.getFileName()) && localFileHeader
/* 226 */       .getCompressionMethod() == CompressionMethod.STORE && localFileHeader
/* 227 */       .getUncompressedSize() < 0L) {
/* 228 */       throw new IOException("Invalid local file header for: " + localFileHeader.getFileName() + ". Uncompressed size has to be set for entry of compression type store which is not a directory");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkIfZip64ExtraDataRecordPresentInLFH(List<ExtraDataRecord> extraDataRecords) {
/* 234 */     if (extraDataRecords == null) {
/* 235 */       return false;
/*     */     }
/*     */     
/* 238 */     for (ExtraDataRecord extraDataRecord : extraDataRecords) {
/* 239 */       if (extraDataRecord.getHeader() == HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue()) {
/* 240 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 244 */     return false;
/*     */   }
/*     */   
/*     */   private void verifyCrc() throws IOException {
/* 248 */     if (this.localFileHeader.getEncryptionMethod() == EncryptionMethod.AES && this.localFileHeader
/* 249 */       .getAesExtraDataRecord().getAesVersion().equals(AesVersion.TWO)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 254 */     if (this.localFileHeader.getCrc() != this.crc32.getValue()) {
/* 255 */       ZipException.Type exceptionType = ZipException.Type.CHECKSUM_MISMATCH;
/*     */       
/* 257 */       if (isEncryptionMethodZipStandard(this.localFileHeader)) {
/* 258 */         exceptionType = ZipException.Type.WRONG_PASSWORD;
/*     */       }
/*     */       
/* 261 */       throw new ZipException("Reached end of entry, but crc verification failed for " + this.localFileHeader.getFileName(), exceptionType);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetFields() {
/* 267 */     this.localFileHeader = null;
/* 268 */     this.crc32.reset();
/*     */   }
/*     */   
/*     */   private boolean isEntryDirectory(String entryName) {
/* 272 */     return (entryName.endsWith("/") || entryName.endsWith("\\"));
/*     */   }
/*     */   
/*     */   private long getCompressedSize(LocalFileHeader localFileHeader) {
/* 276 */     if (Zip4jUtil.getCompressionMethod(localFileHeader).equals(CompressionMethod.STORE)) {
/* 277 */       return localFileHeader.getUncompressedSize();
/*     */     }
/*     */     
/* 280 */     if (localFileHeader.isDataDescriptorExists() && !this.canSkipExtendedLocalFileHeader) {
/* 281 */       return -1L;
/*     */     }
/*     */     
/* 284 */     return localFileHeader.getCompressedSize() - getEncryptionHeaderSize(localFileHeader);
/*     */   }
/*     */   
/*     */   private int getEncryptionHeaderSize(LocalFileHeader localFileHeader) {
/* 288 */     if (!localFileHeader.isEncrypted()) {
/* 289 */       return 0;
/*     */     }
/*     */     
/* 292 */     if (localFileHeader.getEncryptionMethod().equals(EncryptionMethod.AES))
/* 293 */       return 12 + localFileHeader
/* 294 */         .getAesExtraDataRecord().getAesKeyStrength().getSaltLength(); 
/* 295 */     if (localFileHeader.getEncryptionMethod().equals(EncryptionMethod.ZIP_STANDARD)) {
/* 296 */       return 12;
/*     */     }
/* 298 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void readUntilEndOfEntry() throws IOException {
/* 303 */     if (this.localFileHeader.isDirectory() || this.localFileHeader.getCompressedSize() == 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 307 */     if (this.endOfEntryBuffer == null) {
/* 308 */       this.endOfEntryBuffer = new byte[512];
/*     */     }
/*     */     
/* 311 */     while (read(this.endOfEntryBuffer) != -1);
/*     */   }
/*     */   
/*     */   private boolean isEncryptionMethodZipStandard(LocalFileHeader localFileHeader) {
/* 315 */     return (localFileHeader.isEncrypted() && EncryptionMethod.ZIP_STANDARD.equals(localFileHeader.getEncryptionMethod()));
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\inputstream\ZipInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */