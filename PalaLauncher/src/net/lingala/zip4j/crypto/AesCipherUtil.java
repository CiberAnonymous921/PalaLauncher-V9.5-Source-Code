/*    */ package net.lingala.zip4j.crypto;
/*    */ 
/*    */ public class AesCipherUtil
/*    */ {
/*    */   public static void prepareBuffAESIVBytes(byte[] buff, int nonce) {
/*  6 */     buff[0] = (byte)nonce;
/*  7 */     buff[1] = (byte)(nonce >> 8);
/*  8 */     buff[2] = (byte)(nonce >> 16);
/*  9 */     buff[3] = (byte)(nonce >> 24);
/*    */     
/* 11 */     for (int i = 4; i <= 15; i++)
/* 12 */       buff[i] = 0; 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\AesCipherUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */