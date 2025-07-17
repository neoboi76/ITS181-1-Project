package controller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class PlayerThread {
	
	  static {
	    	new javafx.embed.swing.JFXPanel(); // Ensures JavaFX runtime is started exactly once
	    }


    private MediaPlayer mediaPlayer;
    private String fileLocation;
    private boolean loop;
    
    public PlayerThread(String fileLocation, boolean loop) {
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
            mediaPlayer.play();
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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
