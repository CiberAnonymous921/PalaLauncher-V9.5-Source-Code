/*    */ package fr.paladium.router.route.account;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.internal.LinkedTreeMap;
/*    */ import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIHandledException;
/*    */ import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
/*    */ import fr.paladium.core.controller.AccountController;
/*    */ import fr.paladium.core.controller.AuthenticationController;
/*    */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*    */ import fr.paladium.core.utils.format.UUIDUtils;
/*    */ import fr.paladium.router.handler.CefRouteHandler;
/*    */ import fr.paladium.router.request.CefRouteRequest;
/*    */ import fr.paladium.router.route.ARoute;
/*    */ import java.util.List;
/*    */ import org.cef.browser.CefBrowser;
/*    */ import org.cef.browser.CefFrame;
/*    */ 
/*    */ public class AccountRoute
/*    */   extends ARoute
/*    */ {
/*    */   public void init() {
/* 24 */     register("ACCOUNTS", this::handleAccounts);
/* 25 */     register("SWITCH_ACCOUNT", this::handleSwitchAccount);
/* 26 */     register("LOGOUT_ACCOUNT", this::handleLogoutAccount);
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleAccounts(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 30 */     if (AccountController.getInstance().getAccounts().isEmpty()) {
/* 31 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*    */     }
/*    */     
/* 34 */     List<MinecraftAccount> accounts = AccountController.getInstance().getAccounts();
/* 35 */     JsonArray array = new JsonArray();
/* 36 */     for (MinecraftAccount account : accounts) {
/* 37 */       JsonObject object = new JsonObject();
/* 38 */       object.addProperty("name", account.getName());
/* 39 */       object.addProperty("uuid", UUIDUtils.fromWithoutDashes(account.getId()).toString());
/* 40 */       array.add((JsonElement)object);
/*    */     } 
/*    */     
/* 43 */     return CefRouteHandler.create().success(array);
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleSwitchAccount(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, LinkedTreeMap<?, ?> treeMap) {
/* 47 */     String uuid = treeMap.get("uuid").toString();
/* 48 */     List<MinecraftAccount> accounts = AccountController.getInstance().getAccounts();
/* 49 */     MinecraftAccount loggedAccount = null;
/* 50 */     for (MinecraftAccount account : accounts) {
/* 51 */       if (UUIDUtils.fromWithoutDashes(account.getId()).toString().equals(uuid)) {
/* 52 */         loggedAccount = account;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/*    */     try {
/* 58 */       loggedAccount = AuthenticationController.getInstance().verify(loggedAccount);
/* 59 */     } catch (MicrosoftAPIHandledException e) {
/* 60 */       e.printStackTrace();
/* 61 */       NotificationHelper.sendNotification(e.getNotificationMessage());
/* 62 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/* 63 */     } catch (Exception e) {
/* 64 */       e.printStackTrace();
/* 65 */       NotificationHelper.sendNotification("Impossible de vérifier votre compte minecraft, veuillez vous reconnecter.");
/* 66 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*    */     } 
/*    */     
/*    */     try {
/* 70 */       AccountController.getInstance().setCurrentAccount(loggedAccount);
/* 71 */       return CefRouteHandler.create().success();
/* 72 */     } catch (Exception e) {
/* 73 */       e.printStackTrace();
/* 74 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*    */     } 
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleLogoutAccount(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, LinkedTreeMap<?, ?> treeMap) {
/* 79 */     String uuid = treeMap.get("uuid").toString();
/* 80 */     List<MinecraftAccount> accounts = AccountController.getInstance().getAccounts();
/* 81 */     for (MinecraftAccount account : accounts) {
/* 82 */       if (UUIDUtils.fromWithoutDashes(account.getId()).toString().equals(uuid)) {
/* 83 */         if (AccountController.getInstance().getCurrentAccount().equals(account)) {
/* 84 */           AccountController.getInstance().setCurrentAccount(null);
/*    */         }
/*    */         
/* 87 */         AccountController.getInstance().removeAccount(account);
/* 88 */         return CefRouteHandler.create().success();
/*    */       } 
/*    */     } 
/*    */     
/* 92 */     return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\route\account\AccountRoute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */