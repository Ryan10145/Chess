package components.pieces;

import utility.Images;

public class Rook extends Piece
{
    public Rook(boolean black, int col, int row)
    {
        super(black, col, row);

        if(black) this.image = Images.BLACK.ROOK;
        else this.image = Images.WHITE.ROOK;
    }

    public void calculateMoves(Piece[][] board)
    {
        possibleMoves.clear();

        //Loop through columns from current column to left side
        for(int checkCol = col; checkCol >= 0; checkCol--)
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
        for(int checkCol = col; checkCol < board.length; checkCol++)
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
        for(int checkRow = row; checkRow >= 0; checkRow--)
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
        for(int checkRow = row; checkRow < board.length; checkRow++)
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
