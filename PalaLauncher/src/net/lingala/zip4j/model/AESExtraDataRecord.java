/*    */ package net.lingala.zip4j.model;
/*    */ 
/*    */ import net.lingala.zip4j.headers.HeaderSignature;
/*    */ import net.lingala.zip4j.model.enums.AesKeyStrength;
/*    */ import net.lingala.zip4j.model.enums.AesVersion;
/*    */ import net.lingala.zip4j.model.enums.CompressionMethod;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AESExtraDataRecord
/*    */   extends ZipHeader
/*    */ {
/*    */   private int dataSize;
/*    */   private AesVersion aesVersion;
/*    */   private String vendorID;
/*    */   private AesKeyStrength aesKeyStrength;
/*    */   private CompressionMethod compressionMethod;
/*    */   
/*    */   public AESExtraDataRecord() {
/* 33 */     setSignature(HeaderSignature.AES_EXTRA_DATA_RECORD);
/* 34 */     this.dataSize = 7;
/* 35 */     this.aesVersion = AesVersion.TWO;
/* 36 */     this.vendorID = "AE";
/* 37 */     this.aesKeyStrength = AesKeyStrength.KEY_STRENGTH_256;
/* 38 */     this.compressionMethod = CompressionMethod.DEFLATE;
/*    */   }
/*    */   
/*    */   public int getDataSize() {
/* 42 */     return this.dataSize;
/*    */   }
/*    */   
/*    */   public void setDataSize(int dataSize) {
/* 46 */     this.dataSize = dataSize;
/*    */   }
/*    */   
/*    */   public AesVersion getAesVersion() {
/* 50 */     return this.aesVersion;
/*    */   }
/*    */   
/*    */   public void setAesVersion(AesVersion aesVersion) {
/* 54 */     this.aesVersion = aesVersion;
/*    */   }
/*    */   
/*    */   public String getVendorID() {
/* 58 */     return this.vendorID;
/*    */   }
/*    */   
/*    */   public void setVendorID(String vendorID) {
/* 62 */     this.vendorID = vendorID;
/*    */   }
/*    */   
/*    */   public AesKeyStrength getAesKeyStrength() {
/* 66 */     return this.aesKeyStrength;
/*    */   }
/*    */   
/*    */   public void setAesKeyStrength(AesKeyStrength aesKeyStrength) {
/* 70 */     this.aesKeyStrength = aesKeyStrength;
/*    */   }
/*    */   
/*    */   public CompressionMethod getCompressionMethod() {
/* 74 */     return this.compressionMethod;
/*    */   }
/*    */   
/*    */   public void setCompressionMethod(CompressionMethod compressionMethod) {
/* 78 */     this.compressionMethod = compressionMethod;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\AESExtraDataRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */