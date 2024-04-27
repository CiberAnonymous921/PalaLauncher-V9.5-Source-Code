/*    */ package net.lingala.zip4j.tasks;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ 
/*    */ public abstract class AbstractZipTaskParameters
/*    */ {
/*    */   protected Charset charset;
/*    */   
/*    */   protected AbstractZipTaskParameters(Charset charset) {
/* 10 */     this.charset = charset;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\tasks\AbstractZipTaskParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */