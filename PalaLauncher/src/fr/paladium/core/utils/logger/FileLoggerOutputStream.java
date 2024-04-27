/*    */ package fr.paladium.core.utils.logger;
/*    */ 
/*    */ import fr.paladium.core.utils.debugger.Debugger;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.text.SimpleDateFormat;
/*    */ 
/*    */ public class FileLoggerOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   private final PrintStream out;
/*    */   private final OutputStream logger;
/* 15 */   private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
/*    */   
/*    */   private boolean shouldPrintDebugState = false;
/*    */   
/*    */   public FileLoggerOutputStream(PrintStream out, OutputStream logger) {
/* 20 */     this.out = out;
/* 21 */     this.logger = logger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 26 */     if (this.shouldPrintDebugState) {
/* 27 */       String date = "[" + this.dateFormat.format(Long.valueOf(System.currentTimeMillis())) + "] ";
/*    */       
/* 29 */       String state = date + ((Debugger.getActiveState() != null) ? ("[" + Debugger.getActiveState() + "] ") : "[N/A]");
/* 30 */       state = state + " ";
/* 31 */       byte[] bytes = state.getBytes(StandardCharsets.UTF_8);
/* 32 */       this.out.write(bytes);
/* 33 */       this.logger.write(bytes);
/*    */       
/* 35 */       this.shouldPrintDebugState = false;
/*    */     } 
/*    */     
/* 38 */     this.out.write(b);
/* 39 */     this.logger.write(b);
/*    */     
/* 41 */     if (b == 10) {
/* 42 */       this.shouldPrintDebugState = true;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {
/* 48 */     super.flush();
/* 49 */     this.out.flush();
/* 50 */     this.logger.flush();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 55 */     super.close();
/* 56 */     this.out.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\logger\FileLoggerOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */