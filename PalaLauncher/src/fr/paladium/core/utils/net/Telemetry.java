/*    */ package fr.paladium.core.utils.net;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import fr.paladium.core.authentication.paladium.response.PaladiumAccount;
/*    */ import fr.paladium.core.controller.AccountController;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.client.methods.CloseableHttpResponse;
/*    */ import org.apache.http.client.methods.HttpPost;
/*    */ import org.apache.http.client.methods.HttpUriRequest;
/*    */ import org.apache.http.entity.StringEntity;
/*    */ import org.apache.http.impl.client.CloseableHttpClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Telemetry
/*    */ {
/*    */   public enum Type
/*    */   {
/* 23 */     ERR_CRASH_REPORT,
/* 24 */     ERR_CRASH_NO_REPORT,
/* 25 */     ERR_EXIT_CODE,
/*    */ 
/*    */     
/* 28 */     ERR_DOWNLOAD_CHECKSUM,
/* 29 */     ERR_NO_DISTRIBUTION,
/* 30 */     ERR_IO_DISTRIBUTION,
/*    */ 
/*    */     
/* 33 */     ERR_MEM_CRASH,
/* 34 */     ERR_MEM_START,
/* 35 */     ERR_MEM_CONFIG,
/*    */ 
/*    */     
/* 38 */     ERR_AUTH_PALADIUM,
/* 39 */     ERR_AUTH_PALADIUM_TOKEN,
/* 40 */     ERR_AUTH_MICROSOFT,
/*    */ 
/*    */     
/* 43 */     INFO_LAUNCH,
/*    */     
/* 45 */     INFO_DOWNLOAD,
/* 46 */     INFO_ADD_ACCOUNT;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void collect(Type type) {
/* 55 */     collect(type, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void collect(Type type, Object extra) {
/*    */     String token;
/* 65 */     PaladiumAccount account = AccountController.getInstance().getCurrentPaladiumAccount();
/*    */ 
/*    */     
/* 68 */     if (account != null && account.getJwt() != null) {
/* 69 */       token = account.getJwt().getJwt();
/*    */     } else {
/* 71 */       token = null;
/*    */     } 
/*    */ 
/*    */     
/* 75 */     JsonObject json = new JsonObject();
/* 76 */     json.addProperty("type", type.name());
/* 77 */     json.addProperty("extra", (extra != null) ? extra.toString() : null);
/*    */     
/* 79 */     try { CloseableHttpClient client = CustomHttpClient.createClient(); 
/* 80 */       try { HttpPost req = new HttpPost("https://api.paladium-pvp.fr/launcher/telemetry");
/* 81 */         if (token != null) {
/* 82 */           req.addHeader("Authorization", "Bearer " + token);
/*    */         }
/* 84 */         req.addHeader("Content-Type", "application/json");
/* 85 */         req.setEntity((HttpEntity)new StringEntity(json.toString(), StandardCharsets.UTF_8));
/*    */ 
/*    */         
/* 88 */         CloseableHttpResponse response = client.execute((HttpUriRequest)req);
/* 89 */         if (response.getStatusLine().getStatusCode() != 204) {
/* 90 */           System.err.println("Unable to send telemetry: " + response.getStatusLine().getStatusCode());
/*    */         }
/* 92 */         if (client != null) client.close();  } catch (Throwable throwable) { if (client != null) try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 93 */     { System.err.println("Error while sending telemetry:");
/* 94 */       e.printStackTrace(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\net\Telemetry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */