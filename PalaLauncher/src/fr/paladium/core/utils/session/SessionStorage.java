/*    */ package fr.paladium.core.utils.session;
/*    */ 
/*    */ import fr.paladium.core.distribution.dto.DistributionOS;
/*    */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*    */ import fr.paladium.core.utils.encryption.LocalEncryptionKey;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import java.awt.TrayIcon;
/*    */ import java.io.File;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SessionStorage
/*    */ {
/*    */   private static SessionStorage instance;
/*    */   private final File file;
/*    */   private final SessionStorageData data;
/*    */   
/*    */   public SessionStorage() {
/* 22 */     File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/* 23 */     this.file = new File(installDir, ".JAVA_SESSION");
/* 24 */     this.data = getData();
/*    */   }
/*    */   
/*    */   public static SessionStorage getInstance() {
/* 28 */     if (instance == null) {
/* 29 */       instance = new SessionStorage();
/*    */     }
/*    */     
/* 32 */     return instance;
/*    */   }
/*    */   
/*    */   public <T> T getItem(String key, Class<T> type) {
/* 36 */     return this.data.getProperty(key, type);
/*    */   }
/*    */   
/*    */   public <T> T getItem(String key, T defaultValue, Class<T> type) {
/* 40 */     if (!hasItem(key)) {
/* 41 */       setItem(key, defaultValue);
/* 42 */       return defaultValue;
/*    */     } 
/* 44 */     return this.data.getProperty(key, type);
/*    */   }
/*    */   
/*    */   public void setItem(String key, Object value) {
/* 48 */     this.data.setProperty(key, value);
/* 49 */     saveData();
/*    */   }
/*    */   
/*    */   public boolean hasItem(String key) {
/* 53 */     return this.data.hasProperty(key);
/*    */   }
/*    */   
/*    */   private SessionStorageData getData() {
/*    */     try {
/* 58 */       if (!this.file.exists()) {
/* 59 */         SessionStorageData sessionStorageData = new SessionStorageData();
/* 60 */         saveData(sessionStorageData);
/* 61 */         return sessionStorageData;
/*    */       } 
/*    */       
/* 64 */       SessionStorageData cachedData = SessionStorageData.decrypt(new String(Files.readAllBytes(this.file.toPath()), StandardCharsets.UTF_8));
/* 65 */       if (cachedData == null || cachedData.localStorage == null) {
/* 66 */         LocalEncryptionKey.reset();
/* 67 */         NotificationHelper.sendSystemNotification("Votre session a été réinitialisée car elle était corrompue.", TrayIcon.MessageType.ERROR);
/* 68 */         cachedData = new SessionStorageData();
/* 69 */         saveData(cachedData);
/*    */       } 
/*    */       
/* 72 */       return cachedData;
/* 73 */     } catch (Exception e) {
/* 74 */       e.printStackTrace();
/* 75 */       LocalEncryptionKey.reset();
/* 76 */       NotificationHelper.sendSystemNotification("Impossible de charger votre session.", TrayIcon.MessageType.ERROR);
/* 77 */       SessionStorageData cachedData = new SessionStorageData();
/* 78 */       saveData(cachedData);
/* 79 */       return cachedData;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void saveData() {
/* 84 */     saveData(this.data);
/*    */   }
/*    */   
/*    */   private void saveData(SessionStorageData sessionStorageData) {
/*    */     try {
/* 89 */       if (!this.file.exists()) {
/* 90 */         this.file.createNewFile();
/* 91 */         if (OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) {
/* 92 */           Files.setAttribute(this.file.toPath(), "dos:hidden", Boolean.valueOf(true), new java.nio.file.LinkOption[0]);
/*    */         }
/*    */       } 
/*    */       
/* 96 */       Files.write(this.file.toPath(), sessionStorageData.encrypt().getBytes(StandardCharsets.UTF_8), new java.nio.file.OpenOption[0]);
/* 97 */     } catch (Exception e) {
/* 98 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\session\SessionStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */