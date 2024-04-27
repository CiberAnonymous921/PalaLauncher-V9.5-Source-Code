/*    */ package fr.paladium.ui.frame;
/*    */ 
/*    */ import fr.paladium.Launcher;
/*    */ import fr.paladium.core.distribution.dto.DistributionOS;
/*    */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import fr.paladium.ui.CefFrame;
/*    */ import java.awt.TrayIcon;
/*    */ import java.io.IOException;
/*    */ import me.friwi.jcefmaven.CefInitializationException;
/*    */ import me.friwi.jcefmaven.UnsupportedPlatformException;
/*    */ import org.cef.CefApp;
/*    */ 
/*    */ 
/*    */ public class LauncherFrame
/*    */   extends CefFrame
/*    */ {
/*    */   private static LauncherFrame instance;
/*    */   
/*    */   public LauncherFrame() throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
/* 21 */     /*TODO: Default:*/ super("Paladium Games Launcher", "https://palagames-launcher-front.pages.dev/#/Splash", 1280, 720, (OsCheck.getOperatingSystemType() != DistributionOS.LINUX), true);
				//super("Paladium Games Launcher", "https://paladium.netlify.app/Main/#/Splash", 1280, 720, (OsCheck.getOperatingSystemType() != DistributionOS.LINUX), true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 26 */     super.close();
/* 27 */     CefApp.getInstance().dispose();
/* 28 */     Launcher.close(0);
/*    */   }
/*    */   
/*    */   public static LauncherFrame getInstance() {
/* 32 */     if (instance == null) {
/*    */       try {
/* 34 */         instance = new LauncherFrame();
/* 35 */       } catch (UnsupportedPlatformException|CefInitializationException|IOException|InterruptedException e) {
/* 36 */         e.printStackTrace();
/* 37 */         NotificationHelper.sendSystemNotification("Impossible de lancer le launcher", TrayIcon.MessageType.ERROR);
/*    */       } 
/*    */     }
/* 40 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladiu\\ui\frame\LauncherFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */