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

        if(black)
        {
            if(row < board[0].length - 1)
            {
                if(board[col][row + 1] == null)
                {
                    possibleMoves.add(new int[]{col, row + 1});

                    if(!hasMoved)
                    {
                        if(row < board.length - 2)
                        {
                            if(board[col][row + 2] == null) possibleMoves.add(new int[]{col, row + 2});
                        }
                    }
                }
            }

            if(row < board[0].length - 1)
            {
                if(col > 0)
                {
                    if(board[col - 1][row + 1] != null)
                    {
                        if(board[col - 1][row + 1].black != this.black) possibleMoves.add(new int[]{col - 1, row + 1});
                    }
                }
                if(col < board.length - 1)
                {
                    if(board[col + 1][row + 1] != null)
                    {
                        if(board[col + 1][row + 1].black != this.black) possibleMoves.add(new int[]{col + 1, row + 1});
                    }
                }
            }
        }
        else
        {
            if(row > 0)
            {
                if(board[col][row - 1] == null)
                {
                    possibleMoves.add(new int[]{col, row - 1});

                    if(!hasMoved)
                    {
                        if(row >= 2)
                        {
                            if(board[col][row - 2] == null) possibleMoves.add(new int[]{col, row - 2});
                        }
                    }
                }
            }

            if(row > 0)
            {
                if(col > 0)
                {
                    if(board[col - 1][row - 1] != null)
                    {
                        if(board[col - 1][row - 1].black != this.black) possibleMoves.add(new int[]{col - 1, row - 1});
                    }
                }
                if(col < board.length - 1)
                {
                    if(board[col + 1][row - 1] != null)
                    {
                        if(board[col + 1][row - 1].black != this.black) possibleMoves.add(new int[]{col + 1, row - 1});
                    }
                }
            }
        }
    }
}
