package controller;

import controller.pawn.Pawn;
import interfaces.Controller;
import interfaces.DamkaDisplay;
import model.BoardPixelLocation;
import model.BoardPosition;
import model.EatMove;
import model.Move;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Optional;

import static controller.Board.CELLS_IN_ROW;

public class Session implements Controller {
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
        } else if (selectedPawn != null) moveSelectedPawn(row, column, selectedPawn);
    }

    private void moveSelectedPawn(int row, int column, Pawn selectedPawn) {
        Optional<ArrayList<Move>> moves = Optional.ofNullable(calculateMovesForPawn(selectedPawn));
        moves.ifPresent(m -> {
            Optional<Move> move = m.stream().filter(filterTo -> {
                BoardPosition dst = filterTo.getDestination();
                return dst.getRow() == row && dst.getCol() == column;
            }).findFirst();
            if (move.isPresent()) {
                manageMovingPawn(row, column, selectedPawn);
                if (move.get().getType().equals(Move.MoveType.EAT)) {
                    Pawn pawnToEat = ((EatMove) move.get()).getPawnToEat();
                    board.removePawn(pawnToEat);
                    if (pawnToEat.getPlayer().getColor().equals(Color.RED)) {
                        board.setNumberOfRedPawns(board.getNumOfRedPawns() - 1);
                    } else {
                        board.setNumberOfBluePawns(board.getNumOfBluePawns() - 1);
                    }
                }
            } else {
                handleIllegalMove();
            }
        });
    }

    private ArrayList<Move> calculateMovesForPawn(Pawn selectedPawn) {
        ArrayList<Move> moves = new ArrayList<>();
        calculateMoveForPawn(selectedPawn, 0, -1).ifPresent(moves::add);
        calculateMoveForPawn(selectedPawn, CELLS_IN_ROW - 1, 1).ifPresent(moves::add);
        return moves;
    }

    private Optional<Move> calculateMoveForPawn(Pawn selectedPawn, int boundary, int horizontalDir) {
        Optional<Move> moveForPawn = Optional.empty();
        if (selectedPawn.getPosition().getCol() != boundary) {
            int dirValue = selectedPawn.getPlayer().getDirection().getDirValue();
            moveForPawn = Optional.ofNullable(generateMoveForPawn(selectedPawn, horizontalDir));
        }
        return moveForPawn;
    }

    private Move generateMoveForPawn(Pawn selectedPawn, int directionX) {
        Move result = null;
        BoardPosition position = selectedPawn.getPosition();
        int verticalDir = selectedPawn.getPlayer().getDirection().getDirValue();
        int desiredDstRow = position.getRow() + verticalDir;
        int desiredDstCol = position.getCol() + directionX;
        if (!board.spotOnBoardIsFree(desiredDstRow, desiredDstCol)) {
            Pawn otherPawn = board.getPawnAtPosition(desiredDstRow, desiredDstCol);
            if (otherPawn.getPlayer().equals(selectedPawn.getPlayer().equals(p1) ? p2 : p1)) {
                if (board.spotOnBoardIsFree(desiredDstRow + verticalDir, desiredDstCol + directionX)) {
                    BoardPosition destination = new BoardPosition(desiredDstRow + verticalDir, desiredDstCol + directionX);
                    result = new EatMove(destination, otherPawn);
                }
            }
        } else {
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
        turn = turn == p1 ? p2 : p1;
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
}
