/*    */ package fr.paladium.router.route.splash;
/*    */ 
/*    */ import fr.paladium.core.controller.AccountController;
/*    */ import fr.paladium.router.handler.CefRouteHandler;
/*    */ import fr.paladium.router.request.CefRouteRequest;
/*    */ import fr.paladium.router.route.ARoute;
/*    */ import org.cef.browser.CefBrowser;
/*    */ import org.cef.browser.CefFrame;
/*    */ 
/*    */ public class SplashRoute
/*    */   extends ARoute
/*    */ {
/*    */   public void init() {
/* 14 */     register("IS_LOGGED", this::handleIsLogged);
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleIsLogged(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 18 */     if (AccountController.getInstance().getCurrentAccount() == null && !AccountController.getInstance().getAccounts().isEmpty()) {
/* 19 */       AccountController.getInstance().setCurrentAccount(AccountController.getInstance().getAccounts().get(0));
/*    */     }
/*    */     
/* 22 */     if (AccountController.getInstance().getCurrentAccount() == null) {
/* 23 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*    */     }
/*    */     
/* 26 */     return CefRouteHandler.create().success();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\route\splash\SplashRoute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */