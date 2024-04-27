/*    */ package fr.paladium.core.utils.io;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
/*    */ import java.util.Set;

/*    */ import org.apache.commons.codec.digest.DigestUtils;
/*    */ 
/*    */ public class FileUtils {
/*    */   public static boolean checkSha1(File file, String sha1) throws IOException {
/* 16 */     InputStream inputStream = Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]);
/* 17 */     String sha1Hex = DigestUtils.sha1Hex(inputStream);
/* 18 */     inputStream.close();
/* 19 */     return sha1Hex.equals(sha1);
/*    */   }
/*    */   
/*    */   public static String getSha1(File file) throws Exception {
/* 23 */     InputStream inputStream = Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]);
/* 24 */     String sha1Hex = DigestUtils.sha1Hex(inputStream);
/* 25 */     inputStream.close();
/* 26 */     return sha1Hex;
/*    */   }
/*    */   
/*    */   public static long getURLSize(URL url) throws IOException {
/* 30 */     HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
/* 31 */     httpURLConnection.setRequestMethod("HEAD");
/* 32 */     return httpURLConnection.getContentLengthLong();
/*    */   }
/*    */   
/*    */   public static long getURLSize(URL url, String token) throws IOException {
/* 36 */     HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
/* 37 */     httpURLConnection.setRequestMethod("HEAD");
/* 38 */     httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);
/* 39 */     return httpURLConnection.getContentLengthLong();
/*    */   }
/*    */   
/*    */   public static long getFileSize(File file) throws IOException {
/* 43 */     return Files.size(file.toPath());
/*    */   }
/*    */   
/*    */   public static Collection<File> listFileTree(File dir) {
/* 47 */     Set<File> fileTree = new HashSet<>();
/* 48 */     if (dir == null || dir.listFiles() == null) {
/* 49 */       return fileTree;
/*    */     }
/*    */     
/* 52 */     for (File entry : (File[])Objects.<File[]>requireNonNull(dir.listFiles())) {
/* 53 */       if (entry.isFile()) {
/* 54 */         fileTree.add(entry);
/*    */       } else {
/* 56 */         fileTree.addAll(listFileTree(entry));
/*    */       } 
/*    */     } 
/*    */     
/* 60 */     return fileTree;
/*    */   }
/*    */   
/*    */   public static Path getLastFile(File fileDir) throws IOException {
/* 64 */     Path dir = fileDir.toPath();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 69 */     Path lastFilePath = Files.list(dir).filter(f -> !Files.isDirectory(f, new java.nio.file.LinkOption[0])).max(Comparator.comparingLong(f -> f.toFile().lastModified())).orElse(null);
/*    */     
/* 71 */     return lastFilePath;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\io\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */