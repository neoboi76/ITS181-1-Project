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

public class PlayerThread {
	
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
 
        /*
        Platform.runLater(() -> {
    		
        	mediaPlayer.setOnReady(() -> {
            	
        		System.out.println("Nigger");
        		
            	File file = new File(fileLocation);
            	
            	String title = Utils.removeExtension(file.getName());
            	String artist = "Boa";
            	String album = "Based";
            	double duration = 2.25;
            	String lyrics = null;
            	String audioPath = fileLocation;
            	String imagePath = null;
            	
            	System.out.println(audioPath);
            	
            	SongEvent song = new SongEvent(this, title, artist, album, duration, lyrics, audioPath, imagePath);
            	
            	controller.addSong(song); 
            	
            	
            });
    		
    	});*/

    }

    public void play() {
        if (mediaPlayer == null) {
            MediaPlayer();
        }
        mediaPlayer.play();
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
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
