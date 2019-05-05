package view;

import logic.Player;

public class ScoreLabel extends DamkaLabel implements ScoreChangedSubscriber {
    public static final String PLAYER_SCORE = "%s score: %d";
    private final Player player;

    ScoreLabel(Player player) {
        super(String.format(PLAYER_SCORE, player.getName(), 0),player.getColor());
        this.player = player;
        player.subscribeForScoreChange(this);
    }

    @Override
    public void scoreChanged(int score) {
        setText(String.format(PLAYER_SCORE, player.getName(), score));
    }
}
