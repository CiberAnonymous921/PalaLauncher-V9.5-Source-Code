/*    */ package fr.paladium.router.handler;
/*    */ 
/*    */ import fr.paladium.router.LauncherMessageRouter;
/*    */ 
/*    */ public class CefRouteHandlerBuilder
/*    */ {
/*    */   private String route;
/*    */   private ICefRouteHandler<?> handler;
/*    */   
/*    */   public static CefRouteHandlerBuilder builder() {
/* 11 */     return new CefRouteHandlerBuilder();
/*    */   }
/*    */   
/*    */   public CefRouteHandlerBuilder route(String route) {
/* 15 */     this.route = route;
/* 16 */     return this;
/*    */   }
/*    */   
/*    */   public CefRouteHandlerBuilder handler(ICefRouteHandler<?> handler) {
/* 20 */     this.handler = handler;
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   public void build() {
/* 25 */     LauncherMessageRouter.registerRoute(this.route, this.handler);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\handler\CefRouteHandlerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */