/*     */ package fr.paladium.ui;
/*     */ 
/*     */ import fr.paladium.Launcher;
/*     */ import fr.paladium.core.distribution.dto.DistributionOS;
/*     */ import fr.paladium.core.utils.desktop.DesktopUtils;
/*     */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*     */ import fr.paladium.core.utils.io.OsCheck;
/*     */ import fr.paladium.core.utils.tray.PaladiumSystemTray;
/*     */ import fr.paladium.router.LauncherMessageRouter;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.MouseInfo;
/*     */ import java.awt.TrayIcon;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import javax.swing.JFrame;
/*     */ import me.friwi.jcefmaven.CefAppBuilder;
/*     */ import me.friwi.jcefmaven.EnumProgress;
/*     */ import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.cef.CefApp;
/*     */ import org.cef.CefClient;
/*     */ import org.cef.CefSettings;
/*     */ import org.cef.browser.CefBrowser;
/*     */ import org.cef.browser.CefFrame;
/*     */ import org.cef.browser.CefMessageRouter;
/*     */ import org.cef.callback.CefContextMenuParams;
/*     */ import org.cef.callback.CefMenuModel;
/*     */ import org.cef.handler.CefContextMenuHandler;
/*     */ import org.cef.handler.CefContextMenuHandlerAdapter;
/*     */ import org.cef.handler.CefDisplayHandler;
/*     */ import org.cef.handler.CefDisplayHandlerAdapter;
/*     */ import org.cef.handler.CefFocusHandler;
/*     */ import org.cef.handler.CefFocusHandlerAdapter;
/*     */ import org.cef.handler.CefKeyboardHandler;
/*     */ import org.cef.handler.CefKeyboardHandlerAdapter;
/*     */ import org.cef.handler.CefLifeSpanHandler;
/*     */ import org.cef.handler.CefLifeSpanHandlerAdapter;
/*     */ import org.cef.handler.CefMessageRouterHandler;
/*     */ 
/*     */ public class CefFrameBuilder {
/*     */   public static CefApp app;
/*     */   private EnumProgress lastState;
/*     */   private long lastDownloadUpdate;
/*     */   public JFrame frame;
/*  49 */   private final List<Function<String, Boolean>> urlCallbacks = new ArrayList<>(); public CefClient client; public CefBrowser browser; public CefMessageRouter msgRouter; public boolean focused = true;
/*  50 */   private final List<Consumer<String>> urlConsumers = new ArrayList<>();
/*     */   
/*     */   public CefFrameBuilder(JFrame frame, String URL, boolean listen) {
/*  53 */     build(frame, URL, listen);
/*     */   }
/*     */   
/*     */   public void build(JFrame frame, String URL, boolean listen) {
/*  57 */     System.setProperty("java.awt.headless", "false");
/*  58 */     this.frame = frame;
/*     */     
/*  60 */     if (app == null) {
/*  61 */       File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/*  62 */       File jcefInstallDir = new File(installDir, "internal");
/*  63 */       CefAppBuilder builder = new CefAppBuilder();
/*     */       
/*  65 */       builder.setInstallDir(jcefInstallDir);
/*  66 */       builder.setProgressHandler((state, percent) -> {
/*     */             if (state != this.lastState) {
/*     */               this.lastState = state;
/*     */               
/*     */               System.out.println("[CefInterCom] " + state);
/*     */             } 
/*     */             
/*     */             if (System.currentTimeMillis() - this.lastDownloadUpdate < 5000L) {
/*     */               return;
/*     */             }
/*     */             if (state == EnumProgress.DOWNLOADING) {
/*     */               if (percent == -1.0F) {
/*     */                 NotificationHelper.sendSystemNotification("Téléchargement des outils internes.", TrayIcon.MessageType.INFO);
/*     */               } else {
/*     */                 NotificationHelper.sendSystemNotification("Téléchargement des outils internes (" + (int)percent + "%)", TrayIcon.MessageType.INFO);
/*     */               } 
/*     */               this.lastDownloadUpdate = System.currentTimeMillis();
/*     */             } else if (state == EnumProgress.EXTRACTING) {
/*     */               NotificationHelper.sendSystemNotification("Extraction des outils internes.", TrayIcon.MessageType.INFO);
/*     */             } else if (state == EnumProgress.INSTALL) {
/*     */               NotificationHelper.sendSystemNotification("Installation des outils internes.", TrayIcon.MessageType.INFO);
/*     */             } 
/*     */           });
/*  89 */       (builder.getCefSettings()).windowless_rendering_enabled = false;
/*  90 */       (builder.getCefSettings()).log_severity = CefSettings.LogSeverity.LOGSEVERITY_DISABLE;
/*  91 */       builder.addJcefArgs(new String[] { "--force-device-scale-factor=1" });
/*  92 */       builder.setAppHandler(new MavenCefAppHandlerAdapter()
/*     */           {
/*     */             public void stateHasChanged(CefApp.CefAppState state) {
/*  95 */               if (state == CefApp.CefAppState.TERMINATED || state == CefApp.CefAppState.SHUTTING_DOWN) {
/*  96 */                 Launcher.close(0);
/*     */               }
/*     */             }
/*     */           });
/*     */       
/*     */       try {
/* 102 */         app = builder.build();
/* 103 */       } catch (Exception|UnsatisfiedLinkError e) {
/* 104 */         System.out.println("[Cef] Failed to build JCEF.");
/*     */         try {
/* 106 */           Field field = CefAppBuilder.class.getDeclaredField("installDir");
/* 107 */           field.setAccessible(true);
/* 108 */           File localInstallDir = (File)field.get(builder);
/* 109 */           System.out.println("[Cef] InstallDir " + localInstallDir.getAbsolutePath() + " [" + localInstallDir.exists() + "]");
/* 110 */         } catch (Exception ex) {
/* 111 */           ex.printStackTrace();
/*     */         } 
/* 113 */         NotificationHelper.sendSystemNotification("L'installation du Paladium Games Launcher est corrompue.", TrayIcon.MessageType.ERROR);
/* 114 */         e.printStackTrace();
/*     */         try {
/* 116 */           Thread.sleep(3000L);
/* 117 */           FileUtils.forceDeleteOnExit(jcefInstallDir);
/* 118 */           Launcher.close(-1);
/* 119 */         } catch (Exception ex) {
/* 120 */           ex.printStackTrace();
/* 121 */           Launcher.close(-1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     this.client = app.createClient();
/* 127 */     this.client.addKeyboardHandler((CefKeyboardHandler)new CefKeyboardHandlerAdapter()
/*     */         {
/*     */           public boolean onKeyEvent(CefBrowser browser, CefKeyboardHandler.CefKeyEvent event)
/*     */           {
/* 131 */             if (event.modifiers == 4 && event.windows_key_code == 77 && event.type == CefKeyboardHandler.CefKeyEvent.EventType.KEYEVENT_KEYUP && 
/* 132 */               !PaladiumSystemTray.getTrayMenu().isVisible()) {
/* 133 */               PaladiumSystemTray.getTrayMenu().setVisible(true);
/*     */               
/* 135 */               if (OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) {
/* 136 */                 PaladiumSystemTray.getTrayMenu().setLocation(MouseInfo.getPointerInfo().getLocation());
/*     */               }
/* 138 */               return true;
/*     */             } 
/*     */ 
/*     */             
/* 142 */             return super.onKeyEvent(browser, event);
/*     */           }
/*     */         });
/* 145 */     this.client.addLifeSpanHandler((CefLifeSpanHandler)new CefLifeSpanHandlerAdapter()
/*     */         {
/*     */           public boolean onBeforePopup(CefBrowser browser, CefFrame frame, String targetUrl, String targetFrame) {
/* 148 */             DesktopUtils.openURL(targetUrl);
/* 149 */             return super.onBeforePopup(browser, frame, targetUrl, targetFrame);
/*     */           }
/*     */         });
/* 152 */     this.browser = this.client.createBrowser(URL, false, false);
/*     */     
/* 154 */     if (listen) {
/* 155 */       this.msgRouter = CefMessageRouter.create();
/* 156 */       this.msgRouter.addHandler((CefMessageRouterHandler)new LauncherMessageRouter(), true);
/* 157 */       this.client.addMessageRouter(this.msgRouter);
/*     */     } 
/*     */     
/* 160 */     this.client.addContextMenuHandler((CefContextMenuHandler)new CefContextMenuHandlerAdapter()
/*     */         {
/*     */           public void onBeforeContextMenu(CefBrowser browser, CefFrame frame, CefContextMenuParams params, CefMenuModel model) {
/* 163 */             model.clear();
/*     */           }
/*     */         });
/*     */     
/* 167 */     this.client.addDisplayHandler((CefDisplayHandler)new CefDisplayHandlerAdapter()
/*     */         {
/*     */           public void onAddressChange(CefBrowser browser, CefFrame frame, String url)
/*     */           {
/* 171 */             for (Consumer<String> consumer : (Iterable<Consumer<String>>)CefFrameBuilder.this.urlConsumers) {
/* 172 */               consumer.accept(url);
/*     */             }
/* 174 */             for (Function<String, Boolean> callback : (Iterable<Function<String, Boolean>>)CefFrameBuilder.this.urlCallbacks) {
/* 175 */               if (((Boolean)callback.apply(url)).booleanValue()) {
/*     */                 return;
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/* 181 */     this.client.addFocusHandler((CefFocusHandler)new CefFocusHandlerAdapter()
/*     */         {
/*     */           public void onGotFocus(CefBrowser browser) {
/* 184 */             if (CefFrameBuilder.this.focused) {
/*     */               return;
/*     */             }
/*     */             
/* 188 */             CefFrameBuilder.this.focused = true;
/* 189 */             KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
/* 190 */             browser.setFocus(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public void onTakeFocus(CefBrowser browser, boolean next) {
/* 195 */             CefFrameBuilder.this.focused = false;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void addURLCallback(Function<String, Boolean> callback) {
/* 202 */     this.urlCallbacks.add(callback);
/*     */   }
/*     */   
/*     */   public void addURLConsumer(Consumer<String> consumer) {
/* 206 */     this.urlConsumers.add(consumer);
/*     */   }
/*     */   
/*     */   public void execute(String script) {
/* 210 */     this.browser.executeJavaScript(script, "", 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladiu\\ui\CefFrameBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */