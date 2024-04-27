/*     */ package net.lingala.zip4j.headers;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.io.inputstream.NumberedSplitRandomAccessFile;
/*     */ import net.lingala.zip4j.model.AESExtraDataRecord;
/*     */ import net.lingala.zip4j.model.CentralDirectory;
/*     */ import net.lingala.zip4j.model.DataDescriptor;
/*     */ import net.lingala.zip4j.model.DigitalSignature;
/*     */ import net.lingala.zip4j.model.EndOfCentralDirectoryRecord;
/*     */ import net.lingala.zip4j.model.ExtraDataRecord;
/*     */ import net.lingala.zip4j.model.FileHeader;
/*     */ import net.lingala.zip4j.model.LocalFileHeader;
/*     */ import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator;
/*     */ import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord;
/*     */ import net.lingala.zip4j.model.Zip64ExtendedInfo;
/*     */ import net.lingala.zip4j.model.ZipModel;
/*     */ import net.lingala.zip4j.model.enums.AesKeyStrength;
/*     */ import net.lingala.zip4j.model.enums.AesVersion;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*     */ import net.lingala.zip4j.model.enums.EncryptionMethod;
/*     */ import net.lingala.zip4j.util.BitUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HeaderReader
/*     */ {
/*     */   private ZipModel zipModel;
/*  62 */   private RawIO rawIO = new RawIO();
/*  63 */   private byte[] intBuff = new byte[4];
/*     */ 
/*     */   
/*     */   public ZipModel readAllHeaders(RandomAccessFile zip4jRaf, Charset charset) throws IOException {
/*  67 */     if (zip4jRaf.length() < 22L) {
/*  68 */       throw new ZipException("Zip file size less than minimum expected zip file size. Probably not a zip file or a corrupted zip file");
/*     */     }
/*     */ 
/*     */     
/*  72 */     this.zipModel = new ZipModel();
/*     */     
/*     */     try {
/*  75 */       this.zipModel.setEndOfCentralDirectoryRecord(readEndOfCentralDirectoryRecord(zip4jRaf, this.rawIO, charset));
/*  76 */     } catch (ZipException e) {
/*  77 */       throw e;
/*  78 */     } catch (IOException e) {
/*  79 */       throw new ZipException("Zip headers not found. Probably not a zip file or a corrupted zip file", e);
/*     */     } 
/*     */     
/*  82 */     if (this.zipModel.getEndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory() == 0) {
/*  83 */       return this.zipModel;
/*     */     }
/*     */ 
/*     */     
/*  87 */     this.zipModel.setZip64EndOfCentralDirectoryLocator(readZip64EndOfCentralDirectoryLocator(zip4jRaf, this.rawIO, this.zipModel
/*  88 */           .getEndOfCentralDirectoryRecord().getOffsetOfEndOfCentralDirectory()));
/*     */     
/*  90 */     if (this.zipModel.isZip64Format()) {
/*  91 */       this.zipModel.setZip64EndOfCentralDirectoryRecord(readZip64EndCentralDirRec(zip4jRaf, this.rawIO));
/*  92 */       if (this.zipModel.getZip64EndOfCentralDirectoryRecord() != null && this.zipModel
/*  93 */         .getZip64EndOfCentralDirectoryRecord().getNumberOfThisDisk() > 0) {
/*  94 */         this.zipModel.setSplitArchive(true);
/*     */       } else {
/*  96 */         this.zipModel.setSplitArchive(false);
/*     */       } 
/*     */     } 
/*     */     
/* 100 */     this.zipModel.setCentralDirectory(readCentralDirectory(zip4jRaf, this.rawIO, charset));
/*     */     
/* 102 */     return this.zipModel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private EndOfCentralDirectoryRecord readEndOfCentralDirectoryRecord(RandomAccessFile zip4jRaf, RawIO rawIO, Charset charset) throws IOException {
/* 108 */     long offsetEndOfCentralDirectory = zip4jRaf.length() - 22L;
/* 109 */     seekInCurrentPart(zip4jRaf, offsetEndOfCentralDirectory);
/* 110 */     int headerSignature = rawIO.readIntLittleEndian(zip4jRaf);
/*     */     
/* 112 */     if (headerSignature != HeaderSignature.END_OF_CENTRAL_DIRECTORY.getValue()) {
/* 113 */       offsetEndOfCentralDirectory = determineOffsetOfEndOfCentralDirectory(zip4jRaf);
/* 114 */       zip4jRaf.seek(offsetEndOfCentralDirectory + 4L);
/*     */     } 
/*     */     
/* 117 */     EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = new EndOfCentralDirectoryRecord();
/* 118 */     endOfCentralDirectoryRecord.setSignature(HeaderSignature.END_OF_CENTRAL_DIRECTORY);
/* 119 */     endOfCentralDirectoryRecord.setNumberOfThisDisk(rawIO.readShortLittleEndian(zip4jRaf));
/* 120 */     endOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDir(rawIO.readShortLittleEndian(zip4jRaf));
/* 121 */     endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(rawIO
/* 122 */         .readShortLittleEndian(zip4jRaf));
/* 123 */     endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(rawIO.readShortLittleEndian(zip4jRaf));
/* 124 */     endOfCentralDirectoryRecord.setSizeOfCentralDirectory(rawIO.readIntLittleEndian(zip4jRaf));
/* 125 */     endOfCentralDirectoryRecord.setOffsetOfEndOfCentralDirectory(offsetEndOfCentralDirectory);
/*     */     
/* 127 */     zip4jRaf.readFully(this.intBuff);
/* 128 */     endOfCentralDirectoryRecord.setOffsetOfStartOfCentralDirectory(rawIO.readLongLittleEndian(this.intBuff, 0));
/*     */     
/* 130 */     int commentLength = rawIO.readShortLittleEndian(zip4jRaf);
/* 131 */     endOfCentralDirectoryRecord.setComment(readZipComment(zip4jRaf, commentLength, charset));
/*     */     
/* 133 */     this.zipModel.setSplitArchive((endOfCentralDirectoryRecord.getNumberOfThisDisk() > 0));
/* 134 */     return endOfCentralDirectoryRecord;
/*     */   }
/*     */   
/*     */   private CentralDirectory readCentralDirectory(RandomAccessFile zip4jRaf, RawIO rawIO, Charset charset) throws IOException {
/* 138 */     CentralDirectory centralDirectory = new CentralDirectory();
/* 139 */     List<FileHeader> fileHeaders = new ArrayList<>();
/*     */     
/* 141 */     long offSetStartCentralDir = HeaderUtil.getOffsetStartOfCentralDirectory(this.zipModel);
/* 142 */     long centralDirEntryCount = getNumberOfEntriesInCentralDirectory(this.zipModel);
/*     */     
/* 144 */     zip4jRaf.seek(offSetStartCentralDir);
/*     */     
/* 146 */     byte[] shortBuff = new byte[2];
/* 147 */     byte[] intBuff = new byte[4];
/*     */     
/* 149 */     for (int i = 0; i < centralDirEntryCount; i++) {
/* 150 */       FileHeader fileHeader = new FileHeader();
/* 151 */       if (rawIO.readIntLittleEndian(zip4jRaf) != HeaderSignature.CENTRAL_DIRECTORY.getValue()) {
/* 152 */         throw new ZipException("Expected central directory entry not found (#" + (i + 1) + ")");
/*     */       }
/* 154 */       fileHeader.setSignature(HeaderSignature.CENTRAL_DIRECTORY);
/* 155 */       fileHeader.setVersionMadeBy(rawIO.readShortLittleEndian(zip4jRaf));
/* 156 */       fileHeader.setVersionNeededToExtract(rawIO.readShortLittleEndian(zip4jRaf));
/*     */       
/* 158 */       byte[] generalPurposeFlags = new byte[2];
/* 159 */       zip4jRaf.readFully(generalPurposeFlags);
/* 160 */       fileHeader.setEncrypted(BitUtils.isBitSet(generalPurposeFlags[0], 0));
/* 161 */       fileHeader.setDataDescriptorExists(BitUtils.isBitSet(generalPurposeFlags[0], 3));
/* 162 */       fileHeader.setFileNameUTF8Encoded(BitUtils.isBitSet(generalPurposeFlags[1], 3));
/* 163 */       fileHeader.setGeneralPurposeFlag((byte[])generalPurposeFlags.clone());
/*     */       
/* 165 */       fileHeader.setCompressionMethod(CompressionMethod.getCompressionMethodFromCode(rawIO.readShortLittleEndian(zip4jRaf)));
/*     */       
/* 167 */       fileHeader.setLastModifiedTime(rawIO.readIntLittleEndian(zip4jRaf));
/*     */       
/* 169 */       zip4jRaf.readFully(intBuff);
/* 170 */       fileHeader.setCrc(rawIO.readLongLittleEndian(intBuff, 0));
/* 171 */       fileHeader.setCrcRawData(intBuff);
/*     */       
/* 173 */       fileHeader.setCompressedSize(rawIO.readLongLittleEndian(zip4jRaf, 4));
/* 174 */       fileHeader.setUncompressedSize(rawIO.readLongLittleEndian(zip4jRaf, 4));
/*     */       
/* 176 */       int fileNameLength = rawIO.readShortLittleEndian(zip4jRaf);
/* 177 */       fileHeader.setFileNameLength(fileNameLength);
/*     */       
/* 179 */       fileHeader.setExtraFieldLength(rawIO.readShortLittleEndian(zip4jRaf));
/*     */       
/* 181 */       int fileCommentLength = rawIO.readShortLittleEndian(zip4jRaf);
/* 182 */       fileHeader.setFileCommentLength(fileCommentLength);
/*     */       
/* 184 */       fileHeader.setDiskNumberStart(rawIO.readShortLittleEndian(zip4jRaf));
/*     */       
/* 186 */       zip4jRaf.readFully(shortBuff);
/* 187 */       fileHeader.setInternalFileAttributes((byte[])shortBuff.clone());
/*     */       
/* 189 */       zip4jRaf.readFully(intBuff);
/* 190 */       fileHeader.setExternalFileAttributes((byte[])intBuff.clone());
/*     */       
/* 192 */       zip4jRaf.readFully(intBuff);
/* 193 */       fileHeader.setOffsetLocalHeader(rawIO.readLongLittleEndian(intBuff, 0));
/*     */       
/* 195 */       if (fileNameLength > 0) {
/* 196 */         byte[] fileNameBuff = new byte[fileNameLength];
/* 197 */         zip4jRaf.readFully(fileNameBuff);
/* 198 */         String fileName = HeaderUtil.decodeStringWithCharset(fileNameBuff, fileHeader.isFileNameUTF8Encoded(), charset);
/*     */         
/* 200 */         if (fileName.contains(":\\")) {
/* 201 */           fileName = fileName.substring(fileName.indexOf(":\\") + 2);
/*     */         }
/*     */         
/* 204 */         fileHeader.setFileName(fileName);
/* 205 */         fileHeader.setDirectory((fileName.endsWith("/") || fileName.endsWith("\\")));
/*     */       } else {
/* 207 */         fileHeader.setFileName(null);
/*     */       } 
/*     */       
/* 210 */       readExtraDataRecords(zip4jRaf, fileHeader);
/* 211 */       readZip64ExtendedInfo(fileHeader, rawIO);
/* 212 */       readAesExtraDataRecord(fileHeader, rawIO);
/*     */       
/* 214 */       if (fileCommentLength > 0) {
/* 215 */         byte[] fileCommentBuff = new byte[fileCommentLength];
/* 216 */         zip4jRaf.readFully(fileCommentBuff);
/* 217 */         fileHeader.setFileComment(HeaderUtil.decodeStringWithCharset(fileCommentBuff, fileHeader.isFileNameUTF8Encoded(), charset));
/*     */       } 
/*     */       
/* 220 */       if (fileHeader.isEncrypted()) {
/* 221 */         if (fileHeader.getAesExtraDataRecord() != null) {
/* 222 */           fileHeader.setEncryptionMethod(EncryptionMethod.AES);
/*     */         } else {
/* 224 */           fileHeader.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
/*     */         } 
/*     */       }
/*     */       
/* 228 */       fileHeaders.add(fileHeader);
/*     */     } 
/*     */     
/* 231 */     centralDirectory.setFileHeaders(fileHeaders);
/*     */     
/* 233 */     DigitalSignature digitalSignature = new DigitalSignature();
/* 234 */     if (rawIO.readIntLittleEndian(zip4jRaf) == HeaderSignature.DIGITAL_SIGNATURE.getValue()) {
/* 235 */       digitalSignature.setSignature(HeaderSignature.DIGITAL_SIGNATURE);
/* 236 */       digitalSignature.setSizeOfData(rawIO.readShortLittleEndian(zip4jRaf));
/*     */       
/* 238 */       if (digitalSignature.getSizeOfData() > 0) {
/* 239 */         byte[] signatureDataBuff = new byte[digitalSignature.getSizeOfData()];
/* 240 */         zip4jRaf.readFully(signatureDataBuff);
/* 241 */         digitalSignature.setSignatureData(new String(signatureDataBuff));
/*     */       } 
/*     */     } 
/*     */     
/* 245 */     return centralDirectory;
/*     */   }
/*     */ 
/*     */   
/*     */   private void readExtraDataRecords(RandomAccessFile zip4jRaf, FileHeader fileHeader) throws IOException {
/* 250 */     int extraFieldLength = fileHeader.getExtraFieldLength();
/* 251 */     if (extraFieldLength <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 255 */     fileHeader.setExtraDataRecords(readExtraDataRecords(zip4jRaf, extraFieldLength));
/*     */   }
/*     */ 
/*     */   
/*     */   private void readExtraDataRecords(InputStream inputStream, LocalFileHeader localFileHeader) throws IOException {
/* 260 */     int extraFieldLength = localFileHeader.getExtraFieldLength();
/* 261 */     if (extraFieldLength <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 265 */     localFileHeader.setExtraDataRecords(readExtraDataRecords(inputStream, extraFieldLength));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<ExtraDataRecord> readExtraDataRecords(RandomAccessFile zip4jRaf, int extraFieldLength) throws IOException {
/* 272 */     if (extraFieldLength < 4) {
/* 273 */       if (extraFieldLength > 0) {
/* 274 */         zip4jRaf.skipBytes(extraFieldLength);
/*     */       }
/*     */       
/* 277 */       return null;
/*     */     } 
/*     */     
/* 280 */     byte[] extraFieldBuf = new byte[extraFieldLength];
/* 281 */     zip4jRaf.read(extraFieldBuf);
/*     */     
/*     */     try {
/* 284 */       return parseExtraDataRecords(extraFieldBuf, extraFieldLength);
/* 285 */     } catch (Exception e) {
/*     */       
/* 287 */       return Collections.emptyList();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<ExtraDataRecord> readExtraDataRecords(InputStream inputStream, int extraFieldLength) throws IOException {
/* 294 */     if (extraFieldLength < 4) {
/* 295 */       if (extraFieldLength > 0) {
/* 296 */         inputStream.skip(extraFieldLength);
/*     */       }
/*     */       
/* 299 */       return null;
/*     */     } 
/*     */     
/* 302 */     byte[] extraFieldBuf = new byte[extraFieldLength];
/* 303 */     Zip4jUtil.readFully(inputStream, extraFieldBuf);
/*     */     
/*     */     try {
/* 306 */       return parseExtraDataRecords(extraFieldBuf, extraFieldLength);
/* 307 */     } catch (Exception e) {
/*     */       
/* 309 */       return Collections.emptyList();
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<ExtraDataRecord> parseExtraDataRecords(byte[] extraFieldBuf, int extraFieldLength) {
/* 314 */     int counter = 0;
/* 315 */     List<ExtraDataRecord> extraDataRecords = new ArrayList<>();
/* 316 */     while (counter < extraFieldLength) {
/* 317 */       ExtraDataRecord extraDataRecord = new ExtraDataRecord();
/* 318 */       int header = this.rawIO.readShortLittleEndian(extraFieldBuf, counter);
/* 319 */       extraDataRecord.setHeader(header);
/* 320 */       counter += 2;
/*     */       
/* 322 */       int sizeOfRec = this.rawIO.readShortLittleEndian(extraFieldBuf, counter);
/* 323 */       extraDataRecord.setSizeOfData(sizeOfRec);
/* 324 */       counter += 2;
/*     */       
/* 326 */       if (sizeOfRec > 0) {
/* 327 */         byte[] data = new byte[sizeOfRec];
/* 328 */         System.arraycopy(extraFieldBuf, counter, data, 0, sizeOfRec);
/* 329 */         extraDataRecord.setData(data);
/*     */       } 
/* 331 */       counter += sizeOfRec;
/* 332 */       extraDataRecords.add(extraDataRecord);
/*     */     } 
/* 334 */     return (extraDataRecords.size() > 0) ? extraDataRecords : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Zip64EndOfCentralDirectoryLocator readZip64EndOfCentralDirectoryLocator(RandomAccessFile zip4jRaf, RawIO rawIO, long offsetEndOfCentralDirectoryRecord) throws IOException {
/* 340 */     Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator = new Zip64EndOfCentralDirectoryLocator();
/*     */     
/* 342 */     setFilePointerToReadZip64EndCentralDirLoc(zip4jRaf, offsetEndOfCentralDirectoryRecord);
/*     */     
/* 344 */     int signature = rawIO.readIntLittleEndian(zip4jRaf);
/* 345 */     if (signature == HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_LOCATOR.getValue()) {
/* 346 */       this.zipModel.setZip64Format(true);
/* 347 */       zip64EndOfCentralDirectoryLocator.setSignature(HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_LOCATOR);
/*     */     } else {
/* 349 */       this.zipModel.setZip64Format(false);
/* 350 */       return null;
/*     */     } 
/*     */     
/* 353 */     zip64EndOfCentralDirectoryLocator.setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(rawIO
/* 354 */         .readIntLittleEndian(zip4jRaf));
/* 355 */     zip64EndOfCentralDirectoryLocator.setOffsetZip64EndOfCentralDirectoryRecord(rawIO
/* 356 */         .readLongLittleEndian(zip4jRaf));
/* 357 */     zip64EndOfCentralDirectoryLocator.setTotalNumberOfDiscs(rawIO.readIntLittleEndian(zip4jRaf));
/*     */     
/* 359 */     return zip64EndOfCentralDirectoryLocator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Zip64EndOfCentralDirectoryRecord readZip64EndCentralDirRec(RandomAccessFile zip4jRaf, RawIO rawIO) throws IOException {
/* 365 */     if (this.zipModel.getZip64EndOfCentralDirectoryLocator() == null) {
/* 366 */       throw new ZipException("invalid zip64 end of central directory locator");
/*     */     }
/*     */ 
/*     */     
/* 370 */     long offSetStartOfZip64CentralDir = this.zipModel.getZip64EndOfCentralDirectoryLocator().getOffsetZip64EndOfCentralDirectoryRecord();
/*     */     
/* 372 */     if (offSetStartOfZip64CentralDir < 0L) {
/* 373 */       throw new ZipException("invalid offset for start of end of central directory record");
/*     */     }
/*     */     
/* 376 */     zip4jRaf.seek(offSetStartOfZip64CentralDir);
/*     */     
/* 378 */     Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = new Zip64EndOfCentralDirectoryRecord();
/*     */     
/* 380 */     int signature = rawIO.readIntLittleEndian(zip4jRaf);
/* 381 */     if (signature != HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_RECORD.getValue()) {
/* 382 */       throw new ZipException("invalid signature for zip64 end of central directory record");
/*     */     }
/* 384 */     zip64EndOfCentralDirectoryRecord.setSignature(HeaderSignature.ZIP64_END_CENTRAL_DIRECTORY_RECORD);
/* 385 */     zip64EndOfCentralDirectoryRecord.setSizeOfZip64EndCentralDirectoryRecord(rawIO.readLongLittleEndian(zip4jRaf));
/* 386 */     zip64EndOfCentralDirectoryRecord.setVersionMadeBy(rawIO.readShortLittleEndian(zip4jRaf));
/* 387 */     zip64EndOfCentralDirectoryRecord.setVersionNeededToExtract(rawIO.readShortLittleEndian(zip4jRaf));
/* 388 */     zip64EndOfCentralDirectoryRecord.setNumberOfThisDisk(rawIO.readIntLittleEndian(zip4jRaf));
/* 389 */     zip64EndOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDirectory(rawIO.readIntLittleEndian(zip4jRaf));
/* 390 */     zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(rawIO
/* 391 */         .readLongLittleEndian(zip4jRaf));
/* 392 */     zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(rawIO.readLongLittleEndian(zip4jRaf));
/* 393 */     zip64EndOfCentralDirectoryRecord.setSizeOfCentralDirectory(rawIO.readLongLittleEndian(zip4jRaf));
/* 394 */     zip64EndOfCentralDirectoryRecord.setOffsetStartCentralDirectoryWRTStartDiskNumber(rawIO
/* 395 */         .readLongLittleEndian(zip4jRaf));
/*     */ 
/*     */ 
/*     */     
/* 399 */     long extDataSecSize = zip64EndOfCentralDirectoryRecord.getSizeOfZip64EndCentralDirectoryRecord() - 44L;
/* 400 */     if (extDataSecSize > 0L) {
/* 401 */       byte[] extDataSecRecBuf = new byte[(int)extDataSecSize];
/* 402 */       zip4jRaf.readFully(extDataSecRecBuf);
/* 403 */       zip64EndOfCentralDirectoryRecord.setExtensibleDataSector(extDataSecRecBuf);
/*     */     } 
/*     */     
/* 406 */     return zip64EndOfCentralDirectoryRecord;
/*     */   }
/*     */   
/*     */   private void readZip64ExtendedInfo(FileHeader fileHeader, RawIO rawIO) throws ZipException {
/* 410 */     if (fileHeader.getExtraDataRecords() == null || fileHeader.getExtraDataRecords().size() <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 414 */     Zip64ExtendedInfo zip64ExtendedInfo = readZip64ExtendedInfo(fileHeader.getExtraDataRecords(), rawIO, fileHeader
/* 415 */         .getUncompressedSize(), fileHeader.getCompressedSize(), fileHeader.getOffsetLocalHeader(), fileHeader
/* 416 */         .getDiskNumberStart());
/*     */     
/* 418 */     if (zip64ExtendedInfo == null) {
/*     */       return;
/*     */     }
/*     */     
/* 422 */     fileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
/*     */     
/* 424 */     if (zip64ExtendedInfo.getUncompressedSize() != -1L) {
/* 425 */       fileHeader.setUncompressedSize(zip64ExtendedInfo.getUncompressedSize());
/*     */     }
/*     */     
/* 428 */     if (zip64ExtendedInfo.getCompressedSize() != -1L) {
/* 429 */       fileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
/*     */     }
/*     */     
/* 432 */     if (zip64ExtendedInfo.getOffsetLocalHeader() != -1L) {
/* 433 */       fileHeader.setOffsetLocalHeader(zip64ExtendedInfo.getOffsetLocalHeader());
/*     */     }
/*     */     
/* 436 */     if (zip64ExtendedInfo.getDiskNumberStart() != -1) {
/* 437 */       fileHeader.setDiskNumberStart(zip64ExtendedInfo.getDiskNumberStart());
/*     */     }
/*     */   }
/*     */   
/*     */   private void readZip64ExtendedInfo(LocalFileHeader localFileHeader, RawIO rawIO) throws ZipException {
/* 442 */     if (localFileHeader == null) {
/* 443 */       throw new ZipException("file header is null in reading Zip64 Extended Info");
/*     */     }
/*     */     
/* 446 */     if (localFileHeader.getExtraDataRecords() == null || localFileHeader.getExtraDataRecords().size() <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 450 */     Zip64ExtendedInfo zip64ExtendedInfo = readZip64ExtendedInfo(localFileHeader.getExtraDataRecords(), rawIO, localFileHeader
/* 451 */         .getUncompressedSize(), localFileHeader.getCompressedSize(), 0L, 0);
/*     */     
/* 453 */     if (zip64ExtendedInfo == null) {
/*     */       return;
/*     */     }
/*     */     
/* 457 */     localFileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
/*     */     
/* 459 */     if (zip64ExtendedInfo.getUncompressedSize() != -1L) {
/* 460 */       localFileHeader.setUncompressedSize(zip64ExtendedInfo.getUncompressedSize());
/*     */     }
/*     */     
/* 463 */     if (zip64ExtendedInfo.getCompressedSize() != -1L) {
/* 464 */       localFileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Zip64ExtendedInfo readZip64ExtendedInfo(List<ExtraDataRecord> extraDataRecords, RawIO rawIO, long uncompressedSize, long compressedSize, long offsetLocalHeader, int diskNumberStart) {
/* 472 */     for (ExtraDataRecord extraDataRecord : extraDataRecords) {
/* 473 */       if (extraDataRecord == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 477 */       if (HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue() == extraDataRecord.getHeader()) {
/*     */         
/* 479 */         Zip64ExtendedInfo zip64ExtendedInfo = new Zip64ExtendedInfo();
/* 480 */         byte[] extraData = extraDataRecord.getData();
/*     */         
/* 482 */         if (extraDataRecord.getSizeOfData() <= 0) {
/* 483 */           return null;
/*     */         }
/*     */         
/* 486 */         int counter = 0;
/* 487 */         if (counter < extraDataRecord.getSizeOfData() && uncompressedSize == 4294967295L) {
/* 488 */           zip64ExtendedInfo.setUncompressedSize(rawIO.readLongLittleEndian(extraData, counter));
/* 489 */           counter += 8;
/*     */         } 
/*     */         
/* 492 */         if (counter < extraDataRecord.getSizeOfData() && compressedSize == 4294967295L) {
/* 493 */           zip64ExtendedInfo.setCompressedSize(rawIO.readLongLittleEndian(extraData, counter));
/* 494 */           counter += 8;
/*     */         } 
/*     */         
/* 497 */         if (counter < extraDataRecord.getSizeOfData() && offsetLocalHeader == 4294967295L) {
/* 498 */           zip64ExtendedInfo.setOffsetLocalHeader(rawIO.readLongLittleEndian(extraData, counter));
/* 499 */           counter += 8;
/*     */         } 
/*     */         
/* 502 */         if (counter < extraDataRecord.getSizeOfData() && diskNumberStart == 65535) {
/* 503 */           zip64ExtendedInfo.setDiskNumberStart(rawIO.readIntLittleEndian(extraData, counter));
/*     */         }
/*     */         
/* 506 */         return zip64ExtendedInfo;
/*     */       } 
/*     */     } 
/* 509 */     return null;
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
/*     */   private void setFilePointerToReadZip64EndCentralDirLoc(RandomAccessFile zip4jRaf, long offsetEndOfCentralDirectoryRecord) throws IOException {
/* 521 */     seekInCurrentPart(zip4jRaf, offsetEndOfCentralDirectoryRecord - 4L - 8L - 4L - 4L);
/*     */   }
/*     */   
/*     */   public LocalFileHeader readLocalFileHeader(InputStream inputStream, Charset charset) throws IOException {
/* 525 */     LocalFileHeader localFileHeader = new LocalFileHeader();
/* 526 */     byte[] intBuff = new byte[4];
/*     */ 
/*     */     
/* 529 */     int sig = this.rawIO.readIntLittleEndian(inputStream);
/* 530 */     if (sig != HeaderSignature.LOCAL_FILE_HEADER.getValue()) {
/* 531 */       return null;
/*     */     }
/* 533 */     localFileHeader.setSignature(HeaderSignature.LOCAL_FILE_HEADER);
/* 534 */     localFileHeader.setVersionNeededToExtract(this.rawIO.readShortLittleEndian(inputStream));
/*     */     
/* 536 */     byte[] generalPurposeFlags = new byte[2];
/* 537 */     if (Zip4jUtil.readFully(inputStream, generalPurposeFlags) != 2) {
/* 538 */       throw new ZipException("Could not read enough bytes for generalPurposeFlags");
/*     */     }
/* 540 */     localFileHeader.setEncrypted(BitUtils.isBitSet(generalPurposeFlags[0], 0));
/* 541 */     localFileHeader.setDataDescriptorExists(BitUtils.isBitSet(generalPurposeFlags[0], 3));
/* 542 */     localFileHeader.setFileNameUTF8Encoded(BitUtils.isBitSet(generalPurposeFlags[1], 3));
/* 543 */     localFileHeader.setGeneralPurposeFlag((byte[])generalPurposeFlags.clone());
/*     */     
/* 545 */     localFileHeader.setCompressionMethod(CompressionMethod.getCompressionMethodFromCode(this.rawIO
/* 546 */           .readShortLittleEndian(inputStream)));
/* 547 */     localFileHeader.setLastModifiedTime(this.rawIO.readIntLittleEndian(inputStream));
/*     */     
/* 549 */     Zip4jUtil.readFully(inputStream, intBuff);
/* 550 */     localFileHeader.setCrc(this.rawIO.readLongLittleEndian(intBuff, 0));
/* 551 */     localFileHeader.setCrcRawData((byte[])intBuff.clone());
/*     */     
/* 553 */     localFileHeader.setCompressedSize(this.rawIO.readLongLittleEndian(inputStream, 4));
/* 554 */     localFileHeader.setUncompressedSize(this.rawIO.readLongLittleEndian(inputStream, 4));
/*     */     
/* 556 */     int fileNameLength = this.rawIO.readShortLittleEndian(inputStream);
/* 557 */     localFileHeader.setFileNameLength(fileNameLength);
/*     */     
/* 559 */     localFileHeader.setExtraFieldLength(this.rawIO.readShortLittleEndian(inputStream));
/*     */     
/* 561 */     if (fileNameLength > 0) {
/* 562 */       byte[] fileNameBuf = new byte[fileNameLength];
/* 563 */       Zip4jUtil.readFully(inputStream, fileNameBuf);
/*     */ 
/*     */ 
/*     */       
/* 567 */       String fileName = HeaderUtil.decodeStringWithCharset(fileNameBuf, localFileHeader.isFileNameUTF8Encoded(), charset);
/*     */       
/* 569 */       if (fileName == null) {
/* 570 */         throw new ZipException("file name is null, cannot assign file name to local file header");
/*     */       }
/*     */       
/* 573 */       if (fileName.contains(":" + System.getProperty("file.separator"))) {
/* 574 */         fileName = fileName.substring(fileName.indexOf(":" + System.getProperty("file.separator")) + 2);
/*     */       }
/*     */       
/* 577 */       localFileHeader.setFileName(fileName);
/* 578 */       localFileHeader.setDirectory((fileName.endsWith("/") || fileName.endsWith("\\")));
/*     */     } else {
/* 580 */       localFileHeader.setFileName(null);
/*     */     } 
/*     */     
/* 583 */     readExtraDataRecords(inputStream, localFileHeader);
/* 584 */     readZip64ExtendedInfo(localFileHeader, this.rawIO);
/* 585 */     readAesExtraDataRecord(localFileHeader, this.rawIO);
/*     */     
/* 587 */     if (localFileHeader.isEncrypted())
/*     */     {
/* 589 */       if (localFileHeader.getEncryptionMethod() != EncryptionMethod.AES)
/*     */       {
/*     */         
/* 592 */         if (BigInteger.valueOf(localFileHeader.getGeneralPurposeFlag()[0]).testBit(6)) {
/* 593 */           localFileHeader.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD_VARIANT_STRONG);
/*     */         } else {
/* 595 */           localFileHeader.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 601 */     return localFileHeader;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataDescriptor readDataDescriptor(InputStream inputStream, boolean isZip64Format) throws IOException {
/* 606 */     DataDescriptor dataDescriptor = new DataDescriptor();
/*     */     
/* 608 */     byte[] intBuff = new byte[4];
/* 609 */     Zip4jUtil.readFully(inputStream, intBuff);
/* 610 */     long sigOrCrc = this.rawIO.readLongLittleEndian(intBuff, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 615 */     if (sigOrCrc == HeaderSignature.EXTRA_DATA_RECORD.getValue()) {
/* 616 */       dataDescriptor.setSignature(HeaderSignature.EXTRA_DATA_RECORD);
/* 617 */       Zip4jUtil.readFully(inputStream, intBuff);
/* 618 */       dataDescriptor.setCrc(this.rawIO.readLongLittleEndian(intBuff, 0));
/*     */     } else {
/* 620 */       dataDescriptor.setCrc(sigOrCrc);
/*     */     } 
/*     */     
/* 623 */     if (isZip64Format) {
/* 624 */       dataDescriptor.setCompressedSize(this.rawIO.readLongLittleEndian(inputStream));
/* 625 */       dataDescriptor.setUncompressedSize(this.rawIO.readLongLittleEndian(inputStream));
/*     */     } else {
/* 627 */       dataDescriptor.setCompressedSize(this.rawIO.readIntLittleEndian(inputStream));
/* 628 */       dataDescriptor.setUncompressedSize(this.rawIO.readIntLittleEndian(inputStream));
/*     */     } 
/*     */     
/* 631 */     return dataDescriptor;
/*     */   }
/*     */   
/*     */   private void readAesExtraDataRecord(FileHeader fileHeader, RawIO rawIO) throws ZipException {
/* 635 */     if (fileHeader.getExtraDataRecords() == null || fileHeader.getExtraDataRecords().size() <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 639 */     AESExtraDataRecord aesExtraDataRecord = readAesExtraDataRecord(fileHeader.getExtraDataRecords(), rawIO);
/* 640 */     if (aesExtraDataRecord != null) {
/* 641 */       fileHeader.setAesExtraDataRecord(aesExtraDataRecord);
/* 642 */       fileHeader.setEncryptionMethod(EncryptionMethod.AES);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readAesExtraDataRecord(LocalFileHeader localFileHeader, RawIO rawIO) throws ZipException {
/* 647 */     if (localFileHeader.getExtraDataRecords() == null || localFileHeader.getExtraDataRecords().size() <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 651 */     AESExtraDataRecord aesExtraDataRecord = readAesExtraDataRecord(localFileHeader.getExtraDataRecords(), rawIO);
/* 652 */     if (aesExtraDataRecord != null) {
/* 653 */       localFileHeader.setAesExtraDataRecord(aesExtraDataRecord);
/* 654 */       localFileHeader.setEncryptionMethod(EncryptionMethod.AES);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private AESExtraDataRecord readAesExtraDataRecord(List<ExtraDataRecord> extraDataRecords, RawIO rawIO) throws ZipException {
/* 661 */     if (extraDataRecords == null) {
/* 662 */       return null;
/*     */     }
/*     */     
/* 665 */     for (ExtraDataRecord extraDataRecord : extraDataRecords) {
/* 666 */       if (extraDataRecord == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 670 */       if (extraDataRecord.getHeader() == HeaderSignature.AES_EXTRA_DATA_RECORD.getValue()) {
/*     */         
/* 672 */         if (extraDataRecord.getData() == null) {
/* 673 */           throw new ZipException("corrupt AES extra data records");
/*     */         }
/*     */         
/* 676 */         AESExtraDataRecord aesExtraDataRecord = new AESExtraDataRecord();
/*     */         
/* 678 */         aesExtraDataRecord.setSignature(HeaderSignature.AES_EXTRA_DATA_RECORD);
/* 679 */         aesExtraDataRecord.setDataSize(extraDataRecord.getSizeOfData());
/*     */         
/* 681 */         byte[] aesData = extraDataRecord.getData();
/* 682 */         aesExtraDataRecord.setAesVersion(AesVersion.getFromVersionNumber(rawIO.readShortLittleEndian(aesData, 0)));
/* 683 */         byte[] vendorIDBytes = new byte[2];
/* 684 */         System.arraycopy(aesData, 2, vendorIDBytes, 0, 2);
/* 685 */         aesExtraDataRecord.setVendorID(new String(vendorIDBytes));
/* 686 */         aesExtraDataRecord.setAesKeyStrength(AesKeyStrength.getAesKeyStrengthFromRawCode(aesData[4] & 0xFF));
/* 687 */         aesExtraDataRecord.setCompressionMethod(
/* 688 */             CompressionMethod.getCompressionMethodFromCode(rawIO.readShortLittleEndian(aesData, 5)));
/*     */         
/* 690 */         return aesExtraDataRecord;
/*     */       } 
/*     */     } 
/*     */     
/* 694 */     return null;
/*     */   }
/*     */   
/*     */   private long getNumberOfEntriesInCentralDirectory(ZipModel zipModel) {
/* 698 */     if (zipModel.isZip64Format()) {
/* 699 */       return zipModel.getZip64EndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory();
/*     */     }
/*     */     
/* 702 */     return zipModel.getEndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory();
/*     */   }
/*     */   
/*     */   private long determineOffsetOfEndOfCentralDirectory(RandomAccessFile randomAccessFile) throws IOException {
/* 706 */     byte[] buff = new byte[4096];
/* 707 */     long currentFilePointer = randomAccessFile.getFilePointer();
/*     */     
/*     */     do {
/* 710 */       int toRead = (currentFilePointer > 4096L) ? 4096 : (int)currentFilePointer;
/*     */       
/* 712 */       long seekPosition = currentFilePointer - toRead + 4L;
/* 713 */       if (seekPosition == 4L) {
/* 714 */         seekPosition = 0L;
/*     */       }
/*     */       
/* 717 */       seekInCurrentPart(randomAccessFile, seekPosition);
/* 718 */       randomAccessFile.read(buff, 0, toRead);
/* 719 */       currentFilePointer = seekPosition;
/*     */       
/* 721 */       for (int i = 0; i < toRead - 3; i++) {
/* 722 */         if (this.rawIO.readIntLittleEndian(buff, i) == HeaderSignature.END_OF_CENTRAL_DIRECTORY.getValue()) {
/* 723 */           return currentFilePointer + i;
/*     */         }
/*     */       } 
/* 726 */     } while (currentFilePointer > 0L);
/*     */     
/* 728 */     throw new ZipException("Zip headers not found. Probably not a zip file");
/*     */   }
/*     */   
/*     */   private void seekInCurrentPart(RandomAccessFile randomAccessFile, long pos) throws IOException {
/* 732 */     if (randomAccessFile instanceof NumberedSplitRandomAccessFile) {
/* 733 */       ((NumberedSplitRandomAccessFile)randomAccessFile).seekInCurrentPart(pos);
/*     */     } else {
/* 735 */       randomAccessFile.seek(pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String readZipComment(RandomAccessFile raf, int commentLength, Charset charset) {
/* 740 */     if (commentLength <= 0) {
/* 741 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 745 */       byte[] commentBuf = new byte[commentLength];
/* 746 */       raf.readFully(commentBuf);
/* 747 */       return new String(commentBuf, charset);
/* 748 */     } catch (IOException e) {
/*     */       
/* 750 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\headers\HeaderReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */