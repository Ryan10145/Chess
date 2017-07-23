package components.pieces;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Piece
{
    boolean second;
    int col, row;

    BufferedImage image;

    ArrayList<int[]> possibleMoves;
    boolean hasMoved;

    Piece(boolean second, int col, int row)
    {
        this.second = second;
        this.col = col;
        this.row = row;

        possibleMoves = new ArrayList<>();
        hasMoved = false;
    }

    public abstract void calculateMoves(Piece[][] board);

    //Goes through the possible moves and removes the current space if it exists
    void filterPossibleMoves()
    {
        possibleMoves.removeIf(move -> move[0] == col && move[1] == row);
    }

    public ArrayList<int[]> getMoves()
    {
        return possibleMoves;
    }

    //Must call calculateMoves(board) before using this
    public boolean isValidMove(int col, int row)
    {
        for(int[] move : possibleMoves)
        {
            if(move[0] == col && move[1] == row) return true;
        }
        return false;
    }

    public void draw(Graphics2D g2d, int tileLength)
    {
        g2d.drawImage(image, col * tileLength, row * tileLength, tileLength, tileLength, null);
    }

    public void draw(Graphics2D g2d, int tileLength, int x, int y)
    {
        g2d.drawImage(image, x, y, tileLength, tileLength, null);
    }

    public void setPosition(int col, int row)
    {
        this.col = col;
        this.row = row;
    }

    public int getCol()
    {
        return col;
    }

    public int getRow()
    {
        return row;
    }

    boolean canTake(Piece piece)
    {
        return piece == null || piece.second != this.second;
    }

    public void setMoved()
    {
        hasMoved = true;
    }

    public boolean isSecond()
    {
        return second;
    }
}
