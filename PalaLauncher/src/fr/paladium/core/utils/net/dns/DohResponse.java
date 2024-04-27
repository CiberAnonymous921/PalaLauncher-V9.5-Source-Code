/*    */ package fr.paladium.core.utils.net.dns;

import com.google.gson.annotations.SerializedName;

public class DohResponse {
/*    */   @SerializedName("Status")
/*    */   private final int status;
/*    */   @SerializedName("TC")
/*    */   private final boolean truncatedBit;
/*    */   @SerializedName("RD")
/*    */   private final boolean recursiveDesired;
/*    */   @SerializedName("RA")
/*    */   private final boolean recursionAvailable;
/*    */   
/*    */   public DohResponse(int status, boolean truncatedBit, boolean recursiveDesired, boolean recursionAvailable, boolean authenticatedData, boolean checkingDisabled, DohQuestion[] question, DohAnswer[] answer, String comment) {
/* 12 */     this.status = status; this.truncatedBit = truncatedBit; this.recursiveDesired = recursiveDesired; this.recursionAvailable = recursionAvailable; this.authenticatedData = authenticatedData; this.checkingDisabled = checkingDisabled; this.question = question; this.answer = answer; this.comment = comment;
/*    */   }
/*    */   @SerializedName("AD")
/*    */   private final boolean authenticatedData; @SerializedName("CD")
/*    */   private final boolean checkingDisabled;
/*    */   
/*    */   public int getStatus() {
/* 19 */     return this.status;
/*    */   } @SerializedName("Question")
/*    */   private final DohQuestion[] question; @SerializedName("Answer")
/*    */   private final DohAnswer[] answer; @SerializedName("Comment")
/*    */   private final String comment; public boolean isTruncatedBit() {
/* 24 */     return this.truncatedBit;
/*    */   }
/*    */   
/*    */   public boolean isRecursiveDesired() {
/* 28 */     return this.recursiveDesired;
/*    */   }
/*    */   
/*    */   public boolean isRecursionAvailable() {
/* 32 */     return this.recursionAvailable;
/*    */   }
/*    */   
/*    */   public boolean isAuthenticatedData() {
/* 36 */     return this.authenticatedData;
/*    */   }
/*    */   
/*    */   public boolean isCheckingDisabled() {
/* 40 */     return this.checkingDisabled;
/*    */   }
/*    */   
/*    */   public DohQuestion[] getQuestion() {
/* 44 */     return this.question;
/*    */   }
/*    */   
/*    */   public DohAnswer[] getAnswer() {
/* 48 */     return this.answer;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getComment() {
/* 53 */     return this.comment;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\net\dns\DohResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */