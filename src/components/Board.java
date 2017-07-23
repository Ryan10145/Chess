package components;

import components.pieces.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class Board
{
    private Piece[][] pieces;
    private Piece currentPiece;

    private final int TILE_LENGTH = 60;
    private int x, y;
    private int currentPieceX, currentPieceY;
    private boolean selectFlag;
    private boolean startedDragging;

    //TODO Turns
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
        System.out.println(selectFlag);
    }

    public void draw(Graphics2D g2d)
    {
        AffineTransform transform = g2d.getTransform();
        g2d.translate(x, y);

        for(int col = 0; col < pieces.length; col++)
        {
            for(int row = 0; row < pieces[0].length; row++)
            {
                if((col + row) % 2 == 0) g2d.setColor(new Color(255, 255, 224));
                else g2d.setColor(new Color(130, 82, 1));

                g2d.fillRect(col * TILE_LENGTH, row * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);

                Piece piece = pieces[col][row];
                if(piece != null) piece.draw(g2d, TILE_LENGTH);
            }
        }

        if(selectFlag)
        {
            g2d.setColor(new Color(30, 144, 255, 150));
            g2d.fillRect(currentPiece.getCol() * TILE_LENGTH, currentPiece.getRow() * TILE_LENGTH,
                    TILE_LENGTH, TILE_LENGTH);

            g2d.setColor(new Color(255, 102, 102, 150));
            currentPiece.calculateMoves(pieces);
            for(int[] location : currentPiece.getMoves())
            {
                g2d.fillRect(location[0] * TILE_LENGTH, location[1] * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
            }
        }

        if(currentPiece != null) currentPiece.draw(g2d, TILE_LENGTH, currentPieceX - x, currentPieceY - y);

        g2d.setTransform(transform);
    }

    public void mousePressed(MouseEvent e)
    {
        int mouseCol = (e.getX() - x) / TILE_LENGTH;
        int mouseRow = (e.getY() - y) / TILE_LENGTH;

        //Check if the mouse is within the board
        if(mouseCol >= 0 && mouseCol < pieces.length &&
                mouseRow >= 0 && mouseRow < pieces[0].length)
        {
            if(!selectFlag)
            {
                //Check if the square has a piece
                if(currentPiece == null && pieces[mouseCol][mouseRow] != null)
                {
                    //Pick up the piece and remove it
                    currentPiece = pieces[mouseCol][mouseRow];
                    pieces[mouseCol][mouseRow] = null;

                    setCurrentPieceLocation(x + currentPiece.getCol() * TILE_LENGTH + TILE_LENGTH / 2,
                            y + currentPiece.getRow() * TILE_LENGTH + TILE_LENGTH / 2);
                }
            }
            else
            {
                currentPiece.calculateMoves(pieces);
                if(currentPiece.isValidMove(mouseCol, mouseRow))
                {
                    pieces[mouseCol][mouseRow] = currentPiece;
                    currentPiece.setPosition(mouseCol, mouseRow);
                    currentPiece.setMoved();
                }
                else pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;

                currentPiece = null;
                selectFlag = false;
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        int mouseCol = (e.getX() - x) / TILE_LENGTH;
        int mouseRow = (e.getY() - y) / TILE_LENGTH;

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
                }
                //Otherwise, move the piece back to its original position
                else
                {
                    pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;
                }
            }
        }
        else
        {
            pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;
        }

        currentPiece = null;
    }

    public void mouseDragged(MouseEvent e)
    {
//        int mouseCol = (e.getX() - x) / TILE_LENGTH;
//        int mouseRow = (e.getY() - y) / TILE_LENGTH;

        if(currentPiece != null && !selectFlag)
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
//        int mouseCol = (e.getX() - x) / TILE_LENGTH;
//        int mouseRow = (e.getY() - y) / TILE_LENGTH;
    }

    private void setCurrentPieceLocation(int x, int y)
    {
        currentPieceX = x - TILE_LENGTH / 2;
        currentPieceY = y - TILE_LENGTH / 2;
    }
}
