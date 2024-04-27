/*     */ package net.lingala.zip4j.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Calendar;
/*     */ import net.lingala.zip4j.exception.ZipException;
/*     */ import net.lingala.zip4j.model.LocalFileHeader;
/*     */ import net.lingala.zip4j.model.enums.CompressionMethod;
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
/*     */ public class Zip4jUtil
/*     */ {
/*     */   private static final int MAX_RAW_READ_FULLY_RETRY_ATTEMPTS = 15;
/*     */   
/*     */   public static boolean isStringNotNullAndNotEmpty(String str) {
/*  33 */     return (str != null && str.trim().length() > 0);
/*     */   }
/*     */   
/*     */   public static boolean createDirectoryIfNotExists(File file) throws ZipException {
/*  37 */     if (file == null) {
/*  38 */       throw new ZipException("output path is null");
/*     */     }
/*     */     
/*  41 */     if (file.exists()) {
/*  42 */       if (!file.isDirectory()) {
/*  43 */         throw new ZipException("output directory is not valid");
/*     */       }
/*     */     }
/*  46 */     else if (!file.mkdirs()) {
/*  47 */       throw new ZipException("Cannot create output directories");
/*     */     } 
/*     */ 
/*     */     
/*  51 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long javaToDosTime(long time) {
/*  56 */     Calendar cal = Calendar.getInstance();
/*  57 */     cal.setTimeInMillis(time);
/*     */     
/*  59 */     int year = cal.get(1);
/*  60 */     if (year < 1980) {
/*  61 */       return 2162688L;
/*     */     }
/*  63 */     return (year - 1980 << 25 | cal.get(2) + 1 << 21 | cal
/*  64 */       .get(5) << 16 | cal.get(11) << 11 | cal.get(12) << 5 | cal
/*  65 */       .get(13) >> 1);
/*     */   }
/*     */   
/*     */   public static long dosToJavaTme(long dosTime) {
/*  69 */     int sec = (int)(2L * (dosTime & 0x1FL));
/*  70 */     int min = (int)(dosTime >> 5L & 0x3FL);
/*  71 */     int hrs = (int)(dosTime >> 11L & 0x1FL);
/*  72 */     int day = (int)(dosTime >> 16L & 0x1FL);
/*  73 */     int mon = (int)((dosTime >> 21L & 0xFL) - 1L);
/*  74 */     int year = (int)((dosTime >> 25L & 0x7FL) + 1980L);
/*     */     
/*  76 */     Calendar cal = Calendar.getInstance();
/*  77 */     cal.set(year, mon, day, hrs, min, sec);
/*  78 */     cal.set(14, 0);
/*  79 */     return cal.getTime().getTime();
/*     */   }
/*     */   
/*     */   public static byte[] convertCharArrayToByteArray(char[] charArray) {
/*  83 */     byte[] bytes = new byte[charArray.length];
/*  84 */     for (int i = 0; i < charArray.length; i++) {
/*  85 */       bytes[i] = (byte)charArray[i];
/*     */     }
/*  87 */     return bytes;
/*     */   }
/*     */   
/*     */   public static CompressionMethod getCompressionMethod(LocalFileHeader localFileHeader) {
/*  91 */     if (localFileHeader.getCompressionMethod() != CompressionMethod.AES_INTERNAL_ONLY) {
/*  92 */       return localFileHeader.getCompressionMethod();
/*     */     }
/*     */     
/*  95 */     if (localFileHeader.getAesExtraDataRecord() == null) {
/*  96 */       throw new RuntimeException("AesExtraDataRecord not present in local header for aes encrypted data");
/*     */     }
/*     */     
/*  99 */     return localFileHeader.getAesExtraDataRecord().getCompressionMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int readFully(InputStream inputStream, byte[] bufferToReadInto) throws IOException {
/* 104 */     int readLen = inputStream.read(bufferToReadInto);
/*     */     
/* 106 */     if (readLen != bufferToReadInto.length) {
/* 107 */       readLen = readUntilBufferIsFull(inputStream, bufferToReadInto, readLen);
/*     */       
/* 109 */       if (readLen != bufferToReadInto.length) {
/* 110 */         throw new IOException("Cannot read fully into byte buffer");
/*     */       }
/*     */     } 
/*     */     
/* 114 */     return readLen;
/*     */   }
/*     */   
/*     */   public static int readFully(InputStream inputStream, byte[] b, int offset, int length) throws IOException {
/* 118 */     int numberOfBytesRead = 0;
/*     */     
/* 120 */     if (offset < 0) {
/* 121 */       throw new IllegalArgumentException("Negative offset");
/*     */     }
/*     */     
/* 124 */     if (length < 0) {
/* 125 */       throw new IllegalArgumentException("Negative length");
/*     */     }
/*     */     
/* 128 */     if (length == 0) {
/* 129 */       return 0;
/*     */     }
/*     */     
/* 132 */     if (offset + length > b.length) {
/* 133 */       throw new IllegalArgumentException("Length greater than buffer size");
/*     */     }
/*     */     
/* 136 */     while (numberOfBytesRead != length) {
/* 137 */       int currentReadLength = inputStream.read(b, offset + numberOfBytesRead, length - numberOfBytesRead);
/* 138 */       if (currentReadLength == -1) {
/* 139 */         if (numberOfBytesRead == 0) {
/* 140 */           return -1;
/*     */         }
/* 142 */         return numberOfBytesRead;
/*     */       } 
/*     */       
/* 145 */       numberOfBytesRead += currentReadLength;
/*     */     } 
/*     */     
/* 148 */     return numberOfBytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int readUntilBufferIsFull(InputStream inputStream, byte[] bufferToReadInto, int readLength) throws IOException {
/* 154 */     int remainingLength = bufferToReadInto.length - readLength;
/* 155 */     int loopReadLength = 0;
/* 156 */     int retryAttempt = 1;
/*     */     
/* 158 */     while (readLength < bufferToReadInto.length && loopReadLength != -1 && retryAttempt < 15) {
/*     */ 
/*     */ 
/*     */       
/* 162 */       loopReadLength = inputStream.read(bufferToReadInto, readLength, remainingLength);
/*     */       
/* 164 */       if (loopReadLength > 0) {
/* 165 */         readLength += loopReadLength;
/* 166 */         remainingLength -= loopReadLength;
/*     */       } 
/*     */       
/* 169 */       retryAttempt++;
/*     */     } 
/*     */     
/* 172 */     return readLength;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4\\util\Zip4jUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */