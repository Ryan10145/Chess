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
