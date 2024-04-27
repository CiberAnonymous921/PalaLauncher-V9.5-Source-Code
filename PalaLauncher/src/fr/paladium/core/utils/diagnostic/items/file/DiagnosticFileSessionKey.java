/*    */ package fr.paladium.core.utils.diagnostic.items.file;
/*    */ 
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ public class DiagnosticFileSessionKey
/*    */   extends DiagnosticItem
/*    */ {
/*    */   public DiagnosticFileSessionKey(String name) {
/* 12 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 17 */     File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/* 18 */     File file = new File(installDir, ".plkf.key");
/*    */     
/* 20 */     return (file.exists() && file.canRead() && file.canWrite());
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\file\DiagnosticFileSessionKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */