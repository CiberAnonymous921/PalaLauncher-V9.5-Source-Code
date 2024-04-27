/*    */ package net.lingala.zip4j.crypto.PBKDF2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class BinTools
/*    */ {
/*    */   public static final String hex = "0123456789ABCDEF";
/*    */   
/*    */   public static String bin2hex(byte[] b) {
/* 28 */     if (b == null) {
/* 29 */       return "";
/*    */     }
/* 31 */     StringBuffer sb = new StringBuffer(2 * b.length);
/* 32 */     for (int i = 0; i < b.length; i++) {
/* 33 */       int v = (256 + b[i]) % 256;
/* 34 */       sb.append("0123456789ABCDEF".charAt(v / 16 & 0xF));
/* 35 */       sb.append("0123456789ABCDEF".charAt(v % 16 & 0xF));
/*    */     } 
/* 37 */     return sb.toString();
/*    */   }
/*    */   
/*    */   public static byte[] hex2bin(String s) {
/* 41 */     String m = s;
/* 42 */     if (s == null) {
/*    */       
/* 44 */       m = "";
/* 45 */     } else if (s.length() % 2 != 0) {
/*    */       
/* 47 */       m = "0" + s;
/*    */     } 
/* 49 */     byte[] r = new byte[m.length() / 2];
/* 50 */     for (int i = 0, n = 0; i < m.length(); n++) {
/* 51 */       char h = m.charAt(i++);
/* 52 */       char l = m.charAt(i++);
/* 53 */       r[n] = (byte)(hex2bin(h) * 16 + hex2bin(l));
/*    */     } 
/* 55 */     return r;
/*    */   }
/*    */   
/*    */   public static int hex2bin(char c) {
/* 59 */     if (c >= '0' && c <= '9') {
/* 60 */       return c - 48;
/*    */     }
/* 62 */     if (c >= 'A' && c <= 'F') {
/* 63 */       return c - 65 + 10;
/*    */     }
/* 65 */     if (c >= 'a' && c <= 'f') {
/* 66 */       return c - 97 + 10;
/*    */     }
/* 68 */     throw new IllegalArgumentException("Input string may only contain hex digits, but found '" + c + "'");
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\PBKDF2\BinTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */