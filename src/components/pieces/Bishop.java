package components.pieces;

import utility.Images;

public class Bishop extends Piece
{
    public Bishop(boolean black, int col, int row)
    {
        super(black, col, row);

        if(black) this.image = Images.BLACK.BISHOP;
        else this.image = Images.WHITE.BISHOP;
    }

    public void calculateMovesUnfiltered(Piece[][] board)
    {
        possibleMoves.clear();

        //Flags for whether to keep checking along the diagonalboard
        boolean checkUpwardsDiagonal = true;
        boolean checkDownwardsDiagonal = true;

        //Go through a variable that automatically goes from the column to the left side
        for(int check = 1; check <= col; check++)
        {
            //Check if the variable has gone too far, and set flags
            if(row - check < 0) checkUpwardsDiagonal = false;
            if(row + check >= board[0].length) checkDownwardsDiagonal = false;

            if(col - check >= 0)
            {
                if(checkUpwardsDiagonal)
                {
                    //If the up-left diagonal is clear, add the move
                    //Otherwise, check if it is an enemy, and add it and stop checking diagonals
                    //Otherwise, stop checking diagonals and do not add it
                    if(board[col - check][row - check] == null) possibleMoves.add(new int[]{col - check, row - check});
                    else if(canTake(board[col - check][row - check]))
                    {
                        possibleMoves.add(new int[]{col - check, row - check});
                        checkUpwardsDiagonal = false;
                    } else checkUpwardsDiagonal = false;
                }
                if(checkDownwardsDiagonal)
                {
                    //If the down-left diagonal is clear, add the move
                    //Otherwise, check if it is an enemy, and add it and stop checking diagonals
                    //Otherwise, stop checking diagonals and do not add it
                    if(board[col - check][row + check] == null) possibleMoves.add(new int[]{col - check, row + check});
                    else if(canTake(board[col - check][row + check]))
                    {
                        possibleMoves.add(new int[]{col - check, row + check});
                        checkDownwardsDiagonal = false;
                    } else checkDownwardsDiagonal = false;
                }
            }

            if(!(checkUpwardsDiagonal || checkDownwardsDiagonal)) break;
        }

        //Reset the flags
        checkUpwardsDiagonal = true;
        checkDownwardsDiagonal = true;

        //Go through a variable that leads from column to right side
        for(int check = 1; check < board.length - col; check++)
        {
            //Check going too far and set flags
            if(row - check < 0) checkUpwardsDiagonal = false;
            if(row + check >= board[0].length) checkDownwardsDiagonal = false;

            if(col + check < board.length)
            {
                if(checkUpwardsDiagonal)
                {
                    //If the up-right diagonal is clear, add the move
                    //Otherwise, check if it is an enemy, and add it and stop checking diagonals
                    //Otherwise, stop checking diagonals and do not add it
                    if(board[col + check][row - check] == null) possibleMoves.add(new int[]{col + check, row - check});
                    else if(canTake(board[col + check][row - check]))
                    {
                        possibleMoves.add(new int[]{col + check, row - check});
                        checkUpwardsDiagonal = false;
                    } else checkUpwardsDiagonal = false;
                }
                if(checkDownwardsDiagonal)
                {
                    //If the down-right diagonal is clear, add the move
                    //Otherwise, check if it is an enemy, and add it and stop checking diagonals
                    //Otherwise, stop checking diagonals and do not add it
                    if(board[col + check][row + check] == null) possibleMoves.add(new int[]{col + check, row + check});
                    else if(canTake(board[col + check][row + check]))
                    {
                        possibleMoves.add(new int[]{col + check, row + check});
                        checkDownwardsDiagonal = false;
                    } else checkDownwardsDiagonal = false;
                }
            }

            if(!(checkUpwardsDiagonal || checkDownwardsDiagonal)) break;
        }
    }
}
