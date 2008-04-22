/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package naviswirelesscdc;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class ImageButton extends Canvas {
    protected ActionListener actionListener = null;
    int w,h;
    boolean clicked;
    boolean down;
    boolean enabled;
    Image UPimage;
    Image DOWNimage;
    Image disabledimage;

    public ImageButton(String up_b, String down_b) {
        clicked = false;
        down = false;
        enabled = true;
        InitImage(up_b, down_b);
        setSize(w, h);
        addMouseListener(new ImageButtonMouseListener());
        addMouseMotionListener(new ImageButtonMouseMotionListener());
    }

    public void InitImage(String up, String down) {
        MediaTracker tracker;
        try {
            UPimage = getToolkit().getImage(getClass().getResource(up));
            DOWNimage = getToolkit().getImage(getClass().getResource(down));
            tracker = new MediaTracker(this);
            tracker.addImage(UPimage, 0);
            tracker.addImage(DOWNimage, 1);
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disabledimage = createImage(new FilteredImageSource(UPimage.getSource(), new ImageButtonDisableFilter()));
        w = UPimage.getWidth(this);
        h = UPimage.getHeight(this);
    }

    public void paint(Graphics g) {
        if (down) {
            g.drawImage(DOWNimage, 0, 0, this);
        } else {
            if (enabled) {
                g.drawImage(UPimage, 0, 0, this);
            } else {
                g.drawImage(disabledimage, 0, 0, this);
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
        return (new Dimension(UPimage.getWidth(this),
                UPimage.getHeight(this)));
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    class ImageButtonDisableFilter extends RGBImageFilter {

        public ImageButtonDisableFilter() {
            canFilterIndexColorModel = true;
        }

        public int filterRGB(int x, int y, int rgb) {
            return (rgb & ~0xff000000) | 0x80000000;
        }
    }
}
