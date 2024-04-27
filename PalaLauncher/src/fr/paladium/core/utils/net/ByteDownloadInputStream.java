/*     */ package fr.paladium.core.utils.net;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ 
/*     */ public class ByteDownloadInputStream
/*     */   extends FilterInputStream {
/*     */   private String url;
/*     */   private String token;
/*     */   private boolean active;
/*     */   private long size;
/*     */   private long downloadedBytes;
/*     */   private CloseableHttpResponse response;
/*     */   
/*     */   public ByteDownloadInputStream(String url, long size, String token) throws IOException {
/*  25 */     super(null);
/*  26 */     this.token = token;
/*     */     
/*  28 */     this.url = url;
/*  29 */     this.active = true;
/*  30 */     this.size = size;
/*     */   }
/*     */   public boolean startDownload(File file) throws IOException {
/*     */     
/*  34 */     try { CloseableHttpClient client = CustomHttpClient.createClient(); 
/*  35 */       try { URL url = new URL(this.url);
/*  36 */         URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
/*  37 */         HttpGet req = new HttpGet(uri);
/*  38 */         if (this.token != null) {
/*  39 */           req.addHeader("Authorization", "Bearer " + this.token);
/*     */         }
/*     */         
/*  42 */         this.response = client.execute((HttpUriRequest)req);
/*  43 */         this.in = this.response.getEntity().getContent();
/*     */         
/*  45 */         FileUtils.copyInputStreamToFile(this, file);
/*  46 */         boolean bool = true;
/*  47 */         if (client != null) client.close();  return bool; } catch (Throwable throwable) { if (client != null) try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (FileNotFoundException e)
/*  48 */     { throw e; }
/*  49 */     catch (IOException|java.net.URISyntaxException e)
/*  50 */     { e.printStackTrace();
/*     */       
/*  52 */       return false; }
/*     */   
/*     */   }
/*     */   public int read() throws IOException {
/*  56 */     int c = this.in.read();
/*  57 */     if (c >= 0) {
/*  58 */       this.downloadedBytes++;
/*     */     }
/*     */     
/*  61 */     return c;
/*     */   }
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*  65 */     int nr = this.in.read(b);
/*  66 */     if (nr > 0) {
/*  67 */       this.downloadedBytes += nr;
/*     */     }
/*     */     
/*  70 */     return nr;
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  74 */     int nr = this.in.read(b, off, len);
/*  75 */     if (nr > 0) {
/*  76 */       this.downloadedBytes += nr;
/*     */     }
/*     */     
/*  79 */     return nr;
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/*  84 */     return this.in.skip(n);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  89 */     this.active = false;
/*     */     
/*  91 */     if (this.size > 0L) {
/*  92 */       this.downloadedBytes = this.size;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/*  98 */     this.in.reset();
/*  99 */     this.active = true;
/* 100 */     this.downloadedBytes = 0L;
/*     */   }
/*     */   
/*     */   public long getDownloadedBytes() {
/* 104 */     return this.downloadedBytes;
/*     */   }
/*     */   
/*     */   public long getSize() {
/* 108 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 112 */     return this.active;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\net\ByteDownloadInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */