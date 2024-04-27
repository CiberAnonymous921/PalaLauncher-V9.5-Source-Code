/*     */ package me.friwi.jcefmaven;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import me.friwi.jcefmaven.impl.progress.ConsoleProgressHandler;
/*     */ import me.friwi.jcefmaven.impl.step.check.CefInstallationChecker;
/*     */ import me.friwi.jcefmaven.impl.step.extract.TarGzExtractor;
/*     */ import me.friwi.jcefmaven.impl.step.fetch.PackageClasspathStreamer;
/*     */ import me.friwi.jcefmaven.impl.step.fetch.PackageDownloader;
/*     */ import me.friwi.jcefmaven.impl.step.init.CefInitializer;
/*     */ import me.friwi.jcefmaven.impl.util.FileUtils;
/*     */ import me.friwi.jcefmaven.impl.util.macos.UnquarantineUtil;
/*     */ import org.cef.CefApp;
/*     */ import org.cef.CefSettings;
/*     */ import org.cef.handler.CefAppHandler;
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
/*     */ public class CefAppBuilder
/*     */ {
/*  52 */   private static final File DEFAULT_INSTALL_DIR = new File("jcef-bundle");
/*  53 */   private static final IProgressHandler DEFAULT_PROGRESS_HANDLER = (IProgressHandler)new ConsoleProgressHandler();
/*  54 */   private static final List<String> DEFAULT_JCEF_ARGS = new LinkedList<>();
/*  55 */   private static final CefSettings DEFAULT_CEF_SETTINGS = new CefSettings();
/*  56 */   private final Object lock = new Object();
/*     */   private final List<String> jcefArgs;
/*     */   private final CefSettings cefSettings;
/*     */   private File installDir;
/*     */   private IProgressHandler progressHandler;
/*  61 */   private CefApp instance = null;
/*     */   
/*     */   private boolean building = false;
/*     */   
/*     */   private boolean installed = false;
/*     */   
/*     */   private final Set<String> mirrors;
/*     */   
/*     */   public CefAppBuilder() {
/*  70 */     this.installDir = DEFAULT_INSTALL_DIR;
/*  71 */     this.progressHandler = DEFAULT_PROGRESS_HANDLER;
/*  72 */     this.jcefArgs = new LinkedList<>();
/*  73 */     this.jcefArgs.addAll(DEFAULT_JCEF_ARGS);
/*  74 */     this.cefSettings = DEFAULT_CEF_SETTINGS.clone();
/*  75 */     this.mirrors = new HashSet<>();
/*  76 */     this.mirrors.add("https://github.com/jcefmaven/jcefmaven/releases/download/{mvn_version}/jcef-natives-{platform}-{tag}.jar");
/*  77 */     this.mirrors.add("https://repo.maven.apache.org/maven2/me/friwi/jcef-natives-{platform}/{tag}/jcef-natives-{platform}-{tag}.jar");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInstallDir(File installDir) {
/*  86 */     Objects.requireNonNull(installDir, "installDir cannot be null");
/*  87 */     this.installDir = installDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProgressHandler(IProgressHandler progressHandler) {
/*  97 */     Objects.requireNonNull(this.installDir, "progressHandler cannot be null");
/*  98 */     this.progressHandler = progressHandler;
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
/*     */   
/*     */   public List<String> getJcefArgs() {
/* 112 */     return this.jcefArgs;
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
/*     */   
/*     */   public void addJcefArgs(String... args) {
/* 126 */     Objects.requireNonNull(args, "args cannot be null");
/* 127 */     this.jcefArgs.addAll(Arrays.asList(args));
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
/*     */   public CefSettings getCefSettings() {
/* 140 */     return this.cefSettings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppHandler(MavenCefAppHandlerAdapter handlerAdapter) {
/* 149 */     CefApp.addAppHandler((CefAppHandler)handlerAdapter);
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
/*     */   
/*     */   public Collection<String> getMirrors() {
/* 163 */     return new HashSet<>(this.mirrors);
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
/*     */   public void setMirrors(Collection<String> mirrors) {
/* 175 */     Objects.requireNonNull(mirrors, "mirrors can not be null");
/* 176 */     this.mirrors.clear();
/* 177 */     this.mirrors.addAll(mirrors);
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
/*     */   
/*     */   public CefAppBuilder install() throws IOException, UnsupportedPlatformException {
/* 191 */     if (this.installed) {
/* 192 */       return this;
/*     */     }
/* 194 */     this.progressHandler.handleProgress(EnumProgress.LOCATING, -1.0F);
/* 195 */     boolean installOk = CefInstallationChecker.checkInstallation(this.installDir);
/* 196 */     if (!installOk) {
/*     */ 
/*     */       
/* 199 */       FileUtils.deleteDir(this.installDir);
/* 200 */       if (!this.installDir.mkdirs()) throw new IOException("Could not create installation directory");
/*     */       
/* 202 */       InputStream nativesIn = PackageClasspathStreamer.streamNatives(
/* 203 */           CefBuildInfo.fromClasspath(), EnumPlatform.getCurrentPlatform());
/*     */       try {
/* 205 */         boolean downloading = false;
/* 206 */         if (nativesIn == null) {
/* 207 */           this.progressHandler.handleProgress(EnumProgress.DOWNLOADING, -1.0F);
/* 208 */           downloading = true;
/* 209 */           File download = new File(this.installDir, "download.zip.temp");
/* 210 */           PackageDownloader.downloadNatives(
/* 211 */               CefBuildInfo.fromClasspath(), EnumPlatform.getCurrentPlatform(), download, f -> this.progressHandler.handleProgress(EnumProgress.DOWNLOADING, f.floatValue()), this.mirrors);
/*     */ 
/*     */ 
/*     */           
/* 215 */           nativesIn = new ZipInputStream(new FileInputStream(download));
/*     */           
/* 217 */           boolean found = false; ZipEntry entry;
/* 218 */           while ((entry = ((ZipInputStream)nativesIn).getNextEntry()) != null) {
/* 219 */             if (entry.getName().endsWith(".tar.gz")) {
/* 220 */               found = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 224 */           if (!found) {
/* 225 */             throw new IOException("Downloaded artifact did not contain a .tar.gz archive");
/*     */           }
/*     */         } 
/*     */         
/* 229 */         this.progressHandler.handleProgress(EnumProgress.EXTRACTING, -1.0F);
/* 230 */         TarGzExtractor.extractTarGZ(this.installDir, nativesIn);
/* 231 */         if (downloading && 
/* 232 */           !(new File(this.installDir, "download.zip.temp")).delete()) {
/* 233 */           throw new IOException("Could not remove downloaded temp file");
/*     */         }
/*     */       }
/*     */       finally {
/*     */         
/* 238 */         if (nativesIn != null) {
/* 239 */           nativesIn.close();
/*     */         }
/*     */       } 
/*     */       
/* 243 */       this.progressHandler.handleProgress(EnumProgress.INSTALL, -1.0F);
/*     */       
/* 245 */       if (EnumPlatform.getCurrentPlatform().getOs().isMacOSX()) {
/* 246 */         UnquarantineUtil.unquarantine(this.installDir);
/*     */       }
/*     */       
/* 249 */       if (!(new File(this.installDir, "install.lock")).createNewFile()) {
/* 250 */         throw new IOException("Could not create install.lock to complete installation");
/*     */       }
/*     */     } 
/* 253 */     this.installed = true;
/* 254 */     return this;
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
/*     */ 
/*     */   
/*     */   public CefApp build() throws IOException, UnsupportedPlatformException, InterruptedException, CefInitializationException {
/* 269 */     if (this.instance != null) {
/* 270 */       return this.instance;
/*     */     }
/*     */     
/* 273 */     synchronized (this.lock) {
/* 274 */       if (this.building) {
/*     */ 
/*     */         
/* 277 */         if (this.instance == null)
/*     */         {
/* 279 */           this.lock.wait();
/*     */         }
/* 281 */         return this.instance;
/*     */       } 
/* 283 */       this.building = true;
/*     */     } 
/* 285 */     install();
/* 286 */     this.progressHandler.handleProgress(EnumProgress.INITIALIZING, -1.0F);
/* 287 */     synchronized (this.lock) {
/*     */ 
/*     */       
/* 290 */       this.instance = CefInitializer.initialize(this.installDir, this.jcefArgs, this.cefSettings);
/*     */       
/* 292 */       Runtime.getRuntime().addShutdownHook(new Thread(() -> this.instance.dispose()));
/*     */       
/* 294 */       this.progressHandler.handleProgress(EnumProgress.INITIALIZED, -1.0F);
/*     */       
/* 296 */       this.lock.notifyAll();
/*     */     } 
/* 298 */     return this.instance;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\CefAppBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */