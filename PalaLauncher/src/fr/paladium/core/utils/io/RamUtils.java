/*    */ package fr.paladium.core.utils.io;
/*    */ 
/*    */ import fr.paladium.core.distribution.dto.DistributionOS;
/*    */ import fr.paladium.core.utils.memory.Ram;
/*    */ import fr.paladium.core.utils.memory.RamMac;
/*    */ import fr.paladium.core.utils.memory.RamWindows;
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
/*    */ public class RamUtils
/*    */ {
/*    */   private static Ram ram;
/*    */   
/*    */   public static Ram getRam() {
/* 22 */     if (ram == null) {
/* 23 */       ram = (OsCheck.getOperatingSystemType() == DistributionOS.MACOS) ? (Ram)new RamMac() : (Ram)new RamWindows();
/*    */     }
/*    */     
/* 26 */     return ram;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\io\RamUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */