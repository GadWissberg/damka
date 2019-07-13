package controller;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private static final String SOUNDS_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator;

    public void playSound(Sound sound) {
        String soundName = sound.name() + ".wav";
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SOUNDS_PATH + soundName.toLowerCase()).getAbsoluteFile());
            Clip clip = null;
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Sound {CLICK, EAT, MOVE, QUEEN}
}
