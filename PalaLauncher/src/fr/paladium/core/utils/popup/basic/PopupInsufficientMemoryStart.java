/*    */ package fr.paladium.core.utils.popup.basic;
/*    */ 
/*    */ import fr.paladium.core.utils.popup.Popup;
/*    */ import fr.paladium.core.utils.popup.PopupButton;
/*    */ import fr.paladium.core.utils.popup.PopupButtonType;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PopupInsufficientMemoryStart
/*    */   extends Popup
/*    */ {
/*    */   public PopupInsufficientMemoryStart(long availableMemory) {
/* 17 */     setTitle("Mémoire vive insuffisante");
/*    */     
/* 19 */     addLine("Lancement du jeu impossible. La mémoire vive (RAM) disponible sur votre ordinateur est trop faible.");
/* 20 */     addLine("Il ne reste que " + FileUtils.byteCountToDisplaySize(availableMemory) + " de mémoire vive (RAM) disponible sur votre ordinateur.");
/* 21 */     addLine("Essayez de fermer des applications inutilisées sur votre ordinateur puis réessayez.");
/* 22 */     setPrimaryButton(PopupButton.builder().text("Fermer").type(PopupButtonType.CLOSE).build());
/* 23 */     setSecondaryButton(PopupButton.builder().text("Ouvrir le gestionnaire de tâches").type(PopupButtonType.OPEN_TASK_MANAGER).build());
/* 24 */     setCritical();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\popup\basic\PopupInsufficientMemoryStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */