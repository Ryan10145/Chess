package components;

import components.pieces.*;
import utility.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

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
    private String moves;
    private int takenPieceHadMoved;

    //TODO Stalemate

    public Board(int x, int y)
    {
        this.x = x;
        this.y = y;

        pieces = new Piece[8][8];
        setUpPieces();

        moves = "";
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
        boolean checkmate = checkCheckMate();
        if(checkmate)
        {
            System.out.println((secondTurn ? "Black" : "White") + "Wins!");
        }
        else
        {
            if(checkStaleMate()) System.out.println("Stalemate!");
        }
    }

    private boolean checkCheckMate()
    {
        if(!selectFlag && currentPiece == null)
        {
            if(isCheck(pieces, secondTurn))
            {
                for(Piece[] pieceA : pieces)
                {
                    for(Piece piece : pieceA)
                    {
                        if(piece != null)
                        {
                            if(piece.isSecond() == secondTurn)
                            {
                                piece.calculateMoves(pieces);
                                if(!piece.getMoves().isEmpty()) return false;
                            }
                        }
                    }
                }

                return true;
            }
        }
        return false;
    }

    private boolean checkStaleMate()
    {
        if(getAllMoves(secondTurn).isEmpty()) return true;
        //Repeat position three times (maybe 5)
        //Fifty moves without pawn move or capture (maybe 75)
        //King v. King, King and Bishop v. King, King and Knight v. King, King and Bishop v. King and Bishop (Same Color)

        return false;
    }

    public void draw(Graphics2D g2d)
    {
        AffineTransform transform = g2d.getTransform();
        g2d.translate(x, y);

        drawBoard(g2d);
        if(hoverCol != -1 && hoverRow != -1 && !promoting && !selectFlag) drawHover(g2d);
        if(selectFlag) drawSelectionMode(g2d);
        if(currentPiece != null) currentPiece.draw(g2d, TILE_LENGTH, currentPieceX - x, currentPieceY - y);
        if(promoting) drawPromotion(g2d);

        g2d.setTransform(transform);
    }

    private void drawBoard(Graphics2D g2d)
    {
        for(int col = 0; col < pieces.length; col++)
        {
            for(int row = 0; row < pieces[0].length; row++)
            {
                //Set the board colors to be alternating
                if((col + row) % 2 == 0) g2d.setColor(new Color(255, 255, 224));
                else g2d.setColor(new Color(130, 82, 1));

                g2d.fillRect(col * TILE_LENGTH, row * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);

                Piece piece = pieces[col][row];
                if(piece != null) piece.draw(g2d, TILE_LENGTH);
            }
        }
    }

    private void drawHover(Graphics2D g2d)
    {
        Piece hoverPiece = pieces[hoverCol][hoverRow];
        if(hoverPiece != null)
        {
            if(hoverPiece.isSecond() == this.secondTurn)
            {
                g2d.setColor(new Color(152, 251, 152, 150));
                g2d.fillRect(hoverCol * TILE_LENGTH, hoverRow * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
            }
        }
    }

    private void drawSelectionMode(Graphics2D g2d)
    {
        //Color the current piece square
        g2d.setColor(new Color(30, 144, 255, 150));
        g2d.fillRect(currentPiece.getCol() * TILE_LENGTH, currentPiece.getRow() * TILE_LENGTH,
                TILE_LENGTH, TILE_LENGTH);

        //Color all possible movement options
        if(currentPiece != null)
        {
            currentPiece.calculateMoves(pieces);

            if(currentPiece != null)
            {
                for(int[] location : currentPiece.getMoves())
                {
                    if(location[0] == hoverCol && location[1] == hoverRow)
                        g2d.setColor(new Color(152, 251, 152, 150));
                    else g2d.setColor(new Color(255, 102, 102, 150));
                    g2d.fillRect(location[0] * TILE_LENGTH, location[1] * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
                }
            }
        }
    }

    private void drawPromotion(Graphics2D g2d)
    {
        AffineTransform transform2 = g2d.getTransform();
        g2d.translate((promotingCol - 1.75) * TILE_LENGTH, (promotingRow + 0.75) * TILE_LENGTH);
        if(promotingCol == 7) g2d.translate(-TILE_LENGTH * 3.0 / 4, 0);
        if(promotingCol == 0) g2d.translate(TILE_LENGTH * 3.0 / 4, 0);

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
        } else
        {
            g2d.drawImage(Images.WHITE.BISHOP, 5, 5, TILE_LENGTH, TILE_LENGTH, null);
            g2d.drawImage(Images.WHITE.KNIGHT, 10 + TILE_LENGTH, 5, TILE_LENGTH, TILE_LENGTH, null);
            g2d.drawImage(Images.WHITE.ROOK, 15 + TILE_LENGTH * 2, 5, TILE_LENGTH, TILE_LENGTH, null);
            g2d.drawImage(Images.WHITE.QUEEN, 20 + TILE_LENGTH * 3, 5, TILE_LENGTH, TILE_LENGTH, null);
        }

        g2d.setTransform(transform2);
    }

    public void mousePressed(MouseEvent e)
    {
        int mouseCol = (e.getX() - x) / TILE_LENGTH;
        int mouseRow = (e.getY() - y) / TILE_LENGTH;

        if(!promoting)
        {
            if(checkBoardBounds(mouseCol, mouseRow))
            {
                //Pick up the piece and start dragging it
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
                //Move a piece with selection mode
                else if(currentPiece != null)
                {
                    movePiece(mouseCol, mouseRow);

                    currentPiece = null;
                    selectFlag = false;
                }
            }
        }
        //Interact with the promoting menu
        else
        {
            switch(promotingHover)
            {
                case 0:
                    pieces[promotingCol][promotingRow] = new Bishop(secondTurn, promotingCol, promotingRow);
                    break;
                case 1:
                    pieces[promotingCol][promotingRow] = new Knight(secondTurn, promotingCol, promotingRow);
                    break;
                case 2:
                    pieces[promotingCol][promotingRow] = new Rook(secondTurn, promotingCol, promotingRow);
                    break;
                case 3:
                    pieces[promotingCol][promotingRow] = new Queen(secondTurn, promotingCol, promotingRow);
                    break;
                default:
                    return;
            }

            moves += pieces[promotingCol][promotingRow].getID();
            moves += 1;
            moves += takenPieceHadMoved;
            promoting = false;
            secondTurn = !secondTurn;
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        int mouseCol = (e.getX() - x) / TILE_LENGTH;
        int mouseRow = (e.getY() - y) / TILE_LENGTH;

        if(!promoting && currentPiece != null)
        {
            if(checkBoardBounds(mouseCol, mouseRow))
            {
                //If the piece is not moved, then go into the select mode
                if(mouseCol == currentPiece.getCol() && mouseRow == currentPiece.getRow())
                {
                    selectFlag = true;
                    setCurrentPieceLocation(x + currentPiece.getCol() * TILE_LENGTH + TILE_LENGTH / 2,
                            y + currentPiece.getRow() * TILE_LENGTH + TILE_LENGTH / 2);
                    return;
                }

                movePiece(mouseCol, mouseRow);
            }
            else pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;

            currentPiece = null;
        }
    }

    private void movePiece(int mouseCol, int mouseRow)
    {
        //Check if the clicked location is a valid location
        currentPiece.calculateMoves(pieces);

        if(currentPiece.isValidMove(mouseCol, mouseRow))
        {
            for(Piece[] pieceA : pieces)
            {
                for(Piece piece : pieceA)
                {
                    if(piece instanceof Pawn)
                    {
                        if(piece.isSecond() == secondTurn)
                        {
                            ((Pawn) piece).setCanEnPassant(false);
                        }
                    }
                }
            }
            if(currentPiece instanceof Pawn)
            {
                Pawn pawn = (Pawn) currentPiece;
                if(pawn.canEnPassant()) pawn.setCanEnPassant(false);
            }

            boolean enPassant = false;
            int castlingRookOriginalCol = -1;
            int castlingRookNewCol = -1;

            if(currentPiece instanceof King && Math.abs(mouseCol - currentPiece.getCol()) == 2)
            {
                Piece rook;
                if(mouseCol > currentPiece.getCol()) rook = pieces[7][currentPiece.getRow()];
                else rook = pieces[0][currentPiece.getRow()];

                castlingRookOriginalCol = rook.getCol();
                pieces[mouseCol - (int) Math.signum(mouseCol - currentPiece.getCol())][currentPiece.getRow()] = rook;
                pieces[rook.getCol()][rook.getRow()] = null;

                rook.setPosition(mouseCol - (int) Math.signum(mouseCol - currentPiece.getCol()),
                        currentPiece.getRow());
                rook.setMoved(true);

                castlingRookNewCol = rook.getCol();
            }
            else if(currentPiece instanceof Pawn)
            {
                Pawn pawn = (Pawn) currentPiece;
                if(Math.abs(pawn.getRow() - mouseRow) == 2) pawn.setCanEnPassant(true);

                if(pawn.getCol() - 1 >= 0)
                {
                    if(pieces[pawn.getCol() - 1][pawn.getRow()] instanceof Pawn)
                    {
                        if(pieces[pawn.getCol() - 1][pawn.getRow()].isSecond() != pawn.isSecond() &&
                                pieces[mouseCol][mouseRow] == null &&
                                mouseCol == pawn.getCol() - 1)
                        {
                            pieces[pawn.getCol() - 1][pawn.getRow()] = null;
                            enPassant = true;
                        }
                    }
                }
                if(pawn.getCol() + 1 < pieces.length)
                {
                    if(pieces[pawn.getCol() + 1][pawn.getRow()] instanceof Pawn)
                    {
                        if(pieces[pawn.getCol() + 1][pawn.getRow()].isSecond() != pawn.isSecond() &&
                                pieces[mouseCol][mouseRow] == null &&
                                mouseCol == pawn.getCol() + 1)
                        {
                            pieces[pawn.getCol() + 1][pawn.getRow()] = null;
                            enPassant = true;
                        }
                    }
                }
            }

            //If there was no castle
            if(castlingRookNewCol == -1)
            {
                //Normal Move Recording
                if(!enPassant)
                {
                    moves += currentPiece.getID();
                    moves += currentPiece.getCol() + "" + currentPiece.getRow();
                    moves += mouseCol + "" + mouseRow;
                    if(pieces[mouseCol][mouseRow] == null)
                    {
                        moves += "//";
                        takenPieceHadMoved = 0;
                    }
                    else
                    {
                        moves += pieces[mouseCol][mouseRow].getID();
                        takenPieceHadMoved = pieces[mouseCol][mouseRow].getMoved() ? 1 : 0;
                    }
                }
                //Adding en passant
                else
                {
                    moves += "EN";
                    moves += currentPiece.getCol() + "" + currentPiece.getRow();
                    moves += mouseCol + "" + mouseRow;
                    moves += mouseCol + "" + currentPiece.getRow();
                    moves += "//";
                    moves += 1;
                    moves += 1;
                }
            }
            //Adding castle
            else
            {
                moves += "CA";
                moves += currentPiece.getCol() + "" + currentPiece.getRow();
                moves += mouseCol + "" + mouseRow;
                moves += castlingRookOriginalCol + "" + mouseRow;
                moves += castlingRookNewCol + "" + mouseRow;
                moves += 0;
                moves += 0;
            }

            pieces[mouseCol][mouseRow] = currentPiece;
            currentPiece.setPosition(mouseCol, mouseRow);
            int hadMoved = currentPiece.getMoved() ? 1 : 0;
            currentPiece.setMoved(true);

            if(!enPassant && castlingRookNewCol == -1)
            {
                //Check for Pawn promotion
                if(currentPiece instanceof Pawn && ((currentPiece.isSecond() && currentPiece.getRow() == 7) ||
                        (!currentPiece.isSecond() && currentPiece.getRow() == 0)))
                {
                    promoting = true;
                    promotingCol = currentPiece.getCol();
                    promotingRow = currentPiece.getRow();

                    return;
                }
                else moves += "//";
                moves += hadMoved;
                moves += takenPieceHadMoved;
            }

            secondTurn = !secondTurn;
        }
        //Otherwise, revert the position of the current piece
        else pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;
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

            if(checkBoardBounds(mouseCol, mouseRow))
            {
                hoverCol = mouseCol;
                hoverRow = mouseRow;
            } else
            {
                hoverCol = -1;
                hoverRow = -1;
            }
        } else
        {
            int relativeY = e.getY() - y - (int) ((promotingRow + 0.75) * TILE_LENGTH);
            int relativeX = e.getX() - x - (int) ((promotingCol - 1.75) * TILE_LENGTH);

            if(promotingCol == 7) relativeX += TILE_LENGTH * 3.0 / 4;
            if(promotingCol == 0) relativeX -= TILE_LENGTH * 3.0 / 4;

            if(relativeX >= 5 && relativeX <= TILE_LENGTH * 4 + 20 && relativeY >= 5 && relativeY <= TILE_LENGTH + 5)
            {
                promotingHover = (relativeX - 5) / (TILE_LENGTH + 5);
            } else
            {
                promotingHover = -1;
            }

            hoverCol = -1;
            hoverRow = -1;
        }

//        if(checkBoardBounds(hoverCol, hoverRow))
//        {
//            if(pieces[hoverCol][hoverRow] != null && pieces[hoverCol][hoverRow] instanceof Pawn)
//            {
//                System.out.println(((Pawn) pieces[hoverCol][hoverRow]).canEnPassant());
//            }
//        }
    }

    public void keyPressed(KeyEvent e)
    {
        undoLastMove();
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

    private HashMap<Piece, int[]> getAllMoves(boolean second)
    {
        HashMap<Piece, int[]> moves = new HashMap<>();

        for(Piece[] pieceA : pieces)
        {
            for(Piece piece : pieceA)
            {
                if(piece != null)
                {
                    if(piece.isSecond() == second)
                    {
                        piece.calculateMoves(pieces);
                        for(int[] move : piece.getMoves())
                        {
                            moves.put(piece, move);
                        }
                    }
                }
            }
        }

        return moves;
    }

    private boolean checkBoardBounds(int mouseCol, int mouseRow)
    {
        return mouseCol >= 0 && mouseCol < pieces.length &&
                mouseRow >= 0 && mouseRow < pieces[0].length;
    }

    private void undoLastMove()
    {
        if(!moves.isEmpty() && !promoting)
        {
            selectFlag = false;

            String lastMove = moves.substring(moves.length() - 12, moves.length());
            String id = lastMove.substring(0, 2);
            switch(id)
            {
                case "CA":
                {
                    int moveData = Integer.parseInt(lastMove.substring(2, 10));
                    int rookDestRow = moveData % 10;
                    moveData /= 10;
                    int rookDestCol = moveData % 10;
                    moveData /= 10;
                    int rookSrcRow = moveData % 10;
                    moveData /= 10;
                    int rookSrcCol = moveData % 10;
                    moveData /= 10;
                    int kingDestRow = moveData % 10;
                    moveData /= 10;
                    int kingDestCol = moveData % 10;
                    moveData /= 10;
                    int kingSrcRow = moveData % 10;
                    moveData /= 10;
                    int kingSrcCol = moveData % 10;

                    pieces[rookSrcCol][rookSrcRow] = pieces[rookDestCol][rookDestRow];
                    pieces[rookSrcCol][rookSrcRow].setPosition(rookSrcCol, rookSrcRow);
                    pieces[rookSrcCol][rookSrcRow].setMoved(false);

                    pieces[rookDestCol][rookDestRow] = null;

                    pieces[kingSrcCol][kingSrcRow] = pieces[kingDestCol][kingDestRow];
                    pieces[kingSrcCol][kingSrcRow].setPosition(kingSrcCol, kingSrcRow);
                    pieces[kingSrcCol][kingSrcRow].setMoved(false);

                    pieces[kingDestCol][kingDestRow] = null;
                    break;
                }
                case "EN":
                {
                    int moveData = Integer.parseInt(lastMove.substring(2, 8));
                    int takeRow = moveData % 10;
                    moveData /= 10;
                    int takeCol = moveData % 10;
                    moveData /= 10;
                    int destRow = moveData % 10;
                    moveData /= 10;
                    int destCol = moveData % 10;
                    moveData /= 10;
                    int srcRow = moveData % 10;
                    moveData /= 10;
                    int srcCol = moveData % 10;

                    pieces[srcCol][srcRow] = pieces[destCol][destRow];
                    pieces[srcCol][srcRow].setPosition(srcCol, srcRow);
                    pieces[srcCol][srcRow].setMoved(Integer.parseInt(lastMove.substring(10, 11)) == 1);

                    pieces[destCol][destRow] = null;

                    pieces[takeCol][takeRow] = new Pawn(!pieces[srcCol][srcRow].isSecond(), takeCol, takeRow);
                    pieces[takeCol][takeRow].setMoved(true);
                    ((Pawn) pieces[takeCol][takeRow]).setCanEnPassant(true);
                    break;
                }
                default:
                {
                    int moveData = Integer.parseInt(lastMove.substring(2, 6));
                    int destRow = moveData % 10;
                    moveData /= 10;
                    int destCol = moveData % 10;
                    moveData /= 10;
                    int srcRow = moveData % 10;
                    moveData /= 10;
                    int srcCol = moveData % 10;

                    pieces[srcCol][srcRow] = pieces[destCol][destRow];
                    pieces[srcCol][srcRow].setPosition(srcCol, srcRow);
                    pieces[srcCol][srcRow].setMoved(Integer.parseInt(lastMove.substring(10, 11)) == 1);

                    Piece replacePiece = Piece.parseID(lastMove.substring(6, 8), !pieces[srcCol][srcRow].isSecond(), destCol, destRow);
                    pieces[destCol][destRow] = replacePiece;
                    if(replacePiece != null)
                        pieces[destCol][destRow].setMoved(Integer.parseInt(lastMove.substring(11, 12)) == 1);
                    break;
                }
            }

            secondTurn = !secondTurn;
            moves = moves.substring(0, moves.length() - 12);

            if(currentPiece != null)
            {
                pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;
                currentPiece = null;
            }

            //Syntax for Moves:
            //Two Characters for Piece, Two Numbers for col/row of src, Two Numbers for col/row of destination
            //Then, if it was a take, put the id of the take. Otherwise, put a //
            //Then, if it was a promotion, the new piece

            //If it was a castle, then put CA, then both the original king col/row, new king col/row,
            //original rook col/row, and then new rook col/row

            //If it was a en passant, then put EN, then put the original pawn col/row, the new pawn col/row ,
            //taken pawn col/row, and then //

            //At the end, put a 0 if the prior to the move, the piece had not moved, and 1 if the piece had already moved
            //Then, put a number using the top logic, but with the taken piece, and 0 if there was no taken piece
        }
    }
}
