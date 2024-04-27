/*    */ package fr.paladium.ui.frame;
/*    */ import java.io.IOException;

/*    */ 
/*    */ import fr.paladium.core.authentication.microsoft.response.MicrosoftOAuthToken;
/*    */ import fr.paladium.ui.CefFrame;
/*    */ import me.friwi.jcefmaven.CefInitializationException;
/*    */ import me.friwi.jcefmaven.UnsupportedPlatformException;
/*    */ 
/*    */ public class GamepassFrame
/*    */   extends CefFrame
/*    */ {
/*    */   private static GamepassFrame instance;
/*    */   private final MicrosoftOAuthToken oAuthToken;
/*    */   private final String token;
/*    */   
/*    */   public MicrosoftOAuthToken getOAuthToken() {
/* 17 */     return this.oAuthToken; } public String getToken() {
/* 18 */     return this.token;
/*    */   }
/*    */   public GamepassFrame(MicrosoftOAuthToken oAuthToken, String token) throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
/* 21 */     /*TODO: Default: */ super("Créer votre compte minecraft", "https://palagames-launcher-front.pages.dev/#/Gamepass", 536, 693, false, true);
/* 21 */     //super("Créer votre compte minecraft", "https://paladium.netlify.app/Main/#/Gamepass", 536, 693, false, true);
/* 22 */     this.oAuthToken = oAuthToken;
/* 23 */     this.token = token;
/*    */     
/* 25 */     if (instance != null && instance.isVisible()) {
/* 26 */       instance.close();
/*    */     }
/* 28 */     instance = this;
/*    */   }
/*    */   
/*    */   public static GamepassFrame getInstance() {
/* 32 */     return instance;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladiu\\ui\frame\GamepassFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */