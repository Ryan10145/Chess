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

    private final int TILE_LENGTH = 60;
    private int x, y;                          //The current x/y of the board

    private Piece[][] pieces;
    private Piece currentPiece;                //Reference for the piece being moved
    private int hoverCol, hoverRow;            //Coordinates for the square the mouse is on
    private int currentPieceX, currentPieceY;  //Where to draw the current piece if it is being dragged
    private boolean selectMode;
    private boolean secondTurn;                //Holds the current turn

    private boolean promotingMode;
    private int promotingCol, promotingRow;    //The square where the promoting pawn is located
    private int promotingHover;                //The index of the option the user is selecting

    private String moves;                      //A list of all moves currently made, syntax defined in undoLastMove()
    private int takenPieceHadMoved;            //Whether or not the taken piece had moved (use in the moves list)

    //TODO Stalemate

    public Board(int x, int y)
    {
        this.x = x;
        this.y = y;

        pieces = new Piece[8][8];
        setUpPieces();

        moves = "";
    }

    //Setup the default position of the board
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

    //Perform checks on the game state
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

    //Returns true if the current pieces form a checkmate
    private boolean checkCheckMate()
    {
        //Make sure that the select mode is not triggered, as it may print incorrect results due to move calculation
        //Additionally make sure that a piece is not selected, as it may print incorrect resutls due to move calculation
        if(!selectMode && currentPiece == null)
        {
            if(isCheck(pieces, secondTurn))
            {
                //Go through all pieces
                for(Piece[] pieceA : pieces)
                {
                    for(Piece piece : pieceA)
                    {
                        if(piece != null)
                        {
                            if(isCurrentSide(piece))
                            {
                                //If the piece has any possible moves, then return false
                                piece.calculateMoves(pieces);
                                if(!piece.getMoves().isEmpty()) return false;
                            }
                        }
                    }
                }

                //If all pieces have no moves, then declare checkmate
                return true;
            }
        }
        return false;
    }

    private boolean checkStaleMate()
    {
        //No moves check
        if(getAllMoves(secondTurn).isEmpty()) return true;
        //Repeat position three times (maybe 5)
        //Fifty moves without pawn move or capture (maybe 75)
        //King v. King, King and Bishop v. King, King and Knight v. King, King and Bishop v. King and Bishop (Same Color)

        return false;
    }

    //Draw loop that is called every frame
    public void draw(Graphics2D g2d)
    {
        //Translate by the board x/y
        AffineTransform transform = g2d.getTransform();
        g2d.translate(x, y);

        drawBoard(g2d);
        //If mouse is hovering over a square, and there are no modes, then draw a hover
        if(hoverCol != -1 && hoverRow != -1 && !promotingMode && !selectMode) drawHover(g2d);
        //Draw the selection squares if needed
        if(selectMode) drawSelectionMode(g2d);
        //Draw the current piece if it is being dragged
        if(currentPiece != null) currentPiece.draw(g2d, TILE_LENGTH, currentPieceX - x, currentPieceY - y);
        //Draw the promotion GUI
        if(promotingMode) drawPromotion(g2d);

        g2d.setTransform(transform);
    }

    //Draw the checkerboard and the pieces
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

    //Draw a mouse hover
    private void drawHover(Graphics2D g2d)
    {
        Piece hoverPiece = pieces[hoverCol][hoverRow];
        if(hoverPiece != null)
        {
            //Make sure the hover piece is one of the user's pieces
            if(hoverPiece.isSecond() == this.secondTurn)
            {
                g2d.setColor(new Color(152, 251, 152, 150));
                g2d.fillRect(hoverCol * TILE_LENGTH, hoverRow * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
            }
        }
    }

    //Draw all of the possible movement options
    private void drawSelectionMode(Graphics2D g2d)
    {
        //Color the current piece square
        g2d.setColor(new Color(30, 144, 255, 150));
        g2d.fillRect(currentPiece.getCol() * TILE_LENGTH, currentPiece.getRow() * TILE_LENGTH,
                TILE_LENGTH, TILE_LENGTH);

        if(currentPiece != null)
        {
            currentPiece.calculateMoves(pieces);

            //Iterate through all movement options
            for(int[] location : currentPiece.getMoves())
            {
                //Set the color differently if the option is being hovered
                if(location[0] == hoverCol && location[1] == hoverRow) g2d.setColor(new Color(152, 251, 152, 150));
                else g2d.setColor(new Color(255, 102, 102, 150));

                g2d.fillRect(location[0] * TILE_LENGTH, location[1] * TILE_LENGTH, TILE_LENGTH, TILE_LENGTH);
            }
        }
    }

    //Draw the promotion GUI
    private void drawPromotion(Graphics2D g2d)
    {
        //Translate to the promotion piece
        AffineTransform transform2 = g2d.getTransform();
        g2d.translate((promotingCol - 1.75) * TILE_LENGTH, (promotingRow + 0.75) * TILE_LENGTH);
        //If the promotion menu will be drawn offscreen, then adjust it
        if(promotingCol == 7) g2d.translate(-TILE_LENGTH * 3.0 / 4, 0);
        if(promotingCol == 0) g2d.translate(TILE_LENGTH * 3.0 / 4, 0);

        //Draw the background color
        g2d.setColor(new Color(192, 192, 192, 210));
        g2d.fillRect(0, 0, TILE_LENGTH * 4 + 25, TILE_LENGTH + 10);

        //Draw the current option hover
        if(promotingHover != -1)
        {
            g2d.setColor(new Color(152, 251, 152, 150));
            g2d.fillRect((5 * (promotingHover + 1)) + promotingHover * TILE_LENGTH, 5, TILE_LENGTH, TILE_LENGTH);
        }

        //Draw the pieces
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

    //Called whenever the mouse is pressed
    public void mousePressed(MouseEvent e)
    {
        //Calculate where the mouse is
        int mouseCol = (e.getX() - x) / TILE_LENGTH;
        int mouseRow = (e.getY() - y) / TILE_LENGTH;

        //Make sure the promotion GUI is not up
        if(!promotingMode)
        {
            if(checkBoardBounds(mouseCol, mouseRow))
            {
                if(!selectMode) handleDragging(mouseCol, mouseRow);
                else if(currentPiece != null) handleSelection(mouseCol, mouseRow);
            }
        }
        else handlePromotion();
    }

    //Handles the dragging option for moving pieces
    private void handleDragging(int mouseCol, int mouseRow)
    {
        //Check if the square has a friendly piece
        if(currentPiece == null && pieces[mouseCol][mouseRow] != null)
        {
            if(isCurrentSide(pieces[mouseCol][mouseRow]))
            {
                //Pick up the piece and remove it
                currentPiece = pieces[mouseCol][mouseRow];
                pieces[mouseCol][mouseRow] = null;

                //Set the current piece draw location to the square
                setCurrentPieceLocation(x + currentPiece.getCol() * TILE_LENGTH + TILE_LENGTH / 2,
                        y + currentPiece.getRow() * TILE_LENGTH + TILE_LENGTH / 2);
            }
        }
    }

    //Handles the select mode for moving pieces
    private void handleSelection(int mouseCol, int mouseRow)
    {
        movePiece(mouseCol, mouseRow);

        currentPiece = null;
        selectMode = false;
    }

    //Handles the promotion menu interaction
    private void handlePromotion()
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
                //If the click was not on the promotion menu, then do not change anything
                return;
        }

        //Add to the moves list
        moves += pieces[promotingCol][promotingRow].getID();
        moves += 1;
        moves += takenPieceHadMoved;

        //Deactivated the mode and switch the turn
        promotingMode = false;
        secondTurn = !secondTurn;
    }

    //Called whenever the mouse is released
    public void mouseReleased(MouseEvent e)
    {
        //Calculate where the mouse is
        int mouseCol = (e.getX() - x) / TILE_LENGTH;
        int mouseRow = (e.getY() - y) / TILE_LENGTH;

        //Make sure that other modes are not active and that there is currently a held piece
        if(!selectMode && !promotingMode && currentPiece != null)
        {
            if(checkBoardBounds(mouseCol, mouseRow))
            {
                //If the piece is not moved from its square, then go into the select mode
                if(mouseCol == currentPiece.getCol() && mouseRow == currentPiece.getRow())
                {
                    selectMode = true;
                    setCurrentPieceLocation(x + currentPiece.getCol() * TILE_LENGTH + TILE_LENGTH / 2,
                            y + currentPiece.getRow() * TILE_LENGTH + TILE_LENGTH / 2);
                    return;
                }

                //Otherwise, move it to the drop zone
                movePiece(mouseCol, mouseRow);
            }
            //If the piece was dropped out of bounds, then move it back to its original position
            else pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;

            currentPiece = null;
        }
    }

    //TODO Break this method into smaller submethods
    //Moves the current piece to the designated col/row
    private void movePiece(int targetCol, int targetRow)
    {
        //Check if the clicked location is a valid location
        currentPiece.calculateMoves(pieces);

        if(currentPiece.isValidMove(targetCol, targetRow))
        {
            //Go through all pieces, and if they are a friendly pawn, disable attack by en passant
            for(Piece[] pieceA : pieces)
            {
                for(Piece piece : pieceA)
                {
                    if(piece instanceof Pawn)
                    {
                        if(isCurrentSide(piece))
                        {
                            ((Pawn) piece).setCanEnPassant(false);
                        }
                    }
                }
            }

            //Make sure that the current piece can no longer be attacked by en passant
            if(currentPiece instanceof Pawn)
            {
                Pawn pawn = (Pawn) currentPiece;
                if(pawn.canEnPassant()) pawn.setCanEnPassant(false);
            }

            //Data for adding to the list of moves
            boolean enPassant = false;
            int castlingRookOriginalCol = -1;
            int castlingRookNewCol = -1;

            //If the current piece is a king and it is moved the distance for a castle
            if(currentPiece instanceof King && Math.abs(targetCol - currentPiece.getCol()) == 2)
            {
                //Grab the rook that the king is being castled with
                Piece rook;
                if(targetCol > currentPiece.getCol()) rook = pieces[7][currentPiece.getRow()];
                else rook = pieces[0][currentPiece.getRow()];

                //Set the move data
                castlingRookOriginalCol = rook.getCol();

                //Move the rook to the new location
                pieces[targetCol - (int) Math.signum(targetCol - currentPiece.getCol())][currentPiece.getRow()] = rook;
                rook.setPosition(targetCol - (int) Math.signum(targetCol - currentPiece.getCol()),
                        currentPiece.getRow());
                rook.setMoved(true);
                pieces[rook.getCol()][rook.getRow()] = null;

                //Set the move data
                castlingRookNewCol = rook.getCol();
            }
            //If the piece is a pawn
            else if(currentPiece instanceof Pawn)
            {
                Pawn pawn = (Pawn) currentPiece;

                //If the pawn just double moved, then make it possible to be attacked by en passant
                if(Math.abs(pawn.getRow() - targetRow) == 2) pawn.setCanEnPassant(true);

                //TODO Make this check not bad and repeating
                if(pawn.getCol() - 1 >= 0)
                {
                    //Check the adjacent piece for a pawn
                    if(pieces[pawn.getCol() - 1][pawn.getRow()] instanceof Pawn)
                    {
                        //Check to make sure that the current piece is performing an passant
                        if(pieces[pawn.getCol() - 1][pawn.getRow()].isSecond() != pawn.isSecond() &&
                                pieces[targetCol][targetRow] == null &&
                                targetCol == pawn.getCol() - 1)
                        {
                            //If it is, then remove the target piece, and set the move data
                            pieces[pawn.getCol() - 1][pawn.getRow()] = null;
                            enPassant = true;
                        }
                    }
                }
                if(pawn.getCol() + 1 < pieces.length)
                {
                    //Check the adjacent piece for a pawn
                    if(pieces[pawn.getCol() + 1][pawn.getRow()] instanceof Pawn)
                    {
                        //Check to make sure that the current piece is performing an passant
                        if(pieces[pawn.getCol() + 1][pawn.getRow()].isSecond() != pawn.isSecond() &&
                                pieces[targetCol][targetRow] == null &&
                                targetCol == pawn.getCol() + 1)
                        {
                            //If it is, then remove the target piece, and set the move data
                            pieces[pawn.getCol() + 1][pawn.getRow()] = null;
                            enPassant = true;
                        }
                    }
                }
            }

            //If there was no castle
            if(castlingRookNewCol == -1)
            {
                if(!enPassant)
                {
                    //Record a normal move
                    moves += currentPiece.getID();
                    moves += currentPiece.getCol() + "" + currentPiece.getRow();
                    moves += targetCol + "" + targetRow;

                    //Add a take to the move data
                    if(pieces[targetCol][targetRow] == null)
                    {
                        moves += "//";
                        takenPieceHadMoved = 0;
                    }
                    else
                    {
                        moves += pieces[targetCol][targetRow].getID();
                        takenPieceHadMoved = pieces[targetCol][targetRow].getMoved() ? 1 : 0;
                    }
                }
                //Add en passant to moves
                else
                {
                    moves += "EN";
                    moves += currentPiece.getCol() + "" + currentPiece.getRow();
                    moves += targetCol + "" + targetRow;
                    moves += targetCol + "" + currentPiece.getRow();
                    moves += "//";
                    moves += 1;
                    moves += 1;
                }
            }
            //Add castle to moves
            else
            {
                moves += "CA";
                moves += currentPiece.getCol() + "" + currentPiece.getRow();
                moves += targetCol + "" + targetRow;
                moves += castlingRookOriginalCol + "" + targetRow;
                moves += castlingRookNewCol + "" + targetRow;
                moves += 0;
                moves += 0;
            }

            //Move the current piece to the location
            pieces[targetCol][targetRow] = currentPiece;
            currentPiece.setPosition(targetCol, targetRow);

            //Get more move data
            int hadMoved = currentPiece.getMoved() ? 1 : 0;
            currentPiece.setMoved(true);

            //Make sure that the move was not another special move, as they already added the full move data
            if(!enPassant && castlingRookNewCol == -1)
            {
                //Check for Pawn promotion
                if(currentPiece instanceof Pawn && ((currentPiece.isSecond() && currentPiece.getRow() == 7) ||
                        (!currentPiece.isSecond() && currentPiece.getRow() == 0)))
                {
                    //Set promotion data (the promotion move data is finished in handlePromotion())
                    promotingMode = true;
                    promotingCol = currentPiece.getCol();
                    promotingRow = currentPiece.getRow();

                    //Make sure the bottom code does not run
                    return;
                }
                else moves += "//";

                //Finish up the move data
                moves += hadMoved;
                moves += takenPieceHadMoved;
            }

            secondTurn = !secondTurn;
        }
        //If the click location is not a valid move, revert the position of the current piece
        else pieces[currentPiece.getCol()][currentPiece.getRow()] = currentPiece;
    }

    //Called whenever the mouse is being held down and moving
    public void mouseDragged(MouseEvent e)
    {
        //Check if the current piece exists, and that other modes are not active
        if(currentPiece != null && !selectMode && !promotingMode)
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

    //Returns the distance squared between two coordinates
    private int distSq(int x1, int y1, int x2, int y2)
    {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    //Called whenever the mouse is moved
    public void mouseMoved(MouseEvent e)
    {
        if(!promotingMode) handleHover(e);
        else handlePromotionMouse(e);

//        if(checkBoardBounds(hoverCol, hoverRow))
//        {
//            if(pieces[hoverCol][hoverRow] != null && pieces[hoverCol][hoverRow] instanceof Pawn)
//            {
//                System.out.println(((Pawn) pieces[hoverCol][hoverRow]).canEnPassant());
//            }
//        }
    }

    //Handles setting the square the mouse is hovering on
    private void handleHover(MouseEvent e)
    {
        int mouseCol = (e.getX() - x) / TILE_LENGTH;
        int mouseRow = (e.getY() - y) / TILE_LENGTH;

        if(checkBoardBounds(mouseCol, mouseRow))
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

    //Handles the promotion menu hovering
    private void handlePromotionMouse(MouseEvent e)
    {
        int relativeY = e.getY() - y - (int) ((promotingRow + 0.75) * TILE_LENGTH);
        int relativeX = e.getX() - x - (int) ((promotingCol - 1.75) * TILE_LENGTH);

        if(promotingCol == 7) relativeX += TILE_LENGTH * 3.0 / 4;
        if(promotingCol == 0) relativeX -= TILE_LENGTH * 3.0 / 4;

        if(relativeX >= 5 && relativeX <= TILE_LENGTH * 4 + 20 && relativeY >= 5 && relativeY <= TILE_LENGTH + 5)
        {
            promotingHover = (relativeX - 5) / (TILE_LENGTH + 5);
        }
        else promotingHover = -1;

        hoverCol = -1;
        hoverRow = -1;
    }

    //Called whenever a key is pressed
    public void keyPressed(KeyEvent e)
    {
        undoLastMove();
    }

    //Sets the current piece's draw location
    private void setCurrentPieceLocation(int x, int y)
    {
        currentPieceX = x - TILE_LENGTH / 2;
        currentPieceY = y - TILE_LENGTH / 2;
    }

    //Returns whether or not the king of a certain side is in check
    public static boolean isCheck(Piece[][] pieces, boolean secondTurn)
    {
        Piece currentTurnKing = getKing(pieces, secondTurn);

        if(currentTurnKing != null)
        {
            //Go through all enemy pieces and check if they are targeting the king
            for(Piece[] pieceA : pieces)
            {
                for(Piece piece : pieceA)
                {
                    if(piece != null)
                    {
                        if(piece.isSecond() != secondTurn)
                        {
                            //Calculate moves unfiltered, so that the piece doesn't circularly call isCheck()
                            piece.calculateMovesUnfiltered(pieces);
                            for(int[] possibleMove : piece.getMoves())
                            {
                                //Check if the move ends at the king
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

    //Returns the king piece for a given side
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

    //Returns all of the possible moves for a given side in a hashmap
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
                        //Get all moves and add them to the hashmap
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

    //Returns whether the given col/row are within the array dimensions
    private boolean checkBoardBounds(int col, int row)
    {
        return col >= 0 && col < pieces.length &&
                row >= 0 && row < pieces[0].length;
    }

    //Returns whether or not the given piece is on the current side
    private boolean isCurrentSide(Piece piece)
    {
        assert(piece != null);
        return piece.isSecond() == secondTurn;
    }

    //Undos the last move performed
    private void undoLastMove()
    {
        //Make sure that the game has not just begun and that a piece is not being promoted
        if(!moves.isEmpty() && !promotingMode)
        {
            //Deactivate the select mode to make sure that errors do not occur do to switching turns
            selectMode = false;

            //Grab the last move from the moves string
            String lastMove = moves.substring(moves.length() - 12, moves.length());
            //Grab the type of move
            String id = lastMove.substring(0, 2);
            switch(id)
            {
                //Castling
                case "CA":
                {
                    undoCastle(lastMove);
                    break;
                }
                //En Passant
                case "EN":
                {
                    undoEnPassant(lastMove);
                    break;
                }
                //Any normal move
                default:
                {
                    undoNormalMove(lastMove);
                    break;
                }
            }

            //Finalize the undo by switching the turn and removing the last move
            secondTurn = !secondTurn;
            moves = moves.substring(0, moves.length() - 12);

            //If a piece was being dragged while the undoMove was called, then cancel the drag and revert the position of the piece
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

    //Undoes the given move, assuming that the given move is a castle
    private void undoCastle(String lastMove)
    {
        int moveData, rookDestRow, rookDestCol, rookSrcRow, rookSrcCol,
            kingDestRow, kingDestCol, kingSrcRow, kingSrcCol;

        //Every single digit is the next piece of data, so grab the last digit and then divide by ten for next
        moveData = Integer.parseInt(lastMove.substring(2, 10));

        rookDestRow = moveData % 10;

        moveData /= 10;
        rookDestCol = moveData % 10;

        moveData /= 10;
        rookSrcRow = moveData % 10;

        moveData /= 10;
        rookSrcCol = moveData % 10;

        moveData /= 10;
        kingDestRow = moveData % 10;

        moveData /= 10;
        kingDestCol = moveData % 10;

        moveData /= 10;
        kingSrcRow = moveData % 10;

        moveData /= 10;
        kingSrcCol = moveData % 10;

        //Move back the rook
        pieces[rookSrcCol][rookSrcRow] = pieces[rookDestCol][rookDestRow];
        pieces[rookSrcCol][rookSrcRow].setPosition(rookSrcCol, rookSrcRow);
        pieces[rookSrcCol][rookSrcRow].setMoved(false);

        //Remove the new rook location
        pieces[rookDestCol][rookDestRow] = null;

        //Move back the king
        pieces[kingSrcCol][kingSrcRow] = pieces[kingDestCol][kingDestRow];
        pieces[kingSrcCol][kingSrcRow].setPosition(kingSrcCol, kingSrcRow);
        pieces[kingSrcCol][kingSrcRow].setMoved(false);

        //Remove the new king location
        pieces[kingDestCol][kingDestRow] = null;
    }

    //Undoes the given move, assuming that the given move is an en passant
    private void undoEnPassant(String lastMove)
    {
        int moveData, takeRow, takeCol, destRow, destCol, srcRow, srcCol;

        //Every single digit is the next piece of data, so grab the last digit and then divide by ten for next
        moveData = Integer.parseInt(lastMove.substring(2, 8));

        takeRow = moveData % 10;

        moveData /= 10;
        takeCol = moveData % 10;

        moveData /= 10;
        destRow = moveData % 10;

        moveData /= 10;
        destCol = moveData % 10;

        moveData /= 10;
        srcRow = moveData % 10;

        moveData /= 10;
        srcCol = moveData % 10;

        //Move back the taking pawn
        pieces[srcCol][srcRow] = pieces[destCol][destRow];
        pieces[srcCol][srcRow].setPosition(srcCol, srcRow);
        pieces[srcCol][srcRow].setMoved(true);

        //Remove the new pawn location
        pieces[destCol][destRow] = null;

        //Restore the taken pawn
        pieces[takeCol][takeRow] = new Pawn(!pieces[srcCol][srcRow].isSecond(), takeCol, takeRow);
        pieces[takeCol][takeRow].setMoved(true);
        ((Pawn) pieces[takeCol][takeRow]).setCanEnPassant(true);
    }

    //Undoes the given move, assuming that the given move is a normal move
    private void undoNormalMove(String lastMove)
    {
        int moveData, destRow, destCol, srcRow, srcCol;

        //Every single digit is the next piece of data, so grab the last digit and then divide by ten for next
        moveData = Integer.parseInt(lastMove.substring(2, 6));

        destRow = moveData % 10;

        moveData /= 10;
        destCol = moveData % 10;

        moveData /= 10;
        srcRow = moveData % 10;

        moveData /= 10;
        srcCol = moveData % 10;

        //Move back the moving piece
        pieces[srcCol][srcRow] = pieces[destCol][destRow];
        pieces[srcCol][srcRow].setPosition(srcCol, srcRow);
        pieces[srcCol][srcRow].setMoved(Integer.parseInt(lastMove.substring(10, 11)) == 1);

        //Check if a replacement piece exists, and if it does, then set it moved according to the given data
        Piece replacePiece = Piece.parseID(lastMove.substring(6, 8), !pieces[srcCol][srcRow].isSecond(), destCol, destRow);
        pieces[destCol][destRow] = replacePiece;
        if(replacePiece != null)
            pieces[destCol][destRow].setMoved(Integer.parseInt(lastMove.substring(11, 12)) == 1);
    }
}
