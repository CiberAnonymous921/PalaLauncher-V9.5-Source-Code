/*    */ package fr.paladium.core.utils.debugger;
/*    */ import java.lang.management.ManagementFactory;
/*    */ import java.net.URISyntaxException;

/*    */ 
/*    */ import com.sun.management.OperatingSystemMXBean;

import fr.paladium.Launcher;
/*    */ import fr.paladium.core.distribution.dto.DistributionOS;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import fr.paladium.core.utils.io.RamUtils;
/*    */ 
/*    */ public class DebugData {
/*    */   private DistributionOS distributionOS;
/*    */   private String os;
/*    */   private String javaVersion;
/*    */   private String executionPath;
/*    */   
/* 16 */   public DistributionOS getDistributionOS() { return this.distributionOS; } private String appData; private String cpu; private String gpu; private String ram; private long date; public String getOs() {
/* 17 */     return this.os; }
/* 18 */   public String getJavaVersion() { return this.javaVersion; }
/* 19 */   public String getExecutionPath() { return this.executionPath; }
/* 20 */   public String getAppData() { return this.appData; }
/* 21 */   public String getCpu() { return this.cpu; }
/* 22 */   public String getGpu() { return this.gpu; }
/* 23 */   public String getRam() { return this.ram; } public long getDate() {
/* 24 */     return this.date;
/*    */   }
/*    */   public DebugData() {
/* 27 */     OperatingSystemMXBean bean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
/*    */     
/* 29 */     this.distributionOS = OsCheck.getOperatingSystemType();
/* 30 */     this.os = System.getProperty("os.name", "generic");
/* 31 */     this.javaVersion = System.getProperty("java.version");
/*    */     try {
/* 33 */       this.executionPath = Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
/* 34 */     } catch (URISyntaxException e) {
/* 35 */       e.printStackTrace();
/* 36 */       this.executionPath = "null";
/*    */     } 
/* 38 */     this.appData = OsCheck.getAppData().getAbsolutePath();
/* 39 */     this.cpu = System.getenv("PROCESSOR_IDENTIFIER");
/* 40 */     this.gpu = System.getenv("DISPLAY");
/* 41 */     this.ram = RamUtils.getRam().getMemoryInfo();
/* 42 */     this.date = System.currentTimeMillis();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\debugger\DebugData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */