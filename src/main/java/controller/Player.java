package controller;

import view.ScoreChangedSubscriber;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    private final String name;
    private final Color color;
    private final Direction direction;
    private int score;
    private ArrayList<ScoreChangedSubscriber> scoreChangedSubscribers = new ArrayList<>();
    private int moves;

    public enum Direction {
        DOWN(1), UP(-1);

        private final int dirValue;

        Direction(int direction) {
            this.dirValue = direction;
        }

        public int getDirValue() {
            return dirValue;
        }

    }

    public Player(String name, Color color, Direction dir) {
        this.name = name;
        this.color = color;
        this.direction = dir;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        if (score != this.score) {
            this.score = score;
            scoreChangedSubscribers.forEach(sub -> sub.scoreChanged(score));
        }
    }

    public String toString() {
        return this.getColor() == Color.RED ? "R" : "B";
    }

    public int getScore() {
        return score;
    }

    public int getMoves() { return moves; }

    public void increaseMoves() {
        moves++;
    }

    public void subscribeForScoreChange(ScoreChangedSubscriber subscriber) {
        scoreChangedSubscribers.add(subscriber);
    }

    public Color getColor() {
        return color;
    }

    public Direction getDirection() {
        return direction;
    }
}
