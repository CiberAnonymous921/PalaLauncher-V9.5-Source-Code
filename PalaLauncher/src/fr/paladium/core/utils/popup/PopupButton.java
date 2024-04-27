/*    */ package fr.paladium.core.utils.popup;
/*    */ public class PopupButton {
/*    */   private final String text;
/*    */   private final PopupButtonType type;
/*    */   
/*  6 */   PopupButton(String text, PopupButtonType type) { this.text = text; this.type = type; } public static PopupButtonBuilder builder() { return new PopupButtonBuilder(); } public static class PopupButtonBuilder { private String text; public PopupButtonBuilder text(String text) { this.text = text; return this; } private PopupButtonType type; public PopupButtonBuilder type(PopupButtonType type) { this.type = type; return this; } public PopupButton build() { return new PopupButton(this.text, this.type); } public String toString() { return "PopupButton.PopupButtonBuilder(text=" + this.text + ", type=" + this.type + ")"; }
/*    */      }
/*    */ 
/*    */   
/* 10 */   public String getText() { return this.text; } public PopupButtonType getType() {
/* 11 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\popup\PopupButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */