/*    */ package fr.paladium.ui.utils;
/*    */ 
/*    */ import java.awt.MouseInfo;
/*    */ import java.awt.Point;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.JFrame;
/*    */ 
/*    */ public class WindowMover extends MouseAdapter {
/*    */   private Point click;
/*    */   
/*    */   public WindowMover(JFrame window) {
/* 13 */     this.window = window;
/*    */   }
/*    */   private final JFrame window;
/*    */   
/*    */   public void mouseDragged(MouseEvent e) {
/* 18 */     if (this.click != null && this.click.y < 30) {
/* 19 */       Point draggedPoint = MouseInfo.getPointerInfo().getLocation();
/*    */       
/* 21 */       if (this.window.getExtendedState() == 6) {
/* 22 */         this.window.setExtendedState(0);
/*    */       }
/* 24 */       this.window.setLocation(new Point((int)draggedPoint.getX() - (int)this.click.getX(), (int)draggedPoint.getY() - (int)this.click.getY()));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void mousePressed(MouseEvent e) {
/* 30 */     this.click = e.getPoint();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladiu\\u\\utils\WindowMover.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */