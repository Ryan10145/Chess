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

    public void calculateMoves(Piece[][] board)
    {
        possibleMoves.clear();

        boolean checkUpwardsDiagonal = true;
        boolean checkDownwardsDiagonal = true;
        for(int check = 0; check <= col; check++)
        {
            if(row - check < 0) checkUpwardsDiagonal = false;
            if(row + check >= board[0].length) checkDownwardsDiagonal = false;

            if(checkUpwardsDiagonal)
            {
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

        checkUpwardsDiagonal = true;
        checkDownwardsDiagonal = true;
        for(int check = 0; check < board.length - col; check++)
        {
            if(row - check < 0) checkUpwardsDiagonal = false;
            if(row + check >= board[0].length) checkDownwardsDiagonal = false;

            if(checkUpwardsDiagonal)
            {
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
    }
}
