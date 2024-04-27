/*   */ package fr.paladium.ui;
/*   */ 
/*   */ import org.cef.network.CefCookieManager;
/*   */ 
/*   */ public class CookieHelper
/*   */ {
/*   */   public static void clear() {
/* 8 */     CefCookieManager.getGlobalManager().deleteCookies("", "");
/*   */   }
/*   */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladiu\\ui\CookieHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */