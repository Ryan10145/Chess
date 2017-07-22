package components.pieces;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Piece
{
    //How to structure the pieces
    //Each piece will have a method that takes in an array of pieces, and it uses it to return a list of locations it can move to
    //Then, the release checker will simply check if the moving position is one of the possible values, and if it is,
    //then it will move. Otherwise it will go back to its original positon

    boolean black;
    int col, row;

    BufferedImage image;

    ArrayList<int[]> possibleMoves;
    boolean hasMoved;

    Piece(boolean black, int col, int row)
    {
        this.black = black;
        this.col = col;
        this.row = row;

        possibleMoves = new ArrayList<>();
        hasMoved = false;
    }

    public abstract void calculateMoves(Piece[][] board);

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
        return piece == null || piece.black != this.black;
    }

    public void setMoved()
    {
        hasMoved = true;
    }
}
