/*     */ package fr.paladium.core.utils.net;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.file.Files;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.KeyStoreSpi;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;

/*     */ import fr.paladium.core.distribution.dto.DistributionOS;
/*     */ import fr.paladium.core.utils.io.OsCheck;
/*     */ 
/*     */ public class CertificateUtil {
/*     */   private static final String KEYSTORE_NAME = "keystore.jks";
/*     */   
/*     */   public static char[] getKeyStorePassword() {
/*  32 */     return keyStorePassword;
/*  33 */   } private static final char[] keyStorePassword = "01pG^{QV(*6j".toCharArray();
/*     */   
/*     */   private static File KEYSTORE_PATH;
/*     */   
/*     */   private static final String CERT_TYPE = "X.509";
/*     */   private static final String CERT_FOLDER = "/certs/";
/*  39 */   private static final String[] CERTS = new String[] { "cloudflare-dns.pem", "digicert-global-g2-2030.pem", "digicert-globalroot-g2-2038.pem", "isrg-root-x1.pem", "r3-lets-encrypt.pem", "download-paladium-pvp-fr.pem", "dns-google.pem", "gts-ca-1c3.pem", "gts-root-r1.pem", "api-minecraftservices-com.pem", "digicert-geotrust-global-tls-2031.pem", "digicert-globalroot-ca-2031.pem", "gts-ca-1p5.pem", "sessionserver-mojang-com.pem", "palagames-launcher.pem" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final List<Map.Entry<String, X509Certificate>> SYSTEMS_CERTS = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadCertificates() {
/*     */     KeyStore keystore;
/*     */     try {
/*  66 */       keystore = getKeystore();
/*  67 */     } catch (Exception ex) {
/*  68 */       System.err.println("No keystore loaded. Creating a fresh one.");
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  73 */         keystore = KeyStore.getInstance(KeyStore.getDefaultType());
/*  74 */         keystore.load(null, keyStorePassword);
/*  75 */       } catch (KeyStoreException|IOException|NoSuchAlgorithmException|CertificateException e) {
/*  76 */         System.err.println("CRITICAL ERROR: Unable to create a keystore. Certificates won't be loaded! A lot of issues can occur.");
/*     */         
/*  78 */         e.printStackTrace();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/*  85 */       removeExpiredEntries(keystore);
/*  86 */     } catch (KeyStoreException e) {
/*  87 */       System.err.println("Unable to remove expired certificates: " + e.getMessage());
/*  88 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  91 */     AtomicBoolean hangCheck = monitorHang(Thread.currentThread());
/*  92 */     findSystemCerts();
/*  93 */     AtomicInteger added = new AtomicInteger(0);
/*     */ 
/*     */     
/*  96 */     for (String certFile : CERTS) {
/*     */       try {
/*  98 */         InputStream inputStream = CertificateUtil.class.getResourceAsStream("/certs/" + certFile);
/*     */         
/* 100 */         if (inputStream == null)
/* 101 */         { System.err.println("Certificate " + certFile + " not found."); }
/*     */         
/*     */         else
/*     */         
/* 105 */         { CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
/* 106 */           X509Certificate cert = (X509Certificate)certificateFactory.generateCertificate(inputStream);
/*     */           
/* 108 */           keystore.setCertificateEntry(certFile, cert);
/* 109 */           System.out.println("Certificate added: " + certFile);
/* 110 */           inputStream.close();
/* 111 */           added.incrementAndGet(); } 
/* 112 */       } catch (KeyStoreException|CertificateException|IOException e) {
/* 113 */         System.err.println("Unable to add certificate " + certFile + ": " + e.getMessage());
/* 114 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 118 */     Iterator<Map.Entry<String, X509Certificate>> iterator = SYSTEMS_CERTS.iterator();
/*     */     
/* 120 */     while (iterator.hasNext()) {
/* 121 */       Map.Entry<String, X509Certificate> cert = iterator.next();
/*     */       
/*     */       try {
/* 124 */         System.out.println("Adding system certificate " + (String)cert.getKey() + " to keystore...");
/* 125 */         keystore.setCertificateEntry(cert.getKey(), cert.getValue());
/* 126 */         System.out.println("Certificate added: " + (String)cert.getKey());
/* 127 */         added.incrementAndGet();
/* 128 */       } catch (Exception e) {
/* 129 */         System.err.println("Unable to add system certificate " + (String)cert.getKey() + ": " + e.getMessage());
/* 130 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     System.err.println(added.get() + "/" + (CERTS.length + SYSTEMS_CERTS.size()) + " certificates added to keystore.");
/*     */     try {
/* 136 */       keystore.store(Files.newOutputStream(getKeystoreFile().toPath(), new java.nio.file.OpenOption[0]), keyStorePassword);
/* 137 */     } catch (KeyStoreException|IOException|NoSuchAlgorithmException|CertificateException e) {
/* 138 */       System.err.println("Unable to save keystore: " + e.getMessage());
/* 139 */       e.printStackTrace();
/*     */     } 
/* 141 */     hangCheck.set(false);
/*     */ 
/*     */     
/* 144 */     System.setProperty("javax.net.ssl.trustStore", getKeystoreFile().getAbsolutePath());
/*     */   }
/*     */   
/*     */   private static AtomicBoolean monitorHang(Thread monitoredThread) {
/* 148 */     AtomicBoolean running = new AtomicBoolean(true);
/*     */     
/* 150 */     Thread monitoringThread = new Thread(() -> {
/*     */           String lastStack = "";
/*     */           
/*     */           int hang = 0;
/*     */           
/*     */           while (running.get()) {
/*     */             Thread.State state = monitoredThread.getState();
/*     */             
/*     */             StackTraceElement[] stackTrace = monitoredThread.getStackTrace();
/*     */             
/*     */             if (stackTrace.length > 0) {
/*     */               StackTraceElement stackTraceElement = stackTrace[0];
/*     */               
/*     */               String stackString = stackTraceElement.toString();
/*     */               
/*     */               if (stackString.equals(lastStack) && ++hang >= 50) {
/*     */                 System.err.println("Thread " + monitoredThread.getName() + " is hanging. State: " + state);
/*     */                 
/*     */                 for (StackTraceElement element : stackTrace) {
/*     */                   System.err.println(element.toString());
/*     */                 }
/*     */                 break;
/*     */               } 
/*     */               lastStack = stackTraceElement.toString();
/*     */             } 
/*     */             try {
/*     */               Thread.sleep(100L);
/* 177 */             } catch (InterruptedException e) {
/*     */               e.printStackTrace();
/*     */             } 
/*     */           } 
/*     */         });
/* 182 */     monitoringThread.setDaemon(true);
/* 183 */     monitoringThread.start();
/* 184 */     return running;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void findSystemCerts() {
/*     */     try {
/*     */       KeyStore keyStore;
/* 195 */       if (OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) {
/* 196 */         keyStore = KeyStore.getInstance("Windows-ROOT");
/* 197 */       } else if (OsCheck.getOperatingSystemType() == DistributionOS.MACOS) {
/* 198 */         keyStore = KeyStore.getInstance("KeychainStore");
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */       
/* 203 */       keyStore.load(null, null);
/*     */       
/* 205 */       if (OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) {
/*     */         try {
/* 207 */           Field field = keyStore.getClass().getDeclaredField("keyStoreSpi");
/* 208 */           field.setAccessible(true);
/*     */           
/* 210 */           KeyStoreSpi k = (KeyStoreSpi)field.get(keyStore);
/* 211 */           field = k.getClass().getEnclosingClass().getDeclaredField("entries");
/* 212 */           field.setAccessible(true);
/* 213 */         } catch (Exception e) {
/* 214 */           System.err.println("Unable to change entry field accessibility (keystore).");
/*     */         } 
/*     */       }
/*     */       
/* 218 */       Enumeration<String> aliases = keyStore.aliases();
/*     */       
/* 220 */       while (aliases.hasMoreElements()) {
/* 221 */         String alias = aliases.nextElement();
/* 222 */         Certificate certificate = keyStore.getCertificate(alias);
/*     */         
/* 224 */         if (certificate instanceof X509Certificate) {
/* 225 */           X509Certificate x509Certificate = (X509Certificate)certificate;
/*     */           
/* 227 */           byte[] certificateBytes = x509Certificate.getEncoded();
/*     */           
/* 229 */           CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 230 */           ByteArrayInputStream bis = new ByteArrayInputStream(certificateBytes);
/* 231 */           X509Certificate copiedCertificate = (X509Certificate)cf.generateCertificate(bis);
/*     */           
/* 233 */           SYSTEMS_CERTS.add(new AbstractMap.SimpleEntry<>(alias, copiedCertificate));
/*     */         } 
/*     */       } 
/* 236 */     }catch (Exception e) {
				    KeyStore keyStore = null;
				    System.err.println("Unable to load system certificates: " + e.getMessage());
				    e.printStackTrace();
				}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void removeExpiredEntries(KeyStore keyStore) throws KeyStoreException {
/* 249 */     Date currentDate = new Date();
/* 250 */     Enumeration<String> aliases = keyStore.aliases();
/*     */     
/* 252 */     while (aliases.hasMoreElements()) {
/* 253 */       String alias = aliases.nextElement();
/*     */       
/*     */       try {
/* 256 */         if (keyStore.isCertificateEntry(alias)) {
/* 257 */           X509Certificate certificate = (X509Certificate)keyStore.getCertificate(alias);
/* 258 */           if (certificate == null || certificate.getNotAfter().before(currentDate));
/*     */         }
/*     */       
/*     */       }
/* 262 */       catch (KeyStoreException e) {
/* 263 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getKeystoreFile() {
/* 275 */     if (KEYSTORE_PATH == null) {
/* 276 */       File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/* 277 */       KEYSTORE_PATH = new File(installDir, "keystore.jks");
/*     */     } 
/*     */     
/* 280 */     return KEYSTORE_PATH;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static KeyStore getKeystore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
/* 293 */     KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
/*     */     
/* 295 */     InputStream is = Files.newInputStream(getKeystoreFile().toPath(), new java.nio.file.OpenOption[0]); 
/* 296 */     try { keyStore.load(is, keyStorePassword);
/* 297 */       if (is != null) is.close();  } catch (Throwable throwable) { if (is != null)
/* 298 */         try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return keyStore;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\net\CertificateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */