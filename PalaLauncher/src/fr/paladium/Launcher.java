/*     */ package fr.paladium;

/*     */ import fr.paladium.core.distribution.dto.DistributionOS;
/*     */ import fr.paladium.core.utils.debugger.Debugger;
/*     */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*     */ import fr.paladium.core.utils.io.OsCheck;
/*     */ import fr.paladium.core.utils.logger.FileLoggerOutputStream;
/*     */ import fr.paladium.core.utils.net.CertificateUtil;
/*     */ import fr.paladium.core.utils.tray.PaladiumSystemTray;
/*     */ import fr.paladium.router.route.RouteController;
/*     */ import fr.paladium.router.route.account.AccountRoute;
/*     */ import fr.paladium.router.route.common.CommonRoute;
/*     */ import fr.paladium.router.route.home.HomeRoute;
/*     */ import fr.paladium.router.route.login.LoginRoute;
/*     */ import fr.paladium.router.route.option.OptionRoute;
/*     */ import fr.paladium.router.route.splash.SplashRoute;
/*     */ import fr.paladium.ui.frame.LauncherFrame;
/*     */ import java.awt.SystemTray;
/*     */ import java.awt.TrayIcon;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ public class Launcher {
/*     */   private static boolean exited = false;
/*     */   
/*     */   public static void main(String[] args) throws FileNotFoundException {
/*  33 */     mainThread = Thread.currentThread();
/*  34 */     System.setProperty("apple.laf.useScreenMenuBar", "true");
/*  35 */     Runtime.getRuntime().addShutdownHook(new Thread(() -> {
/*     */             System.out.println("[Launcher] " + (exited ? "Closed" : "Crashed"));
/*     */             
/*     */             if (!exited) {
/*     */               for (StackTraceElement element : mainThread.getStackTrace()) {
/*     */                 System.out.println("[Launcher] " + element.toString());
/*     */               }
/*     */             }
/*     */           }));
/*  44 */     File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/*  45 */     if (!installDir.exists()) {
/*  46 */       NotificationHelper.sendSystemNotification("Paladium n'est pas installé sur votre ordinateur, veuillez l'installer avant de lancer le launcher.", TrayIcon.MessageType.ERROR);
/*  47 */       close(-1);
/*     */       
/*     */       return;
/*     */     } 
/*  51 */     File logFile = new File(installDir, "launcher.log");
/*  52 */     if (logFile.exists()) {
/*  53 */       logFile.delete();
/*     */     }
/*     */     
/*  56 */     PrintStream out = new PrintStream(new FileOutputStream(logFile, true), true);
/*  57 */     System.setOut(new PrintStream((OutputStream)new FileLoggerOutputStream(out, System.out)));
/*  58 */     System.setErr(new PrintStream((OutputStream)new FileLoggerOutputStream(out, System.err)));
/*     */     
/*  60 */     Debugger.pushState("debug");
/*  61 */     Debugger.debugData();
/*  62 */     Debugger.popState();
/*     */     
/*  64 */     if (OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) {
/*     */       try {
/*  66 */         String osName = System.getProperty("os.name");
/*  67 */         if (osName != null) {
/*  68 */           float osVersion = Float.parseFloat(osName.replace("Windows ", ""));
/*  69 */           if (osVersion < 10.0F) {
/*  70 */             NotificationHelper.sendSystemNotification("Paladium n'est pas compatible avec votre version de Windows, veuillez mettre à jour votre système d'exploitation.", TrayIcon.MessageType.ERROR);
/*  71 */             close(-1);
/*     */             return;
/*     */           } 
/*     */         } 
/*  75 */       } catch (Exception e) {
/*  76 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/*  80 */     Debugger.pushState("certificates");
/*  81 */     CertificateUtil.loadCertificates();
/*  82 */     Debugger.popState();
/*     */     
/*  84 */     if (SystemTray.isSupported()) {
/*  85 */       PaladiumSystemTray.create();
/*     */     }
/*     */     
/*  88 */     Debugger.pushState("route_controller");
/*  89 */     RouteController.getInstance().addRoute(new Class[] { SplashRoute.class, HomeRoute.class, LoginRoute.class, AccountRoute.class, OptionRoute.class, CommonRoute.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     Debugger.popState();
/*     */     
/*  99 */     Debugger.pushState("init_frame");
/* 100 */     LauncherFrame.getInstance();
/* 101 */     Debugger.popState();
/*     */     
/* 103 */     Debugger.pushState("init_notification");
/* 104 */     NotificationHelper.init();
/* 105 */     Debugger.popState();
/*     */   }
/*     */   private static Thread mainThread;
/*     */   public static void close(int exitCode) {
/* 109 */     exited = true;
/*     */     
/* 111 */     if (SystemTray.isSupported() && PaladiumSystemTray.getTrayIcon() != null) {
/* 112 */       System.out.println("[Launcher] Removing tray icon");
/*     */ 
/*     */       
/* 115 */       Callable<Void> task = () -> {
/*     */           SystemTray.getSystemTray().remove(PaladiumSystemTray.getTrayIcon());
/*     */           return null;
/*     */         };
/* 119 */       Future<Void> future = Executors.newCachedThreadPool().submit(task);
/*     */       
/* 121 */       try { future.get(1L, TimeUnit.SECONDS); }
/* 122 */       catch (Exception exception) {  }
/*     */       finally
/* 124 */       { future.cancel(true); }
/*     */     
/*     */     } 
/* 127 */     System.out.println("[Launcher] Closed with exit code " + exitCode);
/* 128 */     System.exit(exitCode);
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\Launcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */