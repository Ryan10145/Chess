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
}
