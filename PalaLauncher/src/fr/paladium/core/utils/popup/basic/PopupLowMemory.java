/*    */ package fr.paladium.core.utils.popup.basic;
/*    */ 
/*    */ import fr.paladium.core.utils.popup.Popup;
/*    */ import fr.paladium.core.utils.popup.PopupButton;
/*    */ import fr.paladium.core.utils.popup.PopupButtonType;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PopupLowMemory
/*    */   extends Popup
/*    */ {
/*    */   public PopupLowMemory(long memory) {
/* 14 */     setTitle("Mémoire vive faible");
/*    */     
/* 16 */     addLine("Il ne reste que " + FileUtils.byteCountToDisplaySize(memory) + " de mémoire vive (RAM) disponible sur votre ordinateur.");
/* 17 */     addLine("Le jeu peut rencontrer des problèmes de performances ou se fermer inopinément. Essayez de fermer des applications inutilisées sur votre ordinateur.");
/*    */     
/* 19 */     setPrimaryButton(PopupButton.builder().text("J'ai compris").type(PopupButtonType.CLOSE).build());
/* 20 */     setSecondaryButton(PopupButton.builder().text("Ouvrir le gestionnaire de tâches").type(PopupButtonType.OPEN_TASK_MANAGER).build());
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\popup\basic\PopupLowMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */