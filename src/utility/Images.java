package utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Images
{
    private static BufferedImage loadSingleImage(String string)
    {
        try
        {
            Image image = ImageIO.read(Images.class.getResourceAsStream(string));
            return (BufferedImage) image;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static class WHITE
    {
        public static final BufferedImage PAWN = loadSingleImage("/sprites/white/pawn.png");
        public static final BufferedImage QUEEN = loadSingleImage("/sprites/white/queen.png");
        public static final BufferedImage KING = loadSingleImage("/sprites/white/king.png");
        public static final BufferedImage BISHOP = loadSingleImage("/sprites/white/bishop.png");
        public static final BufferedImage KNIGHT = loadSingleImage("/sprites/white/knight.png");
        public static final BufferedImage ROOK = loadSingleImage("/sprites/white/rook.png");
    }

    public static class BLACK
    {
        public static final BufferedImage PAWN = loadSingleImage("/sprites/black/pawn.png");
        public static final BufferedImage QUEEN = loadSingleImage("/sprites/black/queen.png");
        public static final BufferedImage KING = loadSingleImage("/sprites/black/king.png");
        public static final BufferedImage BISHOP = loadSingleImage("/sprites/black/bishop.png");
        public static final BufferedImage KNIGHT = loadSingleImage("/sprites/black/knight.png");
        public static final BufferedImage ROOK = loadSingleImage("/sprites/black/rook.png");
    }
}
