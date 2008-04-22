/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package naviswirelesscdc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author dr. xinyu liu
 */
public class NonProgressBar extends Canvas {
    
    private int pixelWidth, pixelHeight, blockWidth, position;
    private Color bgColor, fgColor;
    private boolean working;

    public NonProgressBar(int pixelWidth, int pixelHeight, int blockWidth, Color bgColor, Color fgColor){
        super();
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        this.blockWidth = blockWidth;
        this.bgColor = bgColor;
        this.fgColor = fgColor;  
    }
    
    public void begin()
    {
        this.setVisible(true);
        
        new Thread() {
            public void run() {        
                working = true;
                position = 1;   // reset

                while(working)
                {
                    if((position++)>(pixelWidth-blockWidth))
                    {
                        position=1; // reset
                    }            
                    repaint();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        // ignore
                    }                
                }
            }
        }.start();       
    }
    
    public void stop()
    {
        working = false;
        this.setVisible(false);
    }
    
    // Paint the progress indicator.
    public void paint(final Graphics g) {

        // border
        g.setColor(bgColor);
        g.drawRect(0, 0, pixelWidth, pixelHeight);
        
        g.setColor(fgColor);
        g.fillRect(position, 1, blockWidth, pixelHeight-1);
                
        // Set the color of the text.  If we don't, it appears in the same color
        // as the rectangle making the text effectively invisible.
        g.setColor(Color.black);
    }    
}
