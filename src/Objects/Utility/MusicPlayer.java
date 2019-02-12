package Objects.Utility;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class MusicPlayer {
    private static Media music = new Media(new File("Music/Stellardrone - Breathe In The Light.mp3").toURI().toString());
    private static MediaPlayer mediaPlayer = new MediaPlayer(music);
    static {
        mediaPlayer.setStartTime(Duration.seconds(0));
        mediaPlayer.setStopTime(music.getDuration());
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public static void play() {
        mediaPlayer.play();
    }

    public static void pause() {
        mediaPlayer.pause();
    }
}
