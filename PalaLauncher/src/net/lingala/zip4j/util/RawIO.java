/*     */ package net.lingala.zip4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import net.lingala.zip4j.exception.ZipException;
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
/*     */ public class RawIO
/*     */ {
/*  28 */   private byte[] shortBuff = new byte[2];
/*  29 */   private byte[] intBuff = new byte[4];
/*  30 */   private byte[] longBuff = new byte[8];
/*     */   
/*     */   public long readLongLittleEndian(RandomAccessFile randomAccessFile) throws IOException {
/*  33 */     randomAccessFile.readFully(this.longBuff);
/*  34 */     return readLongLittleEndian(this.longBuff, 0);
/*     */   }
/*     */   
/*     */   public long readLongLittleEndian(RandomAccessFile randomAccessFile, int readLen) throws IOException {
/*  38 */     resetBytes(this.longBuff);
/*  39 */     randomAccessFile.readFully(this.longBuff, 0, readLen);
/*  40 */     return readLongLittleEndian(this.longBuff, 0);
/*     */   }
/*     */   
/*     */   public long readLongLittleEndian(InputStream inputStream) throws IOException {
/*  44 */     readFully(inputStream, this.longBuff, this.longBuff.length);
/*  45 */     return readLongLittleEndian(this.longBuff, 0);
/*     */   }
/*     */   
/*     */   public long readLongLittleEndian(InputStream inputStream, int readLen) throws IOException {
/*  49 */     resetBytes(this.longBuff);
/*  50 */     readFully(inputStream, this.longBuff, readLen);
/*  51 */     return readLongLittleEndian(this.longBuff, 0);
/*     */   }
/*     */   
/*     */   public long readLongLittleEndian(byte[] array, int pos) {
/*  55 */     if (array.length - pos < 8) {
/*  56 */       resetBytes(this.longBuff);
/*     */     }
/*  58 */     System.arraycopy(array, pos, this.longBuff, 0, (array.length < 8) ? (array.length - pos) : 8);
/*     */     
/*  60 */     long temp = 0L;
/*  61 */     temp |= (this.longBuff[7] & 0xFF);
/*  62 */     temp <<= 8L;
/*  63 */     temp |= (this.longBuff[6] & 0xFF);
/*  64 */     temp <<= 8L;
/*  65 */     temp |= (this.longBuff[5] & 0xFF);
/*  66 */     temp <<= 8L;
/*  67 */     temp |= (this.longBuff[4] & 0xFF);
/*  68 */     temp <<= 8L;
/*  69 */     temp |= (this.longBuff[3] & 0xFF);
/*  70 */     temp <<= 8L;
/*  71 */     temp |= (this.longBuff[2] & 0xFF);
/*  72 */     temp <<= 8L;
/*  73 */     temp |= (this.longBuff[1] & 0xFF);
/*  74 */     temp <<= 8L;
/*  75 */     temp |= (this.longBuff[0] & 0xFF);
/*  76 */     return temp;
/*     */   }
/*     */   
/*     */   public int readIntLittleEndian(RandomAccessFile randomAccessFile) throws IOException {
/*  80 */     randomAccessFile.readFully(this.intBuff);
/*  81 */     return readIntLittleEndian(this.intBuff);
/*     */   }
/*     */   
/*     */   public int readIntLittleEndian(InputStream inputStream) throws IOException {
/*  85 */     readFully(inputStream, this.intBuff, 4);
/*  86 */     return readIntLittleEndian(this.intBuff);
/*     */   }
/*     */   
/*     */   public int readIntLittleEndian(byte[] b) {
/*  90 */     return readIntLittleEndian(b, 0);
/*     */   }
/*     */   
/*     */   public int readIntLittleEndian(byte[] b, int pos) {
/*  94 */     return b[pos] & 0xFF | (b[1 + pos] & 0xFF) << 8 | (b[2 + pos] & 0xFF | (b[3 + pos] & 0xFF) << 8) << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readShortLittleEndian(RandomAccessFile randomAccessFile) throws IOException {
/*  99 */     randomAccessFile.readFully(this.shortBuff);
/* 100 */     return readShortLittleEndian(this.shortBuff, 0);
/*     */   }
/*     */   
/*     */   public int readShortLittleEndian(InputStream inputStream) throws IOException {
/* 104 */     readFully(inputStream, this.shortBuff, this.shortBuff.length);
/* 105 */     return readShortLittleEndian(this.shortBuff, 0);
/*     */   }
/*     */   
/*     */   public int readShortLittleEndian(byte[] buff, int position) {
/* 109 */     return buff[position] & 0xFF | (buff[1 + position] & 0xFF) << 8;
/*     */   }
/*     */   
/*     */   public void writeShortLittleEndian(OutputStream outputStream, int value) throws IOException {
/* 113 */     writeShortLittleEndian(this.shortBuff, 0, value);
/* 114 */     outputStream.write(this.shortBuff);
/*     */   }
/*     */   
/*     */   public void writeShortLittleEndian(byte[] array, int pos, int value) {
/* 118 */     array[pos + 1] = (byte)(value >>> 8);
/* 119 */     array[pos] = (byte)(value & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeIntLittleEndian(OutputStream outputStream, int value) throws IOException {
/* 124 */     writeIntLittleEndian(this.intBuff, 0, value);
/* 125 */     outputStream.write(this.intBuff);
/*     */   }
/*     */   
/*     */   public void writeIntLittleEndian(byte[] array, int pos, int value) {
/* 129 */     array[pos + 3] = (byte)(value >>> 24);
/* 130 */     array[pos + 2] = (byte)(value >>> 16);
/* 131 */     array[pos + 1] = (byte)(value >>> 8);
/* 132 */     array[pos] = (byte)(value & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeLongLittleEndian(OutputStream outputStream, long value) throws IOException {
/* 137 */     writeLongLittleEndian(this.longBuff, 0, value);
/* 138 */     outputStream.write(this.longBuff);
/*     */   }
/*     */   
/*     */   public void writeLongLittleEndian(byte[] array, int pos, long value) {
/* 142 */     array[pos + 7] = (byte)(int)(value >>> 56L);
/* 143 */     array[pos + 6] = (byte)(int)(value >>> 48L);
/* 144 */     array[pos + 5] = (byte)(int)(value >>> 40L);
/* 145 */     array[pos + 4] = (byte)(int)(value >>> 32L);
/* 146 */     array[pos + 3] = (byte)(int)(value >>> 24L);
/* 147 */     array[pos + 2] = (byte)(int)(value >>> 16L);
/* 148 */     array[pos + 1] = (byte)(int)(value >>> 8L);
/* 149 */     array[pos] = (byte)(int)(value & 0xFFL);
/*     */   }
/*     */   
/*     */   private void readFully(InputStream inputStream, byte[] buff, int readLen) throws IOException {
/* 153 */     int actualReadLength = Zip4jUtil.readFully(inputStream, buff, 0, readLen);
/* 154 */     if (actualReadLength != readLen) {
/* 155 */       throw new ZipException("Could not fill buffer");
/*     */     }
/*     */   }
/*     */   
/*     */   private void resetBytes(byte[] b) {
/* 160 */     for (int i = 0; i < b.length; i++)
/* 161 */       b[i] = 0; 
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4\\util\RawIO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */