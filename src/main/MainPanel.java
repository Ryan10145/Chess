package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener
{
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    private Thread updateThread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g2d;

    private static final int FPS = 60;

    public MainPanel()
    {
        super();
        this.setFocusable(true);
        this.requestFocus();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT ));
    }

    public void addNotify()
    {
        super.addNotify();

        if(updateThread == null)
        {
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            updateThread = new Thread(this);
            updateThread.start();
        }
    }

    public void run()
    {
        init();
        long start;
        long elapsed;
        long wait;
        while(running)
        {
            start = System.nanoTime();
            update();
            draw();
            drawToScreen();

            long targetTime = 1000 / FPS;
            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;
            if(wait < 0)
            {
//				System.out.println("We are lagging by " + -wait + " milliseconds on frame " + frameCount + ".");
                wait = 5;
            }
            try
            {

                Thread.sleep(wait);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void init()
    {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        running = true;
    }

    public void update()
    {

    }

    public void draw()
    {

    }

    public void drawToScreen()
    {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        g.dispose();
    }

    public void mouseClicked(MouseEvent e)
    {

    }

    public void mousePressed(MouseEvent e)
    {

    }

    public void mouseReleased(MouseEvent e)
    {

    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    public void mouseDragged(MouseEvent e)
    {

    }

    public void mouseMoved(MouseEvent e)
    {

    }
}
