/*   */ package fr.paladium.router.route;
/*   */ 
/*   */ import fr.paladium.router.handler.CefRouteHandlerBuilder;
/*   */ import fr.paladium.router.handler.ICefRouteHandler;
/*   */ 
/*   */ public abstract class ARoute
/*   */   implements IRoute {
/*   */   public <T> void register(String route, ICefRouteHandler<T> handler) {
/* 9 */     CefRouteHandlerBuilder.builder().route(route).handler(handler).build();
/*   */   }
/*   */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\route\ARoute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */