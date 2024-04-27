/*   */ package fr.paladium.core.utils.format;
/*   */ 
/*   */ import java.util.UUID;
/*   */ 
/*   */ public class UUIDUtils
/*   */ {
/*   */   public static UUID fromWithoutDashes(String uuid) {
/* 8 */     return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
/*   */   }
/*   */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\format\UUIDUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */