/*    */ package fr.paladium.core.utils.option;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Option
/*    */ {
/*  8 */   LAUNCHER_MAXIMIZED_AT_STARTUP("option.launcherMaximizedAtStartup", Boolean.class);
/*    */   private final Class<?> type;
/*    */   private final String key;
/*    */   
/* 12 */   public String getKey() { return this.key; } public Class<?> getType() {
/* 13 */     return this.type;
/*    */   }
/*    */   Option(String key, Class<?> type) {
/* 16 */     this.key = key;
/* 17 */     this.type = type;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\option\Option.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */