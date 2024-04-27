package net.lingala.zip4j.io.outputstream;

import java.io.IOException;

public interface OutputStreamWithSplitZipSupport {
  long getFilePointer() throws IOException;
  
  int getCurrentSplitFileCounter();
}


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\io\outputstream\OutputStreamWithSplitZipSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */