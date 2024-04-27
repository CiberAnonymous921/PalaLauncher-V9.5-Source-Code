/*    */ package fr.paladium.core.utils.desktop;
/*    */ 
/*    */ import fr.paladium.core.authentication.microsoft.exception.MicrosoftAPIHandledException;
/*    */ import fr.paladium.core.distribution.dto.DistributionOS;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import fr.paladium.core.utils.tray.PaladiumSystemTray;
/*    */ import fr.paladium.ui.frame.LauncherFrame;
/*    */ import java.awt.SystemTray;
/*    */ import java.awt.TrayIcon;
/*    */ import java.io.IOException;
/*    */ import org.cef.browser.CefBrowser;
/*    */ import org.cef.browser.CefFrame;
/*    */ import org.cef.handler.CefLoadHandler;
/*    */ import org.cef.handler.CefLoadHandlerAdapter;
/*    */ import org.cef.network.CefRequest;
/*    */ 
/*    */ public class NotificationHelper {
/*    */   private static String notificationMessage;
/*    */   private static String lastMessage;
/*    */   private static long lastNotification;
/*    */   private static String lastUrl;
/*    */   private static boolean loading;
/*    */   
/*    */   public static void init() {
/* 25 */     (LauncherFrame.getInstance().getBuilder()).client.addLoadHandler((CefLoadHandler)new CefLoadHandlerAdapter()
/*    */         {
/*    */           public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
/* 28 */             NotificationHelper.loading = false;
/* 29 */             if (NotificationHelper.notificationMessage != null && !NotificationHelper.lastUrl.equals(browser.getURL())) {
/* 30 */               String message = NotificationHelper.notificationMessage;
/* 31 */               NotificationHelper.notificationMessage = null;
/* 32 */               NotificationHelper.sendNotification(message);
/*    */               
/*    */               return;
/*    */             } 
/* 36 */             if (NotificationHelper.lastMessage != null && System.currentTimeMillis() - NotificationHelper.lastNotification < 5000L) {
/* 37 */               NotificationHelper.sendNotification(NotificationHelper.lastMessage);
/*    */             }
/*    */           }
/*    */ 
/*    */           
/*    */           public void onLoadStart(CefBrowser browser, CefFrame frame, CefRequest.TransitionType transitionType) {
/* 43 */             NotificationHelper.loading = true;
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public static void sendNotification(String message) {
/* 49 */     if (!loading) {
/* 50 */       LauncherFrame.getInstance().getBuilder().execute("window.showError(\"" + message + "\");");
/* 51 */       sendSystemNotification(message, TrayIcon.MessageType.ERROR);
/* 52 */       lastNotification = System.currentTimeMillis();
/* 53 */       lastMessage = message;
/*    */       
/*    */       return;
/*    */     } 
/* 57 */     lastUrl = (LauncherFrame.getInstance().getBuilder()).browser.getURL();
/* 58 */     notificationMessage = message;
/*    */   }
/*    */   
/*    */   public static void sendNotification(MicrosoftAPIHandledException exception) {
/* 62 */     sendNotification(exception.getNotificationMessage());
/*    */   }
/*    */   
/*    */   public static void sendSystemNotification(String message, TrayIcon.MessageType type) {
/* 66 */     String title = "Paladium Games Launcher";
/* 67 */     if (OsCheck.getOperatingSystemType() == DistributionOS.MACOS) {
/*    */       try {
/* 69 */         Runtime.getRuntime().exec(new String[] { "osascript", "-e", "display notification \"" + message + "\" with title \"" + "Paladium Games Launcher" + "\" subtitle \"" + type + "\" sound name \"Funk\"" });
/* 70 */       } catch (IOException iOException) {}
/*    */       
/*    */       return;
/*    */     } 
/* 74 */     if (SystemTray.isSupported() && PaladiumSystemTray.getTrayIcon() != null) {
/* 75 */       PaladiumSystemTray.getTrayIcon().displayMessage("Paladium Games Launcher", message, type);
/*    */     }
/*    */     
/* 78 */     System.out.println("[NotificationSystem][" + type + "] " + message);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\desktop\NotificationHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */