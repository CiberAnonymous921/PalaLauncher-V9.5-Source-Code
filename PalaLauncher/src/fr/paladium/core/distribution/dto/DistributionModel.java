/*    */ package fr.paladium.core.distribution.dto;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class DistributionModel {
/*    */   private String name;
/*    */   private String version;
/*    */   private String description;
/*    */   
/* 12 */   public void setName(String name) { this.name = name; } private String dest; private Map<String, Object> properties; private DistributionOS[] os; private transient File destDir; public void setVersion(String version) { this.version = version; } public void setDescription(String description) { this.description = description; } public void setDest(String dest) { this.dest = dest; } public void setProperties(Map<String, Object> properties) { this.properties = properties; } public void setOs(DistributionOS[] os) { this.os = os; } public DistributionModel(String name, String version, String description, String dest, Map<String, Object> properties, DistributionOS[] os, File destDir) {
/* 13 */     this.name = name; this.version = version; this.description = description; this.dest = dest; this.properties = properties; this.os = os; this.destDir = destDir;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 17 */     return this.name;
/* 18 */   } public String getVersion() { return this.version; }
/* 19 */   public String getDescription() { return this.description; }
/* 20 */   public String getDest() { return this.dest; }
/* 21 */   public Map<String, Object> getProperties() { return this.properties; } public DistributionOS[] getOs() {
/* 22 */     return this.os;
/*    */   }
/*    */   public File getDestDir() {
/* 25 */     return this.destDir;
/*    */   }
/*    */   public DistributionModel setDestDir(File destDir) {
/* 28 */     this.destDir = destDir;
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   public boolean isCompatible(DistributionOS os) {
/* 33 */     for (DistributionOS distributionOS : this.os) {
/* 34 */       if (distributionOS == os || distributionOS == DistributionOS.ALL) {
/* 35 */         return true;
/*    */       }
/*    */     } 
/* 38 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 43 */     if (this == o) {
/* 44 */       return true;
/*    */     }
/* 46 */     if (o == null || getClass() != o.getClass()) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     DistributionModel that = (DistributionModel)o;
/* 51 */     return Objects.equals(this.dest, that.dest);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 56 */     return Objects.hash(new Object[] { this.dest });
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\distribution\dto\DistributionModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */