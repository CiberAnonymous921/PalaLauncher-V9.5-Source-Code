/*    */ package fr.paladium.core.controller;
/*    */ 
/*    */ import fr.paladium.core.utils.option.Option;
/*    */ import fr.paladium.core.utils.option.dto.GameDistributionOptions;
/*    */ import fr.paladium.core.utils.session.SessionStorage;
/*    */ 
/*    */ public class OptionsController
/*    */ {
/*    */   private static OptionsController instance;
/*    */   
/*    */   public <T> T getSetting(Option setting) {
/* 12 */     return (T)SessionStorage.getInstance().getItem(setting.getKey(), setting.getType());
/*    */   }
/*    */   
/*    */   public <T> T getSetting(Option setting, T defaultValue) {
/* 16 */     if (!SessionStorage.getInstance().hasItem(setting.getKey())) {
/* 17 */       SessionStorage.getInstance().setItem(setting.getKey(), defaultValue);
/* 18 */       return defaultValue;
/*    */     } 
/* 20 */     return (T)SessionStorage.getInstance().getItem(setting.getKey(), setting.getType());
/*    */   }
/*    */   
/*    */   public GameDistributionOptions getDistributionOptions(String distribution) {
/* 24 */     if (!SessionStorage.getInstance().hasItem(distribution)) {
/* 25 */       GameDistributionOptions defaultValue = new GameDistributionOptions();
/* 26 */       SessionStorage.getInstance().setItem(distribution, defaultValue);
/* 27 */       return defaultValue;
/*    */     } 
/*    */     
/* 30 */     return (GameDistributionOptions)SessionStorage.getInstance().getItem(distribution, GameDistributionOptions.class);
/*    */   }
/*    */   
/*    */   public <T> void setSetting(Option setting, T value) {
/* 34 */     SessionStorage.getInstance().setItem(setting.getKey(), value);
/*    */   }
/*    */   
/*    */   public void setDistributionOptions(String distribution, GameDistributionOptions options) {
/* 38 */     SessionStorage.getInstance().setItem(distribution, options);
/*    */   }
/*    */   
/*    */   public static OptionsController getInstance() {
/* 42 */     if (instance == null) {
/* 43 */       instance = new OptionsController();
/*    */     }
/* 45 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\controller\OptionsController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */