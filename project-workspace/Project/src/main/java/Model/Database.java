package Model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaPlayer;
import Model.SongEntity;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import javafx.application.Platform;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javafx.embed.swing.JFXPanel;
import java.util.logging.Level;
import View.Utils;

public class Database {
	
	
	private EntityManagerFactory emf;
    private EntityManager em;
    
    private ArrayList<SongEntity> songs;
     

    
    public Database() {
    	emf = Persistence.createEntityManagerFactory("musicdata");
    	em = emf.createEntityManager();
    	songs = new ArrayList<>();
    }
    
    public List<SongEntity> getAllSongs() {
    	return em.createQuery("SELECT s FROM SongEntity s", SongEntity.class).getResultList();
    }
    
    public void addSong(SongEntity music) {
    	
    	em.getTransaction().begin();
    	SongEntity dbSong = new SongEntity();
    	dbSong.setTitle(music.getTitle());
    	dbSong.setLyrics(music.getLyrics());
    	dbSong.setAudioPath(music.getAudioPath());
    	dbSong.setImagePath(music.getImagePath());
    	
    	if (dbSong.getAlbum() == null || dbSong.getAlbum().trim().isEmpty()) {
    	    dbSong.setAlbum("Unknown Album");
    	}
    	if (dbSong.getArtist() == null || dbSong.getArtist().trim().isEmpty()) {
    	    dbSong.setArtist("Unknown Artist");
    	}
    	if (dbSong.getTitle() == null || dbSong.getTitle().trim().isEmpty()) {
    	    dbSong.setTitle("Untitled");
    	}
    	//if (dbSong.getAudioPath() == null || dbSong.getAudioPath().trim().isEmpty()) {
    	//    dbSong.setAudioPath("UnknownPath.mp3"); // You can change this to whatever makes sense
    	//}
    	if (dbSong.getDuration() <= 0.0) {
    	    dbSong.setDuration(0.0); // Default duration if invalid
    	}
    	if (dbSong.getLyrics() == null || dbSong.getLyrics().trim().isEmpty()) {
    	    dbSong.setLyrics("No lyrics available.");
    	}
    	if (dbSong.getImagePath() == null || dbSong.getImagePath().trim().isEmpty()) {
    	    dbSong.setImagePath("default.jpg"); // Replace with your actual default image filename
    	}

    	
    	em.persist(dbSong);
    	em.getTransaction().commit();
    	
    }
    
    public void deleteSong(Long id) {
    	
    	em.getTransaction().begin();
    	SongEntity song = em.find(SongEntity.class, id);
    	if (song != null) {
    		em.remove(song);
    	}
    	
    	em.getTransaction().commit();
    	
    }
    
    public void saveSongMp3(File file) {
    	
    	
  
    	Platform.runLater(() -> {
    		
    		try {
    	        // Step 1: Prepare Media and get metadata
    	        Media media = new Media(file.toURI().toString());
    	         	       
    	        	
    	        MediaPlayer mediaPlayer = new MediaPlayer(media);
    	        
    	        media.setOnError(() -> {
    	            System.out.println("Media error: " + media.getError().getMessage());
    	            media.getError().printStackTrace();
    	        });

    	        mediaPlayer.setOnError(() -> {
    	            System.out.println("MediaPlayer error: " + mediaPlayer.getError().getMessage());
    	            mediaPlayer.getError().printStackTrace();
    	        });
    	        
    	        mediaPlayer.setOnReady(() -> {
    	        	
    	        	System.out.println("CX");
    	        	
    	            String title = Utils.getMeta(media, "title");
    	            String artist = Utils.getMeta(media, "artist");
    	            String album = Utils.getMeta(media, "album");
    	            double duration = media.getDuration().toSeconds();
    	            String audioPath = file.getAbsolutePath();
    	            System.out.println(audioPath);

    	            // Step 2: Load lyrics
    	            String baseName = Utils.removeExtension(file.getName());
    	            File lyricsFile = new File(file.getParent(), baseName + ".txt");
    	            String lyrics = "";
    	            try {
    	                if (lyricsFile.exists()) {
    	                    lyrics = Files.readString(lyricsFile.toPath(), StandardCharsets.UTF_8);
    	                }
    	            } catch (IOException e) {
    	                e.printStackTrace();
    	            }

    	            // Step 3: Load image path
    	            File imageFile = new File(file.getParent(), baseName + ".jpg");
    	            if (!imageFile.exists()) {
    	                imageFile = new File(file.getParent(), baseName + ".png");
    	            }
    	            String imagePath = imageFile.exists() ? imageFile.getAbsolutePath() : null;

    	            // Step 4: Add new song to list
    	            SongEntity song = new SongEntity(title, artist, album, duration, lyrics, imagePath, audioPath);
    	            songs.add(song);

    	            // Step 5: Save songs list to file
    	            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
    	                SongEntity[] music = songs.toArray(new SongEntity[0]);
    	                oos.writeObject(music);
    	            } catch (IOException ex) {
    	                ex.printStackTrace();
    	            }
    	        });

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	
    	});
    	

    }
    
    public void loadSongMp3(File file) {

    	Platform.runLater(() -> {
    		
    		try {
        		Media media = new Media(file.toURI().toASCIIString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                String audioPath = file.getAbsolutePath(); 
                System.out.println("Ok");
                
                mediaPlayer.setOnReady(() -> {
                    System.out.println("CX");

                    /*
                    String title = Utils.getMeta(media, "title");
                    String artist = Utils.getMeta(media, "artist");
                    String album = Utils.getMeta(media, "album");
                    double duration = media.getDuration().toSeconds();*/
                    
                    String title = "Duvet";
                    String artist = "Boa";
                    String album = Utils.getMeta(media, "artist");
                    double duration = media.getDuration().toSeconds();
                    String lyrics = null;
                    String imagePath = null;

                    System.out.println(audioPath);

                    SongEntity song = new SongEntity(title, artist, album, duration, lyrics, audioPath, imagePath);

                    addSong(song);
                    
                    songs.add(song);
                   
                });

                
            } catch (Exception e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Could not load MP3");
                errorAlert.setContentText("Error reading MP3 file.");
                errorAlert.showAndWait();
            }
    	});

   
    }
    
    public void loadSongDatabase(File safFile) {
        try (FileInputStream fis = new FileInputStream(safFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            SongEntity[] music = (SongEntity[]) ois.readObject();
            songs.clear();
            songs.addAll(Arrays.asList(music));
            for (SongEntity s : songs) {
                addSong(s); // persist the loaded songs
            }

            songs.clear();
            songs.addAll(Arrays.asList(music));

            System.out.println("Loaded " + music.length + " songs from database.");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Load Error");
            errorAlert.setHeaderText("Failed to Load Song Database");
            errorAlert.setContentText("Could not read the .saf file.");
            errorAlert.showAndWait();
        }
    }

    public void saveSongDatabase(File safFile) {
        try (FileOutputStream fos = new FileOutputStream(safFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            // Convert your song list to array and write it
            SongEntity[] music = songs.toArray(new SongEntity[0]);
            oos.writeObject(music);

            System.out.println("Saved " + music.length + " songs to database.");

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Save Successful");
            successAlert.setHeaderText("Song database saved");
            successAlert.setContentText("File: " + safFile.getName());
            successAlert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Save Error");
            errorAlert.setHeaderText("Failed to Save Song Database");
            errorAlert.setContentText("Could not write to the .saf file.");
            errorAlert.showAndWait();
        }
    }

    public SongEntity getSongById(Long id) {
    	return em.find(SongEntity.class, id);
    }
	
    
}


