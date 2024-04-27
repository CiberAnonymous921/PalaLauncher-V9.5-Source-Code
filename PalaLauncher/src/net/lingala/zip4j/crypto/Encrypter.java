package net.lingala.zip4j.crypto;

import net.lingala.zip4j.exception.ZipException;

public interface Encrypter {
  int encryptData(byte[] paramArrayOfbyte) throws ZipException;
  
  int encryptData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ZipException;
}


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\crypto\Encrypter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */