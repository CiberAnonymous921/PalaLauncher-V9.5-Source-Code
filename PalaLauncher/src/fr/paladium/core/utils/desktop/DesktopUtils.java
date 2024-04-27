/*    */ package fr.paladium.core.utils.desktop;
/*    */ 
/*    */ import java.awt.Desktop;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ 
/*    */ public class DesktopUtils
/*    */ {
/*    */   public static void openURL(String url) {
/* 11 */     if (lastURL != null && lastURL.equals(url)) {
/*    */       return;
/*    */     }
/*    */     
/* 15 */     lastURL = url;
/* 16 */     if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
/*    */       try {
/* 18 */         Desktop.getDesktop().browse(URI.create(url));
/* 19 */       } catch (IOException e) {
/* 20 */         e.printStackTrace();
/*    */       }  
/*    */   }
/*    */   
/*    */   private static String lastURL;
/*    */   
/*    */   public static void openExplorer() {
/* 27 */     if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN))
/*    */       try {
/* 29 */         Desktop.getDesktop().open(new File(System.getProperty("user.home")));
/* 30 */       } catch (IOException e) {
/* 31 */         e.printStackTrace();
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\desktop\DesktopUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */