/*    */ package fr.paladium.core.utils.diagnostic;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DiagnosticSection {
/*    */   private String name;
/*    */   private List<DiagnosticItem> diagnostics;
/*    */   
/*    */   public String getName() {
/* 12 */     return this.name; } public List<DiagnosticItem> getDiagnostics() {
/* 13 */     return this.diagnostics;
/*    */   }
/*    */   public DiagnosticSection(String name) {
/* 16 */     this.name = name;
/* 17 */     this.diagnostics = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public static DiagnosticSection create(String name) {
/* 21 */     return new DiagnosticSection(name);
/*    */   }
/*    */   
/*    */   public DiagnosticSection add(String name, Class<? extends DiagnosticItem> diagnosticClass) {
/*    */     try {
/* 26 */       this.diagnostics.add(diagnosticClass.getDeclaredConstructor(new Class[] { String.class }).newInstance(new Object[] { name }));
/* 27 */     } catch (Exception e) {
/* 28 */       e.printStackTrace();
/*    */     } 
/* 30 */     return this;
/*    */   }
/*    */   
/*    */   public List<String> generate() {
/* 34 */     List<String> result = new LinkedList<>();
/* 35 */     boolean success = true;
/* 36 */     for (DiagnosticItem diagnostic : this.diagnostics) {
/* 37 */       boolean test = diagnostic.test();
/* 38 */       result.add("|-> " + getIcon(test) + " " + diagnostic.name);
/*    */       
/* 40 */       if (!test) {
/* 41 */         success = false;
/*    */       }
/*    */     } 
/* 44 */     result.add(0, getIcon(success) + " " + this.name);
/* 45 */     return result;
/*    */   }
/*    */   
/*    */   private String getIcon(boolean success) {
/* 49 */     return success ? "[✔]" : "[✘]";
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\DiagnosticSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */