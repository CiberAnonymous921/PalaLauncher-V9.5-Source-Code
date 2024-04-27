/*    */ package fr.paladium.router.route.option;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.internal.LinkedTreeMap;
/*    */ import fr.paladium.core.controller.OptionsController;
/*    */ import fr.paladium.core.utils.io.RamUtils;
/*    */ import fr.paladium.core.utils.option.Option;
/*    */ import fr.paladium.core.utils.option.dto.GameDistributionOptions;
/*    */ import fr.paladium.core.utils.session.SessionStorage;
/*    */ import fr.paladium.router.handler.CefRouteHandler;
/*    */ import fr.paladium.router.request.CefRouteRequest;
/*    */ import fr.paladium.router.route.ARoute;
/*    */ import org.cef.browser.CefBrowser;
/*    */ import org.cef.browser.CefFrame;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OptionRoute
/*    */   extends ARoute
/*    */ {
/*    */   public void init() {
/* 22 */     register("MEMORY", this::handleMemory);
/* 23 */     register("LAUNCHER_MAXIMIZED_AT_STARTUP", this::handleLauncherMaximizedAtStartup);
/* 24 */     register("GAME_SETTINGS", this::handleGameSettings);
/* 25 */     register("GAME_IS_INSTALLED", this::handleGameIsInstalled);
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleMemory(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, Object o) {
/* 29 */     JsonObject object = new JsonObject();
/* 30 */     object.addProperty("totalMem", Long.valueOf((long)Math.ceil(RamUtils.getRam().getTotalMemory() / 1024.0D / 1024.0D / 1024.0D)));
/* 31 */     object.addProperty("freeMem", Long.valueOf((long)Math.ceil(RamUtils.getRam().getFreeMemory() / 1024.0D / 1024.0D / 1024.0D)));
/* 32 */     return CefRouteHandler.create().success(object);
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleLauncherMaximizedAtStartup(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, LinkedTreeMap<?, ?> o) {
/* 36 */     if (type == CefRouteRequest.RequestType.GETTER) {
/* 37 */       return CefRouteHandler.create().success("launcherMaximizedAtStartup", OptionsController.getInstance().getSetting(Option.LAUNCHER_MAXIMIZED_AT_STARTUP, Boolean.valueOf(false)));
/*    */     }
/* 39 */     OptionsController.getInstance().setSetting(Option.LAUNCHER_MAXIMIZED_AT_STARTUP, o.get("launcherMaximizedAtStartup"));
/*    */ 
/*    */     
/* 42 */     return CefRouteHandler.create().success();
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleGameSettings(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, LinkedTreeMap<?, ?> o) {
/* 46 */     String distributionId = (String)o.get("id");
/*    */     
/* 48 */     if (type == CefRouteRequest.RequestType.GETTER) {
/* 49 */       GameDistributionOptions gameDistributionOptions = OptionsController.getInstance().getDistributionOptions(distributionId);
/* 50 */       gameDistributionOptions.checkInvalidRam();
/* 51 */       return CefRouteHandler.create().success(gameDistributionOptions);
/*    */     } 
/* 53 */     GameDistributionOptions options = new GameDistributionOptions();
/*    */     
/* 55 */     LinkedTreeMap<?, ?> memoryJson = (LinkedTreeMap<?, ?>)o.get("memory");
/*    */     
/* 57 */     options.getMemory().setMin((int)Double.parseDouble(memoryJson.get("min").toString()));
/* 58 */     options.getMemory().setMax((int)Double.parseDouble(memoryJson.get("max").toString()));
/*    */ 
/*    */     
/* 61 */     options.checkInvalidRam();
/*    */     
/* 63 */     LinkedTreeMap<?, ?> resolutionJson = (LinkedTreeMap<?, ?>)o.get("resolution");
/* 64 */     options.getResolution().setW((int)Double.parseDouble(resolutionJson.get("w").toString()));
/* 65 */     options.getResolution().setH((int)Double.parseDouble(resolutionJson.get("h").toString()));
/*    */     
/* 67 */     options.setStartInFullscreen(((Boolean)o.get("startInFullscreen")).booleanValue());
/* 68 */     options.setLauncherStayOpen(((Boolean)o.get("launcherStayOpen")).booleanValue());
/*    */     
/* 70 */     OptionsController.getInstance().setDistributionOptions(distributionId, options);
/*    */ 
/*    */     
/* 73 */     return CefRouteHandler.create().success();
/*    */   }
/*    */   
/*    */   private CefRouteHandler handleGameIsInstalled(CefBrowser cefBrowser, CefFrame cefFrame, CefRouteRequest.RequestType type, LinkedTreeMap<?, ?> o) {
/* 77 */     String gameId = (String)o.get("gameId");
/* 78 */     return CefRouteHandler.create().success("installed", SessionStorage.getInstance().getItem("game_" + gameId + "_installed", Boolean.valueOf(false), Boolean.class));
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\route\option\OptionRoute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */