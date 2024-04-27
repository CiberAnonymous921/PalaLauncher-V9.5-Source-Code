/*    */ package me.friwi.jcefmaven;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.file.Files;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import org.cef.CefApp;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CefBuildInfo
/*    */ {
/* 19 */   private static final Gson GSON = new Gson();
/* 20 */   private static CefBuildInfo LOCAL_BUILD_INFO = null;
/*    */   private final String jcefUrl;
/*    */   private final String releaseTag;
/*    */   private final String releaseUrl;
/*    */   private final String platform;
/*    */   
/*    */   private CefBuildInfo(String jcefUrl, String releaseTag, String releaseUrl, String platform) {
/* 27 */     this.jcefUrl = jcefUrl;
/* 28 */     this.releaseTag = releaseTag;
/* 29 */     this.releaseUrl = releaseUrl;
/* 30 */     this.platform = platform;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CefBuildInfo fromClasspath() throws IOException {
/* 41 */     if (LOCAL_BUILD_INFO == null) {
/* 42 */       LOCAL_BUILD_INFO = loadData(
/* 43 */           Objects.<InputStream>requireNonNull(CefApp.class.getResourceAsStream("/build_meta.json"), "The build_meta.json file from the jcef-api artifact could not be read"));
/*    */     }
/*    */ 
/*    */     
/* 47 */     return LOCAL_BUILD_INFO;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CefBuildInfo fromFile(File file) throws IOException {
/* 58 */     return loadData(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]));
/*    */   }
/*    */   
/*    */   private static <T> CefBuildInfo loadData(InputStream in) throws IOException {
/*    */     Map object;
/*    */     try {
/* 64 */       object = (Map)GSON.fromJson(new InputStreamReader(in), Map.class);
/* 65 */     } catch (JsonParseException e) {
/* 66 */       throw new IOException("Invalid json content in build_meta.json", e);
/*    */     } finally {
/* 68 */       in.close();
/*    */     } 
/* 70 */     return new CefBuildInfo(
/* 71 */         Objects.<T>requireNonNull((T)object.get("jcef_url"), "No jcef_url specified in build_meta.json").toString(), 
/* 72 */         Objects.<T>requireNonNull((T)object.get("release_tag"), "No release_tag specified in build_meta.json").toString(), 
/* 73 */         Objects.<T>requireNonNull((T)object.get("release_url"), "No release_url specified in build_meta.json").toString(), 
/* 74 */         Objects.<T>requireNonNull((T)object.get("platform"), "No platform specified in build_meta.json").toString());
/*    */   }
/*    */   
/*    */   public String getJcefUrl() {
/* 78 */     return this.jcefUrl;
/*    */   }
/*    */   
/*    */   public String getReleaseTag() {
/* 82 */     return this.releaseTag;
/*    */   }
/*    */   
/*    */   public String getReleaseUrl() {
/* 86 */     return this.releaseUrl;
/*    */   }
/*    */   
/*    */   public String getPlatform() {
/* 90 */     return this.platform;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\CefBuildInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */