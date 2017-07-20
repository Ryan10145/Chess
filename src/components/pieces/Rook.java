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
}
