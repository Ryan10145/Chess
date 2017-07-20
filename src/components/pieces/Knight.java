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
}
