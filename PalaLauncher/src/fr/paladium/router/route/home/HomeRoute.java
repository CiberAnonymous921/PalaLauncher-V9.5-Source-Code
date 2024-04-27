/*     */ package fr.paladium.router.route.home;
/*     */ 
/*     */ import com.google.gson.internal.LinkedTreeMap;
/*     */ import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIHandledException;
/*     */ import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
/*     */ import fr.paladium.core.authentication.paladium.response.PaladiumAccount;
/*     */ import fr.paladium.core.controller.AccountController;
/*     */ import fr.paladium.core.controller.AuthenticationController;
/*     */ import fr.paladium.core.controller.GameController;
/*     */ import fr.paladium.core.controller.OracleController;
/*     */ import fr.paladium.core.utils.debugger.Debugger;
/*     */ import fr.paladium.core.utils.desktop.DesktopUtils;
/*     */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*     */ import fr.paladium.core.utils.format.UUIDUtils;
/*     */ import fr.paladium.core.utils.io.RamUtils;
/*     */ import fr.paladium.core.utils.memory.Ram;
/*     */ import fr.paladium.core.utils.net.Telemetry;
/*     */ import fr.paladium.router.handler.CefRouteHandler;
/*     */ import fr.paladium.router.request.CefRouteRequest;
/*     */ import fr.paladium.router.route.ARoute;
/*     */ import fr.paladium.ui.CookieHelper;
/*     */ import fr.paladium.ui.frame.LauncherFrame;
/*     */ import java.io.IOException;
/*     */ import org.cef.browser.CefBrowser;
/*     */ import org.cef.browser.CefFrame;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HomeRoute
/*     */   extends ARoute
/*     */ {
/*     */   public void init() {
/*  36 */     register("UUID", this::handleUUID);
/*  37 */     register("NAME", this::handleName);
/*  38 */     register("LAUNCH_GAME", this::handleLaunchGame);
/*  39 */     register("GET_TOKEN", this::handleGetToken);
/*     */ 
/*     */     
/*  42 */     register("OPEN_TICKET", this::handleOpenTicket);
/*  43 */     register("OPEN_TASK_MANAGER", this::handleOpenTaskManager);
/*  44 */     register("OPEN_FILE_EXPLORER", this::handleOpenFileExplorer);
/*     */   }
/*     */   
/*     */   private CefRouteHandler handleUUID(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/*  48 */     if (AccountController.getInstance().getCurrentAccount() == null) {
/*  49 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*     */     }
/*     */     
/*  52 */     return CefRouteHandler.create().success("uuid", UUIDUtils.fromWithoutDashes(AccountController.getInstance().getCurrentAccount().getId()));
/*     */   }
/*     */   
/*     */   private CefRouteHandler handleName(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/*  56 */     if (AccountController.getInstance().getCurrentAccount() == null) {
/*  57 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*     */     }
/*     */     
/*  60 */     return CefRouteHandler.create().success("name", AccountController.getInstance().getCurrentAccount().getName());
/*     */   }
/*     */   
/*     */   private CefRouteHandler handleLaunchGame(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, LinkedTreeMap<?, ?> o) {
/*  64 */     String gameId = (String)o.get("gameId");
/*  65 */     String url = (String)o.get("url");
/*  66 */     String distributionToken = (String)o.get("distributionToken");
/*  67 */     String distributionId = (String)o.get("settingsId");
/*     */     
/*  69 */     if (AccountController.getInstance().getCurrentAccount() == null) {
/*  70 */       LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/#/Login");
/*  71 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*     */     } 
/*     */     
/*  74 */     Debugger.pushState("handle_game_launch");
/*     */     try {
/*  76 */       System.out.println("Checking MSA...");
/*  77 */       MinecraftAccount account = AccountController.getInstance().getCurrentAccount();
/*  78 */       MinecraftAccount loggedAccount = AuthenticationController.getInstance().verify(account);
/*  79 */       if (loggedAccount == null) {
/*  80 */         AccountController.getInstance().removeAccount(account);
/*  81 */         if (AccountController.getInstance().getAccounts().isEmpty()) {
/*  82 */           AccountController.getInstance().setCurrentAccount(null);
/*  83 */           NotificationHelper.sendNotification("Votre compte Microsoft a expiré, veuillez vous reconnecter.");
/*  84 */           LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/#/Login");
/*     */         } else {
/*  86 */           AccountController.getInstance().setCurrentAccount(AccountController.getInstance().getAccounts().get(0));
/*  87 */           NotificationHelper.sendNotification("Votre compte Microsoft a expiré, vous avez été déconnecté.");
/*  88 */           LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/");
/*     */         } 
/*  90 */         Debugger.popState();
/*  91 */         return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*     */       } 
/*     */       
/*  94 */       (new Thread(() -> OracleController.getInstance().captureStartGame())).start();
/*     */       
/*  96 */       GameController.getInstance().startGame(distributionId, distributionToken, gameId, url, loggedAccount);
/*  97 */     } catch (IOException e) {
/*  98 */       e.printStackTrace();
/*  99 */       NotificationHelper.sendNotification("Une erreur est survenue lors de la vérification de votre compte Microsoft.");
/* 100 */       GameController.getInstance().sendProgress(100);
/* 101 */       Debugger.popState();
/* 102 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/* 103 */     } catch (MicrosoftAPIHandledException e) {
/* 104 */       e.printStackTrace();
/* 105 */       NotificationHelper.sendNotification(e);
/* 106 */       AccountController.getInstance().removeAccount(AccountController.getInstance().getCurrentAccount());
/* 107 */       if (AccountController.getInstance().getAccounts().isEmpty()) {
/* 108 */         AccountController.getInstance().setCurrentAccount(null);
/* 109 */         NotificationHelper.sendNotification("Votre compte Microsoft a expiré, veuillez vous reconnecter.");
/* 110 */         LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/#/Login");
/*     */       } else {
/* 112 */         AccountController.getInstance().setCurrentAccount(AccountController.getInstance().getAccounts().get(0));
/* 113 */         NotificationHelper.sendNotification("Votre compte Microsoft a expiré, vous avez été déconnecté.");
/* 114 */         LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/");
/*     */       } 
/* 116 */       Debugger.popState();
/* 117 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*     */     } 
/*     */     
/* 120 */     Debugger.popState();
/* 121 */     return CefRouteHandler.create().success();
/*     */   }
/*     */   
/*     */   private CefRouteHandler handleGetToken(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 125 */     MinecraftAccount account = AccountController.getInstance().getCurrentAccount();
/* 126 */     if (account == null) {
/* 127 */       LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/#/Login");
/* 128 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*     */     } 
/*     */     
/*     */     try {
/* 132 */       if (AccountController.getInstance().getPaladiumAccountMap().containsKey(account)) {
/* 133 */         PaladiumAccount paladiumAccount1 = (PaladiumAccount)AccountController.getInstance().getPaladiumAccountMap().get(account);
/* 134 */         if (!paladiumAccount1.verify(account)) {
/* 135 */           paladiumAccount1.authenticate(account);
/*     */         }
/*     */         
/* 138 */         return CefRouteHandler.create().success(((PaladiumAccount)AccountController.getInstance().getPaladiumAccountMap().get(account)).getJwt());
/*     */       } 
/*     */       
/* 141 */       PaladiumAccount paladiumAccount = new PaladiumAccount(account);
/* 142 */       if (!paladiumAccount.isConnected()) {
/* 143 */         return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.INTERNAL_ERROR.getCode());
/*     */       }
/*     */       
/* 146 */       if (paladiumAccount == null || paladiumAccount.getJwt() == null) {
/* 147 */         AccountController.getInstance().setCurrentAccount(null);
/* 148 */         AccountController.getInstance().removeAccount(account);
/* 149 */         CookieHelper.clear();
/*     */         
/* 151 */         DesktopUtils.openURL("https://support.xbox.com/fr-FR/help/family-online-safety/online-safety/manage-online-safety-and-privacy-settings-xbox-one");
/* 152 */         NotificationHelper.sendNotification("Il semblerait que votre compte n'ait pas les permissions nécessaires pour se connecter à Paladium Games.");
/* 153 */         LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/#/Login");
/* 154 */         return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.NOT_FOUND.getCode());
/*     */       } 
/*     */       
/* 157 */       AccountController.getInstance().getPaladiumAccountMap().put(account, paladiumAccount);
/* 158 */       return CefRouteHandler.create().success(paladiumAccount.getJwt());
/* 159 */     } catch (Exception e) {
/* 160 */       e.printStackTrace();
/* 161 */       NotificationHelper.sendNotification("Impossible de vérifier votre compte Paladium Games.");
/* 162 */       Telemetry.collect(Telemetry.Type.ERR_AUTH_PALADIUM_TOKEN);
/* 163 */       return CefRouteHandler.create().failure(CefRouteHandler.StatusCodes.INTERNAL_ERROR.getCode());
/*     */     } 
/*     */   }
/*     */   
/*     */   private CefRouteHandler handleOpenTicket(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 168 */     DesktopUtils.openURL("https://report.paladium.games");
/* 169 */     return CefRouteHandler.create().success();
/*     */   }
/*     */   
/*     */   private CefRouteHandler handleOpenTaskManager(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 173 */     Ram ram = RamUtils.getRam();
/* 174 */     ram.openTaskManager();
/* 175 */     return CefRouteHandler.create().success();
/*     */   }
/*     */   
/*     */   private CefRouteHandler handleOpenFileExplorer(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 179 */     DesktopUtils.openExplorer();
/* 180 */     return CefRouteHandler.create().success();
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\route\home\HomeRoute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */