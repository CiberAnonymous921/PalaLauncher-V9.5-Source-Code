/*    */ package fr.paladium.core.utils.diagnostic.items.network;
/*    */ 
/*    */ import fr.paladium.core.utils.diagnostic.DiagnosticItem;
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ 
/*    */ 
/*    */ public class DiagnosticNetworkConnection
/*    */   extends DiagnosticItem
/*    */ {
/*    */   public DiagnosticNetworkConnection(String name) {
/* 13 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test() {
/* 18 */     return isHostAvailable("google.com");
/*    */   }
/*    */   private boolean isHostAvailable(String hostName) {
/*    */     
/* 22 */     try { Socket socket = new Socket(); 
/* 23 */       try { int port = 80;
/* 24 */         InetSocketAddress socketAddress = new InetSocketAddress(hostName, port);
/* 25 */         socket.connect(socketAddress, 3000);
/* 26 */         boolean bool = true;
/* 27 */         socket.close(); return bool; } catch (Throwable throwable) { try { socket.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException unknownHost)
/* 28 */     { return false; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\diagnostic\items\network\DiagnosticNetworkConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */