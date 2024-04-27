/*    */ package fr.paladium.core.utils.popup.basic;
/*    */ 
/*    */ import fr.paladium.core.exception.DiskSpaceException;
/*    */ import fr.paladium.core.utils.popup.Popup;
/*    */ import fr.paladium.core.utils.popup.PopupButton;
/*    */ import fr.paladium.core.utils.popup.PopupButtonType;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PopupDiskSpace
/*    */   extends Popup
/*    */ {
/*    */   public PopupDiskSpace(DiskSpaceException ex) {
/* 15 */     setTitle("Espace disque insuffisant");
/*    */     
/* 17 */     addLine("L'espace disque disponible sur votre ordinateur est insuffisant.");
/* 18 */     addLine("Il ne reste que " + FileUtils.byteCountToDisplaySize(ex.getFreeSpace()) + " d'espace disque disponible sur votre ordinateur.");
/* 19 */     addLine("Au moins " + FileUtils.byteCountToDisplaySize(ex.getNeededSpace()) + " d'espace disque est nécessaire pour mettre à jour le jeu.");
/*    */     
/* 21 */     setPrimaryButton(PopupButton.builder().text("Fermer").type(PopupButtonType.CLOSE).build());
/* 22 */     setSecondaryButton(PopupButton.builder().text("Ouvrir l'explorateur de fichiers").type(PopupButtonType.OPEN_FILE_EXPLORER).build());
/* 23 */     setCritical();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\popup\basic\PopupDiskSpace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */