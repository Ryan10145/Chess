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
}
