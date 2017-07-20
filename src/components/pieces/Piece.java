package components.pieces;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Piece
{
    boolean black;
    int col, row;
    BufferedImage image;

    Piece(boolean black, int col, int row)
    {
        this.black = black;
        this.col = col;
        this.row = row;
    }

    public void draw(Graphics2D g2d, int tileLength)
    {
        g2d.drawImage(image, col * tileLength, row * tileLength, tileLength, tileLength, null);
    }
}
