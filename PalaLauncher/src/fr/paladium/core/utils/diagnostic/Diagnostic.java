/*     */ package fr.paladium.core.utils.diagnostic;
/*     */ import java.awt.Desktop;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.TrayIcon;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.UUID;

/*     */ import com.google.gson.JsonObject;

/*     */ import fr.paladium.core.utils.debugger.DebugData;
/*     */ import fr.paladium.core.utils.desktop.NotificationHelper;
import fr.paladium.core.utils.diagnostic.items.account.DiagnosticAccountLogged;
import fr.paladium.core.utils.diagnostic.items.account.DiagnosticAccountMicrosoftMinecraft;
import fr.paladium.core.utils.diagnostic.items.account.DiagnosticAccountMicrosoftUser;
import fr.paladium.core.utils.diagnostic.items.account.DiagnosticAccountMicrosoftXSTS;
import fr.paladium.core.utils.diagnostic.items.account.DiagnosticAccountMinecraft;
import fr.paladium.core.utils.diagnostic.items.account.DiagnosticAccountPaladium;
import fr.paladium.core.utils.diagnostic.items.file.DiagnosticFileJavaExecutable;
import fr.paladium.core.utils.diagnostic.items.file.DiagnosticFileJavaPermission;
import fr.paladium.core.utils.diagnostic.items.file.DiagnosticFileJavaVersion;
import fr.paladium.core.utils.diagnostic.items.file.DiagnosticFileSession;
import fr.paladium.core.utils.diagnostic.items.file.DiagnosticFileSessionKey;
import fr.paladium.core.utils.diagnostic.items.network.DiagnosticNetworkConnection;
import fr.paladium.core.utils.diagnostic.items.network.DiagnosticNetworkMicrosoft;
/*     */ import fr.paladium.core.utils.diagnostic.items.network.DiagnosticNetworkMojang;
/*     */ import fr.paladium.core.utils.diagnostic.items.network.DiagnosticNetworkPaladium;
import fr.paladium.core.utils.io.OsCheck;
/*     */ import fr.paladium.core.utils.json.JsonOnlineParser;
/*     */ import net.lingala.zip4j.ZipFile;
/*     */ import net.lingala.zip4j.model.ZipParameters;
/*     */ import net.lingala.zip4j.model.enums.CompressionLevel;
/*     */ import net.lingala.zip4j.model.enums.EncryptionMethod;
/*     */ 
/*     */ public class Diagnostic {
/*     */   public static void generate() {
/*  38 */     if (generating) {
/*  39 */       NotificationHelper.sendSystemNotification("Un diagnostic est déjà en cours de génération.", TrayIcon.MessageType.ERROR);
/*     */       
/*     */       return;
/*     */     } 
/*  43 */     generating = true;
/*  44 */     NotificationHelper.sendSystemNotification("Génération du diagnostic.", TrayIcon.MessageType.INFO);
/*     */     
/*  46 */     File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/*  47 */     File diagFile = new File(installDir, "launcher.diag");
/*  48 */     if (diagFile.exists()) {
/*  49 */       diagFile.delete();
/*     */     }
/*     */     
/*  52 */     (new Thread(() -> {
/*     */           try {
/*     */             FileWriter fw = new FileWriter(diagFile, true);
/*     */             
/*     */             BufferedWriter out = Files.newBufferedWriter(diagFile.toPath(), StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]);
/*     */             
/*     */             generateDiagnostic(out);
/*     */             
/*     */             out.close();
/*     */             
/*     */             fw.close();
/*     */             
/*     */             String zipPassword = UUID.randomUUID().toString();
/*     */             
/*     */             String currentFormattedDate = (new SimpleDateFormat("dd-MM-yyyy")).format(new Date());
/*     */             
/*     */             File zipFile = new File(installDir, "paladium-games-launcher-diagnostic-" + currentFormattedDate + ".zip");
/*     */             
/*     */             if (zipFile.exists()) {
/*     */               zipFile.delete();
/*     */             }
/*     */             
/*     */             ZipParameters zipParameters = new ZipParameters();
/*     */             
/*     */             zipParameters.setEncryptFiles(true);
/*     */             
/*     */             zipParameters.setCompressionLevel(CompressionLevel.FASTEST);
/*     */             
/*     */             zipParameters.setEncryptionMethod(EncryptionMethod.AES);
/*     */             
/*     */             ZipFile zip = new ZipFile(zipFile, zipPassword.toCharArray());
/*     */             
/*     */             List<File> files = new ArrayList<>();
/*     */             
/*     */             if (diagFile.exists()) {
/*     */               files.add(diagFile);
/*     */             }
/*     */             
/*     */             File logFile = new File(installDir, "launcher.log");
/*     */             
/*     */             if (logFile.exists()) {
/*     */               files.add(logFile);
/*     */             }
/*     */             
/*     */             File bootstrapFile = new File(installDir, "bootstrap.log");
/*     */             
/*     */             if (bootstrapFile.exists()) {
/*     */               files.add(bootstrapFile);
/*     */             }
/*     */             
/*     */             zip.addFiles(files, zipParameters);
/*     */             
/*     */             diagFile.delete();
/*     */             URL url = new URL("https://file.io/");
/*     */             HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/*     */             connection.setRequestMethod("POST");
/*     */             connection.setDoOutput(true);
/*     */             connection.setDoInput(true);
/*     */             String boundary = "*****" + Long.toHexString(System.currentTimeMillis()) + "*****";
/*     */             connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
/*     */             OutputStream outputStream = connection.getOutputStream();
/*     */             PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);
/*     */             writer.append("--" + boundary).append("\r\n");
/*     */             writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + zipFile.getName() + "\"").append("\r\n");
/*     */             writer.append("Content-Type: application/octet-stream").append("\r\n");
/*     */             writer.append("\r\n");
/*     */             writer.flush();
/*     */             FileInputStream fileInputStream = new FileInputStream(zipFile);
/*     */             byte[] buffer = new byte[4096];
/*     */             int bytesRead;
/*     */             while ((bytesRead = fileInputStream.read(buffer)) != -1) {
/*     */               outputStream.write(buffer, 0, bytesRead);
/*     */             }
/*     */             outputStream.flush();
/*     */             fileInputStream.close();
/*     */             writer.append("\r\n").flush();
/*     */             writer.append("--" + boundary + "--").append("\r\n");
/*     */             writer.close();
/*     */             BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/*     */             StringBuilder response = new StringBuilder();
/*     */             String line;
/*     */             while ((line = reader.readLine()) != null) {
/*     */               response.append(line);
/*     */             }
/*     */             reader.close();
/*     */             connection.disconnect();
/*     */             JsonObject json = (JsonObject)JsonOnlineParser.GSON.fromJson(response.toString(), JsonObject.class);
/*     */             if (json.get("link") != null) {
/*     */               String link = json.get("link").getAsString();
/*     */               String content = "Paladium Games Launcher - Diagnostic\nURL: " + link + "\nPSW: " + zipPassword;
/*     */               File contentFile = new File(installDir, "diagnostic.txt");
/*     */               if (contentFile.exists()) {
/*     */                 contentFile.delete();
/*     */               }
/*     */               Files.write(contentFile.toPath(), content.getBytes(StandardCharsets.UTF_8), new java.nio.file.OpenOption[0]);
/*     */               Desktop.getDesktop().open(contentFile);
/*     */               Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
/*     */               NotificationHelper.sendSystemNotification("Diagnostic terminé.", TrayIcon.MessageType.INFO);
/*     */             } else {
/*     */               NotificationHelper.sendSystemNotification("Impossible de générer le diagnostic.", TrayIcon.MessageType.ERROR);
/*     */             } 
/*     */             zipFile.delete();
/* 154 */           } catch (Exception e) {
/*     */             e.printStackTrace();
/*     */             NotificationHelper.sendSystemNotification("Impossible de générer le diagnostic.", TrayIcon.MessageType.ERROR);
/*     */           } 
/*     */           generating = false;
/* 159 */         })).start();
/*     */   }
/*     */   private static boolean generating = false;
/*     */   private static void generateDiagnostic(BufferedWriter out) throws IOException {
/* 163 */     DebugData debugData = new DebugData();
/* 164 */     write(out, "Paladium Launcher Diagnostic");
/* 165 */     write(out, "============================");
/* 166 */     write(out, "");
/* 167 */     write(out, "System Information");
/* 168 */     write(out, "------------------");
/* 169 */     write(out, "OS: " + debugData.getDistributionOS() + " (" + debugData.getOs() + ")");
/* 170 */     write(out, "Java Version: " + debugData.getJavaVersion());
/* 171 */     write(out, "Launcher Path: " + debugData.getExecutionPath());
/* 172 */     write(out, "AppData Path: " + debugData.getAppData());
/* 173 */     write(out, "CPU: " + debugData.getCpu());
/* 174 */     write(out, "RAM: " + debugData.getRam());
/* 175 */     write(out, "DATE: " + debugData.getDate());
/* 176 */     write(out, "------------------");
/* 177 */     write(out, "");
/* 178 */     write(out, "Diagnostics");
/* 179 */     write(out, "------------------");
/* 180 */     diagnostic(out, 
/*     */         
/* 182 */         DiagnosticSection.create("Network")
/* 183 */         .add("Network Connection", (Class)DiagnosticNetworkConnection.class)
/* 184 */         .add("Microsoft API", (Class)DiagnosticNetworkMicrosoft.class)
/* 185 */         .add("Mojang API", (Class)DiagnosticNetworkMojang.class)
/* 186 */         .add("Paladium API", (Class)DiagnosticNetworkPaladium.class));
/*     */     
/* 188 */     write(out, "");
/* 189 */     diagnostic(out, 
/*     */         
/* 191 */         DiagnosticSection.create("Account")
/* 192 */         .add("Logged", (Class)DiagnosticAccountLogged.class)
/* 193 */         .add("MS-User", (Class)DiagnosticAccountMicrosoftUser.class)
/* 194 */         .add("MS-XSTS", (Class)DiagnosticAccountMicrosoftXSTS.class)
/* 195 */         .add("MS-MC", (Class)DiagnosticAccountMicrosoftMinecraft.class)
/* 196 */         .add("MC", (Class)DiagnosticAccountMinecraft.class)
/* 197 */         .add("Paladium-Games", (Class)DiagnosticAccountPaladium.class));
/*     */     
/* 199 */     write(out, "");
/* 200 */     diagnostic(out, 
/*     */         
/* 202 */         DiagnosticSection.create("Files")
/* 203 */         .add("Java Version", (Class)DiagnosticFileJavaVersion.class)
/* 204 */         .add("Java Executable", (Class)DiagnosticFileJavaExecutable.class)
/* 205 */         .add("Java Permission", (Class)DiagnosticFileJavaPermission.class)
/* 206 */         .add("Session Storage", (Class)DiagnosticFileSession.class)
/* 207 */         .add("Session Encryption", (Class)DiagnosticFileSessionKey.class));
/*     */     
/* 209 */     write(out, "------------------");
/*     */   }
/*     */   
/*     */   private static void diagnostic(BufferedWriter out, DiagnosticSection section) throws IOException {
/* 213 */     for (String line : section.generate()) {
/* 214 */       write(out, line);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void write(BufferedWriter out, String message) throws IOException {
/* 219 */     out.write(message);
/* 220 */     out.newLine();
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\Diagnostic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */