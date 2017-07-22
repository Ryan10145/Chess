package components.pieces;

import utility.Images;

public class King extends Piece
{
    public King(boolean black, int col, int row)
    {
        super(black, col, row);

        if(black) this.image = Images.BLACK.KING;
        else this.image = Images.WHITE.KING;
    }

    public void calculateMoves(Piece[][] board)
    {
        //TODO Castling
        //TODO Limiting Moves Based On Checks
        possibleMoves.clear();
        if(col > 0)
        {
            if(canTake(board[col - 1][row])) possibleMoves.add(new int[] {col - 1, row});
            if(row > 0)
            {
                if(canTake(board[col - 1][row - 1])) possibleMoves.add(new int[] {col - 1, row - 1});
            }
            if(row < board[0].length - 1)
            {
                if(canTake(board[col - 1][row + 1])) possibleMoves.add(new int[] {col - 1, row + 1});
            }
        }
        if(col < board.length - 1)
        {
            if(canTake(board[col + 1][row])) possibleMoves.add(new int[] {col + 1, row});
            if(row > 0)
            {
                if(canTake(board[col + 1][row - 1])) possibleMoves.add(new int[] {col + 1, row - 1});
            }
            if(row < board[0].length - 1)
            {
                if(canTake(board[col + 1][row + 1])) possibleMoves.add(new int[] {col + 1, row + 1});
            }
        }
        if(row > 0)
        {
            if(canTake(board[col][row - 1])) possibleMoves.add(new int[]{col, row - 1});
        }
        if(row < board[0].length - 1)
        {
            if(canTake(board[col][row + 1])) possibleMoves.add(new int[]{col, row + 1});
        }
    }
}
