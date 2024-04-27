/*     */ package fr.paladium.core.distribution;
/*     */ import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import fr.paladium.core.controller.DistributionController;
/*     */ 
/*     */ import fr.paladium.core.distribution.dto.DistributionArguments;
/*     */ import fr.paladium.core.distribution.dto.DistributionFile;
import fr.paladium.core.distribution.dto.DistributionMaintenance;
/*     */ import fr.paladium.core.distribution.dto.DistributionModel;
/*     */ import fr.paladium.core.distribution.dto.DistributionOS;
/*     */ import fr.paladium.core.distribution.dto.asset.DistributionMinecraftAssetObject;
/*     */ import fr.paladium.core.distribution.dto.asset.DistributionMinecraftAssets;
import fr.paladium.core.utils.io.FileUtils;
/*     */ import fr.paladium.core.utils.io.OsCheck;
/*     */ import fr.paladium.core.utils.json.JsonOnlineParser;
/*     */ 
/*     */ public class GameDistribution {
/*     */   private String name;
/*     */   private String parent;
/*     */   private String root;
/*     */   private String version;
/*     */   private String assetIndex;
/*     */   @SerializedName("mc_version")
/*     */   private String mcVersion;
/*     */   private DistributionMaintenance maintenance;
/*     */   
/*  27 */   public String getName() { return this.name; } private String main; private DistributionArguments arguments; private List<DistributionModel> models; private List<DistributionFile> files; private transient File rootDir; private transient Map<String, DistributionModel> modelByName; private transient List<String> classpath; private transient String java; public String getParent() {
/*  28 */     return this.parent; }
/*  29 */   public String getRoot() { return this.root; }
/*  30 */   public String getVersion() { return this.version; } public String getAssetIndex() {
/*  31 */     return this.assetIndex;
/*     */   }
/*  33 */   public String getMcVersion() { return this.mcVersion; }
/*  34 */   public DistributionMaintenance getMaintenance() { return this.maintenance; }
/*  35 */   public String getMain() { return this.main; }
/*  36 */   public DistributionArguments getArguments() { return this.arguments; }
/*  37 */   public List<DistributionModel> getModels() { return this.models; } public List<DistributionFile> getFiles() {
/*  38 */     return this.files;
/*     */   }
/*     */   
/*  41 */   public File getRootDir() { return this.rootDir; }
/*  42 */   public Map<String, DistributionModel> getModelByName() { return this.modelByName; }
/*  43 */   public List<String> getClasspath() { return this.classpath; } public String getJava() {
/*  44 */     return this.java;
/*     */   }
/*     */   public void init() throws Exception {
/*  47 */     if (this.parent != null && !this.parent.isEmpty()) {
/*  48 */       GameDistribution parent = DistributionController.getInstance().parseDistribution(this.parent);
/*  49 */       if (parent == null) {
/*  50 */         throw new FileNotFoundException("Parent distribution not found");
/*     */       }
/*     */       
/*  53 */       this.name = (this.name == null) ? parent.name : this.name;
/*  54 */       this.parent = null;
/*  55 */       this.root = (this.root == null) ? parent.root : this.root;
/*  56 */       this.version = (this.version == null) ? parent.version : this.version;
/*  57 */       this.assetIndex = (this.assetIndex == null) ? parent.assetIndex : this.assetIndex;
/*  58 */       this.mcVersion = (this.mcVersion == null) ? parent.mcVersion : this.mcVersion;
/*  59 */       this.maintenance = (this.maintenance == null) ? parent.maintenance : this.maintenance;
/*  60 */       this.main = (this.main == null) ? parent.main : this.main;
/*  61 */       this.arguments = (this.arguments == null) ? parent.arguments : this.arguments;
/*     */       
/*  63 */       List<DistributionModel> copiedModels = new ArrayList<>(parent.models);
/*  64 */       if (this.models != null) {
/*  65 */         for (DistributionModel model : this.models) {
/*  66 */           if (copiedModels.contains(model)) {
/*  67 */             copiedModels.remove(model);
/*     */           }
/*  69 */           copiedModels.add(model);
/*     */         } 
/*     */       }
/*  72 */       this.models = copiedModels;
/*     */       
/*  74 */       List<DistributionFile> copiedFiles = new ArrayList<>(parent.files);
/*  75 */       if (this.files != null) {
/*  76 */         for (DistributionFile file : this.files) {
/*  77 */           if (copiedFiles.contains(file)) {
/*  78 */             copiedFiles.remove(file);
/*     */           }
/*  80 */           copiedFiles.add(file);
/*     */         } 
/*     */       }
/*  83 */       this.files = copiedFiles;
/*     */     } 
/*     */     
/*  86 */     this.rootDir = new File(OsCheck.getAppData(), ((OsCheck.getOperatingSystemType() == DistributionOS.WINDOWS) ? "." : "") + this.root);
/*     */     
/*  88 */     Map<String, Object> assetProperties = new HashMap<>();
/*  89 */     assetProperties.put("check_sha1", Boolean.valueOf(true));
/*  90 */     this.models.add(new DistributionModel("mcassets", "1.0.0", "Minecraft vanilla assets", "assets", assetProperties, null, null));
/*     */     
/*  92 */     DistributionMinecraftAssets assets = (DistributionMinecraftAssets)JsonOnlineParser.GSON.fromJson((JsonElement)JsonOnlineParser.parse(this.assetIndex), DistributionMinecraftAssets.class);
/*  93 */     for (DistributionMinecraftAssetObject object : assets.getObjects().values()) {
/*  94 */       String subHash = object.getHash().substring(0, 2);
/*  95 */       String url = "https://resources.download.minecraft.net/" + subHash + "/" + object.getHash();
/*  96 */       this.files.add(new DistributionFile("objects/" + object.getHash(), "mcassets", url, object.getSize(), object.getHash(), "objects/" + subHash + "/" + object.getHash(), null, new DistributionOS[] { DistributionOS.ALL }, null, null));
/*     */     } 
/*     */     
/*  99 */     String indexSha1 = this.assetIndex.split("/")[(this.assetIndex.split("/")).length - 2];
/* 100 */     this.files.add(new DistributionFile("indexes/" + this.mcVersion + ".json", "mcassets", this.assetIndex, FileUtils.getURLSize(new URL(this.assetIndex)), indexSha1, "indexes/" + this.mcVersion + ".json", null, new DistributionOS[] { DistributionOS.ALL }, null, null));
/*     */     
/* 102 */     this.modelByName = new HashMap<>();
/* 103 */     for (DistributionModel model : this.models) {
/* 104 */       model.setDest(model.getDest().replace("/", File.separator));
/* 105 */       this.modelByName.put(model.getName(), model);
/*     */     } 
/*     */     
/* 108 */     for (DistributionFile file : this.files) {
/* 109 */       file.setPath(file.getPath().replace("/", File.separator));
/*     */     }
/*     */     
/* 112 */     this.classpath = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public GameDistribution addFileToClasspath(File file) {
/* 116 */     this.classpath.add(file.getAbsolutePath());
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public GameDistribution setJava(File file) {
/* 121 */     this.java = file.getAbsolutePath();
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public DistributionModel getModelByName(String name) {
/* 126 */     return this.modelByName.get(name);
/*     */   }
/*     */   
/*     */   public DistributionModel getModelByFile(DistributionFile file) {
/* 130 */     if (file.getModelInstance() != null) {
/* 131 */       return file.getModelInstance();
/*     */     }
/*     */     
/* 134 */     DistributionModel model = getModelByName(file.getModel());
/* 135 */     file.setModelInstance(model);
/* 136 */     return model;
/*     */   }
/*     */   
/*     */   public GameDistribution copy() {
/* 140 */     GameDistribution distribution = new GameDistribution();
/* 141 */     distribution.name = this.name;
/* 142 */     distribution.parent = this.parent;
/* 143 */     distribution.root = this.root;
/* 144 */     distribution.version = this.version;
/* 145 */     distribution.assetIndex = this.assetIndex;
/* 146 */     distribution.mcVersion = this.mcVersion;
/* 147 */     distribution.maintenance = this.maintenance;
/* 148 */     distribution.main = this.main;
/* 149 */     distribution.arguments = this.arguments;
/* 150 */     distribution.models = this.models;
/* 151 */     distribution.files = this.files;
/* 152 */     distribution.rootDir = this.rootDir;
/* 153 */     distribution.modelByName = this.modelByName;
/* 154 */     distribution.classpath = this.classpath;
/* 155 */     distribution.java = this.java;
/* 156 */     return distribution;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\distribution\GameDistribution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */