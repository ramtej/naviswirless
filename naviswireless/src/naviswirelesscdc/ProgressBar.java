/*
 * ProgressBar.java
 *
 * Created on February 24, 2008, 10:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package naviswirelesscdc;

import java.awt.*;

public class ProgressBar extends Canvas {

    private int pixelWidth,  pixelHeight,  count, max, mark, thrust;
    private Color bgColor,  fgColor, sdColor;
    boolean shadow;
    
    public ProgressBar(int pixelWidth, int pixelHeight, int max, int mark, int thrust, 
            Color bgColor, Color fgColor, Color sdColor) {
        super();
        this.count = 0;
        this.thrust = thrust;
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        this.max = max;
        this.bgColor = bgColor;
        this.fgColor = fgColor;  
        this.mark = mark;
        this.sdColor = sdColor;        
        this.shadow = true;
    }    
    
    
    public void resetProgress(int count, int thrust) {
        this.count = count;      
        this.thrust = thrust;
        repaint();
    }
    
  

    // Update the count and then update the progress indicator.  If we have
    // updated the progress indicator once for each item, dispose of the
    // progress indicator.
    public void updateProgress(final int increment) {
        
        //new Thread() {    // thread is not imperative
        //    public void run() {
                count += increment;
                
                if (count >= max) 
                    count = max;
                if(count>thrust)
                    thrust = count;
                
                repaint();
        //    }
        //}.start();
    }

    // Paint the progress indicator.
    public void paint(Graphics g) {
                
        int barPixelWidth = pixelWidth*count/max;
        
        // Fill the bar the appropriate percent full.
        g.setColor(fgColor);
        g.fillRect(0, 0, barPixelWidth, pixelHeight);
        
        //shadow
        if(shadow)
        {
            int thrustPixelWidth = pixelWidth*thrust/max;
            g.setColor(sdColor);
            g.fillRect(barPixelWidth, 0, thrustPixelWidth-barPixelWidth, pixelHeight);
        }        
        
        //mark
        if(mark>0)
        {
            int markPixelWidth = pixelWidth*mark/max;
            g.setColor(bgColor);
            g.drawLine(markPixelWidth, 0, markPixelWidth, pixelHeight);
        }        
        
        // border
        g.setColor(bgColor);
        g.drawRect(0, 0, pixelWidth, pixelHeight);        
        
        // Set the color of the text.  If we don't, it appears in the same color
        // as the rectangle making the text effectively invisible.
        g.setColor(Color.black);
    }

    public int getCount() {
        return count;
    }

    public int getPercentComplete() {
        return count / max;
    }

    public int getMark() {
        return mark;
    }

    public int getMax() {
        return max;
    }

    public int getThrust() {
        return thrust;
    }
    
}
