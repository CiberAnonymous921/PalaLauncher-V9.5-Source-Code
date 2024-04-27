/*    */ package fr.paladium.core.utils.net;
/*    */ 
/*    */ import fr.paladium.core.utils.net.dns.DNSResolver;
/*    */ import java.io.IOException;
/*    */ import java.security.KeyManagementException;
/*    */ import java.security.KeyStore;
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import javax.net.ssl.SSLContext;
/*    */ import javax.net.ssl.SSLSocketFactory;
/*    */ import javax.net.ssl.TrustManager;
/*    */ import javax.net.ssl.TrustManagerFactory;
/*    */ import org.apache.http.client.methods.CloseableHttpResponse;
/*    */ import org.apache.http.client.methods.HttpRequestBase;
/*    */ import org.apache.http.client.methods.HttpUriRequest;
/*    */ import org.apache.http.config.Lookup;
/*    */ import org.apache.http.config.RegistryBuilder;
/*    */ import org.apache.http.conn.HttpClientConnectionManager;
/*    */ import org.apache.http.conn.socket.PlainConnectionSocketFactory;
/*    */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*    */ import org.apache.http.impl.client.CloseableHttpClient;
/*    */ import org.apache.http.impl.client.HttpClientBuilder;
/*    */ import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
/*    */ import org.apache.http.ssl.SSLContexts;
/*    */ 
/*    */ public class CustomHttpClient
/*    */ {
/*    */   private static SSLSocketFactory socketFactory;
/*    */   private static SSLContext context;
/*    */   
/*    */   public static SSLContext loadSSLContext() {
/* 32 */     if (context != null) {
/* 33 */       return context;
/*    */     }
/*    */     
/*    */     try {
/* 37 */       KeyStore keyStore = CertificateUtil.getKeystore();
/*    */       
/* 39 */       if (keyStore == null) {
/* 40 */         throw new IOException("Unable to load keystore");
/*    */       }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 46 */       context = SSLContexts.custom().loadKeyMaterial(keyStore, CertificateUtil.getKeyStorePassword()).loadTrustMaterial(keyStore, null).build();
/* 47 */       socketFactory = createSSLSocketFactory(keyStore);
/* 48 */       SSLContext.setDefault(context);
/* 49 */       System.out.println("SSL keystore set.");
/* 50 */     } catch (Exception e) {
/* 51 */       System.out.println("Unable to set keystore. Using default SSLContext.");
/* 52 */       context = SSLContexts.createDefault();
/* 53 */       e.printStackTrace();
/*    */     } 
/* 55 */     return context;
/*    */   }
/*    */   
/*    */   private static SSLSocketFactory createSSLSocketFactory(KeyStore keyStore) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
/* 59 */     TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/* 60 */     tmf.init(keyStore);
/* 61 */     TrustManager[] trustManagers = tmf.getTrustManagers();
/*    */     
/* 63 */     SSLContext sslContext = SSLContext.getInstance("SSL");
/* 64 */     sslContext.init(null, trustManagers, null);
/* 65 */     return sslContext.getSocketFactory();
/*    */   }
/*    */ 
/*    */   
/*    */   public static BasicHttpClientConnectionManager getConnectionManager() {
/* 70 */     loadSSLContext();
/*    */     
/* 72 */     return new BasicHttpClientConnectionManager(
/* 73 */         (Lookup)RegistryBuilder.create()
/* 74 */         .register("http", PlainConnectionSocketFactory.getSocketFactory())
/* 75 */         .register("https", new SSLConnectionSocketFactory(socketFactory, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
/* 76 */         .build(), null, null, 
/*    */ 
/*    */         
/* 79 */         DNSResolver.getResolver());
/*    */   }
/*    */ 
/*    */   
/*    */   public static CloseableHttpClient createClient() {
/* 84 */     BasicHttpClientConnectionManager manager = getConnectionManager();
/*    */     
/* 86 */     return HttpClientBuilder.create()
/* 87 */       .setConnectionManager((HttpClientConnectionManager)manager)
/* 88 */       .setSslcontext(context)
/* 89 */       .build();
/*    */   }
/*    */   
/*    */   public static CloseableHttpResponse execute(HttpRequestBase request) throws IOException {
/* 93 */     return createClient().execute((HttpUriRequest)request);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\net\CustomHttpClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */