/*    */ package fr.paladium.core.utils.json;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonObject;
/*    */ import fr.paladium.core.utils.net.CustomHttpClient;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import org.apache.http.client.methods.CloseableHttpResponse;
/*    */ import org.apache.http.client.methods.HttpGet;
/*    */ import org.apache.http.client.methods.HttpUriRequest;
/*    */ import org.apache.http.impl.client.CloseableHttpClient;
/*    */ 
/*    */ public class JsonOnlineParser
/*    */ {
/* 16 */   public static final Gson GSON = (new GsonBuilder()).create();
/* 17 */   public static final Gson PRETTY_GSON = (new GsonBuilder()).setPrettyPrinting().create();
/*    */   
/*    */   public static JsonObject parse(String urlString) {
/* 20 */     HttpGet get = new HttpGet(urlString);
/*    */ 
/*    */     
/* 23 */     try { CloseableHttpClient client = CustomHttpClient.createClient(); 
/* 24 */       try { CloseableHttpResponse response = client.execute((HttpUriRequest)get);
/*    */         
/* 26 */         if (response.getStatusLine().getStatusCode() != 200) {
/* 27 */           throw new IOException("Bad status code: " + response.getStatusLine().getStatusCode());
/*    */         }
/*    */         
/* 30 */         InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
/* 31 */         JsonObject result = (JsonObject)GSON.fromJson(reader, JsonObject.class);
/* 32 */         JsonObject jsonObject1 = result;
/* 33 */         if (client != null) client.close();  return jsonObject1; } catch (Throwable throwable) { if (client != null) try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 34 */     { e.printStackTrace();
/*    */       
/* 36 */       return null; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\json\JsonOnlineParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */