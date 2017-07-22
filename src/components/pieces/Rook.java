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

        for(int checkCol = col; checkCol >= 0; checkCol--)
        {
            if(board[checkCol][row] == null) possibleMoves.add(new int[]{checkCol, row});
            else if(canTake(board[checkCol][row]))
            {
                possibleMoves.add(new int[]{checkCol, row});
                break;
            }
            else break;
        }
        for(int checkCol = col; checkCol < board.length; checkCol++)
        {
            if(board[checkCol][row] == null) possibleMoves.add(new int[]{checkCol, row});
            else if(canTake(board[checkCol][row]))
            {
                possibleMoves.add(new int[]{checkCol, row});
                break;
            }
            else break;
        }

        for(int checkRow = row; checkRow >= 0; checkRow--)
        {
            if(board[col][checkRow] == null) possibleMoves.add(new int[]{col, checkRow});
            else if(canTake(board[col][checkRow]))
            {
                possibleMoves.add(new int[]{col, checkRow});
                break;
            }
            else break;
        }
        for(int checkRow = row; checkRow < board.length; checkRow++)
        {
            if(board[col][checkRow] == null) possibleMoves.add(new int[]{col, checkRow});
            else if(canTake(board[col][checkRow]))
            {
                possibleMoves.add(new int[]{col, checkRow});
                break;
            }
            else break;
        }
    }
}
