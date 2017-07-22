package components.pieces;

import utility.Images;

public class Knight extends Piece
{
    public Knight(boolean black, int col, int row)
    {
        super(black, col, row);

        if(black) this.image = Images.BLACK.KNIGHT;
        else this.image = Images.WHITE.KNIGHT;
    }

    public void calculateMoves(Piece[][] board)
    {
        possibleMoves.clear();
        if(col >= 2)
        {
            if(row > 0)
            {
                if(canTake(board[col - 2][row - 1])) possibleMoves.add(new int[] {col - 2, row - 1});
            }
            if(row < board[0].length - 1)
            {
                if(canTake(board[col - 2][row + 1])) possibleMoves.add(new int[] {col - 2, row + 1});
            }
        }
        if(row >= 2)
        {
            if(col > 0)
            {
                if(canTake(board[col - 1][row - 2])) possibleMoves.add(new int[] {col - 1, row - 2});
            }
            if(col < board.length - 1)
            {
                if(canTake(board[col + 1][row - 2])) possibleMoves.add(new int[] {col + 1, row - 2});
            }
        }
        if(col < board.length - 2)
        {
            if(row > 0)
            {
                if(canTake(board[col + 2][row - 1])) possibleMoves.add(new int[] {col + 2, row - 1});
            }
            if(row < board[0].length - 1)
            {
                if(canTake(board[col + 2][row + 1])) possibleMoves.add(new int[] {col + 2, row + 1});
            }
        }
        if(row < board[0].length - 2)
        {
            if(col > 0)
            {
                if(canTake(board[col - 1][row + 2])) possibleMoves.add(new int[] {col - 1, row + 2});
            }
            if(col < board.length - 1)
            {
                if(canTake(board[col + 1][row + 2])) possibleMoves.add(new int[] {col + 1, row + 2});
            }
        }
    }
}
