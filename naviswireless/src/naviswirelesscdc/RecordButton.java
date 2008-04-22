/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package naviswirelesscdc;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class RecordButton extends Canvas {
    protected ActionListener actionListener = null;
    boolean clicked;
    boolean down;
    boolean enabled;
    int w=17, h=17;

    public RecordButton() {
        clicked = false;
        down = false;
        enabled = true;
        setSize(17, 17);
        addMouseListener(new ImageButtonMouseListener());
        addMouseMotionListener(new ImageButtonMouseMotionListener());
    }

    public void paint(Graphics g) {
        if (down) {
        	g.setColor(Color.CYAN);
        	//g.drawOval(0, 0, 16, 16);
        	g.drawRoundRect(0, 0, 16, 16, 4, 4);
            g.setColor(Color.CYAN);
            g.fillOval(5,5,7,7);
            g.setColor(Color.BLACK);
        } else {
            if (enabled) {
            	g.setColor(Color.BLUE);
            	//g.drawOval(0, 0, 16, 16);
            	g.drawRoundRect(0, 0, 16, 16, 4, 4);
                g.setColor(Color.RED);
                g.fillOval(5,5,7,7);
                g.setColor(Color.BLACK);
            } else {
            	g.setColor(Color.LIGHT_GRAY);
            	//g.drawOval(0, 0, 16, 16);
            	g.drawRoundRect(0, 0, 16, 16, 4, 4);
                g.setColor(Color.RED);
                g.fillOval(5,5,7,7);
                g.setColor(Color.BLACK);
            }
        }
    }

    public void setEnabled(boolean b) {
        enabled = b;
        repaint();
    }

    public boolean isEnabled() {
        return (enabled);
    }

    public void addActionListener(ActionListener l) {
        actionListener =
                AWTEventMulticaster.add(actionListener, l);
    }

    public void removeActionListener(ActionListener l) {
        actionListener =
                AWTEventMulticaster.remove(actionListener, l);
    }

    public class ImageButtonMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            if ((p.x < w) && (p.y < h) && (p.x > 0) && (p.y > 0) && (enabled == true)) {
                clicked = true;
                down = true;
                repaint();
            }
        }

        public void mouseReleased(MouseEvent e) {
            Point p = e.getPoint();
            if (down) {
                down = false;
                repaint();
            }
            if ((p.x < w) && (p.y < h) && (p.x > 0) && (p.y > 0) && (clicked == true)) {
                ActionEvent ae =
                        new ActionEvent(e.getComponent(), 0, "click");
                if (actionListener != null) {
                    actionListener.actionPerformed(ae);
                }
            }
            clicked = false;
        }
    }

    public class ImageButtonMouseMotionListener extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {
            Point p = e.getPoint();
            if ((p.x < w) && (p.y < h) && (p.x > 0) && (p.y > 0) && (clicked == true)) {
                if (down == false) {
                    down = true;
                    repaint();
                }
            } else {
                if (down == true) {
                    down = false;
                    repaint();
                }
            }
        }
    }

    public Dimension getPreferredSize() {
        return (new Dimension(17, 17));
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

}