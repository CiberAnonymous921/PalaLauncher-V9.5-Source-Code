/*    */ package fr.paladium.core.utils.debugger;
/*    */ 
/*    */ import fr.paladium.core.utils.io.FileUtils;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Debugger
/*    */ {
/*    */   private static String activeState;
/*    */   
/*    */   public static void debugData() {
/* 15 */     DebugData debugData = new DebugData();
/* 16 */     File executionFile = new File(debugData.getExecutionPath());
/*    */     
/* 18 */     System.out.println("This is a debug data, please send it to the developer if you have a problem.");
/* 19 */     System.out.println("--------------------");
/* 20 */     System.out.println("DOS: " + debugData.getDistributionOS());
/* 21 */     System.out.println("OS: " + debugData.getOs());
/* 22 */     System.out.println("JAVA: " + debugData.getJavaVersion());
/*    */     try {
/* 24 */       System.out.println("PATH: " + debugData.getExecutionPath() + " (" + ((executionFile.exists() && !executionFile.isDirectory()) ? FileUtils.getSha1(executionFile) : "DOES NOT EXIST") + ")");
/* 25 */     } catch (Exception|Error e) {
/* 26 */       System.out.println("PATH: " + debugData.getExecutionPath() + " (ERRORED)");
/*    */     } 
/* 28 */     System.out.println("----");
/* 29 */     System.out.println("AppData: " + debugData.getAppData());
/* 30 */     System.out.println("----");
/* 31 */     System.out.println("CPU: " + debugData.getCpu());
/* 32 */     System.out.println("RAM: " + debugData.getRam());
/* 33 */     System.out.println("----");
/* 34 */     System.out.println("Date: " + debugData.getDate());
/* 35 */     System.out.println("--------------------");
/*    */   }
/*    */   
/*    */   public static void pushState(String state) {
/* 39 */     activeState = state.toUpperCase();
/* 40 */     System.out.println("Entering state " + state.toUpperCase());
/*    */   }
/*    */   
/*    */   public static void popState() {
/* 44 */     System.out.println("Leaving state " + activeState);
/* 45 */     activeState = null;
/*    */   }
/*    */   
/*    */   public static String getActiveState() {
/* 49 */     return activeState;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\debugger\Debugger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */