/*    */ package fr.paladium.core.utils.popup;
/*    */ 
/*    */ import fr.paladium.ui.frame.LauncherFrame;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Popup
/*    */ {
/*    */   private static final String SCRIPT_FORMAT = "window.displayErrors(\"%s\", %s, \"%s\", %s, \"%s\", \"%s\", %s);";
/*    */   private String title;
/* 32 */   private List<String> lines = new ArrayList<>();
/*    */   private boolean critical;
/*    */   
/*    */   public Popup setTitle(String title) {
/* 36 */     this.title = title;
/* 37 */     return this;
/*    */   }
/*    */   private PopupButton button1; private PopupButton button2;
/*    */   public Popup setCritical() {
/* 41 */     this.critical = true;
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public Popup setPrimaryButton(PopupButton button) {
/* 46 */     this.button1 = button;
/* 47 */     return this;
/*    */   }
/*    */   
/*    */   public Popup setSecondaryButton(PopupButton button) {
/* 51 */     this.button2 = button;
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public Popup addLine(String line) {
/* 56 */     this.lines.add("\"" + line + "\"");
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public void show() {
/* 61 */     String script = formatScript();
/* 62 */     System.out.println(script);
/* 63 */     LauncherFrame.getInstance().getBuilder().execute(script);
/* 64 */     System.out.println("[Popup] " + this.title + ": " + this.lines);
/*    */   }
/*    */   
/*    */   private String formatScript() {
/* 68 */     return String.format("window.displayErrors(\"%s\", %s, \"%s\", %s, \"%s\", \"%s\", %s);", new Object[] { this.title, this.lines, this.button1.getText(), 
/* 69 */           (this.button2 != null) ? ("\"" + this.button2.getText() + "\"") : "undefined", this.button1
/* 70 */           .getType().name(), (this.button2 != null) ? this.button2.getType().name() : "CLOSE", 
/* 71 */           Boolean.valueOf(this.critical) });
/*    */   }
/*    */   
/*    */   public static Popup create() {
/* 75 */     return new Popup();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\popup\Popup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */