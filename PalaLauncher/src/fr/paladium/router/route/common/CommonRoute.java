/*    */ package fr.paladium.router.route.common;
/*    */ 
/*    */ import com.google.gson.internal.LinkedTreeMap;
/*    */ import fr.paladium.core.distribution.dto.DistributionOS;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import fr.paladium.router.handler.CefRouteHandler;
/*    */ import fr.paladium.router.request.CefRouteRequest;
/*    */ import fr.paladium.router.route.ARoute;
/*    */ import fr.paladium.ui.frame.LauncherFrame;
/*    */ import fr.paladium.ui.frame.PopupFrame;
/*    */ import java.awt.Dimension;
/*    */ import org.cef.browser.CefBrowser;
/*    */ import org.cef.browser.CefFrame;
/*    */ 
/*    */ 
/*    */ public class CommonRoute
/*    */   extends ARoute
/*    */ {
/*    */   public void init() {
/* 20 */     register("GET_OS", this::handleGetOS);
/* 21 */     register("CLOSE_WINDOW", this::handleCloseWindow);
/* 22 */     register("MINIMIZE_WINDOW", this::handleMinimizeWindow);
/* 23 */     register("RESTORE_WINDOW", this::handleRestoreWindow);
/* 24 */     register("MAXIMIZE_WINDOW", this::handleMaximizeWindow);
/* 25 */     register("RESIZE", this::handleResize);
/* 26 */     register("POPUP", this::handlePopup);
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleGetOS(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 30 */     return CefRouteHandler.create().success("os", (OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) ? "windows" : ((OsCheck.getOperatingSystemType() == DistributionOS.MACOS) ? "darwin" : ((OsCheck.getOperatingSystemType() == DistributionOS.LINUX) ? "linux" : "windows")));
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleCloseWindow(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 34 */     LauncherFrame.getInstance().close();
/* 35 */     return CefRouteHandler.create().success();
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleMinimizeWindow(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 39 */     if ((LauncherFrame.getInstance().getBuilder()).frame != null) {
/* 40 */       (LauncherFrame.getInstance().getBuilder()).frame.setState(1);
/*    */     }
/* 42 */     return CefRouteHandler.create().success();
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleRestoreWindow(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 46 */     if ((LauncherFrame.getInstance().getBuilder()).frame != null) {
/* 47 */       (LauncherFrame.getInstance().getBuilder()).frame.setExtendedState(0);
/*    */     }
/* 49 */     return CefRouteHandler.create().success();
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleMaximizeWindow(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 53 */     if ((LauncherFrame.getInstance().getBuilder()).frame != null) {
/* 54 */       (LauncherFrame.getInstance().getBuilder()).frame.setExtendedState(6);
/*    */     }
/* 56 */     return CefRouteHandler.create().success();
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleResize(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, LinkedTreeMap<?, ?> o) {
/* 60 */     int width = ((Double)o.get("width")).intValue();
/* 61 */     int height = ((Double)o.get("height")).intValue();
/* 62 */     (LauncherFrame.getInstance().getBuilder()).frame.setMinimumSize(new Dimension(width, height));
/* 63 */     (LauncherFrame.getInstance().getBuilder()).frame.setSize(width, height);
/* 64 */     (LauncherFrame.getInstance().getBuilder()).frame.setLocationRelativeTo(null);
/* 65 */     return CefRouteHandler.create().success();
/*    */   }
/*    */   
/*    */   private CefRouteHandler handlePopup(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, LinkedTreeMap<?, ?> o) {
/*    */     try {
/* 70 */       new PopupFrame(o.get("title").toString(), o.get("url").toString(), ((Double)o.get("width")).intValue(), ((Double)o.get("height")).intValue());
/* 71 */       return CefRouteHandler.create().success();
/* 72 */     } catch (Exception e) {
/* 73 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\route\common\CommonRoute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */