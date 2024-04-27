/*     */ package me.friwi.jcefmaven.impl.step.fetch;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import me.friwi.jcefmaven.CefBuildInfo;
/*     */ import me.friwi.jcefmaven.EnumPlatform;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PackageDownloader
/*     */ {
/*  24 */   private static final Gson GSON = new Gson();
/*  25 */   private static final Logger LOGGER = Logger.getLogger(PackageDownloader.class.getName());
/*     */   
/*     */   private static final int BUFFER_SIZE = 16384;
/*     */ 
/*     */   
/*     */   public static void downloadNatives(CefBuildInfo info, EnumPlatform platform, File destination, Consumer<Float> progressConsumer, Set<String> mirrors) throws IOException {
/*  31 */     Objects.requireNonNull(info, "info cannot be null");
/*  32 */     Objects.requireNonNull(platform, "platform cannot be null");
/*  33 */     Objects.requireNonNull(destination, "destination cannot be null");
/*  34 */     Objects.requireNonNull(progressConsumer, "progressConsumer cannot be null");
/*  35 */     Objects.requireNonNull(mirrors, "mirrors can not be null");
/*  36 */     if (mirrors.isEmpty()) {
/*  37 */       throw new RuntimeException("mirrors can not be empty");
/*     */     }
/*     */ 
/*     */     
/*  41 */     if (!destination.createNewFile()) {
/*  42 */       throw new IOException("Could not create target file " + destination.getAbsolutePath());
/*     */     }
/*     */     
/*  45 */     String mvn_version = loadJCefMavenVersion();
/*     */ 
/*     */     
/*  48 */     Exception lastException = null;
/*  49 */     for (String mirror : mirrors) {
/*     */ 
/*     */ 
/*     */       
/*  53 */       String m = mirror.replace("{platform}", platform.getIdentifier()).replace("{tag}", info.getReleaseTag()).replace("{mvn_version}", mvn_version);
/*     */       
/*     */       try {
/*  56 */         URL url = new URL(m);
/*  57 */         HttpURLConnection uc = (HttpURLConnection)url.openConnection(); 
/*  58 */         try { InputStream in = uc.getInputStream(); 
/*  59 */           try { if (uc.getResponseCode() != 200)
/*  60 */             { LOGGER.log(Level.WARNING, "Request to mirror failed with code " + uc.getResponseCode() + " from server: " + m);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  86 */               if (in != null) in.close();  continue; }  long length = uc.getContentLengthLong(); FileOutputStream fos = new FileOutputStream(destination); try { long progress = 0L; progressConsumer.accept(Float.valueOf(0.0F)); byte[] buffer = new byte[16384]; long transferred = 0L; int r; while ((r = in.read(buffer)) > 0) { fos.write(buffer, 0, r); transferred += r; long newprogress = transferred * 100L / length; if (newprogress > progress) { progress = newprogress; progressConsumer.accept(Float.valueOf((float)progress)); }  }  fos.flush(); fos.close(); } catch (Throwable throwable) { try { fos.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  uc.disconnect(); if (in != null) in.close();  return; } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*     */         
/*  88 */         { lastException = e;
/*  89 */           LOGGER.log(Level.WARNING, "Request failed with exception on mirror: " + m + " (" + e
/*  90 */               .getClass().getSimpleName() + (
/*  91 */               (e.getMessage() == null) ? "" : (": " + e.getMessage())) + ")");
/*  92 */           uc.disconnect(); }
/*     */       
/*  94 */       } catch (Exception e) {
/*  95 */         LOGGER.log(Level.WARNING, "Request failed with exception on mirror: " + m, e);
/*  96 */         lastException = e;
/*     */       } 
/*     */     } 
/*     */     
/* 100 */     if (lastException != null) {
/* 101 */       throw new IOException("None of the supplied mirrors were working", lastException);
/*     */     }
/* 103 */     throw new IOException("None of the supplied mirrors were working");
/*     */   }
/*     */   
/*     */   private static String loadJCefMavenVersion() throws IOException {
/*     */     Map object;
/*     */     
/* 109 */     try { InputStream in = PackageDownloader.class.getResourceAsStream("/jcefmaven_build_meta.json"); 
/* 110 */       try { if (in == null) {
/* 111 */           throw new IOException("/jcefmaven_build_meta.json not found on class path");
/*     */         }
/* 113 */         object = (Map)GSON.fromJson(new InputStreamReader(in), Map.class);
/* 114 */         if (in != null) in.close();  } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 115 */     { throw new IOException("Invalid json content in jcefmaven_build_meta.json", e); }
/*     */     
/* 117 */     return (String)object.get("version");
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\impl\step\fetch\PackageDownloader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */