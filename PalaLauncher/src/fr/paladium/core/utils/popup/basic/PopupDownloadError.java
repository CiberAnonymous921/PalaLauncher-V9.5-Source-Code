/*    */ package fr.paladium.core.utils.popup.basic;
/*    */ 
/*    */ import fr.paladium.core.utils.popup.Popup;
/*    */ import fr.paladium.core.utils.popup.PopupButton;
/*    */ import fr.paladium.core.utils.popup.PopupButtonType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PopupDownloadError
/*    */   extends Popup
/*    */ {
/*    */   public PopupDownloadError() {
/* 14 */     setTitle("Erreur de téléchargement");
/*    */     
/* 16 */     addLine("Une erreur est survenue lors du téléchargement des fichiers du jeu.");
/* 17 */     addLine("Vérifiez que vous disposez d'une connexion internet stable et que vous avez suffisamment d'espace disque disponible. Assurez-vous que votre antivirus ou pare-feu ne bloque pas le téléchargement.");
/* 18 */     addLine("Si le problème persiste, contactez le support technique.");
/*    */     
/* 20 */     setPrimaryButton(PopupButton.builder().text("Fermer").type(PopupButtonType.CLOSE).build());
/* 21 */     setSecondaryButton(PopupButton.builder().text("Signaler un problème").type(PopupButtonType.OPEN_TICKET).build());
/* 22 */     setCritical();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\popup\basic\PopupDownloadError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */