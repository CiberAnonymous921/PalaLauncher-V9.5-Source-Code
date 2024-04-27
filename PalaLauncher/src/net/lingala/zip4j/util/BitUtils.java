/*    */ package net.lingala.zip4j.util;
/*    */ 
/*    */ public class BitUtils
/*    */ {
/*    */   public static boolean isBitSet(byte b, int pos) {
/*  6 */     return ((b & 1L << pos) != 0L);
/*    */   }
/*    */   
/*    */   public static byte setBit(byte b, int pos) {
/* 10 */     return (byte)(b | 1 << pos);
/*    */   }
/*    */   
/*    */   public static byte unsetBit(byte b, int pos) {
/* 14 */     return (byte)(b & (1 << pos ^ 0xFFFFFFFF));
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\net\lingala\zip4\\util\BitUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */