/*    */ package fr.paladium.ui.utils;
/*    */ 
/*    */ import fr.paladium.core.utils.net.CustomHttpClient;
/*    */ import java.awt.Image;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.ImageIcon;
/*    */ import org.apache.http.client.methods.CloseableHttpResponse;
/*    */ import org.apache.http.client.methods.HttpGet;
/*    */ import org.apache.http.client.methods.HttpUriRequest;
/*    */ import org.apache.http.impl.client.CloseableHttpClient;
/*    */ 
/*    */ 
/*    */ public class IconUtil
/*    */ {
/*    */   public static ImageIcon getImage() {
/*    */     
/* 18 */     try { HttpGet get = new HttpGet("https://download.paladium-pvp.fr/icons/paladium-games-icon.png");
/*    */       
/* 20 */       CloseableHttpClient client = CustomHttpClient.createClient(); 
/* 21 */       try { CloseableHttpResponse response = client.execute((HttpUriRequest)get);
/* 22 */         if (response.getStatusLine().getStatusCode() == 200)
/* 23 */         { Image image = ImageIO.read(response.getEntity().getContent());
/* 24 */           ImageIcon imageIcon = new ImageIcon(image);
/*    */           
/* 26 */           if (client != null) client.close();  return imageIcon; }  if (client != null) client.close();  } catch (Throwable throwable) { if (client != null)
/* 27 */           try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 28 */     { e.printStackTrace(); }
/*    */     
/* 30 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladiu\\u\\utils\IconUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */