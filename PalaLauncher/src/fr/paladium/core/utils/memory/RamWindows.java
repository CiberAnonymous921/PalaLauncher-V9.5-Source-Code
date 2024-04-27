/*    */ package fr.paladium.core.utils.memory;
/*    */ import java.io.IOException;
/*    */ import java.lang.management.ManagementFactory;

/*    */ import org.apache.commons.io.FileUtils;

/*    */ 
/*    */ import com.sun.management.OperatingSystemMXBean;
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
/*    */ public class RamWindows
/*    */   implements Ram
/*    */ {
/*    */   public long getTotalMemory() {
/* 22 */     OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
/* 23 */     return osBean.getTotalPhysicalMemorySize();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getFreeMemory() {
/* 28 */     OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
/* 29 */     return osBean.getFreePhysicalMemorySize();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getUsedMemory() {
/* 34 */     return getTotalMemory() - getFreeMemory();
/*    */   }
/*    */ 
/*    */   
/*    */   public void printMemoryInfo() {
/* 39 */     System.out.println(getMemoryInfo());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMemoryInfo() {
/* 44 */     OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
/*    */     
/* 46 */     return "Physical(" + FileUtils.byteCountToDisplaySize(osBean.getFreePhysicalMemorySize()) + " free on " + FileUtils.byteCountToDisplaySize(osBean.getTotalPhysicalMemorySize()) + ") Swap(" + 
/* 47 */       FileUtils.byteCountToDisplaySize(osBean.getFreeSwapSpaceSize()) + " free on " + FileUtils.byteCountToDisplaySize(osBean.getTotalSwapSpaceSize()) + ")";
/*    */   }
/*    */ 
/*    */   
/*    */   public void openTaskManager() {
/*    */     try {
/* 53 */       Runtime.getRuntime().exec(new String[] { "taskmgr" });
/* 54 */     } catch (IOException e) {
/* 55 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void main(String[] args) {
/* 64 */     RamWindows ram = new RamWindows();
/* 65 */     ram.printMemoryInfo();
/*    */     
/* 67 */     System.out.println("Total: " + (ram.getTotalMemory() / 1024L / 1024L) + " MB");
/* 68 */     System.out.println("Used: " + (ram.getUsedMemory() / 1024L / 1024L) + " MB");
/* 69 */     System.out.println("Free: " + (ram.getFreeMemory() / 1024L / 1024L) + " MB");
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\memory\RamWindows.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */