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
}
