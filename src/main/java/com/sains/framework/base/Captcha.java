package com.sains.framework.base;

import com.opensymphony.xwork2.ActionContext;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.awt.font.TextAttribute;
 
public class Captcha extends HttpServlet {

  private int height=30;
  private int width=120;

  public void init() {}
 
   public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
    //Expire response
//    response.setHeader("Cache-Control", "no-cache"); response.setDateHeader("Expires", 0);
//    response.setHeader("Pragma", "no-cache"); response.setDateHeader("Max-Age", 0);
    
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
    Graphics2D graphics2D = image.createGraphics();
    
    
    Random r = new Random();
    String token = Long.toString(Math.abs(r.nextLong()), 36);
    String ch = token.substring(0, 6);
    
    //GradientPaint gp = new GradientPaint(30, 30, Color.white, 15, 25, Color.black, true);
    //graphics2D.setBackground(Color.WHITE);
    //graphics2D.clearRect(0, 0, width, height);
    // Hex to color
    graphics2D.setColor(new Color(198, 198, 198));
    graphics2D.fillRect(0,0,width,height);
    graphics2D.setPaint(Color.BLACK);
    Font font=new Font("Verdana", Font.CENTER_BASELINE, 26);
    graphics2D.setFont(font);
    graphics2D.drawString(ch, 2, 24);
    graphics2D.dispose();
    
    Map sessionMap = ActionContext.getContext().getSession();
    sessionMap.put("sSysCaptcha", ch);
    OutputStream outputStream = response.getOutputStream();
    ImageIO.write(image, "png", outputStream);
    Debug.printFrameworkDebug("Captcha.java ::"+ch);
    outputStream.close();
  }
}