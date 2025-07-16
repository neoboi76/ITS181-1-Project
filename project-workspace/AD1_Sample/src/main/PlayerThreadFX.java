package main;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class PlayerThreadFX {

    private MediaPlayer mediaPlayer;
    private String fileLocation;
    private boolean loop;

    public PlayerThreadFX(String fileLocation, boolean loop) {
        this.fileLocation = fileLocation;
        this.loop = loop;
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        Media media = new Media(new File(fileLocation).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        if (loop) {
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.play();
            });
        }
    }

    public void play() {
        if (mediaPlayer == null) {
            initMediaPlayer();
        }
        mediaPlayer.play();
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.play(); // continues from paused position
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

}
