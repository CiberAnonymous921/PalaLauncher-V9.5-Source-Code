/*     */ package fr.paladium.ui;
import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;

/*     */ 
/*     */ import fr.paladium.core.distribution.dto.DistributionOS;
import fr.paladium.core.utils.io.OsCheck;
import fr.paladium.core.utils.tray.PaladiumSystemTray;
/*     */ import fr.paladium.ui.utils.IconUtil;
/*     */ import fr.paladium.ui.utils.WindowMover;
/*     */ import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;
/*     */ 
/*     */ public class CefFrame extends JFrame {
/*     */   private CefFrameBuilder builder;
/*     */   private String title;
/*     */   private int width;
/*     */   
/*  19 */   public CefFrameBuilder getBuilder() { return this.builder; } private int height; private boolean undecorated; private boolean closed; public String getTitle() {
/*  20 */     return this.title; }
/*  21 */   public int getWidth() { return this.width; }
/*  22 */   public int getHeight() { return this.height; } public boolean isUndecorated() {
/*  23 */     return this.undecorated;
/*     */   } public boolean isClosed() {
/*  25 */     return this.closed;
/*     */   }
/*     */   public CefFrame(String title, String startURL, int width, int height, boolean undecorated, boolean listen) throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
/*  28 */     this.builder = new CefFrameBuilder(this, startURL, listen);
/*     */     
/*  30 */     this.title = title;
/*  31 */     this.width = width;
/*  32 */     this.height = height;
/*  33 */     this.undecorated = undecorated;
/*     */     
/*  35 */     init();
/*  36 */     registerComponents();
/*  37 */     start();
/*     */   }
/*     */   
/*     */   private void init() {
/*  41 */     fetchIcon();
/*     */     
/*  43 */     setTitle(this.title);
/*  44 */     setSize(this.width, this.height);
/*  45 */     setMinimumSize(getSize());
/*  46 */     setLocationRelativeTo((Component)null);
/*  47 */     setUndecorated(this.undecorated);
/*  48 */     setDefaultCloseOperation(3);
/*  49 */     setBackground(Color.BLACK);
/*     */   }
/*     */   
/*     */   private void fetchIcon() {
/*  53 */     ImageIcon imageIcon = IconUtil.getImage();
/*     */     
/*  55 */     if (imageIcon != null) {
/*  56 */       setIconImage(imageIcon.getImage());
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerComponents() {
/*  61 */     JPanel content = new JPanel()
/*     */       {
/*     */         protected void paintComponent(Graphics g)
/*     */         {
/*  65 */           g.setColor(Color.BLACK);
/*  66 */           g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
/*  67 */           super.paintComponent(g);
/*     */         }
/*     */       };
/*     */     
/*  71 */     content.setBackground(Color.BLACK);
/*  72 */     content.setLayout(new BorderLayout());
/*     */     
/*  74 */     Component browserComponent = this.builder.browser.getUIComponent();
/*  75 */     browserComponent.setBounds(0, 0, this.width, this.height);
/*  76 */     browserComponent.setBackground(Color.BLACK);
/*     */     
/*  78 */     if (this.undecorated) {
/*  79 */       WindowMover mover = new WindowMover(this);
/*  80 */       browserComponent.addMouseListener((MouseListener)mover);
/*  81 */       browserComponent.addMouseMotionListener((MouseMotionListener)mover);
/*     */     } 
/*     */     
/*  84 */     content.add(browserComponent, "Center");
/*  85 */     setContentPane(content);
/*     */     
/*  87 */     if (OsCheck.getOperatingSystemType() == DistributionOS.MACOS) {
/*  88 */       setJMenuBar(PaladiumSystemTray.getMenuBar());
/*     */     }
/*     */   }
/*     */   
/*     */   private void start() {
/*  93 */     setVisible(true);
/*     */     
/*  95 */     setSize(this.width + 1, this.height + 1);
/*  96 */     setSize(this.width, this.height);
/*     */   }
/*     */   
/*     */   public void load(String url) {
/* 100 */     this.builder.browser.loadURL(url);
/*     */   }
/*     */   
/*     */   public void close() {
/* 104 */     if (this.closed) {
/*     */       return;
/*     */     }
/*     */     
/* 108 */     this.closed = true;
/* 109 */     if (isVisible())
/* 110 */       setVisible(false); 
/*     */   }
/*     */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladiu\\ui\CefFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */