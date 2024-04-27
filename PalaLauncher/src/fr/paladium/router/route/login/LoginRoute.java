/*    */ package fr.paladium.router.route.login;
/*    */ 
/*    */ import com.google.gson.internal.LinkedTreeMap;
/*    */ import fr.paladium.core.authentication.microsoft.MicrosoftAuthenticator;
/*    */ import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
/*    */ import fr.paladium.core.authentication.microsoft.response.profile.MinecraftProfile;
/*    */ import fr.paladium.core.authentication.microsoft.response.profile.MinecraftProfileDetailsStatus;
/*    */ import fr.paladium.core.controller.AccountController;
/*    */ import fr.paladium.core.controller.AuthenticationController;
/*    */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*    */ import fr.paladium.router.handler.CefRouteHandler;
/*    */ import fr.paladium.router.request.CefRouteRequest;
/*    */ import fr.paladium.router.route.ARoute;
/*    */ import fr.paladium.ui.CookieHelper;
/*    */ import fr.paladium.ui.frame.GamepassFrame;
/*    */ import fr.paladium.ui.frame.LauncherFrame;
/*    */ import java.awt.TrayIcon;
/*    */ import org.cef.browser.CefBrowser;
/*    */ import org.cef.browser.CefFrame;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoginRoute
/*    */   extends ARoute
/*    */ {
/*    */   public void init() {
/* 27 */     register("GET_LOGIN_LINK", this::handleGetLoginLink);
/* 28 */     register("GAMEPASS", this::handleGamepass);
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleGetLoginLink(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 32 */     CookieHelper.clear();
/* 33 */     return CefRouteHandler.create().success("link", MicrosoftAuthenticator.getLoginURL());
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleGamepass(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, LinkedTreeMap<?, ?> o) {
/* 37 */     if (GamepassFrame.getInstance() == null) {
/* 38 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_ALLOWED.getCode());
/*    */     }
/* 40 */     String username = (String)o.get("username");
/*    */     try {
/* 42 */       String token = GamepassFrame.getInstance().getToken();
/* 43 */       MinecraftProfile profile = MicrosoftAuthenticator.createMinecraftProfile(token, username);
/* 44 */       if (profile.getDetails().getStatus() == MinecraftProfileDetailsStatus.SUCCESS) {
/* 45 */         MinecraftAccount minecraftAccount = AuthenticationController.getInstance().getMinecraftAccount(GamepassFrame.getInstance().getOAuthToken());
/* 46 */         AccountController.getInstance().setCurrentAccount(minecraftAccount);
/* 47 */         LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/");
/* 48 */         GamepassFrame.getInstance().close();
/* 49 */         CookieHelper.clear();
/* 50 */         return CefRouteHandler.create().success();
/*    */       } 
/*    */       
/* 53 */       NotificationHelper.sendSystemNotification("Ce pseudonyme n'est pas valide.", TrayIcon.MessageType.ERROR);
/* 54 */     } catch (Exception e) {
/* 55 */       e.printStackTrace();
/* 56 */       NotificationHelper.sendSystemNotification("Une erreur est survenue lors de la création de votre compte minecraft.", TrayIcon.MessageType.ERROR);
/*    */     } 
/*    */     
/* 59 */     return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.INTERNAL_ERROR.getCode());
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\route\login\LoginRoute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */