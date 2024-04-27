/*     */ package net.lingala.zip4j.io.outputstream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.zip.CRC32;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.headers.FileHeaderFactory;
/*     */ import net.lingala.zip4j.headers.HeaderSignature;
/*     */ import net.lingala.zip4j.headers.HeaderWriter;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.LocalFileHeader;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.model.ZipParameters;
/*     */ import net.lingala.zip4j.model.enums.AesVersion;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*     */ import net.lingala.zip4j.model.enums.EncryptionMethod;
/*     */ import net.lingala.zip4j.util.InternalZipConstants;
/*     */ import net.lingala.zip4j.util.RawIO;
/*     */ 
/*     */ public class ZipOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private CountingOutputStream countingOutputStream;
/*     */   private char[] password;
/*     */   private ZipModel zipModel;
/*     */   private CompressedOutputStream compressedOutputStream;
/*     */   private FileHeader fileHeader;
/*     */   private LocalFileHeader localFileHeader;
/*  30 */   private FileHeaderFactory fileHeaderFactory = new FileHeaderFactory();
/*  31 */   private HeaderWriter headerWriter = new HeaderWriter();
/*  32 */   private CRC32 crc32 = new CRC32();
/*  33 */   private RawIO rawIO = new RawIO();
/*  34 */   private long uncompressedSizeForThisEntry = 0L;
/*     */   private Charset charset;
/*     */   private boolean streamClosed;
/*     */   
/*     */   public ZipOutputStream(OutputStream outputStream) throws IOException {
/*  39 */     this(outputStream, null, InternalZipConstants.CHARSET_UTF_8);
/*     */   }
/*     */   
/*     */   public ZipOutputStream(OutputStream outputStream, Charset charset) throws IOException {
/*  43 */     this(outputStream, null, charset);
/*     */   }
/*     */   
/*     */   public ZipOutputStream(OutputStream outputStream, char[] password) throws IOException {
/*  47 */     this(outputStream, password, InternalZipConstants.CHARSET_UTF_8);
/*     */   }
/*     */   
/*     */   public ZipOutputStream(OutputStream outputStream, char[] password, Charset charset) throws IOException {
/*  51 */     this(outputStream, password, charset, new ZipModel());
/*     */   }
/*     */   
/*     */   public ZipOutputStream(OutputStream outputStream, char[] password, Charset charset, ZipModel zipModel) throws IOException {
/*  55 */     if (charset == null) {
/*  56 */       charset = InternalZipConstants.CHARSET_UTF_8;
/*     */     }
/*     */     
/*  59 */     this.countingOutputStream = new CountingOutputStream(outputStream);
/*  60 */     this.password = password;
/*  61 */     this.charset = charset;
/*  62 */     this.zipModel = initializeZipModel(zipModel, this.countingOutputStream);
/*  63 */     this.streamClosed = false;
/*  64 */     writeSplitZipHeaderIfApplicable();
/*     */   }
/*     */   
/*     */   public void putNextEntry(ZipParameters zipParameters) throws IOException {
/*  68 */     verifyZipParameters(zipParameters);
/*  69 */     initializeAndWriteFileHeader(zipParameters);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.compressedOutputStream = initializeCompressedOutputStream(zipParameters);
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException {
/*  78 */     write(new byte[] { (byte)b });
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  82 */     write(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  86 */     ensureStreamOpen();
/*  87 */     this.crc32.update(b, off, len);
/*  88 */     this.compressedOutputStream.write(b, off, len);
/*  89 */     this.uncompressedSizeForThisEntry += len;
/*     */   }
/*     */   
/*     */   public FileHeader closeEntry() throws IOException {
/*  93 */     this.compressedOutputStream.closeEntry();
/*     */     
/*  95 */     long compressedSize = this.compressedOutputStream.getCompressedSize();
/*  96 */     this.fileHeader.setCompressedSize(compressedSize);
/*  97 */     this.localFileHeader.setCompressedSize(compressedSize);
/*     */     
/*  99 */     this.fileHeader.setUncompressedSize(this.uncompressedSizeForThisEntry);
/* 100 */     this.localFileHeader.setUncompressedSize(this.uncompressedSizeForThisEntry);
/*     */     
/* 102 */     if (writeCrc(this.fileHeader)) {
/* 103 */       this.fileHeader.setCrc(this.crc32.getValue());
/* 104 */       this.localFileHeader.setCrc(this.crc32.getValue());
/*     */     } 
/*     */     
/* 107 */     this.zipModel.getLocalFileHeaders().add(this.localFileHeader);
/* 108 */     this.zipModel.getCentralDirectory().getFileHeaders().add(this.fileHeader);
/*     */     
/* 110 */     if (this.localFileHeader.isDataDescriptorExists()) {
/* 111 */       this.headerWriter.writeExtendedLocalHeader(this.localFileHeader, this.countingOutputStream);
/*     */     }
/* 113 */     reset();
/* 114 */     return this.fileHeader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 119 */     this.zipModel.getEndOfCentralDirectoryRecord().setOffsetOfStartOfCentralDirectory(this.countingOutputStream.getNumberOfBytesWritten());
/* 120 */     this.headerWriter.finalizeZipFile(this.zipModel, this.countingOutputStream, this.charset);
/* 121 */     this.countingOutputStream.close();
/* 122 */     this.streamClosed = true;
/*     */   }
/*     */   
/*     */   public void setComment(String comment) throws IOException {
/* 126 */     ensureStreamOpen();
/* 127 */     this.zipModel.getEndOfCentralDirectoryRecord().setComment(comment);
/*     */   }
/*     */   
/*     */   private void ensureStreamOpen() throws IOException {
/* 131 */     if (this.streamClosed) {
/* 132 */       throw new IOException("Stream is closed");
/*     */     }
/*     */   }
/*     */   
/*     */   private ZipModel initializeZipModel(ZipModel zipModel, CountingOutputStream countingOutputStream) {
/* 137 */     if (zipModel == null) {
/* 138 */       zipModel = new ZipModel();
/*     */     }
/*     */     
/* 141 */     if (countingOutputStream.isSplitZipFile()) {
/* 142 */       zipModel.setSplitArchive(true);
/* 143 */       zipModel.setSplitLength(countingOutputStream.getSplitLength());
/*     */     } 
/*     */     
/* 146 */     return zipModel;
/*     */   }
/*     */   
/*     */   private void initializeAndWriteFileHeader(ZipParameters zipParameters) throws IOException {
/* 150 */     this.fileHeader = this.fileHeaderFactory.generateFileHeader(zipParameters, this.countingOutputStream.isSplitZipFile(), this.countingOutputStream
/* 151 */         .getCurrentSplitFileCounter(), this.charset, this.rawIO);
/* 152 */     this.fileHeader.setOffsetLocalHeader(this.countingOutputStream.getOffsetForNextEntry());
/*     */     
/* 154 */     this.localFileHeader = this.fileHeaderFactory.generateLocalFileHeader(this.fileHeader);
/* 155 */     this.headerWriter.writeLocalFileHeader(this.zipModel, this.localFileHeader, this.countingOutputStream, this.charset);
/*     */   }
/*     */   
/*     */   private void reset() throws IOException {
/* 159 */     this.uncompressedSizeForThisEntry = 0L;
/* 160 */     this.crc32.reset();
/* 161 */     this.compressedOutputStream.close();
/*     */   }
/*     */   
/*     */   private void writeSplitZipHeaderIfApplicable() throws IOException {
/* 165 */     if (!this.countingOutputStream.isSplitZipFile()) {
/*     */       return;
/*     */     }
/*     */     
/* 169 */     this.rawIO.writeIntLittleEndian(this.countingOutputStream, (int)HeaderSignature.SPLIT_ZIP.getValue());
/*     */   }
/*     */   
/*     */   private CompressedOutputStream initializeCompressedOutputStream(ZipParameters zipParameters) throws IOException {
/* 173 */     ZipEntryOutputStream zipEntryOutputStream = new ZipEntryOutputStream(this.countingOutputStream);
/* 174 */     CipherOutputStream cipherOutputStream = initializeCipherOutputStream(zipEntryOutputStream, zipParameters);
/* 175 */     return initializeCompressedOutputStream(cipherOutputStream, zipParameters);
/*     */   }
/*     */ 
/*     */   
/*     */   private CipherOutputStream initializeCipherOutputStream(ZipEntryOutputStream zipEntryOutputStream, ZipParameters zipParameters) throws IOException {
/* 180 */     if (!zipParameters.isEncryptFiles()) {
/* 181 */       return new NoCipherOutputStream(zipEntryOutputStream, zipParameters, null);
/*     */     }
/*     */     
/* 184 */     if (this.password == null || this.password.length == 0) {
/* 185 */       throw new ZipException("password not set");
/*     */     }
/*     */     
/* 188 */     if (zipParameters.getEncryptionMethod() == EncryptionMethod.AES)
/* 189 */       return new AesCipherOutputStream(zipEntryOutputStream, zipParameters, this.password); 
/* 190 */     if (zipParameters.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD) {
/* 191 */       return new ZipStandardCipherOutputStream(zipEntryOutputStream, zipParameters, this.password);
/*     */     }
/* 193 */     throw new ZipException("Invalid encryption method");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CompressedOutputStream initializeCompressedOutputStream(CipherOutputStream cipherOutputStream, ZipParameters zipParameters) {
/* 199 */     if (zipParameters.getCompressionMethod() == CompressionMethod.DEFLATE) {
/* 200 */       return new DeflaterOutputStream(cipherOutputStream, zipParameters.getCompressionLevel());
/*     */     }
/*     */     
/* 203 */     return new StoreOutputStream(cipherOutputStream);
/*     */   }
/*     */   
/*     */   private void verifyZipParameters(ZipParameters zipParameters) {
/* 207 */     if (zipParameters.getCompressionMethod() == CompressionMethod.STORE && zipParameters
/* 208 */       .getEntrySize() < 0L && 
/* 209 */       !isEntryDirectory(zipParameters.getFileNameInZip()) && zipParameters
/* 210 */       .isWriteExtendedLocalFileHeader()) {
/* 211 */       throw new IllegalArgumentException("uncompressed size should be set for zip entries of compression type store");
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean writeCrc(FileHeader fileHeader) {
/* 216 */     boolean isAesEncrypted = (fileHeader.isEncrypted() && fileHeader.getEncryptionMethod().equals(EncryptionMethod.AES));
/*     */     
/* 218 */     if (!isAesEncrypted) {
/* 219 */       return true;
/*     */     }
/*     */     
/* 222 */     return fileHeader.getAesExtraDataRecord().getAesVersion().equals(AesVersion.ONE);
/*     */   }
/*     */   
/*     */   private boolean isEntryDirectory(String entryName) {
/* 226 */     return (entryName.endsWith("/") || entryName.endsWith("\\"));
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\ZipOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */