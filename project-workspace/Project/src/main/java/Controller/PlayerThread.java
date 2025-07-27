package Controller;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */

//Manages music playback. Uses JavaFX

public class PlayerThread {
	
    private MediaPlayer mediaPlayer;
    private String fileLocation;
    private boolean loop;  
    private Media media;
    
    public PlayerThread(String fileLocation, boolean loop) {
        this.fileLocation = fileLocation;
        this.loop = loop;
        MediaPlayer();
    }

    private void MediaPlayer() {
        Media media = new Media(fileLocation);
        mediaPlayer = new MediaPlayer(media);

        //If looping, play at the song's end
        if (loop) {
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.play();
            });
            
        }
        
        //else stop it and return slider to beginning
        else {
        	mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.stop();
            });
        }
 
        
    }

    //Plays a song
    public void play() {
    	 if (mediaPlayer == null) {
             MediaPlayer();
         }
         mediaPlayer.play();
    }

    //Pauses a song
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    
    //Stops a song
    public void stop() {
    	mediaPlayer.stop();
    }

    //Force stops JavaFx and fades the song away
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
                    try {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();        
                            mediaPlayer.dispose();    
                            mediaPlayer = null;        
                        }

                        if (media != null) {
                            media = null;        
                        }

                        System.gc(); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }).start();
        }
    }

    //Sets song volume
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    //Returns instance of MediaPlayer
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    

}
