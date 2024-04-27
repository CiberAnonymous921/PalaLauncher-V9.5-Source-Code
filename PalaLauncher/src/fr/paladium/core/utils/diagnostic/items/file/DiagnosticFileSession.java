/*    */ package fr.paladium.core.utils.diagnostic.items.file;
/*    */ 
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DiagnosticFileSession
/*    */   extends DiagnosticItem
/*    */ {
/*    */   public DiagnosticFileSession(String name) {
/* 14 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 19 */     File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/* 20 */     File file = new File(installDir, ".JAVA_SESSION");
/*    */     
/* 22 */     return (file.exists() && file.canRead() && file.canWrite());
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\file\DiagnosticFileSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */