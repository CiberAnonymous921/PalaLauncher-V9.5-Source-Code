/*    */ package fr.paladium.router.handler;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import fr.paladium.core.utils.json.JsonOnlineParser;
/*    */ 
/*    */ public class CefRouteHandler {
/*    */   private int status;
/*    */   private Object payload;
/*    */   
/* 10 */   public int getStatus() { return this.status; } public Object getPayload() {
/* 11 */     return this.payload;
/*    */   }
/*    */   public static CefRouteHandler create() {
/* 14 */     return new CefRouteHandler();
/*    */   }
/*    */   
/*    */   public CefRouteHandler success(Object payload) {
/* 18 */     this.status = StatusCodes.OK.getCode();
/* 19 */     this.payload = payload;
/* 20 */     return this;
/*    */   }
/*    */   
/*    */   public CefRouteHandler success() {
/* 24 */     this.status = StatusCodes.OK.getCode();
/* 25 */     this.payload = new JsonObject();
/* 26 */     return this;
/*    */   }
/*    */   
/*    */   public CefRouteHandler success(String key, Object value) {
/* 30 */     this.status = StatusCodes.OK.getCode();
/* 31 */     JsonObject object = new JsonObject();
/* 32 */     object.add(key, JsonOnlineParser.GSON.toJsonTree(value));
/* 33 */     this.payload = object;
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public CefRouteHandler success(String key, String value) {
/* 38 */     this.status = StatusCodes.OK.getCode();
/* 39 */     JsonObject object = new JsonObject();
/* 40 */     object.addProperty(key, value);
/* 41 */     this.payload = object;
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public CefRouteHandler failure(int status, Object payload) {
/* 46 */     this.status = status;
/* 47 */     this.payload = payload;
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   public CefRouteHandler failure(int status) {
/* 52 */     this.status = status;
/* 53 */     this.payload = new JsonObject();
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public enum StatusCodes {
/* 58 */     OK(200),
/* 59 */     NOT_FOUND(404),
/* 60 */     NOT_ALLOWED(405),
/* 61 */     INTERNAL_ERROR(500);
/*    */     
/*    */     private int code;
/*    */     
/*    */     StatusCodes(int code) {
/* 66 */       this.code = code;
/*    */     }
/*    */     
/*    */     public int getCode() {
/* 70 */       return this.code;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\handler\CefRouteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */