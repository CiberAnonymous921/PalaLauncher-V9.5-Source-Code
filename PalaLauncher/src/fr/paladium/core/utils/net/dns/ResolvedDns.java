/*    */ package fr.paladium.core.utils.net.dns;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ 
/*    */ public class ResolvedDns {
/*    */   private final InetAddress address;
/*    */   private final long expiry;
/*    */   
/*    */   public ResolvedDns(InetAddress address, long expiry) {
/* 10 */     this.address = address; this.expiry = expiry;
/*    */   }
/*    */   
/*    */   public InetAddress getAddress() {
/* 14 */     return this.address;
/*    */   }
/*    */   public long getExpiry() {
/* 17 */     return this.expiry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isExpired() {
/* 24 */     return (System.currentTimeMillis() > this.expiry);
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\net\dns\ResolvedDns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */