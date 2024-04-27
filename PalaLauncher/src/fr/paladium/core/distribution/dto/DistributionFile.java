/*    */ package fr.paladium.core.distribution.dto;
/*    */ 
/*    */ import fr.paladium.core.distribution.property.Properties;
/*    */ import java.io.File;
/*    */ import java.util.Map;
import java.util.Objects;
/*    */ 
/*    */ public class DistributionFile {
/*    */   private String name;
/*    */   private String model;
/*    */   private String url;
/*    */   private long size;
/*    */   private String sha1;
/*    */   
/* 14 */   public void setName(String name) { this.name = name; } private String path; private Map<String, Object> properties; private DistributionOS[] os; private transient File fileDest; private transient DistributionModel modelInstance; public void setModel(String model) { this.model = model; } public void setUrl(String url) { this.url = url; } public void setSize(long size) { this.size = size; } public void setSha1(String sha1) { this.sha1 = sha1; } public void setPath(String path) { this.path = path; } public void setProperties(Map<String, Object> properties) { this.properties = properties; } public void setOs(DistributionOS[] os) { this.os = os; } public void setFileDest(File fileDest) { this.fileDest = fileDest; } public void setModelInstance(DistributionModel modelInstance) { this.modelInstance = modelInstance; } public DistributionFile(String name, String model, String url, long size, String sha1, String path, Map<String, Object> properties, DistributionOS[] os, File fileDest, DistributionModel modelInstance) {
/* 15 */     this.name = name; this.model = model; this.url = url; this.size = size; this.sha1 = sha1; this.path = path; this.properties = properties; this.os = os; this.fileDest = fileDest; this.modelInstance = modelInstance;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 20 */     return this.name;
/* 21 */   } public String getModel() { return this.model; }
/* 22 */   public String getUrl() { return this.url; }
/* 23 */   public long getSize() { return this.size; }
/* 24 */   public String getSha1() { return this.sha1; }
/* 25 */   public String getPath() { return this.path; }
/* 26 */   public Map<String, Object> getProperties() { return this.properties; } public DistributionOS[] getOs() {
/* 27 */     return this.os;
/*    */   }
/*    */   
/* 30 */   public File getFileDest() { return this.fileDest; } public DistributionModel getModelInstance() {
/* 31 */     return this.modelInstance;
/*    */   }
/*    */   public <T> T getProperty(String key, DistributionModel model) {
/* 34 */     if (!Properties.hasProperty(key, model) && !Properties.hasProperty(key, this)) {
/* 35 */       return (T)Properties.getDefault(key);
/*    */     }
/*    */     
/* 38 */     if (Properties.hasProperty(key, this)) {
/* 39 */       return (T)Properties.getProperty(key, this);
/*    */     }
/*    */     
/* 42 */     return (T)Properties.getProperty(key, model);
/*    */   }
/*    */   
/*    */   public boolean isCompatible(DistributionOS os) {
/* 46 */     for (DistributionOS distributionOS : this.os) {
/* 47 */       if (distributionOS == os || distributionOS == DistributionOS.ALL) {
/* 48 */         return true;
/*    */       }
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 56 */     if (this == o) {
/* 57 */       return true;
/*    */     }
/* 59 */     if (o == null || getClass() != o.getClass()) {
/* 60 */       return false;
/*    */     }
/*    */     
/* 63 */     DistributionFile that = (DistributionFile)o;
/* 64 */     return Objects.equals(this.path, that.path);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     return Objects.hash(new Object[] { this.path });
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\distribution\dto\DistributionFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */