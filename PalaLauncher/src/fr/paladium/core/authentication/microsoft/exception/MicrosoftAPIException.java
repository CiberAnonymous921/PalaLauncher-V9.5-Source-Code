/*    */ package fr.paladium.core.authentication.microsoft.exception;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import fr.paladium.core.utils.json.JsonOnlineParser;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import org.apache.http.client.methods.CloseableHttpResponse;
/*    */ 
/*    */ public class MicrosoftAPIException
/*    */   extends Exception
/*    */ {
/*    */   private int code;
/*    */   private String error;
/*    */   
/*    */   public MicrosoftAPIException(CloseableHttpResponse response) throws IOException {
/* 16 */     this(response
/* 17 */         .getStatusLine().getStatusCode(), response
/* 18 */         .getStatusLine().getReasonPhrase() + " (" + response.getStatusLine().getStatusCode() + ")", ((JsonObject)JsonOnlineParser.GSON
/* 19 */         .fromJson(new InputStreamReader(response.getEntity().getContent()), JsonObject.class)).toString());
/*    */   }
/*    */ 
/*    */   
/*    */   private MicrosoftAPIException(int code, String message, String error) {
/* 24 */     super(message);
/* 25 */     this.code = code;
/* 26 */     this.error = error;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 30 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getError() {
/* 34 */     return this.error;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\authentication\microsoft\exception\MicrosoftAPIException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */