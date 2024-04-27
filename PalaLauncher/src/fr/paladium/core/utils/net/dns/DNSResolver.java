/*     */ package fr.paladium.core.utils.net.dns;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.stream.Collectors;

/*     */ import javax.naming.NamingException;
/*     */ import javax.net.ssl.HttpsURLConnection;

/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.entity.StringEntity;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.impl.conn.SystemDefaultDnsResolver;

/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonObject;

/*     */ import fr.paladium.core.utils.io.OsCheck;
/*     */ import fr.paladium.core.utils.net.CustomHttpClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DNSResolver
/*     */ {
/*  41 */   private static final Gson GSON = new Gson();
/*     */   
/*     */   private static final long MIN_TTL = 30L;
/*  44 */   private static final List<String> DOH_RESOLVERS = Arrays.asList(new String[] { "https://cloudflare-dns.com/dns-query", "https://1.1.1.1/dns-query", "https://dns.google/resolve", "https://8.8.8.8/resolve" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static final Map<String, CopyOnWriteArrayList<ResolvedDns>> cache = new ConcurrentHashMap<>();
/*     */   public static DnsResolver getResolver() {
/*  53 */     return resolver;
/*     */   }
/*     */ 
/*     */   
/*  57 */   private static final DnsResolver resolver = (DnsResolver)new SystemDefaultDnsResolver()
/*     */     {
/*     */       public InetAddress[] resolve(String host) throws UnknownHostException {
/*     */         try {
/*  61 */           InetAddress[] addresses = DNSResolver.resolve(host);
/*     */           
/*  63 */           if (addresses == null || addresses.length == 0) {
/*  64 */             System.err.println("[DNS] Critical error: Unable to resolve " + host + " using DOH.");
/*  65 */             return super.resolve(host);
/*     */           } 
/*  67 */           return addresses;
/*  68 */         } catch (Exception e) {
/*  69 */           System.out.println("[DNS] Unable to resolve " + host + " using DOH: " + e.getMessage());
/*  70 */           e.printStackTrace();
/*  71 */           return super.resolve(host);
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<ResolvedDns> resolveDOH(String dohUri, String hostname) throws IOException, NamingException {
/*  87 */     URL uri = new URL(dohUri + "?name=" + hostname);
/*  88 */     HttpsURLConnection connection = (HttpsURLConnection)uri.openConnection();
/*     */     
/*  90 */     connection.setRequestMethod("GET");
/*  91 */     connection.setRequestProperty("accept", "application/dns-json");
/*  92 */     connection.setSSLSocketFactory(CustomHttpClient.loadSSLContext().getSocketFactory());
/*  93 */     connection.setConnectTimeout(10000);
/*  94 */     connection.setReadTimeout(10000);
/*     */ 
/*     */     
/*  97 */     String rawResponse = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
/*  98 */     DohResponse doh = (DohResponse)GSON.fromJson(rawResponse, DohResponse.class);
/*     */ 
/*     */     
/* 101 */     if (doh.getStatus() != 0) {
/* 102 */       throw new IOException("DOH request failed with status " + doh.getStatus());
/*     */     }
/*     */ 
/*     */     
/* 106 */     if (doh.getAnswer() == null) {
/* 107 */       throw new IOException("DOH request failed with no answer");
/*     */     }
/*     */     
/* 110 */     CopyOnWriteArrayList<ResolvedDns> resolved = cache.getOrDefault(hostname, new CopyOnWriteArrayList<>());
/*     */     
/* 112 */     for (DohAnswer answer : doh.getAnswer()) {
/*     */       
/* 114 */       if (answer.getType() == 1 || answer.getType() == 28) {
/* 115 */         long ttl; InetAddress addr = toInetAddress(answer.getData());
/*     */ 
/*     */ 
/*     */         
/* 119 */         if (answer.getTtl() < 30L) {
/* 120 */           ttl = 30L;
/*     */         } else {
/* 122 */           ttl = answer.getTtl();
/*     */         } 
/*     */         
/* 125 */         ResolvedDns sameDns = resolved.stream().filter(r -> r.getAddress().equals(addr)).findFirst().orElse(null);
/* 126 */         if (sameDns == null || sameDns.isExpired()) {
/* 127 */           resolved.add(new ResolvedDns(addr, System.currentTimeMillis() + ttl * 1000L));
/* 128 */           cache.put(hostname, resolved);
/*     */         } 
/* 130 */       } else if (answer.getType() == 27) {
/*     */         
/* 132 */         List<ResolvedDns> cnameResolved = resolveDOH(dohUri, answer.getData());
/*     */         
/* 134 */         resolved.addAll(cnameResolved);
/* 135 */         cache.put(hostname, resolved);
/*     */       } 
/*     */     } 
/* 138 */     return cache.getOrDefault(hostname, new CopyOnWriteArrayList<>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static InetAddress toInetAddress(String address) throws UnknownHostException {
/*     */     byte[] addr;
/* 151 */     if (IPAddressUtil.isIPv4LiteralAddress(address)) {
/* 152 */       addr = IPAddressUtil.textToNumericFormatV4(address);
/* 153 */     } else if (IPAddressUtil.isIPv6LiteralAddress(address)) {
/* 154 */       addr = IPAddressUtil.textToNumericFormatV6(address);
/*     */     } else {
/* 156 */       throw new UnknownHostException("Invalid address: " + address);
/*     */     } 
/* 158 */     return InetAddress.getByAddress(addr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static InetAddress[] resolve(String hostname) throws UnknownHostException, NamingException {
/*     */     try {
/* 171 */       List<ResolvedDns> resolved = cache.get(hostname);
/*     */       
/* 173 */       if (resolved != null) {
/*     */         
/* 175 */         if (resolved.stream().anyMatch(ResolvedDns::isExpired)) {
/*     */           
/* 177 */           resolved = resolveDOHWithoutCache(hostname);
/* 178 */           System.out.println("[DNS] " + hostname + " resolved to " + (String)resolved.stream().map(ResolvedDns::getAddress).map(InetAddress::getHostAddress).collect(Collectors.joining(", ")));
/*     */           
/* 180 */           resolved.removeIf(ResolvedDns::isExpired);
/*     */         } 
/*     */       } else {
/* 183 */         resolved = resolveDOHWithoutCache(hostname);
/* 184 */         System.out.println("[DNS] " + hostname + " resolved to " + (String)resolved.stream().map(ResolvedDns::getAddress).map(InetAddress::getHostAddress).collect(Collectors.joining(", ")));
/*     */       } 
/* 186 */       return (InetAddress[])resolved.stream().map(ResolvedDns::getAddress).toArray(x$0 -> new InetAddress[x$0]);
/* 187 */     } catch (Exception e) {
/* 188 */       System.out.println("[DNS] Unable to resolve " + hostname + " using DOH: " + e.getMessage());
/* 189 */       e.printStackTrace();
/* 190 */       throw new UnknownHostException("Unable to resolve " + hostname + " with DOH");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<ResolvedDns> resolveDOHWithoutCache(String hostname) {
/* 201 */     List<ResolvedDns> resolved = null;
/*     */     
/* 203 */     for (String doh : DOH_RESOLVERS) {
/*     */       try {
/* 205 */         resolved = resolveDOH(doh, hostname);
/*     */         break;
/* 207 */       } catch (Exception ex) {
/*     */         
/* 209 */         if (ex.getMessage().contains("unable to find valid certification path to requested target")) {
/*     */           
/*     */           try {
/* 212 */             HttpPost post = new HttpPost("http://api.paladium-pvp.fr/launcher/ca-issue/v10/" + doh.replace("https://", "") + "/" + hostname);
/*     */             
/* 214 */             JsonObject json = new JsonObject();
/*     */             
/* 216 */             File installDir = new File(OsCheck.getAppData(), "paladium-games-inject");
/* 217 */             File logFile = new File(installDir, "launcher.log");
/* 218 */             String log = IOUtils.toString(logFile.toURI(), StandardCharsets.UTF_8);
/*     */ 
/*     */             
/* 221 */             json.addProperty("error", log);
/*     */             
/* 223 */             StringEntity entity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
/* 224 */             post.setEntity((HttpEntity)entity);
/*     */             
/* 226 */             try { CloseableHttpClient client = HttpClientBuilder.create().build(); 
/* 227 */               try { CloseableHttpResponse closeableHttpResponse = client.execute((HttpUriRequest)post);
/*     */ 
/*     */                 
/* 230 */                 if (client != null) client.close();  } catch (Throwable throwable) { if (client != null) try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception exception) {}
/*     */           }
/* 232 */           catch (Exception exception) {}
/*     */         }
/*     */         
/* 235 */         System.out.println("[DNS] Unable to resolve " + hostname + " using " + doh + ": " + ex.getMessage());
/* 236 */         ex.printStackTrace();
/*     */       } 
/*     */     } 
/* 239 */     return resolved;
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\net\dns\DNSResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */