/*    */ package fr.paladium.core.utils.encryption;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Base64;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.spec.IvParameterSpec;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ 
/*    */ 
/*    */ public class AESEncryptionHelper
/*    */ {
/*    */   private static final String cipherTransformationWithPadding = "AES/CBC/PKCS5Padding";
/*    */   private static final String aesEncryptionAlgorithm = "AES";
/*    */   
/*    */   public static String encrypt(byte[] kKey, byte[] data) {
/*    */     try {
/* 17 */       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 18 */       SecretKeySpec secretKey = new SecretKeySpec(kKey, "AES");
/* 19 */       IvParameterSpec ivparameterspec = new IvParameterSpec(kKey);
/* 20 */       cipher.init(1, secretKey, ivparameterspec);
/* 21 */       byte[] cipherText = cipher.doFinal(data);
/* 22 */       return Base64.getEncoder().encodeToString(cipherText);
/* 23 */     } catch (Exception exception) {
/* 24 */       exception.printStackTrace();
/*    */       
/* 26 */       return null;
/*    */     } 
/*    */   }
/*    */   public static String decrypt(byte[] kKey, byte[] data) {
/*    */     try {
/* 31 */       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 32 */       SecretKeySpec secretKey = new SecretKeySpec(kKey, "AES");
/* 33 */       IvParameterSpec ivparameterspec = new IvParameterSpec(kKey);
/* 34 */       cipher.init(2, secretKey, ivparameterspec);
/* 35 */       byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(data));
/* 36 */       return new String(cipherText, StandardCharsets.UTF_8);
/* 37 */     } catch (Exception exception) {
/* 38 */       exception.printStackTrace();
/*    */       
/* 40 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\encryption\AESEncryptionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */