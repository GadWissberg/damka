package controller;

import controller.pawn.Pawn;
import controller.Board;
import interfaces.Controller;
import interfaces.DamkaDisplay;
import model.BoardPixelLocation;
import model.BoardPosition;
import model.EatMove;
import model.Move;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Optional;

import static controller.Board.CELLS_IN_ROW;

public class Session implements Controller, PropertyChangeListener {
    private static final String MSG_ILLEGAL_MOVE = "You cannot move this pawn over there!";
    private Player p1;
    private Player p2;
    private Board board = new Board();
    private Player turn;
    private Rectangle cellSize = new Rectangle();
    private ArrayList<DamkaDisplay> displays = new ArrayList<>();

    public void initialize(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        turn = Math.random() > 0.5f ? p1 : p2;
        board.fillBoard(p1, p2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = (int) (e.getY() / cellSize.getWidth());
        int column = (int) (e.getX() / cellSize.getHeight());
        Pawn selectedPawn = board.getSelectedPawn();
        Optional<Pawn> pawnAtPosition = Optional.ofNullable(board.getPawnAtPosition(row, column));
        if (pawnAtPosition.isPresent()) {
            if (pawnAtPosition.get().getPlayer().equals(turn)) tryToSelectPawn(pawnAtPosition.get());
        } else if (selectedPawn != null) {
            boolean moved = moveSelectedPawn(row, column, selectedPawn, calculateMovesForPawn(selectedPawn, false));
            if (moved) {
                turn = turn == p1 ? p2 : p1;
                requestToRefreshDisplay();
            }
        }
    }

    private boolean moveSelectedPawn(int row, int column, Pawn selectedPawn, ArrayList<Move> availableMoves) {
        Optional<ArrayList<Move>> moves = Optional.ofNullable(availableMoves);
        if (moves.isPresent()) {
            Optional<Move> move = moves.get().stream().filter(filterTo -> {
                BoardPosition dst = filterTo.getDestination();
                return dst.getRow() == row && dst.getCol() == column;
            }).findFirst();
            if (move.isPresent()) {
                manageMovingPawn(row, column, selectedPawn);
                if (move.get().getType().equals(Move.MoveType.EAT)) {
                    performEat(selectedPawn, move);
                }
                return true;
            } else {
                handleIllegalMove();
            }
        }
        return false;
    }

    private void performEat(Pawn selectedPawn, Optional<Move> move) {
        Pawn pawnToEat = ((EatMove) move.get()).getPawnToEat();
        board.removePawn(pawnToEat);
        if (pawnToEat.getPlayer().getColor().equals(Color.RED)) {
            board.setNumberOfRedPawns(board.getNumOfRedPawns() - 1);
        } else {
            board.setNumberOfBluePawns(board.getNumOfBluePawns() - 1);
        }
        ArrayList<Move> eatMoves = calculateMovesForPawn(selectedPawn, true);
        if (!eatMoves.isEmpty()) {
            BoardPosition destination = eatMoves.get(0).getDestination();
            moveSelectedPawn(destination.getRow(), destination.getCol(), selectedPawn, eatMoves);
        }
    }

    private ArrayList<Move> calculateMovesForPawn(Pawn selectedPawn) {
        return calculateMovesForPawn(selectedPawn, false);
    }

    private ArrayList<Move> calculateMovesForPawn(Pawn selectedPawn, boolean eatOnly) {
        ArrayList<Move> moves = new ArrayList<>();
        calculateMoveForPawn(selectedPawn, 0, -1, eatOnly, selectedPawn.getPlayer().getDirection().getDirValue()).ifPresent(moves::add);
        calculateMoveForPawn(selectedPawn, CELLS_IN_ROW - 1, 1, eatOnly, selectedPawn.getPlayer().getDirection().getDirValue()).ifPresent(moves::add);
        if (eatOnly) {
            calculateMoveForPawn(selectedPawn, 0, -1, true, -1 * selectedPawn.getPlayer().getDirection().getDirValue()).ifPresent(moves::add);
            calculateMoveForPawn(selectedPawn, CELLS_IN_ROW - 1, 1, true, -1 * selectedPawn.getPlayer().getDirection().getDirValue()).ifPresent(moves::add);
        }
        return moves;
    }

    private Optional<Move> calculateMoveForPawn(Pawn selectedPawn, int boundary, int horizontalDir, boolean eatOnly, int verticalDir) {
        Optional<Move> moveForPawn = Optional.empty();
        if (selectedPawn.getPosition().getCol() != boundary) {
            moveForPawn = Optional.ofNullable(generateMoveForPawn(selectedPawn, horizontalDir, eatOnly, verticalDir));
        }
        return moveForPawn;
    }

    private Move generateMoveForPawn(Pawn selectedPawn, int directionX, boolean eatOnly, int verticalDir) {
        Move result = null;
        BoardPosition position = selectedPawn.getPosition();
        int desiredDstRow = position.getRow() + verticalDir;
        int desiredDstCol = position.getCol() + directionX;
        if (!board.spotOnBoardIsFree(desiredDstRow, desiredDstCol)) {
            Optional<Pawn> otherPawn = Optional.ofNullable(board.getPawnAtPosition(desiredDstRow, desiredDstCol));
            if (otherPawn.isPresent() && otherPawn.get().getPlayer().equals(selectedPawn.getPlayer().equals(p1) ? p2 : p1)) {
                if (board.spotOnBoardIsFree(desiredDstRow + verticalDir, desiredDstCol + directionX)) {
                    BoardPosition destination = new BoardPosition(desiredDstRow + verticalDir, desiredDstCol + directionX);
                    result = new EatMove(destination, otherPawn.get());
                }
            }
        } else if (!eatOnly) {
            BoardPosition destination = new BoardPosition(desiredDstRow, desiredDstCol);
            result = new Move(destination);
        }
        return result;
    }

    private void handleIllegalMove() {
        displays.forEach(damkaDisplay -> {
            damkaDisplay.refreshDisplay();
            damkaDisplay.displayMessage(MSG_ILLEGAL_MOVE);
        });
    }

    private void manageMovingPawn(int row, int column, Pawn selectedPawn) {
        board.movePawn(selectedPawn, row, column);
        board.setSelectedPawn(null);
        requestToRefreshDisplay();
    }

    private void requestToRefreshDisplay() {
        displays.forEach(damkaDisplay -> {
            damkaDisplay.refreshDisplay();
            damkaDisplay.setSelectionImageVisibility(false);
            damkaDisplay.setAvailableMovesLocations(null);
        });
    }

    private void tryToSelectPawn(Pawn pawnAtPosition) {
        if (pawnAtPosition != null && board.getSelectedPawn() != pawnAtPosition && pawnAtPosition.getPlayer() == turn) {
            board.setSelectedPawn(pawnAtPosition);
            displays.forEach(subscriber -> {
                BoardPosition pawnPosition = pawnAtPosition.getPosition();
                double x = pawnPosition.getCol() * cellSize.getWidth();
                double y = pawnPosition.getRow() * cellSize.getHeight();
                subscriber.setSelectionImage(x, y);
            });
            requestToDrawAvailableMoves();
        }
    }

    private void requestToDrawAvailableMoves() {
        ArrayList<Move> availableMoves = calculateMovesForPawn(board.getSelectedPawn());
        if (availableMoves != null) {
            ArrayList<BoardPixelLocation> boardPixelLocations = new ArrayList<>();
            availableMoves.forEach(move -> {
                double x = move.getDestination().getCol() * cellSize.width;
                double y = move.getDestination().getRow() * cellSize.height;
                BoardPixelLocation location = new BoardPixelLocation(x, y);
                boardPixelLocations.add(location);
            });
            displays.forEach(subscriber -> subscriber.setAvailableMovesLocations(boardPixelLocations));
        }
    }

    private boolean isGameOver() {
      if( board.getNumOfBluePawns() == 0)
          return true;
      if( board.getNumOfRedPawns() == 0)
          return true;
      return false;
    }


        @Override
    public void mousePressed(MouseEvent e) {
//Nothing yet...
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//Nothing yet...
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//Nothing yet...
    }

    @Override
    public void mouseExited(MouseEvent e) {
//Nothing yet...
    }

    public Player getPlayer1() {
        return p1;
    }

    public Player getPlayer2() {
        return p2;
    }

    public Player getCurrentTurn() {
        return turn;
    }

    public Board getBoard() {
        return board;
    }

//    private ArrayList<Move> canMoveTo() {
//        Pawn p;
//        p = this.board.getSelectedPawn();
//        if (p == null) // no pawn selected
//            return null;
//
//        ArrayList<Move> arr = new ArrayList<>();
//        BoardPosition pawnPos = p.getPosition();
//        int r, c;
//
//        int pCol = pawnPos.getCol();
//        int pRow = pawnPos.getRow();
//        if (p.getPlayer() == p1 && pRow + 1 <= board.CELLS_IN_ROW) { // controller for player 1
//            if (pCol - 1 >= 0) { // left move in bounds
//                p = board.getPawnAtPosition(pRow + 1, pCol - 1);
//                if (p == null) // no player, move freely
//                    arr.add(new Move(new BoardPosition(pRow + 1, pCol - 1), Move.MoveType.REGULAR));
//                else if (p.getPlayer() == p2 && pRow + 2 <= board.CELLS_IN_ROW && pCol - 2 >= 0) // can eat
//                    arr.add(new Move(new BoardPosition(pRow + 2, pCol - 2), Move.MoveType.EAT));
//            }
//
//            if (pCol + 1 <= board.CELLS_IN_ROW) { // right move in bounds
//                p = board.getPawnAtPosition(pRow + 1, pCol + 1);
//                if (p == null) // move freely
//                    arr.add(new Move(new BoardPosition(pRow + 1, pCol + 1), Move.MoveType.REGULAR));
//                else if (p.getPlayer() == p2 && pRow + 2 <= board.CELLS_IN_ROW && pCol + 2 <= board.CELLS_IN_ROW) // can eat
//                    arr.add(new Move(new BoardPosition(pRow + 2, pCol + 2), Move.MoveType.EAT));
//            }
//        } else if (pRow - 1 >= 0) { // controller for player 2
//            if (pCol - 1 >= 0) { // left move in bounds
//                p = board.getPawnAtPosition(pRow - 1, pCol - 1);
//                if (p == null) // no player, move freely
//                    arr.add(new Move(new BoardPosition(pRow - 1, pCol - 1), Move.MoveType.REGULAR));
//                else if (p.getPlayer() == p1 && pRow - 2 >= 0 && pCol - 2 >= 0) // can eat
//                    arr.add(new Move(new BoardPosition(pRow - 2, pCol - 2), Move.MoveType.EAT));
//            }
//
//            if (pCol + 1 <= board.CELLS_IN_ROW) { // right move in bounds
//                p = board.getPawnAtPosition(pRow - 1, pCol + 1);
//                if (p == null) // move freely
//                    arr.add(new Move(new BoardPosition(pRow - 1, pCol + 1), Move.MoveType.REGULAR));
//                else if (p.getPlayer() == p1 && pRow - 2 >= 0 && pCol + 2 <= board.CELLS_IN_ROW) // can eat
//                    arr.add(new Move(new BoardPosition(pRow - 2, pCol + 2), Move.MoveType.EAT));
//            }
//        }
//
//        return arr;
//    }

    @Override
    public void setCellSize(int width, int height) {
        cellSize.setSize(width, height);
    }

    @Override
    public void subscribeForOutput(DamkaDisplay subscriber) {
        if (!displays.contains(subscriber)) {
            displays.add(subscriber);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        //TODO restart variables after restart emit
        board.resetBoard(p1, p2);
        requestToRefreshDisplay();
        turn = Math.random() > 0.5f ? p1 : p2;
    }
}
