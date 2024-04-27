/*    */ package fr.paladium.router.route;
/*    */ 
/*    */ public class RouteController
/*    */ {
/*    */   private static RouteController instance;
/*    */   
/*    */   public void addRoute(Class<? extends IRoute>... route) {
/*  8 */     for (Class<? extends IRoute> r : route) {
/*    */       try {
/* 10 */         IRoute routeInstance = r.newInstance();
/* 11 */         routeInstance.init();
/* 12 */       } catch (InstantiationException|IllegalAccessException e) {
/* 13 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static RouteController getInstance() {
/* 19 */     if (instance == null) {
/* 20 */       instance = new RouteController();
/*    */     }
/* 22 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\route\RouteController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */