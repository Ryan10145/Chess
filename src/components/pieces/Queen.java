package components.pieces;

import utility.Images;

public class Queen extends Piece
{
    public Queen(boolean black, int col, int row)
    {
        super(black, col, row);

        if(black) this.image = Images.BLACK.QUEEN;
        else this.image = Images.WHITE.QUEEN;
    }

    public void calculateMoves(Piece[][] board)
    {
        possibleMoves.clear();

        //Flags for whether to keep checking along the diagonal
        boolean checkUpwardsDiagonal = true;
        boolean checkDownwardsDiagonal = true;

        //Go through a variable that automatically goes from the column to the left side
        for(int check = 1; check <= col; check++)
        {
            //Check if the variable has gone too far, and set flags
            if(row - check < 0) checkUpwardsDiagonal = false;
            if(row + check >= board[0].length) checkDownwardsDiagonal = false;

            if(checkUpwardsDiagonal)
            {
                //If the up-left diagonal is clear, add the move
                //Otherwise, check if it is an enemy, and add it and stop checking diagonals
                //Otherwise, stop checking diagonals and do not add it
                if(board[col - check][row - check] == null) possibleMoves.add(new int[] {col - check, row - check});
                else if(canTake(board[col - check][row - check]))
                {
                    possibleMoves.add(new int[] {col - check, row - check});
                    checkUpwardsDiagonal = false;
                }
                else checkUpwardsDiagonal = false;
            }
            if(checkDownwardsDiagonal)
            {
                //If the down-left diagonal is clear, add the move
                //Otherwise, check if it is an enemy, and add it and stop checking diagonals
                //Otherwise, stop checking diagonals and do not add it
                if(board[col - check][row + check] == null) possibleMoves.add(new int[] {col - check, row + check});
                else if(canTake(board[col - check][row + check]))
                {
                    possibleMoves.add(new int[] {col - check, row + check});
                    checkDownwardsDiagonal = false;
                }
                else checkDownwardsDiagonal = false;
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

            if(checkUpwardsDiagonal)
            {
                //If the up-right diagonal is clear, add the move
                //Otherwise, check if it is an enemy, and add it and stop checking diagonals
                //Otherwise, stop checking diagonals and do not add it
                if(board[col + check][row - check] == null) possibleMoves.add(new int[] {col + check, row - check});
                else if(canTake(board[col + check][row - check]))
                {
                    possibleMoves.add(new int[] {col + check, row - check});
                    checkUpwardsDiagonal = false;
                }
                else checkUpwardsDiagonal = false;
            }
            if(checkDownwardsDiagonal)
            {
                //If the down-right diagonal is clear, add the move
                //Otherwise, check if it is an enemy, and add it and stop checking diagonals
                //Otherwise, stop checking diagonals and do not add it
                if(board[col + check][row + check] == null) possibleMoves.add(new int[] {col + check, row + check});
                else if(canTake(board[col + check][row + check]))
                {
                    possibleMoves.add(new int[] {col + check, row + check});
                    checkDownwardsDiagonal = false;
                }
                else checkDownwardsDiagonal = false;
            }

            if(!(checkUpwardsDiagonal || checkDownwardsDiagonal)) break;
        }

        //Loop through columns from current column to left side
        for(int checkCol = col - 1; checkCol >= 0; checkCol--)
        {
            //Check if the location is clear, if it is add it
            //Otherwise, if the location's piece is opp. side, then add it and stop looping
            //Otherwise, if the location's piece is same side, then stop looping
            if(board[checkCol][row] == null) possibleMoves.add(new int[]{checkCol, row});
            else if(canTake(board[checkCol][row]))
            {
                possibleMoves.add(new int[]{checkCol, row});
                break;
            }
            else break;
        }
        //loop through columns from current column to right side
        for(int checkCol = col + 1; checkCol < board.length; checkCol++)
        {
            //Check if the location is clear, if it is add it
            //Otherwise, if the location's piece is opp. side, then add it and stop looping
            //Otherwise, if the location's piece is same side, then stop looping
            if(board[checkCol][row] == null) possibleMoves.add(new int[]{checkCol, row});
            else if(canTake(board[checkCol][row]))
            {
                possibleMoves.add(new int[]{checkCol, row});
                break;
            }
            else break;
        }

        //Loop through rows from current row to top
        for(int checkRow = row - 1; checkRow >= 0; checkRow--)
        {
            //Check if the location is clear, if it is add it
            //Otherwise, if the location's piece is opp. side, then add it and stop looping
            //Otherwise, if the location's piece is same side, then stop looping
            if(board[col][checkRow] == null) possibleMoves.add(new int[]{col, checkRow});
            else if(canTake(board[col][checkRow]))
            {
                possibleMoves.add(new int[]{col, checkRow});
                break;
            }
            else break;
        }
        //Loop through rows from current row to bottom row
        for(int checkRow = row + 1; checkRow < board.length; checkRow++)
        {
            //Check if the location is clear, if it is add it
            //Otherwise, if the location's piece is opp. side, then add it and stop looping
            //Otherwise, if the location's piece is same side, then stop looping
            if(board[col][checkRow] == null) possibleMoves.add(new int[]{col, checkRow});
            else if(canTake(board[col][checkRow]))
            {
                possibleMoves.add(new int[]{col, checkRow});
                break;
            }
            else break;
        }

        filterPossibleMoves();
    }
}
