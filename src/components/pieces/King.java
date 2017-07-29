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

    public void calculateMovesUnfiltered(Piece[][] board)
    {
        possibleMoves.clear();

        //Add all locations on the left side
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
        //Add all locations on the right side
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
        //Add the top location
        if(row > 0)
        {
            if(canTake(board[col][row - 1])) possibleMoves.add(new int[] {col, row - 1});
        }
        //Add the bottom location
        if(row < board[0].length - 1)
        {
            if(canTake(board[col][row + 1])) possibleMoves.add(new int[] {col, row + 1});
        }

        //Search current row for castling, if the king has not been moved
        if(!hasMoved)
        {
            for(int col = 0; col < board.length; col++)
            {
                if(board[col][this.row] instanceof Rook && !board[col][this.row].hasMoved)
                {
                    //Check one, make sure that the path to the rook is clear
                    //Check two, make sure that the king, anywhere along that line, is not in check
                    boolean canCastle = true;

                    for(int checkCol = this.col + (int) Math.signum(col - this.col);
                        checkCol != col;
                        checkCol += Math.signum(col - this.col))
                    {
                        if(board[checkCol][this.row] != null)
                        {
                            canCastle = false;
                            break;
                        }
                    }

                    if(canCastle) possibleMoves.add(new int[] {this.col + (int) (2 * Math.signum(col - this.col)), this.row});
                }
            }
        }
    }

    void filterPossibleMoves(Piece[][] board)
    {
        super.filterPossibleMoves(board);
        for(Piece[] pieceA : board)
        {
            for(Piece piece : pieceA)
            {
                //TODO Optimize this by checking only if the piece is within striking range
                // w/ casework that checks the type of the piece

                if(piece != null)
                {
                    if(piece.second != this.second)
                    {
                        if(piece instanceof Pawn)
                        {
                            possibleMoves.removeIf(possibleMove -> Math.abs(possibleMove[0] - piece.col) == 1 &&
                                    ((piece.second && possibleMove[1] == piece.row + 1) ||
                                            (!piece.second && possibleMove[1] == piece.row - 1)));
                        }
                        else if(piece instanceof King)
                        {
                            possibleMoves.removeIf(possibleMoves -> Math.abs(possibleMoves[0] - piece.col) <= 1 &&
                                    Math.abs(possibleMoves[1] - piece.row) <= 1);
                        }
                        else
                        {
                            piece.calculateMoves(board);
                            for(int[] location : piece.possibleMoves)
                            {
                                possibleMoves.removeIf(possibleMove -> possibleMove[0] == location[0] &&
                                        possibleMove[1] == location[1]);
                            }
                        }
                    }
                }
            }
        }

        //Remove a castling option if the King cannot move to the space between it and the castling spot
        for(int[] location : possibleMoves)
        {
            if(Math.abs(location[0] - this.col) == 2)
            {
                boolean found = false;
                for(int[] check : possibleMoves)
                {
                    if(check[0] == (location[0] + this.col) / 2 && check[1] == this.row)
                    {
                        found = true;
                        break;
                    }
                }

                if(!found) possibleMoves.remove(location);
            }
        }
    }
}
