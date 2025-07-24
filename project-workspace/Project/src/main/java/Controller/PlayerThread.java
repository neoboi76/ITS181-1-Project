package Controller;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.List;

import Controller.MusicController;
import Model.SongEntity;
import View.ControlPanel;
import View.SongEvent;
import View.Utils;
import View.TablePanel;

public class PlayerThread{
	
    private MediaPlayer mediaPlayer;
    private String fileLocation;
    private boolean loop;
    private MusicController controller;
    private ControlPanel controlPanel;
    private TablePanel tablePanel;  
    
    public PlayerThread(String fileLocation, boolean loop) {
        this.fileLocation = fileLocation;
        this.loop = loop;
        MediaPlayer();
    }

    private void MediaPlayer() {
        Media media = new Media(fileLocation);
        mediaPlayer = new MediaPlayer(media);
        controller = new MusicController();

        if (loop) {
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.play();
            });
            
        }
        
        else {
        	mediaPlayer.stop();
        }
 
        
    }

    public void play() {
        mediaPlayer.play();
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }


    public void stopPlayback() {
        if (mediaPlayer != null) {
            new Thread(() -> {
                for (double vol = mediaPlayer.getVolume(); vol > 0; vol -= 0.1) {
                    double v = vol;
                    Platform.runLater(() -> mediaPlayer.setVolume(v));
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {}
                }
                Platform.runLater(() -> {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                    mediaPlayer = null;
                });
            }).start();
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
