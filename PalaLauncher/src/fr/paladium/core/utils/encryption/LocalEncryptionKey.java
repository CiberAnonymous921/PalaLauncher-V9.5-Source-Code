/*    */ package fr.paladium.core.utils.encryption;
/*    */ 
/*    */ import fr.paladium.Launcher;
/*    */ import fr.paladium.core.distribution.dto.DistributionOS;
/*    */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*    */ import fr.paladium.core.utils.io.OsCheck;
/*    */ import java.awt.TrayIcon;
/*    */ import java.io.File;
/*    */ import java.nio.file.Files;
/*    */ import javax.crypto.KeyGenerator;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocalEncryptionKey
/*    */ {
/*    */   private static byte[] localKey;
/*    */   
/*    */   private static byte[] generateLocalKey() {
/* 19 */     File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/* 20 */     File keyFile = new File(installDir, ".plkf.key");
/* 21 */     if (keyFile.exists()) {
/*    */       try {
/* 23 */         byte[] key = Files.readAllBytes(keyFile.toPath());
/* 24 */         setLocalKey(key);
/* 25 */         return key;
/* 26 */       } catch (Exception e) {
/* 27 */         e.printStackTrace();
/*    */       } 
/*    */     }
/*    */     
/*    */     try {
/* 32 */       KeyGenerator gen = KeyGenerator.getInstance("AES");
/* 33 */       gen.init(128);
/* 34 */       byte[] secureRandomKeyBytes = gen.generateKey().getEncoded();
/* 35 */       setLocalKey(secureRandomKeyBytes);
/*    */       
/* 37 */       Files.write(keyFile.toPath(), secureRandomKeyBytes, new java.nio.file.OpenOption[0]);
/* 38 */       if (OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) {
/* 39 */         Files.setAttribute(keyFile.toPath(), "dos:hidden", Boolean.valueOf(true), new java.nio.file.LinkOption[0]);
/*    */       }
/* 41 */       return secureRandomKeyBytes;
/* 42 */     } catch (Exception e) {
/* 43 */       e.printStackTrace();
/* 44 */       NotificationHelper.sendSystemNotification("Une erreur est survenue lors de la génération de la clé de chiffrement local.", TrayIcon.MessageType.ERROR);
/*    */ 
/*    */       
/* 47 */       Launcher.close(-1);
/* 48 */       return null;
/*    */     } 
/*    */   }
/*    */   private static void setLocalKey(byte[] localKey) {
/* 52 */     LocalEncryptionKey.localKey = localKey;
/*    */   }
/*    */   
/*    */   public static byte[] getLocalKey() {
/* 56 */     if (localKey == null) {
/* 57 */       return generateLocalKey();
/*    */     }
/*    */     
/* 60 */     return localKey;
/*    */   }
/*    */   
/*    */   public static void reset() {
/* 64 */     File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/* 65 */     File keyFile = new File(installDir, ".plkf.key");
/* 66 */     if (keyFile.exists()) {
/* 67 */       keyFile.delete();
/*    */     }
/* 69 */     setLocalKey(null);
/* 70 */     getLocalKey();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\encryption\LocalEncryptionKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */