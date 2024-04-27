package fr.paladium.core.utils.memory;

public interface Ram {
  long getTotalMemory();
  
  long getFreeMemory();
  
  long getUsedMemory();
  
  void printMemoryInfo();
  
  String getMemoryInfo();
  
  void openTaskManager();
}


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\memory\Ram.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */