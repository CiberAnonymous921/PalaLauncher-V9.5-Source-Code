/*    */ package fr.paladium.core.utils.option.dto;
import fr.paladium.core.utils.io.RamUtils;
/*    */ 
/*    */ import fr.paladium.core.utils.memory.Ram;
/*    */ 
/*    */ public class GameDistributionOptions {
/*    */   private GameDistributionMemoryOption memory;
/*    */   private GameDistributionResolutionOption resolution;
/*    */   
/*  9 */   public void setMemory(GameDistributionMemoryOption memory) { this.memory = memory; } private boolean startInFullscreen; private boolean launcherStayOpen; public void setResolution(GameDistributionResolutionOption resolution) { this.resolution = resolution; } public void setStartInFullscreen(boolean startInFullscreen) { this.startInFullscreen = startInFullscreen; } public void setLauncherStayOpen(boolean launcherStayOpen) { this.launcherStayOpen = launcherStayOpen; }
/*    */ 
/*    */   
/* 12 */   public GameDistributionMemoryOption getMemory() { return this.memory; }
/* 13 */   public GameDistributionResolutionOption getResolution() { return this.resolution; }
/* 14 */   public boolean isStartInFullscreen() { return this.startInFullscreen; } public boolean isLauncherStayOpen() {
/* 15 */     return this.launcherStayOpen;
/*    */   }
/*    */   public GameDistributionOptions() {
/* 18 */     this.memory = new GameDistributionMemoryOption();
/* 19 */     setDefaultMemoryValues();
/*    */     
/* 21 */     Ram ram = RamUtils.getRam();
/* 22 */     if (ram.getFreeMemory() / 1024L / 1024L / 1024L < 4L) {
/* 23 */       int freeMemory = (int)(ram.getFreeMemory() / 1024L / 1024L / 1024L);
/* 24 */       this.memory.setMin(freeMemory);
/* 25 */       this.memory.setMax(freeMemory);
/*    */     } 
/*    */     
/* 28 */     this.resolution = new GameDistributionResolutionOption();
/* 29 */     this.resolution.setW(854);
/* 30 */     this.resolution.setH(480);
/* 31 */     this.startInFullscreen = false;
/* 32 */     this.launcherStayOpen = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void setDefaultMemoryValues() {
/* 39 */     this.memory.setMin(2);
/* 40 */     this.memory.setMax(4);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void checkInvalidRam() {
/* 49 */     if (this.memory.getMin() == 0 || this.memory.getMax() == 0) {
/* 50 */       Ram ram = RamUtils.getRam();
/* 51 */       int freeMemory = (int)(ram.getFreeMemory() / 1024L / 1024L / 1024L);
/*    */       
/* 53 */       if (freeMemory < 4) {
/* 54 */         this.memory.setMin(freeMemory);
/* 55 */         this.memory.setMax(freeMemory);
/*    */       } else {
/* 57 */         setDefaultMemoryValues();
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 62 */     if (this.memory.getMin() > this.memory.getMax())
/* 63 */       this.memory.setMin(this.memory.getMax()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\option\dto\GameDistributionOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */