/*
Copyright 2009-2016 Igor Polevoy
Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 
http://www.apache.org/licenses/LICENSE-2.0 
Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/
package com.sains.common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * This is a simple captcha class, use it to generate a random string and then to create an image of it.
 *
 * @author Igor Polevoy
 */
public class Captcha {

    private Captcha(){}

    /**
     * Generates a random alpha-numeric string of eight characters.
     *
     * @return random alpha-numeric string of eight characters.
     */
    public static String generateText() {
        return new StringTokenizer(UUID.randomUUID().toString(), "-").nextToken();
    }

    /**
     * Generates a PNG image of text 180 pixels wide, 40 pixels high with white background.
     *
     * @param text expects string size eight (8) characters.
     * @return byte array that is a PNG image generated with text displayed.
     */
    public static byte[] generateImage(String text) {
        
        int w = 20;
        w += (20 * text.length());
        int h = 40;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        String[] fontTypeArr = new String[]{Font.SERIF, Font.SANS_SERIF};
        g.setColor(Color.blue);
        int start = 10;
        byte[] bytes = text.getBytes();
        Color transparent = new Color(0, 0, 0, 0.0f); //Red 
        Random random = new Random();
        double angle = 0.3;
        for (int i = 0; i < bytes.length; i++) {
            String name= fontTypeArr[random.nextInt(fontTypeArr.length)];
//            if (name.equals(Font.MONOSPACED)) {
//                g.setFont(new Font(name, Font.BOLD, 26));
//            } else {
                g.setFont(new Font(name, Font.PLAIN, 26));
//            }
                    
//            g.setColor( new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.setColor(Color.darkGray);
            angle = angle*-1;
            int startX = start + (i * 20);
            int startY = (int) (Math.random() * 20 + 20);
            
            g.rotate(angle, startX, 20);
            g.drawString(new String(new byte[]{bytes[i]}), startX, startY);
            g.rotate(angle*-1, startX, 20);
            
//            g.setPaint(transparent);
            g.drawLine(startX-5, startY-10, startX+20, (angle<0?startY-7:startY-13));
            
//            g.setColor(Color.WHITE);
//            g.drawLine(startX-6, startY-5, startX+20, startY);
//            g.drawLine(startX-7, startY-15, startX+20, startY-20);
        }
//        g.setColor(Color.red);
//        g.drawOval((int) ((w/2-2)+random.nextInt(5)), (int) ((h/2-2)+random.nextInt(5)), w-10, 25);
//        g.drawOval((int) ((w/3*2-2)+random.nextInt(5)), (int) ((h/2-2)+random.nextInt(5)), w/2, 25);
//        g.drawOval((int) ((w/3-2)+random.nextInt(5)), (int) ((h/2-2)+random.nextInt(5)), w/2, 25);
                
//        g.drawLine(1, 1, w-1, h-1);
//        g.drawLine(w-1, h-1, 1, 1);
//        g.drawLine(1, 20, w-1, 21);
//        for (int i = 0; i < 2; i++) {
//            g.drawLine(1, (int) (Math.random() * h), w-1, (int) (Math.random() * h));
//        }
        g.dispose();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", bout);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bout.toByteArray();
    }
    
    public static String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getEncoder().encodeToString(bytes);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }
    public static String encodeBytesToBase64Binary(byte[] bytes){
        String encodedfile = null;
        try {
            encodedfile = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }
}
