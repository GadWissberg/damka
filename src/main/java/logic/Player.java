package logic;

import view.ScoreChangedSubscriber;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    private final String name;
    private final Color color;
    private int score;
    private ArrayList<ScoreChangedSubscriber> scoreChangedSubscribers = new ArrayList<>();

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
//        this.pawns = new ArrayList<Pawn>; TODO: Waiting for Pawn class.
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

    public void subscribeForScoreChange(ScoreChangedSubscriber subscriber) {
        scoreChangedSubscribers.add(subscriber);
    }

    public Color getColor() {
        return color;
    }
}
