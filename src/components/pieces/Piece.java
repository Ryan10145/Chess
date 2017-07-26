package components.pieces;

import components.Board;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

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

    public abstract void calculateMovesUnfiltered(Piece[][] board);

    public void calculateMoves(Piece[][] board)
    {
        calculateMovesUnfiltered(board);
        possibleMoves.removeIf(move -> move[0] == col && move[1] == row);
        filterPossibleMoves(board);
    }

    //Goes through the possible moves and removes any moves that bring the current player under check
    void filterPossibleMoves(Piece[][] board)
    {
        int currentCol = col;
        int currentRow = row;

        Iterator<int[]> iterator = possibleMoves.iterator();
        while(iterator.hasNext())
        {
            int[] location = iterator.next();

            Piece priorPiece = board[location[0]][location[1]];
            Piece currentPiece = board[currentCol][currentRow];

            board[location[0]][location[1]] = this;
            board[currentCol][currentRow] = null;

            col = location[0];
            row = location[1];

            if(Board.isCheck(board, second))
            {
                iterator.remove();
            }

            board[currentCol][currentRow] = currentPiece;
            board[location[0]][location[1]] = priorPiece;

            col = currentCol;
            row = currentRow;
        }
    }

    public ArrayList<int[]> getMoves()
    {
        possibleMoves.removeIf(move -> move[0] == col && move[1] == row);
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
