package components.pieces;

import utility.Images;

public class Pawn extends Piece
{
    public Pawn(boolean black, int col, int row)
    {
        super(black, col, row);

        if(black) this.image = Images.BLACK.PAWN;
        else this.image = Images.WHITE.PAWN;
    }

    public void calculateMoves(Piece[][] board)
    {
        //TODO Promotion
        possibleMoves.clear();

        //If the piece is black, then move it downwards instead of upwards
        if(black)
        {
            //Check to make sure that the piece is not in last row
            if(row < board[0].length - 1)
            {
                //If the space ahead is clear, then add it
                if(board[col][row + 1] == null)
                {
                    possibleMoves.add(new int[]{col, row + 1});

                    //If the pawn has not been moved, and the space two spots ahead is clear, add it
                    if(!hasMoved)
                    {
                        if(row < board.length - 2)
                        {
                            if(board[col][row + 2] == null) possibleMoves.add(new int[]{col, row + 2});
                        }
                    }
                }

                //If the piece is not on the left side
                if(col > 0)
                {
                    //Check if there is an opposing piece on a diagonal, and add it
                    if(board[col - 1][row + 1] != null)
                    {
                        if(board[col - 1][row + 1].black != this.black) possibleMoves.add(new int[]{col - 1, row + 1});
                    }
                }
                //If the piece is not on the right side
                if(col < board.length - 1)
                {
                    //Check if there is an opposing piece on a diagonal, and add it
                    if(board[col + 1][row + 1] != null)
                    {
                        if(board[col + 1][row + 1].black != this.black) possibleMoves.add(new int[]{col + 1, row + 1});
                    }
                }
            }
        }
        //If the piece is white
        else
        {
            //Check to make sure that the piece is not in last row
            if(row > 0)
            {
                //If the space ahead is clear, then add it
                if(board[col][row - 1] == null)
                {
                    possibleMoves.add(new int[]{col, row - 1});

                    //If the pawn has not been moved, and the space two spots ahead is clear, add it
                    if(!hasMoved)
                    {
                        if(row >= 2)
                        {
                            if(board[col][row - 2] == null) possibleMoves.add(new int[]{col, row - 2});
                        }
                    }
                }

                //If the piece is not on the left side
                if(col > 0)
                {
                    //Check if there is an opposing piece on a diagonal, and add it
                    if(board[col - 1][row - 1] != null)
                    {
                        if(board[col - 1][row - 1].black != this.black) possibleMoves.add(new int[]{col - 1, row - 1});
                    }
                }
                //If the piece is not on the right side
                if(col < board.length - 1)
                {
                    //Check if there is an opposing piece on a diagonal, and add it
                    if(board[col + 1][row - 1] != null)
                    {
                        if(board[col + 1][row - 1].black != this.black) possibleMoves.add(new int[]{col + 1, row - 1});
                    }
                }
            }
        }
    }
}
