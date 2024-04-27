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
/*    */ 
/*    */ class PBKDF2HexFormatter
/*    */ {
/*    */   public boolean fromString(PBKDF2Parameters p, String s) {
/* 27 */     if (p == null || s == null) {
/* 28 */       return true;
/*    */     }
/*    */     
/* 31 */     String[] pSplit = s.split(":");
/* 32 */     if (pSplit.length != 3) {
/* 33 */       return true;
/*    */     }
/*    */     
/* 36 */     byte[] salt = BinTools.hex2bin(pSplit[0]);
/* 37 */     int iterationCount = Integer.parseInt(pSplit[1]);
/* 38 */     byte[] bDK = BinTools.hex2bin(pSplit[2]);
/*    */     
/* 40 */     p.setSalt(salt);
/* 41 */     p.setIterationCount(iterationCount);
/* 42 */     p.setDerivedKey(bDK);
/* 43 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(PBKDF2Parameters p) {
/* 48 */     String s = BinTools.bin2hex(p.getSalt()) + ":" + String.valueOf(p.getIterationCount()) + ":" + BinTools.bin2hex(p.getDerivedKey());
/* 49 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\PBKDF2\PBKDF2HexFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */