/*    */ package fr.paladium.core.distribution.property;
/*    */ 
/*    */ import fr.paladium.core.distribution.dto.DistributionFile;
/*    */ import fr.paladium.core.distribution.dto.DistributionModel;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Properties
/*    */ {
/*    */   public static final String CLASSPATH = "classpath";
/*    */   public static final String JAVA = "java";
/*    */   public static final String CHECK_SHA1 = "check_sha1";
/*    */   public static final String WHITELIST = "whitelist";
/*    */   public static final String DOWNLOAD_TOKEN = "download_token";
/* 23 */   private static final Map<String, Property<?>> propertyMap = new HashMap<>(); static {
/* 24 */     propertyMap.put("classpath", new Property<Boolean>(Boolean.FALSE)
/*    */         {
/*    */           public Boolean getValue(Object object) {
/* 27 */             return Boolean.valueOf(Boolean.parseBoolean(object.toString()));
/*    */           }
/*    */         });
/*    */     
/* 31 */     propertyMap.put("java", new Property<Boolean>(Boolean.FALSE)
/*    */         {
/*    */           public Boolean getValue(Object object) {
/* 34 */             return Boolean.valueOf(Boolean.parseBoolean(object.toString()));
/*    */           }
/*    */         });
/*    */     
/* 38 */     propertyMap.put("check_sha1", new Property<Boolean>(Boolean.TRUE)
/*    */         {
/*    */           public Boolean getValue(Object object) {
/* 41 */             return Boolean.valueOf(Boolean.parseBoolean(object.toString()));
/*    */           }
/*    */         });
/*    */     
/* 45 */     propertyMap.put("whitelist", new Property<List<String>>(null)
/*    */         {
/*    */           public List<String> getValue(Object object) {
/* 48 */             return (List<String>)object;
/*    */           }
/*    */         });
/*    */     
/* 52 */     propertyMap.put("download_token", new Property<Boolean>(Boolean.FALSE)
/*    */         {
/*    */           public Boolean getValue(Object object) {
/* 55 */             return Boolean.valueOf(Boolean.parseBoolean(object.toString()));
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public static <T> T getDefault(String key) {
/* 61 */     return ((Property<T>)propertyMap.get(key)).getDefaultValue();
/*    */   }
/*    */   
/*    */   public static <T> T getProperty(String key, Map<String, Object> properties) {
/* 65 */     if (properties == null || !properties.containsKey(key)) {
/* 66 */       return ((Property<T>)propertyMap.get(key)).getDefaultValue();
/*    */     }
/* 68 */     return ((Property<T>)propertyMap.get(key)).getValue(properties.get(key));
/*    */   }
/*    */   
/*    */   public static <T> T getProperty(String key, DistributionModel model) {
/* 72 */     return getProperty(key, model.getProperties());
/*    */   }
/*    */   
/*    */   public static <T> T getProperty(String key, DistributionFile file) {
/* 76 */     return getProperty(key, file.getProperties());
/*    */   }
/*    */   
/*    */   public static boolean hasProperty(String key, Map<String, Object> properties) {
/* 80 */     return (properties != null && properties.containsKey(key));
/*    */   }
/*    */   
/*    */   public static boolean hasProperty(String key, DistributionModel model) {
/* 84 */     return hasProperty(key, model.getProperties());
/*    */   }
/*    */   
/*    */   public static boolean hasProperty(String key, DistributionFile file) {
/* 88 */     return hasProperty(key, file.getProperties());
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\distribution\property\Properties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */