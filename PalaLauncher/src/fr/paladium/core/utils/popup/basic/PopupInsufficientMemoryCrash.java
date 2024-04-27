/*    */ package fr.paladium.core.utils.popup.basic;
/*    */ 
/*    */ import fr.paladium.core.controller.OptionsController;
/*    */ import fr.paladium.core.utils.io.RamUtils;
/*    */ import fr.paladium.core.utils.option.dto.GameDistributionOptions;
/*    */ import fr.paladium.core.utils.popup.Popup;
/*    */ import fr.paladium.core.utils.popup.PopupButton;
/*    */ import fr.paladium.core.utils.popup.PopupButtonType;
/*    */ 
/*    */ 
/*    */ public class PopupInsufficientMemoryCrash
/*    */   extends Popup
/*    */ {
/*    */   public PopupInsufficientMemoryCrash(String distributionId) {
/* 15 */     setTitle("Mémoire vive insuffisante");
/*    */     
/* 17 */     addLine("Le jeu s’est fermé en raison d’un manque de mémoire vive (RAM) disponible sur votre ordinateur.");
/* 18 */     GameDistributionOptions options = OptionsController.getInstance().getDistributionOptions(distributionId);
/* 19 */     if (RamUtils.getRam().getFreeMemory() / 1024L / 1024L / 1024L > (options.getMemory().getMax() + 2)) {
/* 20 */       addLine("Essayez d'augmenter la mémoire vive allouée dans les paramètres du launcher, puis réessayez.");
/*    */     } else {
/* 22 */       addLine("Essayez de fermer des applications inutilisées sur votre ordinateur puis réessayez.");
/*    */     } 
/* 24 */     setPrimaryButton(PopupButton.builder().text("Fermer").type(PopupButtonType.CLOSE).build());
/* 25 */     setSecondaryButton(PopupButton.builder().text("Ouvrir le gestionnaire de tâches").type(PopupButtonType.OPEN_TASK_MANAGER).build());
/* 26 */     setCritical();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\popup\basic\PopupInsufficientMemoryCrash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */