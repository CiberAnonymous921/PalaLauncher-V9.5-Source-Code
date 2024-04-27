/*    */ package net.lingala.zip4j.model;
/*    */ 
/*    */ import net.lingala.zip4j.headers.HeaderSignature;
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
/*    */ public class FileHeader
/*    */   extends AbstractFileHeader
/*    */ {
/*    */   private int versionMadeBy;
/* 24 */   private int fileCommentLength = 0;
/*    */   private int diskNumberStart;
/*    */   private byte[] internalFileAttributes;
/*    */   private byte[] externalFileAttributes;
/*    */   private long offsetLocalHeader;
/*    */   private String fileComment;
/*    */   
/*    */   public FileHeader() {
/* 32 */     setSignature(HeaderSignature.CENTRAL_DIRECTORY);
/*    */   }
/*    */   
/*    */   public int getVersionMadeBy() {
/* 36 */     return this.versionMadeBy;
/*    */   }
/*    */   
/*    */   public void setVersionMadeBy(int versionMadeBy) {
/* 40 */     this.versionMadeBy = versionMadeBy;
/*    */   }
/*    */   
/*    */   public int getFileCommentLength() {
/* 44 */     return this.fileCommentLength;
/*    */   }
/*    */   
/*    */   public void setFileCommentLength(int fileCommentLength) {
/* 48 */     this.fileCommentLength = fileCommentLength;
/*    */   }
/*    */   
/*    */   public int getDiskNumberStart() {
/* 52 */     return this.diskNumberStart;
/*    */   }
/*    */   
/*    */   public void setDiskNumberStart(int diskNumberStart) {
/* 56 */     this.diskNumberStart = diskNumberStart;
/*    */   }
/*    */   
/*    */   public byte[] getInternalFileAttributes() {
/* 60 */     return this.internalFileAttributes;
/*    */   }
/*    */   
/*    */   public void setInternalFileAttributes(byte[] internalFileAttributes) {
/* 64 */     this.internalFileAttributes = internalFileAttributes;
/*    */   }
/*    */   
/*    */   public byte[] getExternalFileAttributes() {
/* 68 */     return this.externalFileAttributes;
/*    */   }
/*    */   
/*    */   public void setExternalFileAttributes(byte[] externalFileAttributes) {
/* 72 */     this.externalFileAttributes = externalFileAttributes;
/*    */   }
/*    */   
/*    */   public long getOffsetLocalHeader() {
/* 76 */     return this.offsetLocalHeader;
/*    */   }
/*    */   
/*    */   public void setOffsetLocalHeader(long offsetLocalHeader) {
/* 80 */     this.offsetLocalHeader = offsetLocalHeader;
/*    */   }
/*    */   
/*    */   public String getFileComment() {
/* 84 */     return this.fileComment;
/*    */   }
/*    */   
/*    */   public void setFileComment(String fileComment) {
/* 88 */     this.fileComment = fileComment;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 93 */     return getFileName();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\model\FileHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */