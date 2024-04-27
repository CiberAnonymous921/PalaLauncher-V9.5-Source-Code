/*    */ package fr.paladium.core.utils.diagnostic.items.network;
/*    */ 
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ 
/*    */ public class DiagnosticNetworkPaladium
/*    */   extends DiagnosticItem
/*    */ {
/*    */   public DiagnosticNetworkPaladium(String name) {
/* 12 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 17 */     return isHostAvailable("palagames-launcher-front.pages.dev");
/*    */   }
/*    */   private boolean isHostAvailable(String hostName) {
/*    */     
/* 21 */     try { Socket socket = new Socket(); 
/* 22 */       try { int port = 80;
/* 23 */         InetSocketAddress socketAddress = new InetSocketAddress(hostName, port);
/* 24 */         socket.connect(socketAddress, 3000);
/* 25 */         boolean bool = true;
/* 26 */         socket.close(); return bool; } catch (Throwable throwable) { try { socket.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException unknownHost)
/* 27 */     { return false; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\network\DiagnosticNetworkPaladium.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */