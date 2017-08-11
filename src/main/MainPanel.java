package main;

import components.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener, KeyListener
{
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    private Thread updateThread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g2d;

    private Board board;

    private static final int FPS = 60;

    MainPanel()
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
            this.addKeyListener(this);
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

    private void init()
    {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        running = true;

        board = new Board(60, 60);
    }

    private void update()
    {
        board.update();
    }

    private void draw()
    {
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        board.draw(g2d);
    }

    private void drawToScreen()
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
        board.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        board.mouseReleased(e);
    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    public void mouseDragged(MouseEvent e)
    {
        board.mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e)
    {
        board.mouseMoved(e);
    }

    public void keyTyped(KeyEvent e)
    {

    }

    public void keyPressed(KeyEvent e)
    {
        board.keyPressed(e);
    }

    public void keyReleased(KeyEvent e)
    {

    }
}
