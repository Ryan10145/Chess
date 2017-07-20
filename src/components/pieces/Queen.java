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
}
