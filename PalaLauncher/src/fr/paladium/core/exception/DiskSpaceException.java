/*    */ package fr.paladium.core.exception;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ public class DiskSpaceException extends IOException {
/*    */   private final long freeSpace;
/*    */   private final long neededSpace;
/*    */   
/*    */   public long getFreeSpace() {
/* 11 */     return this.freeSpace; } public long getNeededSpace() {
/* 12 */     return this.neededSpace;
/*    */   }
/*    */   public DiskSpaceException(long freeSpace, long neededSpace) {
/* 15 */     super("Espace disque insuffisant pour mettre à jour le launcher (" + FileUtils.byteCountToDisplaySize(neededSpace) + ")");
/* 16 */     this.freeSpace = freeSpace;
/* 17 */     this.neededSpace = neededSpace;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\exception\DiskSpaceException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */