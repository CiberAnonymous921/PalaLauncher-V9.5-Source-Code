/*     */ package net.lingala.zip4j.headers;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.io.outputstream.CountingOutputStream;
/*     */ import net.lingala.zip4j.io.outputstream.OutputStreamWithSplitZipSupport;
/*     */ import net.lingala.zip4j.io.outputstream.SplitOutputStream;
/*     */ import net.lingala.zip4j.model.AESExtraDataRecord;
/*     */ import net.lingala.zip4j.model.ExtraDataRecord;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.LocalFileHeader;
/*     */ import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator;
/*     */ import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.util.FileUtils;
/*     */ import net.lingala.zip4j.util.RawIO;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HeaderWriter
/*     */ {
/*     */   private static final short ZIP64_EXTRA_DATA_RECORD_SIZE_LFH = 16;
/*     */   private static final short ZIP64_EXTRA_DATA_RECORD_SIZE_FH = 28;
/*     */   private static final short AES_EXTRA_DATA_RECORD_SIZE = 11;
/*  51 */   private RawIO rawIO = new RawIO();
/*  52 */   private byte[] longBuff = new byte[8];
/*  53 */   private byte[] intBuff = new byte[4];
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLocalFileHeader(ZipModel zipModel, LocalFileHeader localFileHeader, OutputStream outputStream, Charset charset) throws IOException {
/*  58 */     try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
/*  59 */       this.rawIO.writeIntLittleEndian(byteArrayOutputStream, (int)localFileHeader.getSignature().getValue());
/*  60 */       this.rawIO.writeShortLittleEndian(byteArrayOutputStream, localFileHeader.getVersionNeededToExtract());
/*  61 */       byteArrayOutputStream.write(localFileHeader.getGeneralPurposeFlag());
/*  62 */       this.rawIO.writeShortLittleEndian(byteArrayOutputStream, localFileHeader.getCompressionMethod().getCode());
/*     */       
/*  64 */       this.rawIO.writeLongLittleEndian(this.longBuff, 0, localFileHeader.getLastModifiedTime());
/*  65 */       byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */       
/*  67 */       this.rawIO.writeLongLittleEndian(this.longBuff, 0, localFileHeader.getCrc());
/*  68 */       byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */ 
/*     */       
/*  71 */       boolean writeZip64Header = (localFileHeader.getCompressedSize() >= 4294967295L || localFileHeader.getUncompressedSize() >= 4294967295L);
/*     */       
/*  73 */       if (writeZip64Header) {
/*  74 */         this.rawIO.writeLongLittleEndian(this.longBuff, 0, 4294967295L);
/*     */ 
/*     */ 
/*     */         
/*  78 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/*  79 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */         
/*  81 */         zipModel.setZip64Format(true);
/*  82 */         localFileHeader.setWriteCompressedSizeInZip64ExtraRecord(true);
/*     */       } else {
/*  84 */         this.rawIO.writeLongLittleEndian(this.longBuff, 0, localFileHeader.getCompressedSize());
/*  85 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */         
/*  87 */         this.rawIO.writeLongLittleEndian(this.longBuff, 0, localFileHeader.getUncompressedSize());
/*  88 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */         
/*  90 */         localFileHeader.setWriteCompressedSizeInZip64ExtraRecord(false);
/*     */       } 
/*     */       
/*  93 */       byte[] fileNameBytes = new byte[0];
/*  94 */       if (Zip4jUtil.isStringNotNullAndNotEmpty(localFileHeader.getFileName())) {
/*  95 */         fileNameBytes = localFileHeader.getFileName().getBytes(charset);
/*     */       }
/*  97 */       this.rawIO.writeShortLittleEndian(byteArrayOutputStream, fileNameBytes.length);
/*     */       
/*  99 */       int extraFieldLength = 0;
/* 100 */       if (writeZip64Header) {
/* 101 */         extraFieldLength += 20;
/*     */       }
/* 103 */       if (localFileHeader.getAesExtraDataRecord() != null) {
/* 104 */         extraFieldLength += 11;
/*     */       }
/* 106 */       this.rawIO.writeShortLittleEndian(byteArrayOutputStream, extraFieldLength);
/*     */       
/* 108 */       if (fileNameBytes.length > 0) {
/* 109 */         byteArrayOutputStream.write(fileNameBytes);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       if (writeZip64Header) {
/* 117 */         this.rawIO.writeShortLittleEndian(byteArrayOutputStream, 
/* 118 */             (int)HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue());
/* 119 */         this.rawIO.writeShortLittleEndian(byteArrayOutputStream, 16);
/* 120 */         this.rawIO.writeLongLittleEndian(byteArrayOutputStream, localFileHeader.getUncompressedSize());
/* 121 */         this.rawIO.writeLongLittleEndian(byteArrayOutputStream, localFileHeader.getCompressedSize());
/*     */       } 
/*     */       
/* 124 */       if (localFileHeader.getAesExtraDataRecord() != null) {
/* 125 */         AESExtraDataRecord aesExtraDataRecord = localFileHeader.getAesExtraDataRecord();
/* 126 */         this.rawIO.writeShortLittleEndian(byteArrayOutputStream, (int)aesExtraDataRecord.getSignature().getValue());
/* 127 */         this.rawIO.writeShortLittleEndian(byteArrayOutputStream, aesExtraDataRecord.getDataSize());
/* 128 */         this.rawIO.writeShortLittleEndian(byteArrayOutputStream, aesExtraDataRecord.getAesVersion().getVersionNumber());
/* 129 */         byteArrayOutputStream.write(aesExtraDataRecord.getVendorID().getBytes());
/*     */         
/* 131 */         byte[] aesStrengthBytes = new byte[1];
/* 132 */         aesStrengthBytes[0] = (byte)aesExtraDataRecord.getAesKeyStrength().getRawCode();
/* 133 */         byteArrayOutputStream.write(aesStrengthBytes);
/*     */         
/* 135 */         this.rawIO.writeShortLittleEndian(byteArrayOutputStream, aesExtraDataRecord.getCompressionMethod().getCode());
/*     */       } 
/*     */       
/* 138 */       outputStream.write(byteArrayOutputStream.toByteArray());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeExtendedLocalHeader(LocalFileHeader localFileHeader, OutputStream outputStream) throws IOException {
/* 145 */     if (localFileHeader == null || outputStream == null) {
/* 146 */       throw new ZipException("input parameters is null, cannot write extended local header");
/*     */     }
/*     */     
/* 149 */     try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
/* 150 */       this.rawIO.writeIntLittleEndian(byteArrayOutputStream, (int)HeaderSignature.EXTRA_DATA_RECORD.getValue());
/*     */       
/* 152 */       this.rawIO.writeLongLittleEndian(this.longBuff, 0, localFileHeader.getCrc());
/* 153 */       byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */       
/* 155 */       if (localFileHeader.isWriteCompressedSizeInZip64ExtraRecord()) {
/* 156 */         this.rawIO.writeLongLittleEndian(byteArrayOutputStream, localFileHeader.getCompressedSize());
/* 157 */         this.rawIO.writeLongLittleEndian(byteArrayOutputStream, localFileHeader.getUncompressedSize());
/*     */       } else {
/* 159 */         this.rawIO.writeLongLittleEndian(this.longBuff, 0, localFileHeader.getCompressedSize());
/* 160 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */         
/* 162 */         this.rawIO.writeLongLittleEndian(this.longBuff, 0, localFileHeader.getUncompressedSize());
/* 163 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */       } 
/*     */       
/* 166 */       outputStream.write(byteArrayOutputStream.toByteArray());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void finalizeZipFile(ZipModel zipModel, OutputStream outputStream, Charset charset) throws IOException {
/* 171 */     if (zipModel == null || outputStream == null) {
/* 172 */       throw new ZipException("input parameters is null, cannot finalize zip file");
/*     */     }
/*     */     
/* 175 */     try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
/* 176 */       processHeaderData(zipModel, outputStream);
/* 177 */       long offsetCentralDir = getOffsetOfCentralDirectory(zipModel);
/* 178 */       writeCentralDirectory(zipModel, byteArrayOutputStream, this.rawIO, charset);
/* 179 */       int sizeOfCentralDir = byteArrayOutputStream.size();
/*     */       
/* 181 */       if (zipModel.isZip64Format() || offsetCentralDir >= 4294967295L || zipModel
/* 182 */         .getCentralDirectory().getFileHeaders().size() >= 65535) {
/*     */         
/* 184 */         if (zipModel.getZip64EndOfCentralDirectoryRecord() == null) {
/* 185 */           zipModel.setZip64EndOfCentralDirectoryRecord(new Zip64EndOfCentralDirectoryRecord());
/*     */         }
/* 187 */         if (zipModel.getZip64EndOfCentralDirectoryLocator() == null) {
/* 188 */           zipModel.setZip64EndOfCentralDirectoryLocator(new Zip64EndOfCentralDirectoryLocator());
/*     */         }
/*     */         
/* 191 */         zipModel.getZip64EndOfCentralDirectoryLocator().setOffsetZip64EndOfCentralDirectoryRecord(offsetCentralDir + sizeOfCentralDir);
/*     */ 
/*     */         
/* 194 */         if (isSplitZipFile(outputStream)) {
/* 195 */           int currentSplitFileCounter = getCurrentSplitFileCounter(outputStream);
/* 196 */           zipModel.getZip64EndOfCentralDirectoryLocator().setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(currentSplitFileCounter);
/*     */           
/* 198 */           zipModel.getZip64EndOfCentralDirectoryLocator().setTotalNumberOfDiscs(currentSplitFileCounter + 1);
/*     */         } else {
/* 200 */           zipModel.getZip64EndOfCentralDirectoryLocator().setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(0);
/* 201 */           zipModel.getZip64EndOfCentralDirectoryLocator().setTotalNumberOfDiscs(1);
/*     */         } 
/*     */         
/* 204 */         writeZip64EndOfCentralDirectoryRecord(zipModel, sizeOfCentralDir, offsetCentralDir, byteArrayOutputStream, this.rawIO);
/*     */         
/* 206 */         writeZip64EndOfCentralDirectoryLocator(zipModel, byteArrayOutputStream, this.rawIO);
/*     */       } 
/*     */       
/* 209 */       writeEndOfCentralDirectoryRecord(zipModel, sizeOfCentralDir, offsetCentralDir, byteArrayOutputStream, this.rawIO, charset);
/* 210 */       writeZipHeaderBytes(zipModel, outputStream, byteArrayOutputStream.toByteArray(), charset);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void finalizeZipFileWithoutValidations(ZipModel zipModel, OutputStream outputStream, Charset charset) throws IOException {
/* 216 */     if (zipModel == null || outputStream == null) {
/* 217 */       throw new ZipException("input parameters is null, cannot finalize zip file without validations");
/*     */     }
/*     */     
/* 220 */     try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
/* 221 */       long offsetCentralDir = zipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory();
/* 222 */       writeCentralDirectory(zipModel, byteArrayOutputStream, this.rawIO, charset);
/* 223 */       int sizeOfCentralDir = byteArrayOutputStream.size();
/*     */       
/* 225 */       if (zipModel.isZip64Format() || offsetCentralDir >= 4294967295L || zipModel
/* 226 */         .getCentralDirectory().getFileHeaders().size() >= 65535) {
/*     */         
/* 228 */         if (zipModel.getZip64EndOfCentralDirectoryRecord() == null) {
/* 229 */           zipModel.setZip64EndOfCentralDirectoryRecord(new Zip64EndOfCentralDirectoryRecord());
/*     */         }
/* 231 */         if (zipModel.getZip64EndOfCentralDirectoryLocator() == null) {
/* 232 */           zipModel.setZip64EndOfCentralDirectoryLocator(new Zip64EndOfCentralDirectoryLocator());
/*     */         }
/*     */         
/* 235 */         zipModel.getZip64EndOfCentralDirectoryLocator().setOffsetZip64EndOfCentralDirectoryRecord(offsetCentralDir + sizeOfCentralDir);
/*     */ 
/*     */         
/* 238 */         writeZip64EndOfCentralDirectoryRecord(zipModel, sizeOfCentralDir, offsetCentralDir, byteArrayOutputStream, this.rawIO);
/*     */         
/* 240 */         writeZip64EndOfCentralDirectoryLocator(zipModel, byteArrayOutputStream, this.rawIO);
/*     */       } 
/*     */       
/* 243 */       writeEndOfCentralDirectoryRecord(zipModel, sizeOfCentralDir, offsetCentralDir, byteArrayOutputStream, this.rawIO, charset);
/* 244 */       writeZipHeaderBytes(zipModel, outputStream, byteArrayOutputStream.toByteArray(), charset);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLocalFileHeader(FileHeader fileHeader, ZipModel zipModel, SplitOutputStream outputStream) throws IOException {
/*     */     SplitOutputStream currOutputStream;
/* 251 */     if (fileHeader == null || zipModel == null) {
/* 252 */       throw new ZipException("invalid input parameters, cannot update local file header");
/*     */     }
/*     */     
/* 255 */     boolean closeFlag = false;
/*     */ 
/*     */     
/* 258 */     if (fileHeader.getDiskNumberStart() != outputStream.getCurrentSplitFileCounter()) {
/* 259 */       String parentFile = zipModel.getZipFile().getParent();
/* 260 */       String fileNameWithoutExt = FileUtils.getZipFileNameWithoutExtension(zipModel.getZipFile().getName());
/* 261 */       String fileName = parentFile + System.getProperty("file.separator");
/* 262 */       if (fileHeader.getDiskNumberStart() < 9) {
/* 263 */         fileName = fileName + fileNameWithoutExt + ".z0" + (fileHeader.getDiskNumberStart() + 1);
/*     */       } else {
/* 265 */         fileName = fileName + fileNameWithoutExt + ".z" + (fileHeader.getDiskNumberStart() + 1);
/*     */       } 
/* 267 */       currOutputStream = new SplitOutputStream(new File(fileName));
/* 268 */       closeFlag = true;
/*     */     } else {
/* 270 */       currOutputStream = outputStream;
/*     */     } 
/*     */     
/* 273 */     long currOffset = currOutputStream.getFilePointer();
/*     */     
/* 275 */     currOutputStream.seek(fileHeader.getOffsetLocalHeader() + 14L);
/* 276 */     this.rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getCrc());
/* 277 */     currOutputStream.write(this.longBuff, 0, 4);
/*     */     
/* 279 */     updateFileSizesInLocalFileHeader(currOutputStream, fileHeader);
/*     */     
/* 281 */     if (closeFlag) {
/* 282 */       currOutputStream.close();
/*     */     } else {
/* 284 */       outputStream.seek(currOffset);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateFileSizesInLocalFileHeader(SplitOutputStream outputStream, FileHeader fileHeader) throws IOException {
/* 291 */     if (fileHeader.getUncompressedSize() >= 4294967295L) {
/* 292 */       this.rawIO.writeLongLittleEndian(this.longBuff, 0, 4294967295L);
/* 293 */       outputStream.write(this.longBuff, 0, 4);
/* 294 */       outputStream.write(this.longBuff, 0, 4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 303 */       int zip64CompressedSizeOffset = 4 + fileHeader.getFileNameLength() + 2 + 2;
/* 304 */       if (outputStream.skipBytes(zip64CompressedSizeOffset) != zip64CompressedSizeOffset) {
/* 305 */         throw new ZipException("Unable to skip " + zip64CompressedSizeOffset + " bytes to update LFH");
/*     */       }
/* 307 */       this.rawIO.writeLongLittleEndian((OutputStream)outputStream, fileHeader.getUncompressedSize());
/* 308 */       this.rawIO.writeLongLittleEndian((OutputStream)outputStream, fileHeader.getCompressedSize());
/*     */     } else {
/* 310 */       this.rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getCompressedSize());
/* 311 */       outputStream.write(this.longBuff, 0, 4);
/*     */       
/* 313 */       this.rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getUncompressedSize());
/* 314 */       outputStream.write(this.longBuff, 0, 4);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isSplitZipFile(OutputStream outputStream) {
/* 319 */     if (outputStream instanceof SplitOutputStream)
/* 320 */       return ((SplitOutputStream)outputStream).isSplitZipFile(); 
/* 321 */     if (outputStream instanceof CountingOutputStream) {
/* 322 */       return ((CountingOutputStream)outputStream).isSplitZipFile();
/*     */     }
/*     */     
/* 325 */     return false;
/*     */   }
/*     */   
/*     */   private int getCurrentSplitFileCounter(OutputStream outputStream) {
/* 329 */     if (outputStream instanceof SplitOutputStream) {
/* 330 */       return ((SplitOutputStream)outputStream).getCurrentSplitFileCounter();
/*     */     }
/* 332 */     return ((CountingOutputStream)outputStream).getCurrentSplitFileCounter();
/*     */   }
/*     */   
/*     */   private void writeZipHeaderBytes(ZipModel zipModel, OutputStream outputStream, byte[] buff, Charset charset) throws IOException {
/* 336 */     if (buff == null) {
/* 337 */       throw new ZipException("invalid buff to write as zip headers");
/*     */     }
/*     */     
/* 340 */     if (outputStream instanceof CountingOutputStream && (
/* 341 */       (CountingOutputStream)outputStream).checkBuffSizeAndStartNextSplitFile(buff.length)) {
/* 342 */       finalizeZipFile(zipModel, outputStream, charset);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 347 */     outputStream.write(buff);
/*     */   }
/*     */   
/*     */   private void processHeaderData(ZipModel zipModel, OutputStream outputStream) throws IOException {
/* 351 */     int currentSplitFileCounter = 0;
/* 352 */     if (outputStream instanceof OutputStreamWithSplitZipSupport) {
/* 353 */       zipModel.getEndOfCentralDirectoryRecord().setOffsetOfStartOfCentralDirectory(((OutputStreamWithSplitZipSupport)outputStream)
/* 354 */           .getFilePointer());
/* 355 */       currentSplitFileCounter = ((OutputStreamWithSplitZipSupport)outputStream).getCurrentSplitFileCounter();
/*     */     } 
/*     */     
/* 358 */     if (zipModel.isZip64Format()) {
/* 359 */       if (zipModel.getZip64EndOfCentralDirectoryRecord() == null) {
/* 360 */         zipModel.setZip64EndOfCentralDirectoryRecord(new Zip64EndOfCentralDirectoryRecord());
/*     */       }
/* 362 */       if (zipModel.getZip64EndOfCentralDirectoryLocator() == null) {
/* 363 */         zipModel.setZip64EndOfCentralDirectoryLocator(new Zip64EndOfCentralDirectoryLocator());
/*     */       }
/*     */       
/* 366 */       zipModel.getZip64EndOfCentralDirectoryRecord().setOffsetStartCentralDirectoryWRTStartDiskNumber(zipModel
/* 367 */           .getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory());
/* 368 */       zipModel.getZip64EndOfCentralDirectoryLocator().setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(currentSplitFileCounter);
/*     */       
/* 370 */       zipModel.getZip64EndOfCentralDirectoryLocator().setTotalNumberOfDiscs(currentSplitFileCounter + 1);
/*     */     } 
/* 372 */     zipModel.getEndOfCentralDirectoryRecord().setNumberOfThisDisk(currentSplitFileCounter);
/* 373 */     zipModel.getEndOfCentralDirectoryRecord().setNumberOfThisDiskStartOfCentralDir(currentSplitFileCounter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeCentralDirectory(ZipModel zipModel, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO, Charset charset) throws ZipException {
/* 379 */     if (zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null || zipModel
/* 380 */       .getCentralDirectory().getFileHeaders().size() <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 384 */     for (FileHeader fileHeader : zipModel.getCentralDirectory().getFileHeaders()) {
/* 385 */       writeFileHeader(zipModel, fileHeader, byteArrayOutputStream, rawIO, charset);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeFileHeader(ZipModel zipModel, FileHeader fileHeader, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO, Charset charset) throws ZipException {
/* 391 */     if (fileHeader == null) {
/* 392 */       throw new ZipException("input parameters is null, cannot write local file header");
/*     */     }
/*     */     
/*     */     try {
/* 396 */       byte[] emptyShortByte = { 0, 0 };
/* 397 */       boolean writeZip64ExtendedInfo = isZip64Entry(fileHeader);
/*     */       
/* 399 */       rawIO.writeIntLittleEndian(byteArrayOutputStream, (int)fileHeader.getSignature().getValue());
/* 400 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, fileHeader.getVersionMadeBy());
/* 401 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, fileHeader.getVersionNeededToExtract());
/* 402 */       byteArrayOutputStream.write(fileHeader.getGeneralPurposeFlag());
/* 403 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, fileHeader.getCompressionMethod().getCode());
/*     */       
/* 405 */       rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getLastModifiedTime());
/* 406 */       byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */       
/* 408 */       rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getCrc());
/* 409 */       byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */       
/* 411 */       if (writeZip64ExtendedInfo) {
/* 412 */         rawIO.writeLongLittleEndian(this.longBuff, 0, 4294967295L);
/* 413 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/* 414 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/* 415 */         zipModel.setZip64Format(true);
/*     */       } else {
/* 417 */         rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getCompressedSize());
/* 418 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/* 419 */         rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getUncompressedSize());
/* 420 */         byteArrayOutputStream.write(this.longBuff, 0, 4);
/*     */       } 
/*     */       
/* 423 */       byte[] fileNameBytes = new byte[0];
/* 424 */       if (Zip4jUtil.isStringNotNullAndNotEmpty(fileHeader.getFileName())) {
/* 425 */         fileNameBytes = fileHeader.getFileName().getBytes(charset);
/*     */       }
/* 427 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, fileNameBytes.length);
/*     */ 
/*     */ 
/*     */       
/* 431 */       byte[] offsetLocalHeaderBytes = new byte[4];
/* 432 */       if (writeZip64ExtendedInfo) {
/* 433 */         rawIO.writeLongLittleEndian(this.longBuff, 0, 4294967295L);
/* 434 */         System.arraycopy(this.longBuff, 0, offsetLocalHeaderBytes, 0, 4);
/*     */       } else {
/* 436 */         rawIO.writeLongLittleEndian(this.longBuff, 0, fileHeader.getOffsetLocalHeader());
/* 437 */         System.arraycopy(this.longBuff, 0, offsetLocalHeaderBytes, 0, 4);
/*     */       } 
/*     */       
/* 440 */       int extraFieldLength = calculateExtraDataRecordsSize(fileHeader, writeZip64ExtendedInfo);
/* 441 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, extraFieldLength);
/*     */       
/* 443 */       String fileComment = fileHeader.getFileComment();
/* 444 */       byte[] fileCommentBytes = new byte[0];
/* 445 */       if (Zip4jUtil.isStringNotNullAndNotEmpty(fileComment)) {
/* 446 */         fileCommentBytes = fileComment.getBytes(charset);
/*     */       }
/* 448 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, fileCommentBytes.length);
/*     */       
/* 450 */       if (writeZip64ExtendedInfo) {
/* 451 */         rawIO.writeIntLittleEndian(this.intBuff, 0, 65535);
/* 452 */         byteArrayOutputStream.write(this.intBuff, 0, 2);
/*     */       } else {
/* 454 */         rawIO.writeShortLittleEndian(byteArrayOutputStream, fileHeader.getDiskNumberStart());
/*     */       } 
/*     */       
/* 457 */       byteArrayOutputStream.write(emptyShortByte);
/*     */ 
/*     */       
/* 460 */       byteArrayOutputStream.write(fileHeader.getExternalFileAttributes());
/*     */ 
/*     */       
/* 463 */       byteArrayOutputStream.write(offsetLocalHeaderBytes);
/*     */       
/* 465 */       if (fileNameBytes.length > 0) {
/* 466 */         byteArrayOutputStream.write(fileNameBytes);
/*     */       }
/*     */       
/* 469 */       if (writeZip64ExtendedInfo) {
/* 470 */         zipModel.setZip64Format(true);
/*     */ 
/*     */         
/* 473 */         rawIO.writeShortLittleEndian(byteArrayOutputStream, 
/* 474 */             (int)HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue());
/*     */ 
/*     */         
/* 477 */         rawIO.writeShortLittleEndian(byteArrayOutputStream, 28);
/* 478 */         rawIO.writeLongLittleEndian(byteArrayOutputStream, fileHeader.getUncompressedSize());
/* 479 */         rawIO.writeLongLittleEndian(byteArrayOutputStream, fileHeader.getCompressedSize());
/* 480 */         rawIO.writeLongLittleEndian(byteArrayOutputStream, fileHeader.getOffsetLocalHeader());
/* 481 */         rawIO.writeIntLittleEndian(byteArrayOutputStream, fileHeader.getDiskNumberStart());
/*     */       } 
/*     */       
/* 484 */       if (fileHeader.getAesExtraDataRecord() != null) {
/* 485 */         AESExtraDataRecord aesExtraDataRecord = fileHeader.getAesExtraDataRecord();
/* 486 */         rawIO.writeShortLittleEndian(byteArrayOutputStream, (int)aesExtraDataRecord.getSignature().getValue());
/* 487 */         rawIO.writeShortLittleEndian(byteArrayOutputStream, aesExtraDataRecord.getDataSize());
/* 488 */         rawIO.writeShortLittleEndian(byteArrayOutputStream, aesExtraDataRecord.getAesVersion().getVersionNumber());
/* 489 */         byteArrayOutputStream.write(aesExtraDataRecord.getVendorID().getBytes());
/*     */         
/* 491 */         byte[] aesStrengthBytes = new byte[1];
/* 492 */         aesStrengthBytes[0] = (byte)aesExtraDataRecord.getAesKeyStrength().getRawCode();
/* 493 */         byteArrayOutputStream.write(aesStrengthBytes);
/*     */         
/* 495 */         rawIO.writeShortLittleEndian(byteArrayOutputStream, aesExtraDataRecord.getCompressionMethod().getCode());
/*     */       } 
/*     */       
/* 498 */       writeRemainingExtraDataRecordsIfPresent(fileHeader, byteArrayOutputStream);
/*     */       
/* 500 */       if (fileCommentBytes.length > 0) {
/* 501 */         byteArrayOutputStream.write(fileCommentBytes);
/*     */       }
/* 503 */     } catch (Exception e) {
/* 504 */       throw new ZipException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int calculateExtraDataRecordsSize(FileHeader fileHeader, boolean writeZip64ExtendedInfo) throws IOException {
/* 509 */     int extraFieldLength = 0;
/*     */     
/* 511 */     if (writeZip64ExtendedInfo) {
/* 512 */       extraFieldLength += 32;
/*     */     }
/*     */     
/* 515 */     if (fileHeader.getAesExtraDataRecord() != null) {
/* 516 */       extraFieldLength += 11;
/*     */     }
/*     */     
/* 519 */     if (fileHeader.getExtraDataRecords() != null) {
/* 520 */       for (ExtraDataRecord extraDataRecord : fileHeader.getExtraDataRecords()) {
/* 521 */         if (extraDataRecord.getHeader() == HeaderSignature.AES_EXTRA_DATA_RECORD.getValue() || extraDataRecord
/* 522 */           .getHeader() == HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue()) {
/*     */           continue;
/*     */         }
/*     */         
/* 526 */         extraFieldLength += 4 + extraDataRecord.getSizeOfData();
/*     */       } 
/*     */     }
/*     */     
/* 530 */     return extraFieldLength;
/*     */   }
/*     */   
/*     */   private void writeRemainingExtraDataRecordsIfPresent(FileHeader fileHeader, OutputStream outputStream) throws IOException {
/* 534 */     if (fileHeader.getExtraDataRecords() == null || fileHeader.getExtraDataRecords().size() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 538 */     for (ExtraDataRecord extraDataRecord : fileHeader.getExtraDataRecords()) {
/* 539 */       if (extraDataRecord.getHeader() == HeaderSignature.AES_EXTRA_DATA_RECORD.getValue() || extraDataRecord
/* 540 */         .getHeader() == HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 544 */       this.rawIO.writeShortLittleEndian(outputStream, (int)extraDataRecord.getHeader());
/* 545 */       this.rawIO.writeShortLittleEndian(outputStream, extraDataRecord.getSizeOfData());
/*     */       
/* 547 */       if (extraDataRecord.getSizeOfData() > 0 && extraDataRecord.getData() != null) {
/* 548 */         outputStream.write(extraDataRecord.getData());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeZip64EndOfCentralDirectoryRecord(ZipModel zipModel, int sizeOfCentralDir, long offsetCentralDir, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO) throws IOException {
/* 557 */     byte[] emptyShortByte = { 0, 0 };
/*     */     
/* 559 */     rawIO.writeIntLittleEndian(byteArrayOutputStream, 
/* 560 */         (int)HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_RECORD.getValue());
/* 561 */     rawIO.writeLongLittleEndian(byteArrayOutputStream, 44L);
/*     */     
/* 563 */     if (zipModel.getCentralDirectory() != null && zipModel
/* 564 */       .getCentralDirectory().getFileHeaders() != null && zipModel
/* 565 */       .getCentralDirectory().getFileHeaders().size() > 0) {
/* 566 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, ((FileHeader)zipModel
/* 567 */           .getCentralDirectory().getFileHeaders().get(0)).getVersionMadeBy());
/*     */       
/* 569 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, ((FileHeader)zipModel
/* 570 */           .getCentralDirectory().getFileHeaders().get(0)).getVersionNeededToExtract());
/*     */     } else {
/* 572 */       byteArrayOutputStream.write(emptyShortByte);
/* 573 */       byteArrayOutputStream.write(emptyShortByte);
/*     */     } 
/*     */     
/* 576 */     rawIO.writeIntLittleEndian(byteArrayOutputStream, zipModel
/* 577 */         .getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
/* 578 */     rawIO.writeIntLittleEndian(byteArrayOutputStream, zipModel.getEndOfCentralDirectoryRecord()
/* 579 */         .getNumberOfThisDiskStartOfCentralDir());
/*     */     
/* 581 */     long numEntries = zipModel.getCentralDirectory().getFileHeaders().size();
/* 582 */     long numEntriesOnThisDisk = numEntries;
/* 583 */     if (zipModel.isSplitArchive()) {
/* 584 */       numEntriesOnThisDisk = countNumberOfFileHeaderEntriesOnDisk(zipModel.getCentralDirectory().getFileHeaders(), zipModel
/* 585 */           .getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
/*     */     }
/*     */     
/* 588 */     rawIO.writeLongLittleEndian(byteArrayOutputStream, numEntriesOnThisDisk);
/* 589 */     rawIO.writeLongLittleEndian(byteArrayOutputStream, numEntries);
/* 590 */     rawIO.writeLongLittleEndian(byteArrayOutputStream, sizeOfCentralDir);
/* 591 */     rawIO.writeLongLittleEndian(byteArrayOutputStream, offsetCentralDir);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeZip64EndOfCentralDirectoryLocator(ZipModel zipModel, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO) throws IOException {
/* 596 */     rawIO.writeIntLittleEndian(byteArrayOutputStream, (int)HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_LOCATOR.getValue());
/* 597 */     rawIO.writeIntLittleEndian(byteArrayOutputStream, zipModel
/* 598 */         .getZip64EndOfCentralDirectoryLocator().getNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord());
/* 599 */     rawIO.writeLongLittleEndian(byteArrayOutputStream, zipModel
/* 600 */         .getZip64EndOfCentralDirectoryLocator().getOffsetZip64EndOfCentralDirectoryRecord());
/* 601 */     rawIO.writeIntLittleEndian(byteArrayOutputStream, zipModel
/* 602 */         .getZip64EndOfCentralDirectoryLocator().getTotalNumberOfDiscs());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeEndOfCentralDirectoryRecord(ZipModel zipModel, int sizeOfCentralDir, long offsetCentralDir, ByteArrayOutputStream byteArrayOutputStream, RawIO rawIO, Charset charset) throws IOException {
/* 610 */     byte[] longByte = new byte[8];
/* 611 */     rawIO.writeIntLittleEndian(byteArrayOutputStream, (int)HeaderSignature.END_OF_CENTRAL_DIRECTORY.getValue());
/* 612 */     rawIO.writeShortLittleEndian(byteArrayOutputStream, zipModel
/* 613 */         .getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
/* 614 */     rawIO.writeShortLittleEndian(byteArrayOutputStream, zipModel
/* 615 */         .getEndOfCentralDirectoryRecord().getNumberOfThisDiskStartOfCentralDir());
/*     */     
/* 617 */     long numEntries = zipModel.getCentralDirectory().getFileHeaders().size();
/* 618 */     long numEntriesOnThisDisk = numEntries;
/* 619 */     if (zipModel.isSplitArchive()) {
/* 620 */       numEntriesOnThisDisk = countNumberOfFileHeaderEntriesOnDisk(zipModel.getCentralDirectory().getFileHeaders(), zipModel
/* 621 */           .getEndOfCentralDirectoryRecord().getNumberOfThisDisk());
/*     */     }
/*     */     
/* 624 */     if (numEntriesOnThisDisk > 65535L) {
/* 625 */       numEntriesOnThisDisk = 65535L;
/*     */     }
/* 627 */     rawIO.writeShortLittleEndian(byteArrayOutputStream, (int)numEntriesOnThisDisk);
/*     */     
/* 629 */     if (numEntries > 65535L) {
/* 630 */       numEntries = 65535L;
/*     */     }
/* 632 */     rawIO.writeShortLittleEndian(byteArrayOutputStream, (int)numEntries);
/*     */     
/* 634 */     rawIO.writeIntLittleEndian(byteArrayOutputStream, sizeOfCentralDir);
/* 635 */     if (offsetCentralDir > 4294967295L) {
/* 636 */       rawIO.writeLongLittleEndian(longByte, 0, 4294967295L);
/* 637 */       byteArrayOutputStream.write(longByte, 0, 4);
/*     */     } else {
/* 639 */       rawIO.writeLongLittleEndian(longByte, 0, offsetCentralDir);
/* 640 */       byteArrayOutputStream.write(longByte, 0, 4);
/*     */     } 
/*     */     
/* 643 */     String comment = zipModel.getEndOfCentralDirectoryRecord().getComment();
/* 644 */     if (Zip4jUtil.isStringNotNullAndNotEmpty(comment)) {
/* 645 */       byte[] commentBytes = comment.getBytes(charset);
/* 646 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, commentBytes.length);
/* 647 */       byteArrayOutputStream.write(commentBytes);
/*     */     } else {
/* 649 */       rawIO.writeShortLittleEndian(byteArrayOutputStream, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private long countNumberOfFileHeaderEntriesOnDisk(List<FileHeader> fileHeaders, int numOfDisk) throws ZipException {
/* 654 */     if (fileHeaders == null) {
/* 655 */       throw new ZipException("file headers are null, cannot calculate number of entries on this disk");
/*     */     }
/*     */     
/* 658 */     int noEntries = 0;
/* 659 */     for (FileHeader fileHeader : fileHeaders) {
/* 660 */       if (fileHeader.getDiskNumberStart() == numOfDisk) {
/* 661 */         noEntries++;
/*     */       }
/*     */     } 
/* 664 */     return noEntries;
/*     */   }
/*     */   
/*     */   private boolean isZip64Entry(FileHeader fileHeader) {
/* 668 */     return (fileHeader.getCompressedSize() >= 4294967295L || fileHeader
/* 669 */       .getUncompressedSize() >= 4294967295L || fileHeader
/* 670 */       .getOffsetLocalHeader() >= 4294967295L || fileHeader
/* 671 */       .getDiskNumberStart() >= 65535);
/*     */   }
/*     */   
/*     */   private long getOffsetOfCentralDirectory(ZipModel zipModel) {
/* 675 */     if (zipModel.isZip64Format() && zipModel
/* 676 */       .getZip64EndOfCentralDirectoryRecord() != null && zipModel
/* 677 */       .getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber() != -1L) {
/* 678 */       return zipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber();
/*     */     }
/*     */     
/* 681 */     return zipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory();
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\headers\HeaderWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */