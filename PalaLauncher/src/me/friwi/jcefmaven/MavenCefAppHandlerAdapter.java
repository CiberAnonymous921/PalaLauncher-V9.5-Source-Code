/*    */ package me.friwi.jcefmaven;
/*    */ 
/*    */ import org.cef.CefApp;
/*    */ import org.cef.callback.CefCommandLine;
/*    */ import org.cef.handler.CefAppHandlerAdapter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class MavenCefAppHandlerAdapter
/*    */   extends CefAppHandlerAdapter
/*    */ {
/*    */   public MavenCefAppHandlerAdapter() {
/* 17 */     super(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void onBeforeCommandLineProcessing(String process_type, CefCommandLine command_line) {
/* 22 */     CefApp.getInstance().onBeforeCommandLineProcessing(process_type, command_line);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\MavenCefAppHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */