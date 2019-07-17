package controller;

import interfaces.DamkaDisplay;
import interfaces.ViewListener;
import model.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static controller.Board.CELLS_IN_ROW;

public class Session implements ViewListener, PropertyChangeListener {
    private static final String MSG_ILLEGAL_MOVE = "You cannot move this pawn over there!";
    private static final String MSG_WIN = "%s wins!";
    private static final String MSG_FAILED_TO_SAVE = "Failed to save!";
    private static final String MSG_SAVED = "Game saved!";
    private Player p1;
    private Player p2;
    private Board board = new Board();
    private Player turn;
    private Rectangle cellSize = new Rectangle();
    private ArrayList<DamkaDisplay> listeners = new ArrayList<>();
    private SoundPlayer soundPlayer = new SoundPlayer();
    private JsonHandler jsonHandler;
    private int gamesWonOne, gamesWonTwo;

    public void initialize(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        turn = Math.random() > 0.5f ? p1 : p2;
        board.fillBoard(p1, p2);
        jsonHandler = new JsonHandler();
        updatePreviousGamesWon();
    }

    private void updatePreviousGamesWon() {
        try {
            this.gamesWonOne = jsonHandler.getScoreOne();
            this.gamesWonTwo = jsonHandler.getScoreTwo();
        } catch (FileNotFoundException e) {
            this.gamesWonOne = 0;
            this.gamesWonTwo = 0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = (int) (e.getY() / cellSize.getWidth());
        int column = (int) (e.getX() / cellSize.getHeight());
        Pawn selectedPawn = board.getSelectedPawn();
        Optional<Pawn> pawnAtPosition = Optional.ofNullable(board.getPawnAtPosition(row, column));
        if (pawnAtPosition.isPresent()) {
            if (pawnAtPosition.get().getPlayer().equals(turn)) {
                tryToSelectPawn(pawnAtPosition.get());
            }
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
                // increase moves counter
                selectedPawn.getPlayer().increaseMoves();
                soundPlayer.playSound(SoundPlayer.Sound.MOVE);
                return true;
            } else {
                handleIllegalMove();
            }
        }
        return false;
    }

    private void performEat(Pawn selectedPawn, Optional<Move> move) {
        soundPlayer.playSound(SoundPlayer.Sound.EAT);
        Pawn pawnToEat = ((EatMove) move.get()).getPawnToEat();
        board.removePawn(pawnToEat);
        Player player = selectedPawn.getPlayer();
        player.setScore(player.getScore() + 1);
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
        if (isGameOver()) {
            listeners.forEach(damkaDisplay -> {
                damkaDisplay.refreshDisplay();
                updateTotalGamesScore(player.getName());
                damkaDisplay.displayMessage(String.format(MSG_WIN, player.getName()));
                try {
                    jsonHandler.saveSessionData(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            try {
                jsonHandler.saveSessionData(this);
                restartSession();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateTotalGamesScore(String name) {
        if (name.equals(p1.getName()))
            gamesWonOne++;
        else
            gamesWonTwo++;

    }

    private ArrayList<Move> calculateMovesForPawn(Pawn selectedPawn) {
        return calculateMovesForPawn(selectedPawn, false);
    }

    private ArrayList<Move> calculateMovesForPawn(Pawn selectedPawn, boolean eatOnly) {
        ArrayList<Move> moves = new ArrayList<>();
        int verticalDir = selectedPawn.getPlayer().getDirection().getDirValue();
        calculateRoadForPawn(selectedPawn, eatOnly, moves, verticalDir, selectedPawn.isQueen() ? Integer.MAX_VALUE : 1, -1);
        calculateRoadForPawn(selectedPawn, eatOnly, moves, verticalDir, selectedPawn.isQueen() ? Integer.MAX_VALUE : 1, 1);
        if (eatOnly || selectedPawn.isQueen()) {
            calculateRoadForPawn(selectedPawn, eatOnly, moves, -1 * verticalDir, Integer.MAX_VALUE, -1);
            calculateRoadForPawn(selectedPawn, eatOnly, moves, -1 * verticalDir, Integer.MAX_VALUE, 1);
        }
        return moves;
    }

    private void calculateRoadForPawn(Pawn selectedPawn, boolean eatOnly, ArrayList<Move> moves, int verticalDir,
                                      double maxSteps, int horizontalDir) {
        BoardPosition pawnPosition = selectedPawn.getPosition();
        BoardPosition iterationPos = new BoardPosition(pawnPosition.getRow(), pawnPosition.getCol());
        while ((iterationPos.getRow() == pawnPosition.getRow() && iterationPos.getCol() == pawnPosition.getCol())
                || board.spotOnBoardIsFree(iterationPos.getRow(), iterationPos.getCol()) && maxSteps > 0) {
            calculateMoveForPawn(selectedPawn, iterationPos, horizontalDir == 1 ? CELLS_IN_ROW - 1 : 0, horizontalDir,
                    eatOnly, verticalDir).ifPresent(moves::add);
            iterationPos.set(iterationPos.getRow() + verticalDir, iterationPos.getCol() + horizontalDir);
            maxSteps--;
        }
    }

    private Optional<Move> calculateMoveForPawn(Pawn selectedPawn, BoardPosition iterationPos, int boundary,
                                                int horizontalDir, boolean eatOnly, int verticalDir) {
        Optional<Move> moveForPawn = Optional.empty();
        if (iterationPos.getCol() != boundary) {
            moveForPawn = Optional.ofNullable(generateMoveForPawn(selectedPawn, iterationPos, horizontalDir, eatOnly, verticalDir));
        }
        return moveForPawn;
    }

    private Move generateMoveForPawn(Pawn selectedPawn, BoardPosition iterationPos, int directionX, boolean eatOnly, int verticalDir) {
        Move result = null;
        int desiredDstRow = iterationPos.getRow() + verticalDir;
        int desiredDstCol = iterationPos.getCol() + directionX;
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
        listeners.forEach(damkaDisplay -> {
            damkaDisplay.refreshDisplay();
            damkaDisplay.displayMessage(MSG_ILLEGAL_MOVE);
        });
    }

    private void manageMovingPawn(int destinationRow, int destinationColumn, Pawn selectedPawn) {
        makeQueenIfNeeded(destinationRow, selectedPawn);
        board.movePawn(selectedPawn, destinationRow, destinationColumn);
        board.setSelectedPawn(null);
        requestToRefreshDisplay();
    }

    private void makeQueenIfNeeded(int destinationRow, Pawn selectedPawn) {
        Player.Direction direction = selectedPawn.getPlayer().getDirection();
        boolean ifRedReachedBottom = direction.equals(Player.Direction.DOWN) && destinationRow == CELLS_IN_ROW - 1;
        boolean ifBlueReachedTop = direction.equals(Player.Direction.UP) && destinationRow == 0;
        if (!selectedPawn.isQueen() && (ifRedReachedBottom || ifBlueReachedTop)) {
            selectedPawn.setQueen(true);
            soundPlayer.playSound(SoundPlayer.Sound.QUEEN);
        }
    }

    private void requestToRefreshDisplay() {
        listeners.forEach(damkaDisplay -> {
            damkaDisplay.refreshDisplay();
            damkaDisplay.setSelectionImageVisibility(false);
            damkaDisplay.setAvailableMovesLocations(null);
        });
    }

    private void tryToSelectPawn(Pawn pawnAtPosition) {
        if (pawnAtPosition != null && board.getSelectedPawn() != pawnAtPosition && pawnAtPosition.getPlayer() == turn) {
            soundPlayer.playSound(SoundPlayer.Sound.CLICK);
            board.setSelectedPawn(pawnAtPosition);
            listeners.forEach(subscriber -> {
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
            listeners.forEach(subscriber -> subscriber.setAvailableMovesLocations(boardPixelLocations));
        }
    }

    private boolean isGameOver() {
        if (board.getNumOfBluePawns() == 0)
            return true;
        if (board.getNumOfRedPawns() == 0)
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

    public int getGamesWonOne() {
        return gamesWonOne;
    }

    public int getGamesWonTwo() {
        return gamesWonTwo;
    }

    @Override
    public void setCellSize(int width, int height) {
        cellSize.setSize(width, height);
    }

    @Override
    public void subscribeForOutput(DamkaDisplay subscriber) {
        if (!listeners.contains(subscriber)) {
            listeners.add(subscriber);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("Restart button")) {
            restartSession();
        } else if (event.getPropertyName().equals("Save Game")) {
            try {
                jsonHandler.saveSessionData(this);
            } catch (IOException e) {
                e.printStackTrace();
                listeners.forEach(damkaDisplay -> {
                    damkaDisplay.refreshDisplay();
                    damkaDisplay.displayMessage(MSG_FAILED_TO_SAVE);
                });
            }
            listeners.forEach(damkaDisplay -> {
                damkaDisplay.refreshDisplay();
                damkaDisplay.displayMessage(MSG_SAVED);
            });
        }

    }

    private void restartSession() {
        p1.reset();
        p2.reset();
        board.resetBoard(p1, p2);
        requestToRefreshDisplay();
        turn = Math.random() > 0.5f ? p1 : p2;
    }
}
