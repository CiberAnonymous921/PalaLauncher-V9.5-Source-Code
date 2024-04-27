/*    */ package fr.paladium.router.request;
/*    */ 
/*    */ public class CefRouteRequest {
/*    */   private RequestType type;
/*    */   private String name;
/*    */   private Object payload;
/*    */   
/*  8 */   public RequestType getType() { return this.type; }
/*  9 */   public String getName() { return this.name; } public Object getPayload() {
/* 10 */     return this.payload;
/*    */   }
/*    */   
/* 13 */   public enum RequestType { ACTION,
/* 14 */     SETTER,
/* 15 */     GETTER; }
/*    */ 
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\request\CefRouteRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */