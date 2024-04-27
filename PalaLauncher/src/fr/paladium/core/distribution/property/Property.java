/*    */ package fr.paladium.core.distribution.property;
/*    */ 
/*    */ public abstract class Property<T>
/*    */ {
/*    */   private T defaultValue;
/*    */   
/*    */   public Property(T defaultValue) {
/*  8 */     this.defaultValue = defaultValue;
/*    */   }
/*    */   
/*    */   public abstract T getValue(Object paramObject);
/*    */   
/*    */   public T getDefaultValue() {
/* 14 */     return this.defaultValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\core\distribution\property\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */