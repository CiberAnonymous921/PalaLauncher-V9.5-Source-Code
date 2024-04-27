/*     */ package fr.paladium.ui.frame;
/*     */ 
/*     */ import fr.paladium.core.authentication.microsoft.MicrosoftAuthenticator;
/*     */ import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIExceptionHandler;
/*     */ import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIHandledException;
/*     */ import fr.paladium.core.authentication.microsoft.response.MicrosoftMinecraftAccount;
/*     */ import fr.paladium.core.authentication.microsoft.response.MicrosoftOAuthToken;
/*     */ import fr.paladium.core.authentication.microsoft.response.MinecraftAccount;
/*     */ import fr.paladium.core.authentication.microsoft.response.MinecraftProductType;
/*     */ import fr.paladium.core.controller.AccountController;
/*     */ import fr.paladium.core.controller.AuthenticationController;
/*     */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*     */ import fr.paladium.core.utils.net.Telemetry;
/*     */ import fr.paladium.ui.CefFrame;
/*     */ import fr.paladium.ui.CookieHelper;
/*     */ import java.awt.TrayIcon;
/*     */ import java.io.IOException;
/*     */ import me.friwi.jcefmaven.CefInitializationException;
/*     */ import me.friwi.jcefmaven.UnsupportedPlatformException;
/*     */ 
/*     */ 
/*     */ public class PopupFrame
/*     */   extends CefFrame
/*     */ {
/*     */   public PopupFrame(String title, String redirectUrl, int width, int height) throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
/*  26 */     super(title, redirectUrl, width, height, false, false);
/*     */     
/*  28 */     getBuilder().addURLCallback(url -> {
/*     */           if (url.contains("#error")) {
/*     */             System.out.println("Error while logging in with microsoft account (" + url + ")");
/*     */             
/*     */             close();
/*     */             
/*     */             return Boolean.valueOf(true);
/*     */           } 
/*     */           return Boolean.valueOf(false);
/*     */         });
/*  38 */     getBuilder().addURLCallback(url -> {
/*     */           if (MicrosoftAuthenticator.isCodeURL(url)) {
/*     */             try {
/*     */               String code = MicrosoftAuthenticator.getCodeFromURL(url);
/*     */               
/*     */               MicrosoftOAuthToken token = MicrosoftAuthenticator.loginWithCode(code);
/*     */               
/*     */               MicrosoftMinecraftAccount account = AuthenticationController.getInstance().getMicrosoftAccount(token);
/*     */               
/*     */               MinecraftProductType minecraftProduct = MicrosoftAuthenticator.getMinecraftProduct(account.getAccessToken());
/*     */               if (minecraftProduct == MinecraftProductType.NONE) {
/*     */                 System.out.println("Unable to find minecraft product with this account (" + code + ")");
/*     */                 NotificationHelper.sendSystemNotification("Votre compte ne possède pas Minecraft Java Edition", TrayIcon.MessageType.ERROR);
/*     */                 close();
/*     */                 return Boolean.valueOf(true);
/*     */               } 
/*     */               if (minecraftProduct == MinecraftProductType.PURCHASE) {
/*     */                 System.out.println("Found minecraft product with this account (" + code + ")");
/*     */                 MinecraftAccount minecraftAccount = AuthenticationController.getInstance().getMinecraftAccount(token);
/*     */                 AccountController.getInstance().setCurrentAccount(minecraftAccount);
/*     */                 /*TODO: Default:*/ LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/");
/*     */                 //LauncherFrame.getInstance().load("https://paladium.netlify.app/");
/*     */                 close();
/*     */                 return Boolean.valueOf(true);
/*     */               } 
/*     */               if (minecraftProduct == MinecraftProductType.GAMEPASS) {
/*     */                 System.out.println("Found minecraft gamepass with this account (" + code + "), try to create minecraft account");
/*     */                 try {
/*     */                   MinecraftAccount minecraftAccount = AuthenticationController.getInstance().getMinecraftAccount(token);
/*     */                   if (minecraftAccount != null) {
/*     */                     System.out.println("Found existing minecraft account with this account (" + code + ")");
/*     */                     AccountController.getInstance().setCurrentAccount(minecraftAccount);
/*     */                     /*TODO: Default:*/ LauncherFrame.getInstance().load("https://palagames-launcher-front.pages.dev/");
/*     */                     //LauncherFrame.getInstance().load("https://paladium.netlify.app/");
/*     */                     close();
/*     */                     return Boolean.valueOf(true);
/*     */                   } 
/*     */                   try {
/*     */                     System.out.println("Try to create minecraft account with this account (" + code + ")");
/*     */                     new GamepassFrame(token, account.getAccessToken());
/*     */                     NotificationHelper.sendSystemNotification("Vous devez créer un compte Minecraft pour pouvoir jouer", TrayIcon.MessageType.INFO);
/*  77 */                   } catch (Exception e1) {
/*     */                     e1.printStackTrace();
/*     */                     NotificationHelper.sendNotification("Impossible de charger votre compte gamepass");
/*     */                   } 
/*  81 */                 } catch (MicrosoftAPIHandledException e) {
/*     */                   if (e.getError() == MicrosoftAPIExceptionHandler.Error.MINECRAFT_PROFILE_DOESNT_EXIST) {
/*     */                     try {
/*     */                       System.out.println("Try to create minecraft account with this account (" + code + ")");
/*     */                       new GamepassFrame(token, account.getAccessToken());
/*     */                       NotificationHelper.sendSystemNotification("Vous devez créer un compte Minecraft pour pouvoir jouer", TrayIcon.MessageType.INFO);
/*  87 */                     } catch (Exception e1) {
/*     */                       e1.printStackTrace();
/*     */                       
/*     */                       NotificationHelper.sendNotification("Impossible de charger votre compte gamepass");
/*     */                     } 
/*     */                   } else {
/*     */                     e.printStackTrace();
/*     */                     NotificationHelper.sendNotification(e);
/*     */                   } 
/*     */                 } 
/*     */                 close();
/*     */                 return Boolean.valueOf(true);
/*     */               } 
/* 100 */             } catch (IOException e) {
/*     */               e.printStackTrace();
/*     */               NotificationHelper.sendNotification("Impossible de se connecter aux serveurs de Microsoft");
/*     */               Telemetry.collect(Telemetry.Type.ERR_AUTH_MICROSOFT);
/*     */               close();
/*     */               return Boolean.valueOf(true);
/* 106 */             } catch (MicrosoftAPIHandledException e) {
/*     */               e.printStackTrace();
/*     */               
/*     */               NotificationHelper.sendNotification(e);
/*     */               
/*     */               close();
/*     */               return Boolean.valueOf(true);
/*     */             } 
/*     */           }
/*     */           return Boolean.valueOf(false);
/*     */         });
/* 117 */     getBuilder().addURLCallback(url -> {
/*     */           if (MicrosoftAuthenticator.isTokenURL(url)) {
/*     */             String token = MicrosoftAuthenticator.getTokenFromURL(url);
/*     */             if (token != null && !token.isEmpty()) {
/*     */               load(MicrosoftAuthenticator.getLoginURL());
/*     */               return Boolean.valueOf(true);
/*     */             } 
/*     */           } 
/*     */           return Boolean.valueOf(false);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 132 */     super.close();
/* 133 */     CookieHelper.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladiu\\ui\frame\PopupFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */