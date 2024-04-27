/*     */ package fr.paladium.core.utils.game;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
import java.io.InputStreamReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Path;

/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.entity.StringEntity;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;

/*     */ import fr.paladium.core.authentication.paladium.response.PaladiumAccount;
/*     */ import fr.paladium.core.controller.AccountController;
/*     */ import fr.paladium.core.distribution.GameDistribution;
/*     */ import fr.paladium.core.utils.desktop.NotificationHelper;
/*     */ import fr.paladium.core.utils.io.FileUtils;
/*     */ import fr.paladium.core.utils.io.RamUtils;
import fr.paladium.core.utils.net.CustomHttpClient;
/*     */ import fr.paladium.core.utils.net.Telemetry;
/*     */ import fr.paladium.core.utils.popup.basic.PopupInsufficientMemoryCrash;
/*     */ 
/*     */ public class ProcessLogManager extends Thread {
/*     */   private GameDistribution distribution;
/*     */   private String distributionId;
/*     */   private String paladiumToken;
/*     */   private Process process;
/*     */   private BufferedReader reader;
/*     */   
/*     */   public ProcessLogManager(GameDistribution distribution, String distributionId, Process process) {
/*  32 */     this.distribution = distribution;
/*  33 */     this.distributionId = distributionId;
/*  34 */     this.process = process;
/*  35 */     this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
/*     */     
/*  37 */     PaladiumAccount account = AccountController.getInstance().getCurrentPaladiumAccount();
/*  38 */     if (account != null && account.getJwt() != null) {
/*  39 */       this.paladiumToken = account.getJwt().getJwt();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  46 */     boolean crashed = false; try {
/*     */       String line;
/*  48 */       while ((line = this.reader.readLine()) != null) {
/*  49 */         System.out.println(line);
/*  50 */         if (!crashed) {
/*  51 */           crashed = line.contains("#@!@# Game crashed! Crash report saved to: #@!@#");
/*     */         }
/*     */       } 
/*     */       
/*  55 */       int exitCode = this.process.waitFor();
/*  56 */       if (crashed) {
/*  57 */         Path crashReport = getLastCrashReport();
/*     */         
/*  59 */         if (crashReport != null) {
/*  60 */           handleCrashReport(crashReport);
/*     */         } else {
/*  62 */           NotificationHelper.sendNotification("Le jeu a crash sans laisser de crash-report.");
/*  63 */           Telemetry.collect(Telemetry.Type.ERR_CRASH_NO_REPORT);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*  68 */       if (exitCode != 0) {
/*  69 */         secureLogs();
/*     */         
/*  71 */         if (exitCode == 3) {
/*  72 */           PopupInsufficientMemoryCrash.create().show();
/*     */           return;
/*     */         } 
/*  75 */         NotificationHelper.sendNotification("Le jeu s'est arrêté de manière inattendue. (code: " + this.process.exitValue() + ")");
/*  76 */         Telemetry.collect(Telemetry.Type.ERR_EXIT_CODE, Integer.valueOf(exitCode));
/*     */       } 
/*  78 */     } catch (Exception e) {
/*  79 */       e.printStackTrace();
/*  80 */       interrupt();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void secureLogs() {
/*  85 */     File secureLogs = new File(this.distribution.getRootDir(), "secure-logs");
/*  86 */     if (!secureLogs.exists()) {
/*  87 */       secureLogs.mkdirs();
/*     */     }
/*     */     
/*  90 */     File[] files = this.distribution.getRootDir().listFiles((dir, name) -> (name.startsWith("hs_err_pid") && name.endsWith(".log")));
/*  91 */     if (files == null) {
/*     */       return;
/*     */     }
/*     */     
/*  95 */     for (File file : files) {
/*     */       try {
/*  97 */         String content = org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
/*  98 */         content = content.replaceAll("--accessToken [a-zA-Z0-9.-_]+", "--accessToken [CENSORED]");
/*  99 */         org.apache.commons.io.FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
/*     */         
/* 101 */         org.apache.commons.io.FileUtils.moveFile(file, new File(secureLogs, file.getName()));
/* 102 */       } catch (IOException e) {
/* 103 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Path getLastCrashReport() throws IOException {
/* 109 */     return FileUtils.getLastFile(new File(this.distribution.getRootDir(), "crash-reports"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleCrashReport(Path crashReport) {
/*     */     String content;
/* 117 */     File file = crashReport.toFile();
/* 118 */     String fileName = crashReport.toAbsolutePath().toString().replace("\\", "/");
/*     */     
/* 120 */     boolean outOfMemory = false;
/*     */ 
/*     */     
/*     */     try {
/* 124 */       FileInputStream stream = new FileInputStream(file);
/* 125 */       content = IOUtils.toString(stream, StandardCharsets.UTF_8);
/* 126 */     } catch (IOException e) {
/* 127 */       System.err.println("Error while reading crash report:");
/* 128 */       e.printStackTrace();
/* 129 */       content = null;
/*     */       
/*     */       return;
/*     */     } 
/* 133 */     if (content != null) {
/*     */       
/* 135 */       for (String line : content.split("\n")) {
/* 136 */         if (line.startsWith("java.lang.OutOfMemoryError")) {
/* 137 */           outOfMemory = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 143 */       if (!outOfMemory) {
/* 144 */         sendCrashReport(content);
/*     */       }
/*     */     } 
/*     */     
/* 148 */     if (outOfMemory) {
/* 149 */       (new PopupInsufficientMemoryCrash(this.distributionId)).show();
/* 150 */       Telemetry.collect(Telemetry.Type.ERR_MEM_CRASH, RamUtils.getRam().getMemoryInfo());
/*     */     } else {
/* 152 */       NotificationHelper.sendNotification("Le jeu a crash (" + fileName + ")");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendCrashReport(String crashReportContent) {
/* 157 */     if (this.paladiumToken == null) {
/* 158 */       System.err.println("Unable to send crash report. Token is null."); return;
/*     */     } 
/*     */     
/* 161 */     try { CloseableHttpClient client = CustomHttpClient.createClient(); 
/* 162 */       try { HttpPost req = new HttpPost("https://api.paladium-pvp.fr/launcher/crash-report");
/*     */         
/* 164 */         req.addHeader("Authorization", "Bearer " + this.paladiumToken);
/* 165 */         req.addHeader("Content-Type", "application/json");
/* 166 */         req.setEntity((HttpEntity)new StringEntity(crashReportContent));
/*     */         
/* 168 */         CloseableHttpResponse response = client.execute((HttpUriRequest)req);
/*     */         
/* 170 */         if (response.getStatusLine().getStatusCode() != 204) {
/* 171 */           System.err.println("Unable to send crash report: " + response.getStatusLine().getStatusCode());
/*     */         } else {
/* 173 */           System.out.println("Crash report sent.");
/*     */         } 
/* 175 */         if (client != null) client.close();  } catch (Throwable throwable) { if (client != null) try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 176 */     { System.err.println("Error while sending crash report.");
/* 177 */       e.printStackTrace(); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\game\ProcessLogManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */