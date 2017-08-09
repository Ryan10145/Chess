package components.pieces;

import utility.Images;

public class Pawn extends Piece
{
    private boolean canEnPassant;

    public Pawn(boolean black, int col, int row)
    {
        super(black, col, row);

        if(black) this.image = Images.BLACK.PAWN;
        else this.image = Images.WHITE.PAWN;
    }

    public void calculateMovesUnfiltered(Piece[][] board)
    {
        possibleMoves.clear();

        int targetRow = second ? row + 1 : row - 1;
        int targetDoubleRow = second ? row + 2 : row - 2;

        //Check to make sure that the piece is not in last row
        if(row + 1 < board[0].length  && row > 0)
        {
            //If the space ahead is clear, then add it
            if(board[col][targetRow] == null)
            {
                possibleMoves.add(new int[]{col, targetRow});

                //If the pawn has not been moved, and the space two spots ahead is clear, add it
                if(!hasMoved)
                {
                    if(board[col][targetDoubleRow] == null) possibleMoves.add(new int[]{col, targetDoubleRow});
                }
            }

            //If the piece is not on the left side
            if(col - 1 >= 0)
            {
                //Check if there is an opposing piece on a diagonal, and add it
                if(board[col - 1][targetRow] != null)
                {
                    if(board[col - 1][targetRow].second != this.second) possibleMoves.add(new int[]{col - 1, targetRow});
                }

                //Check for En Passants
                if(board[col - 1][row] instanceof Pawn)
                {
                    if(board[col - 1][row].second != this.second)
                    {
                        Pawn pawn = (Pawn) board[col - 1][row];
                        if(pawn.canEnPassant) possibleMoves.add(new int[]{col - 1, targetRow});
                    }
                }
            }
            //If the piece is not on the right side
            if(col + 1 < board.length)
            {
                //Check if there is an opposing piece on a diagonal, and add it
                if(board[col + 1][targetRow] != null)
                {
                    if(board[col + 1][targetRow].second != this.second) possibleMoves.add(new int[]{col + 1, targetRow});
                }

                //Check for En Passants
                if(board[col + 1][row] instanceof Pawn)
                {
                    if(board[col + 1][row].second != this.second)
                    {
                        Pawn pawn = (Pawn) board[col + 1][row];
                        if(pawn.canEnPassant) possibleMoves.add(new int[]{col + 1, targetRow});
                    }
                }
            }
        }
    }

    public void setCanEnPassant(boolean canEnPassant)
    {
        this.canEnPassant = canEnPassant;
    }

    public boolean canEnPassant()
    {
        return canEnPassant;
    }

    public String getID()
    {
        return "PA";
    }
}
