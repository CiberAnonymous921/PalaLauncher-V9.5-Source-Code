/*    */ package fr.paladium.core.authentication.microsoft.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MicrosoftAPIHandledException
/*    */   extends Exception
/*    */ {
/*    */   private final MicrosoftAPIExceptionHandler.Error error;
/*    */   private final MicrosoftAPIException exception;
/*    */   
/*    */   public MicrosoftAPIHandledException(MicrosoftAPIException exception, MicrosoftAPIExceptionHandler.Error error) {
/* 12 */     super(exception.getMessage() + " [" + error.name() + "] (" + exception.getError() + ")", exception);
/* 13 */     this.error = error;
/* 14 */     this.exception = exception;
/*    */   }
/*    */   
/*    */   public String getNotificationMessage() {
/* 18 */     return this.error.getMessage();
/*    */   }
/*    */   
/*    */   public MicrosoftAPIException getException() {
/* 22 */     return this.exception;
/*    */   }
/*    */   
/*    */   public MicrosoftAPIExceptionHandler.Error getError() {
/* 26 */     return this.error;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\authentication\microsoft\exception\MicrosoftAPIHandledException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */