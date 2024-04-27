/*    */ package fr.paladium.router;
/*    */ 
/*    */ import com.google.gson.GsonBuilder;
/*    */ import fr.paladium.core.utils.debugger.Debugger;
/*    */ import fr.paladium.core.utils.json.JsonOnlineParser;
/*    */ import fr.paladium.router.handler.CefRouteHandler;
/*    */ import fr.paladium.router.handler.ICefRouteHandler;
/*    */ import fr.paladium.router.request.CefRouteRequest;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.cef.browser.CefBrowser;
/*    */ import org.cef.browser.CefFrame;
/*    */ import org.cef.callback.CefQueryCallback;
/*    */ import org.cef.handler.CefMessageRouterHandler;
/*    */ 
/*    */ public class LauncherMessageRouter
/*    */   implements CefMessageRouterHandler
/*    */ {
/* 19 */   private static final Map<String, ICefRouteHandler<?>> routes = new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent, CefQueryCallback callback) {
/* 23 */     CefRouteRequest routeRequest = (CefRouteRequest)JsonOnlineParser.GSON.fromJson(request, CefRouteRequest.class);
/* 24 */     String routeName = routeRequest.getName();
/* 25 */     if (routes.containsKey(routeName)) {
/* 26 */       ICefRouteHandler handler = routes.get(routeName);
/* 27 */       CefRouteHandler response = handler.handle(browser, frame, routeRequest.getType(), routeRequest.getPayload());
/* 28 */       callback.success(JsonOnlineParser.GSON.toJson(response));
/* 29 */       return true;
/*    */     } 
/*    */     
/* 32 */     Debugger.pushState("cef_query");
/* 33 */     System.out.println("[Warn] Route not found: " + routeName);
/* 34 */     System.out.println("[Warn] Request: " + (new GsonBuilder()).setPrettyPrinting().create().toJson(routeRequest));
/* 35 */     Debugger.popState();
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onQueryCanceled(CefBrowser browser, CefFrame frame, long queryId) {}
/*    */ 
/*    */   
/*    */   public void setNativeRef(String identifier, long nativeRef) {}
/*    */ 
/*    */   
/*    */   public long getNativeRef(String identifier) {
/* 47 */     return 0L;
/*    */   }
/*    */   
/*    */   public static void registerRoute(String route, ICefRouteHandler<?> handler) {
/* 51 */     routes.put(route, handler);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\LauncherMessageRouter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */