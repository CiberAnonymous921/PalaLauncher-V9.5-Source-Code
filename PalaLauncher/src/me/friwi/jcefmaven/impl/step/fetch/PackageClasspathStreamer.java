/*    */ package me.friwi.jcefmaven.impl.step.fetch;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import me.friwi.jcefmaven.CefBuildInfo;
/*    */ import me.friwi.jcefmaven.EnumPlatform;
/*    */ import org.cef.CefApp;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PackageClasspathStreamer
/*    */ {
/*    */   private static final String LOCATION = "/jcef-natives-{platform}-{tag}.tar.gz";
/*    */   
/*    */   public static InputStream streamNatives(CefBuildInfo info, EnumPlatform platform) {
/* 18 */     return CefApp.class.getResourceAsStream("/jcef-natives-{platform}-{tag}.tar.gz"
/* 19 */         .replace("{platform}", platform.getIdentifier())
/* 20 */         .replace("{tag}", info.getReleaseTag()));
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\impl\step\fetch\PackageClasspathStreamer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */