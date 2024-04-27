/*     */ package fr.paladium.core.utils.memory;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;

/*     */ import org.apache.commons.io.FileUtils;

/*     */ 
/*     */ import com.sun.management.OperatingSystemMXBean;
/*     */ 
/*     */ public class RamMac
/*     */   implements Ram
/*     */ {
/*     */   private static final String VM_STAT_COMMAND = "vm_stat";
/*  18 */   private static final Pattern patternPageSize = Pattern.compile("page size of (\\d+) bytes");
/*     */ 
/*     */   
/*     */   public long getTotalMemory() {
/*  22 */     OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
/*     */     
/*  24 */     return osBean.getTotalPhysicalMemorySize();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getUsedMemory() {
/*  29 */     Map<String, Long> vmStat = readVmStats();
/*  30 */     long pageSize = ((Long)vmStat.getOrDefault("pageSize", Long.valueOf(-1L))).longValue();
/*  31 */     long pagesFree = ((Long)vmStat.getOrDefault("pagesFree", Long.valueOf(-1L))).longValue();
/*  32 */     long pagesInactive = ((Long)vmStat.getOrDefault("pagesInactive", Long.valueOf(-1L))).longValue();
/*     */     
/*  34 */     if (pageSize <= 0L) {
/*  35 */       return -1L;
/*     */     }
/*     */     
/*  38 */     long totalPages = getTotalPages(pageSize);
/*     */     
/*  40 */     long used = totalPages - pagesFree - pagesInactive;
/*  41 */     return used * pageSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void printMemoryInfo() {
/*     */     try {
/*  48 */       Process process = Runtime.getRuntime().exec("vm_stat");
/*     */ 
/*     */       
/*  51 */       BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
/*     */       
/*     */       String line;
/*  54 */       while ((line = reader.readLine()) != null) {
/*  55 */         System.out.println(line);
/*     */       }
/*  57 */       reader.close();
/*  58 */     } catch (IOException e) {
/*  59 */       System.err.println("Error while reading vm_stat command:");
/*  60 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getFreeMemory() {
/*  66 */     return getTotalMemory() - getUsedMemory();
/*     */   }
/*     */   
/*     */   private long getTotalPages(long pageSize) {
/*  70 */     return getTotalMemory() / pageSize;
/*     */   }
/*     */   
/*     */   private long extractMacPageSize(String line) {
/*  74 */     Matcher matcher = patternPageSize.matcher(line);
/*     */     
/*  76 */     if (matcher.find()) {
/*  77 */       return Long.parseLong(matcher.group(1));
/*     */     }
/*  79 */     return -1L;
/*     */   }
/*     */   
/*     */   private long extractPages(String line) {
/*  83 */     String[] parts = line.split(":");
/*     */     
/*  85 */     if (parts.length < 2) {
/*  86 */       return -1L;
/*     */     }
/*  88 */     String raw = parts[1].trim().replace(".", "");
/*     */     try {
/*  90 */       return Long.parseLong(raw);
/*  91 */     } catch (NumberFormatException e) {
/*  92 */       return -1L;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Map<String, Long> readVmStats() {
/*  97 */     Map<String, Long> result = new HashMap<>();
/*     */ 
/*     */     
/*     */     try {
/* 101 */       Process process = Runtime.getRuntime().exec("vm_stat");
/*     */ 
/*     */       
/* 104 */       BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
/*     */       
/* 106 */       long pagesFree = -1L;
/* 107 */       long pagesInactive = -1L;
/* 108 */       long pageSize = -1L;
/*     */       String line;
/* 110 */       while ((line = reader.readLine()) != null) {
/* 111 */         if (pageSize == -1L)
/* 112 */           pageSize = extractMacPageSize(line); 
/* 113 */         if (pagesFree == -1L && line.startsWith("Pages free:"))
/* 114 */           pagesFree = extractPages(line); 
/* 115 */         if (pagesInactive == -1L && line.startsWith("Pages inactive:"))
/* 116 */           pagesInactive = extractPages(line); 
/*     */       } 
/* 118 */       reader.close();
/*     */       
/* 120 */       result.put("pageSize", Long.valueOf(pageSize));
/* 121 */       result.put("pagesFree", Long.valueOf(pagesFree));
/* 122 */       result.put("pagesInactive", Long.valueOf(pagesInactive));
/* 123 */     } catch (IOException e) {
/* 124 */       System.err.println("Error while reading vm_stat command:");
/* 125 */       e.printStackTrace();
/*     */     } 
/* 127 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMemoryInfo() {
/* 132 */     return "Total: " + FileUtils.byteCountToDisplaySize(getTotalMemory()) + " | Used: " + FileUtils.byteCountToDisplaySize(getUsedMemory()) + " | Free: " + FileUtils.byteCountToDisplaySize(getFreeMemory());
/*     */   }
/*     */ 
/*     */   
/*     */   public void openTaskManager() {
/*     */     try {
/* 138 */       Runtime.getRuntime().exec(new String[] { "open", "-a", "Activity Monitor" });
/* 139 */     } catch (IOException e) {
/* 140 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 149 */     RamMac ram = new RamMac();
/* 150 */     ram.printMemoryInfo();
/*     */     
/* 152 */     System.out.println("Total: " + (ram.getTotalMemory() / 1024L / 1024L) + " MB");
/* 153 */     System.out.println("Used: " + (ram.getUsedMemory() / 1024L / 1024L) + " MB");
/* 154 */     System.out.println("Free: " + (ram.getFreeMemory() / 1024L / 1024L) + " MB");
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\memory\RamMac.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */