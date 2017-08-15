package components.pieces;

import components.Board;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Piece
{
    boolean second;
    int col, row;

    BufferedImage image;

    //Copy on write array to prevent ConcurrentModificationExceptions
    CopyOnWriteArrayList<int[]> possibleMoves;
    boolean hasMoved;

    Piece(boolean second, int col, int row)
    {
        this.second = second;
        this.col = col;
        this.row = row;

        possibleMoves = new CopyOnWriteArrayList<>();
        hasMoved = false;
    }

    //Returns the id of the piece
    public abstract String getID();

    //Returns all moves without checking for checks
    public abstract void calculateMovesUnfiltered(Piece[][] board);

    //Calculate all moves and filters them
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

        //ArrayList that contains all locations to remove
        ArrayList<int[]> remove = new ArrayList<>();

        //Filter moves by doing the move, checking if it leaves the king exposed, and then reverting the move
        for(int[] location : possibleMoves)
        {
            //Grab all values from before the temporary move
            Piece priorPiece = board[location[0]][location[1]];
            Piece currentPiece = board[currentCol][currentRow];

            //Do the temporary move
            board[location[0]][location[1]] = this;
            board[currentCol][currentRow] = null;
            setPosition(location[0], location[1]);

            //If the move leaves the king in check, add the move to the remove
            if(Board.isCheck(board, second)) remove.add(location);

            //Undo the temporary move
            board[currentCol][currentRow] = currentPiece;
            board[location[0]][location[1]] = priorPiece;
            setPosition(currentCol, currentRow);
        }

        //Remove everything that was added to the remove
        possibleMoves.removeAll(remove);
    }

    //Returns the current moves, after filtering one small move
    public CopyOnWriteArrayList<int[]> getMoves()
    {
        possibleMoves.removeIf(move -> move[0] == col && move[1] == row);
        return possibleMoves;
    }

    //Returns whether or not a move is known to be a valid move (NOTE: Must call calculateMoves() before using this)
    public boolean isValidMove(int col, int row)
    {
        for(int[] move : possibleMoves)
        {
            if(move[0] == col && move[1] == row) return true;
        }
        return false;
    }

    //Draws the piece at its current col and row
    public void draw(Graphics2D g2d, int tileLength)
    {
        g2d.drawImage(image, col * tileLength, row * tileLength, tileLength, tileLength, null);
    }

    //Draws the piece at a given x/y
    public void draw(Graphics2D g2d, int tileLength, int x, int y)
    {
        g2d.drawImage(image, x, y, tileLength, tileLength, null);
    }

    //Sets the col/row
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

    //Returns whether or not the current piece can attack another piece
    //(NOTE: for null, it will return true, so that it can "attack"/move to empty squares)
    boolean canTake(Piece piece)
    {
        return piece == null || piece.second != this.second;
    }

    public void setMoved(boolean moved)
    {
        hasMoved = moved;
    }

    public boolean getMoved()
    {
        return hasMoved;
    }

    public boolean isSecond()
    {
        return second;
    }

    //Converts an id and other data into a piece
    //(NOTE: a non-piece id will return null, which is used to ensure that pieces are not created)
    public static Piece parseID(String id, boolean secondTurn, int col, int row)
    {
        switch(id)
        {
            case "BI":
                return new Bishop(secondTurn, col, row);
            case "KI":
                return new King(secondTurn, col, row);
            case "KN":
                return new Knight(secondTurn, col, row);
            case "PA":
                return new Pawn(secondTurn, col, row);
            case "QU":
                return new Queen(secondTurn, col, row);
            case "RO":
                return new Rook(secondTurn, col, row);
            default:
                return null;
        }
    }
}
