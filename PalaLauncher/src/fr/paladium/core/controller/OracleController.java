/*    */ package fr.paladium.core.controller;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import fr.paladium.core.utils.format.UUIDUtils;
/*    */ import fr.paladium.core.utils.json.JsonOnlineParser;
/*    */ import fr.paladium.core.utils.net.CustomHttpClient;
/*    */ import java.io.InputStreamReader;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.client.methods.CloseableHttpResponse;
/*    */ import org.apache.http.client.methods.HttpGet;
/*    */ import org.apache.http.client.methods.HttpPost;
/*    */ import org.apache.http.client.methods.HttpUriRequest;
/*    */ import org.apache.http.entity.ContentType;
/*    */ import org.apache.http.entity.StringEntity;
/*    */ import org.apache.http.impl.client.CloseableHttpClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OracleController
/*    */ {
/*    */   private static OracleController instance;
/*    */   
/*    */   private OracleController() {
/* 24 */     instance = this;
/*    */   }
/*    */   
/*    */   public void captureStartGame() {
/* 28 */     String ip = getPublicIp();
/* 29 */     HttpPost post = new HttpPost("https://api.paladium-pvp.fr/oracle/events/launcher/startgame");
/*    */     
/* 31 */     JsonObject json = new JsonObject();
/* 32 */     json.addProperty("ip", ip);
/* 33 */     json.addProperty("playerUUID", UUIDUtils.fromWithoutDashes(AccountController.getInstance().getCurrentAccount().getId()).toString());
/*    */     
/* 35 */     StringEntity entity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
/* 36 */     post.setEntity((HttpEntity)entity);
/*    */     
/* 38 */     try { CloseableHttpClient client = CustomHttpClient.createClient(); 
/* 39 */       try { CloseableHttpResponse closeableHttpResponse = client.execute((HttpUriRequest)post);
/*    */ 
/*    */         
/* 42 */         if (client != null) client.close();  } catch (Throwable throwable) { if (client != null) try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 43 */     { e.printStackTrace(); }
/*    */   
/*    */   }
/*    */   
/*    */   private String getPublicIp() {
/* 48 */     HttpGet get = new HttpGet("https://api.paladium-pvp.fr/oracle/ip");
/*    */     
/* 50 */     try { CloseableHttpClient client = CustomHttpClient.createClient(); 
/* 51 */       try { CloseableHttpResponse response = client.execute((HttpUriRequest)get);
/* 52 */         if (response.getStatusLine().getStatusCode() != 200)
/* 53 */         { String str1 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 58 */           if (client != null) client.close();  return str1; }  JsonObject object = (JsonObject)JsonOnlineParser.GSON.fromJson(new InputStreamReader(response.getEntity().getContent()), JsonObject.class); String str = object.get("ip").getAsString(); if (client != null) client.close();  return str; } catch (Throwable throwable) { if (client != null) try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 59 */     { e.printStackTrace();
/*    */       
/* 61 */       return null; }
/*    */   
/*    */   }
/*    */   public static OracleController getInstance() {
/* 65 */     if (instance == null) {
/* 66 */       new OracleController();
/*    */     }
/* 68 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\controller\OracleController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */