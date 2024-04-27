/*    */ package me.friwi.jcefmaven.impl.progress;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import me.friwi.jcefmaven.EnumProgress;
/*    */ import me.friwi.jcefmaven.IProgressHandler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConsoleProgressHandler
/*    */   implements IProgressHandler
/*    */ {
/* 16 */   private static final Logger LOGGER = Logger.getLogger(ConsoleProgressHandler.class.getName());
/*    */ 
/*    */   
/*    */   public void handleProgress(EnumProgress state, float percent) {
/* 20 */     Objects.requireNonNull(state, "state cannot be null");
/* 21 */     if (percent != -1.0F && (percent < 0.0F || percent > 100.0F)) {
/* 22 */       throw new RuntimeException("percent has to be -1f or between 0f and 100f. Got " + percent + " instead");
/*    */     }
/* 24 */     LOGGER.log(Level.INFO, state + " |> " + ((percent == -1.0F) ? "In progress..." : (String)Float.valueOf(percent)));
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\me\friwi\jcefmaven\impl\progress\ConsoleProgressHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */