/*    */ package fr.paladium.core.utils.session;
/*    */ 
/*    */ import fr.paladium.core.utils.encryption.AESEncryptionHelper;
/*    */ import fr.paladium.core.utils.encryption.LocalEncryptionKey;
/*    */ import fr.paladium.core.utils.json.JsonOnlineParser;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class SessionStorageData
/*    */ {
/* 13 */   public Map<String, String> localStorage = new HashMap<>();
/*    */   
/*    */   public <T> T getProperty(String key, Class<T> type) {
/* 16 */     return (T)JsonOnlineParser.GSON.fromJson(this.localStorage.get(key), type);
/*    */   }
/*    */   
/*    */   public void setProperty(String key, Object value) {
/* 20 */     this.localStorage.put(key, JsonOnlineParser.GSON.toJson(value));
/*    */   }
/*    */   
/*    */   public boolean hasProperty(String key) {
/* 24 */     return this.localStorage.containsKey(key);
/*    */   }
/*    */   
/*    */   public String encrypt() {
/* 28 */     return AESEncryptionHelper.encrypt(LocalEncryptionKey.getLocalKey(), JsonOnlineParser.GSON.toJson(this).getBytes(StandardCharsets.UTF_8));
/*    */   }
/*    */   
/*    */   public static SessionStorageData decrypt(String data) {
/* 32 */     return (SessionStorageData)JsonOnlineParser.GSON.fromJson(AESEncryptionHelper.decrypt(LocalEncryptionKey.getLocalKey(), data.getBytes(StandardCharsets.UTF_8)), SessionStorageData.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\session\SessionStorageData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */