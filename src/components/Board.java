package components;

import components.pieces.*;
import utility.Images;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class Board
{
    private Piece[][] pieces;
    private Piece currentPiece;
    private int hoverCol;
    private int hoverRow;

    private final int TILE_LENGTH = 60;
    private int x, y;
    private int currentPieceX, currentPieceY;
    private boolean selectFlag;
    private boolean secondTurn;

    private boolean promoting;
    private int promotingCol;
    private int promotingRow;
    private int promotingHover;

    public Board(int x, int y)
    {
        this.x = x;
        this.y = y;

        pieces = new Piece[8][8];
        setUpPieces();
    }

    private void setUpPieces()
    {
        pieces[0][0] = new Rook(true, 0, 0);
        pieces[1][0] = new Knight(true, 1, 0);
        pieces[2][0] = new Bishop(true, 2, 0);
        pieces[3][0] = new Queen(true, 3, 0);
        pieces[4][0] = new King(true, 4, 0);
        pieces[5][0] = new Bishop(true, 5, 0);
        pieces[6][0] = new Knight(true, 6, 0);
        pieces[7][0] = new Rook(true, 7, 0);

        pieces[0][1] = new Pawn(true, 0, 1);
        pieces[1][1] = new Pawn(true, 1, 1);
        pieces[2][1] = new Pawn(true, 2, 1);
        pieces[3][1] = new Pawn(true, 3, 1);
        pieces[4][1] = new Pawn(true, 4, 1);
        pieces[5][1] = new Pawn(true, 5, 1);
        pieces[6][1] = new Pawn(true, 6, 1);
        pieces[7][1] = new Pawn(true, 7, 1);

        //White Side
        pieces[0][7] = new Rook(false, 0, 7);
        pieces[1][7] = new Knight(false, 1, 7);
        pieces[2][7] = new Bishop(false, 2, 7);
        pieces[3][7] = new Queen(false, 3, 7);
        pieces[4][7] = new King(false, 4, 7);
        pieces[5][7] = new Bishop(false, 5, 7);
        pieces[6][7] = new Knight(false, 6, 7);
        pieces[7][7] = new Rook(false, 7, 7);

        pieces[0][6] = new Pawn(false, 0, 6);
        pieces[1][6] = new Pawn(false, 1, 6);
        pieces[2][6] = new Pawn(false, 2, 6);
        pieces[3][6] = new Pawn(false, 3, 6);
        pieces[4][6] = new Pawn(false, 4, 6);
        pieces[5][6] = new Pawn(false, 5, 6);
        pieces[6][6] = new Pawn(false, 6, 6);
        pieces[7][6] = new Pawn(false, 7, 6);
    }

    public void update()
    {
        if(!selectFlag && currentPiece == null)
        {
            if(isCheck(pieces, secondTurn))
            {
                boolean checkmate = true;
                for(Piece[] pieceA : pieces)
                {
                    for(Piece piece : pieceA)
                    {
                        if(piece != null)
                        {
                            if(piece.isSecond() == secondTurn)
                            {
                                piece.calculateMoves(pieces);
                                if(!piece.getMoves().isEmpty())
                                {
                                    checkmate = false;
                                    break;
                                }
                            }
                        }
                    }

                    if(checkmate) break;
                }

                if(checkmate) System.out.println(secondTurn ? "White Wins!" : "Black Wins!");
            }
        }
    }

    public void draw(Graphics2D g2d)
    {
        AffineTransform transform = g2d.getTransform();
        g2d.translate(x, y);

        //Flag to make sure that the checks for the draw hover is only drawn when not found yet
        boolean drewHover = false;
        for(int col = 0; col < pieces.length; col++)
        {
            for(int row = 0; row < pieces[0].length; row++)
            {
                //Draw the board
                if((col + row) % 2 == 0) g2d.setColor(new Color(255, 255, 224));
                else g2d.setColor(new Color(130, 82, 1));

                g2d.fillRect(col * TILE_LENGTH, row * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);

                //Draw the hover if it is not drawn yet
                if(hoverCol != -1 && hoverRow != -1)
                {
                    Piece hoverPiece = pieces[hoverCol][hoverRow];
                    if(!drewHover && !selectFlag && !promoting && hoverPiece != null)
                    {
                        if(hoverPiece.isSecond() == this.secondTurn &&
                                hoverPiece.getCol() == col && hoverPiece.getRow() == row)
                        {
                            g2d.setColor(new Color(152, 251, 152, 150));
                            g2d.fillRect(col * TILE_LENGTH, row * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
                            drewHover = true;
                        }
                    }
                }

                Piece piece = pieces[col][row];
                if(piece != null) piece.draw(g2d, TILE_LENGTH);
            }
        }

        //Draw the select mode
        if(selectFlag)
        {
            //Color the current piece square
            g2d.setColor(new Color(30, 144, 255, 150));
            g2d.fillRect(currentPiece.getCol() * TILE_LENGTH, currentPiece.getRow() * TILE_LENGTH,
                    TILE_LENGTH, TILE_LENGTH);

            //Color all possible movement options
            if(currentPiece != null)
            {
                currentPiece.calculateMoves(pieces);

                for(int[] location : currentPiece.getMoves())
                {
                    if(location[0] == hoverCol && location[1] == hoverRow)
                        g2d.setColor(new Color(152, 251, 152, 150));
                    else g2d.setColor(new Color(255, 102, 102, 150));
                    g2d.fillRect(location[0] * TILE_LENGTH, location[1] * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
                }
            }
        }

        //Draw the current dragged/selected piece
        if(currentPiece != null) currentPiece.draw(g2d, TILE_LENGTH, currentPieceX - x, currentPieceY - y);

        //Draw the promotion menu
        if(promoting)
        {
            AffineTransform transform2 = g2d.getTransform();
            g2d.translate((promotingCol - 1.75) * TILE_LENGTH, (promotingRow + 0.75) * TILE_LENGTH);

            g2d.setColor(new Color(192, 192, 192, 210));
            g2d.fillRect(0, 0, TILE_LENGTH * 4 + 25, TILE_LENGTH + 10);

            if(promotingHover != -1)
            {
                g2d.setColor(new Color(152, 251, 152, 150));
                g2d.fillRect((5 * (promotingHover + 1)) + promotingHover * TILE_LENGTH, 5, TILE_LENGTH, TILE_LENGTH);
            }

            if(secondTurn)
            {
                g2d.drawImage(Images.BLACK.BISHOP, 5, 5, TILE_LENGTH, TILE_LENGTH, null);
                g2d.drawImage(Images.BLACK.KNIGHT, 10 + TILE_LENGTH, 5, TILE_LENGTH, TILE_LENGTH, null);
                g2d.drawImage(Images.BLACK.ROOK, 15 + TILE_LENGTH * 2, 5, TILE_LENGTH, TILE_LENGTH, null);
                g2d.drawImage(Images.BLACK.QUEEN, 20 + TILE_LENGTH * 3, 5, TILE_LENGTH, TILE_LENGTH, null);
            }
            else
            {
                g2d.drawImage(Images.WHITE.BISHOP, 5, 5, TILE_LENGTH, TILE_LENGTH, null);
                g2d.drawImage(Images.WHITE.KNIGHT, 10 + TILE_LENGTH, 5, TILE_LENGTH, TILE_LENGTH, null);
                g2d.drawImage(Images.WHITE.ROOK, 15 + TILE_LENGTH * 2, 5, TILE_LENGTH, TILE_LENGTH, null);
                g2d.drawImage(Images.WHITE.QUEEN, 20 + TILE_LENGTH * 3, 5, TILE_LENGTH, TILE_LENGTH, null);
            }

            g2d.setTransform(transform2);
        }

        g2d.setTransform(transform);
    }

    public void mousePressed(MouseEvent e)
    {
        int mouseCol = (e.getX() - x) / TILE_LENGTH;
        int mouseRow = (e.getY() - y) / TILE_LENGTH;

        if(!promoting)
        {
            //Check if the mouse is within the board
            if(mouseCol >= 0 && mouseCol < pieces.length &&
                    mouseRow >= 0 && mouseRow < pieces[0].length)
            {
                if(!selectFlag)
                {
                    //Check if the square has a piece
                    if(currentPiece == null && pieces[mouseCol][mouseRow] != null)
                    {
                        if(pieces[mouseCol][mouseRow].isSecond() == secondTurn)
                        {
                            //Pick up the piece and remove it
                            currentPiece = pieces[mouseCol][mouseRow];
                            pieces[mouseCol][mouseRow] = null;

                            setCurrentPieceLocation(x + currentPiece.getCol() * TILE_LENGTH + TILE_LENGTH / 2,
                                    y + currentPiece.getRow() * TILE_LENGTH + TILE_LENGTH / 2);
                        }
                    }
                }
                //Selection Mode
                else if(currentPiece != null)
                {
                    //Check if the clicked location is a valid location
                    currentPiece.calculateMoves(pieces);
                    if(currentPiece.isValidMove(mouseCol, mouseRow))
                    {
                        pieces[mouseCol][mouseRow] = currentPiece;
                        currentPiece.setPosition(mouseCol, mouseRow);
                        currentPiece.setMoved();

                        //Check for Pawn promotion
                        if(currentPiece instanceof Pawn && (currentPiece.isSecond() && currentPiece.getRow() == 7) ||
                                (!currentPiece.isSecond() && currentPiece.getRow() == 0))
                        {
                            promoting = true;
                            promotingCol = currentPiece.getCol();
                            promotingRow = currentPiece.getRow();
                        }
                        else
                        {
                            secondTurn = !secondTurn;
                        }
                    }
                    //Otherwise, deactivate the select mode and revert the position of the current piece
                    else pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;

                    currentPiece = null;
                    selectFlag = false;
                }
            }
        }
        else
        {
            switch(promotingHover)
            {
                case 0:
                    pieces[promotingCol][promotingRow] = new Bishop(secondTurn, promotingCol, promotingRow);
                    break;
                case 1:
                    pieces[promotingCol][promotingRow] = new Bishop(secondTurn, promotingCol, promotingRow);
                    break;
                case 2:
                    pieces[promotingCol][promotingRow] = new Bishop(secondTurn, promotingCol, promotingRow);
                     break;
                case 3:
                    pieces[promotingCol][promotingRow] = new Bishop(secondTurn, promotingCol, promotingRow);
                    break;
                default:
                    return;
            }

            promoting = false;
            secondTurn = !secondTurn;
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        int mouseCol = (e.getX() - x) / TILE_LENGTH;
        int mouseRow = (e.getY() - y) / TILE_LENGTH;

        if(!promoting)
        {
            //Check if the mouse is within the board
            if(mouseCol >= 0 && mouseCol < pieces.length &&
                    mouseRow >= 0 && mouseRow < pieces[0].length)
            {
                if(currentPiece != null)
                {
                    //If the piece is not moved, then go into the select mode
                    if(mouseCol == currentPiece.getCol() && mouseRow == currentPiece.getRow())
                    {
                        selectFlag = true;
                        setCurrentPieceLocation(x + currentPiece.getCol() * TILE_LENGTH + TILE_LENGTH / 2,
                                y + currentPiece.getRow() * TILE_LENGTH + TILE_LENGTH / 2);
                        return;
                    }

                    currentPiece.calculateMoves(pieces);

                    //Check if the drop location is clear and a valid position
                    if(currentPiece.isValidMove(mouseCol, mouseRow))
                    {
                        pieces[mouseCol][mouseRow] = currentPiece;
                        currentPiece.setPosition(mouseCol, mouseRow);
                        currentPiece.setMoved();

                        //Check for Pawn promotion
                        if(currentPiece instanceof Pawn && (currentPiece.isSecond() && currentPiece.getRow() == 7) ||
                                (!currentPiece.isSecond() && currentPiece.getRow() == 0))
                        {
                            promoting = true;
                        }
                        else
                        {
                            secondTurn = !secondTurn;
                        }
                    }
                    //Otherwise, move the piece back to its original position
                    else
                    {
                        pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;
                    }
                }
            } else
            {
                pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;
            }

            currentPiece = null;
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        if(currentPiece != null && !selectFlag && !promoting)
        {
            //Update currentPiece position if the mouse is far enough between the original position
            if(distSq(e.getX(), e.getY(),
                    x + currentPiece.getCol() * TILE_LENGTH + TILE_LENGTH / 2,
                    y + currentPiece.getRow() * TILE_LENGTH + TILE_LENGTH / 2) > 225)
            {
                setCurrentPieceLocation(e.getX(), e.getY());
            }
            //Otherwise set the position to the original position
            else
            {
                setCurrentPieceLocation(x + currentPiece.getCol() * TILE_LENGTH + TILE_LENGTH / 2,
                        y + currentPiece.getRow() * TILE_LENGTH + TILE_LENGTH / 2);
            }
        }
    }

    private int distSq(int x1, int y1, int x2, int y2)
    {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    public void mouseMoved(MouseEvent e)
    {
        if(!promoting)
        {
            int mouseCol = (e.getX() - x) / TILE_LENGTH;
            int mouseRow = (e.getY() - y) / TILE_LENGTH;

            if(mouseCol >= 0 && mouseCol < pieces.length &&
                    mouseRow >= 0 && mouseRow < pieces[0].length)
            {
                hoverCol = mouseCol;
                hoverRow = mouseRow;
            }
            else
            {
                hoverCol = -1;
                hoverRow = -1;
            }
        }
        else
        {
            int relativeY = e.getY() - y - (int) ((promotingRow + 0.75) * TILE_LENGTH);
            int relativeX = e.getX() - x - (int) ((promotingCol - 1.75) * TILE_LENGTH);

            if(relativeX >= 5 && relativeX <= TILE_LENGTH * 4 + 20 && relativeY >= 5 && relativeY <= TILE_LENGTH + 5)
            {
                promotingHover = (relativeX - 5) / (TILE_LENGTH + 5);
            }
            else
            {
                promotingHover = -1;
            }

            hoverCol = -1;
            hoverRow = -1;
        }
    }

    private void setCurrentPieceLocation(int x, int y)
    {
        currentPieceX = x - TILE_LENGTH / 2;
        currentPieceY = y - TILE_LENGTH / 2;
    }

    //Check if a piece is directly attacking the king
    public static boolean isCheck(Piece[][] pieces, boolean secondTurn)
    {
        Piece currentTurnKing = getKing(pieces, secondTurn);

        if(currentTurnKing != null)
        {
            for(Piece[] pieceA : pieces)
            {
                for(Piece piece : pieceA)
                {
                    if(piece != null)
                    {
                        if(piece.isSecond() != secondTurn)
                        {
                            piece.calculateMovesUnfiltered(pieces);
                            for(int[] possibleMove : piece.getMoves())
                            {
                                if(possibleMove[0] == currentTurnKing.getCol() && possibleMove[1] == currentTurnKing.getRow())
                                {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private static Piece getKing(Piece[][] pieces, boolean second)
    {
        for(Piece[] pieceA : pieces)
        {
            for(Piece piece : pieceA)
            {
                if(piece instanceof King && piece.isSecond() == second)
                {
                    return piece;
                }
            }
        }

        return null;
    }
}
