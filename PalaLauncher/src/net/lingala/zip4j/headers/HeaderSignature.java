/*    */ package net.lingala.zip4j.headers;
/*    */ 
/*    */ public enum HeaderSignature
/*    */ {
/*  5 */   LOCAL_FILE_HEADER(67324752L),
/*  6 */   EXTRA_DATA_RECORD(134695760L),
/*  7 */   CENTRAL_DIRECTORY(33639248L),
/*  8 */   END_OF_CENTRAL_DIRECTORY(101010256L),
/*  9 */   DIGITAL_SIGNATURE(84233040L),
/* 10 */   ARCEXTDATREC(134630224L),
/* 11 */   SPLIT_ZIP(134695760L),
/* 12 */   ZIP64_END_CENTRAL_DIRECTORY_LOCATOR(117853008L),
/* 13 */   ZIP64_END_CENTRAL_DIRECTORY_RECORD(101075792L),
/* 14 */   ZIP64_EXTRA_FIELD_SIGNATURE(1L),
/* 15 */   AES_EXTRA_DATA_RECORD(39169L);
/*    */   
/*    */   private long value;
/*    */   
/*    */   HeaderSignature(long value) {
/* 20 */     this.value = value;
/*    */   }
/*    */   
/*    */   public long getValue() {
/* 24 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4j\headers\HeaderSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */