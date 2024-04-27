package net.lingala.zip4j.crypto.PBKDF2;

interface PRF {
  void init(byte[] paramArrayOfbyte);
  
  byte[] doFinal(byte[] paramArrayOfbyte);
  
  int getHLen();
}


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\PBKDF2\PRF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */