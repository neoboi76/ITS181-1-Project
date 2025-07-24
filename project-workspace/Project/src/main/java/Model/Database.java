package Model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaPlayer;
import Model.SongEntity;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javafx.embed.swing.JFXPanel;
import java.util.logging.Level;

import View.SongEvent;
import View.Utils;
import Controller.MusicController;
import Controller.PlayerThread;
import com.mpatric.mp3agic.*;

public class Database {
	
	
	private EntityManagerFactory emf;
    private EntityManager em;
    private MusicController controller;
    
    private List<SongEntity> music;
    
    public Database() {
    	emf = Persistence.createEntityManagerFactory("musicdata");
    	em = emf.createEntityManager();
    }
    
    public List<SongEntity> getAllSongs() {
    	return em.createQuery("SELECT s FROM SongEntity s", SongEntity.class).getResultList();
    }
    
    public void addSong(SongEntity music) {
    	
    	em.getTransaction().begin();
    	SongEntity dbSong = new SongEntity();
    	dbSong.setTitle(music.getTitle());
    	dbSong.setArtist(music.getArtist());
    	dbSong.setAlbum(music.getAlbum());
    	dbSong.setDuration(music.getDuration());
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

    	String duration = dbSong.getDuration();
    	if (duration == null || duration.isBlank() || duration.equals("0:00")) {
    	    dbSong.setDuration("0:00"); 
    	}

    	if (dbSong.getLyrics() == null || dbSong.getLyrics().trim().isEmpty()) {
    	    dbSong.setLyrics("No lyrics available.");
    	}
    	if (dbSong.getImagePath() == null || dbSong.getImagePath().trim().isEmpty()) {
    	    dbSong.setImagePath("default.jpg"); 
    	}

    	
    	em.persist(dbSong);
    	em.getTransaction().commit();
    	System.out.println("Committed");
    	
    }
    
    public void deleteSongMp3(Long id) {
    	
    	em.getTransaction().begin();
    	
    	SongEntity song = em.find(SongEntity.class, id);
    	if (song != null) {
    		em.remove(song);
    	}
    	
    	em.getTransaction().commit();
    	
    }
    
    
    public void loadSongMp3(File file) {
    	

    	Platform.runLater(() -> {
    		
    		try {   		
    	
    			String fileLocation = file.toURI().toASCIIString();

            	Mp3File songFile = new Mp3File(file.getAbsolutePath());        	
            	
    			
            	if (songFile.hasId3v2Tag()) {
            		ID3v2 tag = songFile.getId3v2Tag();
            		String title = Utils.removeExtension(file.getName());
                	String lyrics = Utils.getLyricsPath(file.getAbsolutePath());
        			String audioPath = fileLocation;
        			String imagePath = Utils.getImagePath(file.getAbsolutePath());
        			double mediaDuration = songFile.getLengthInSeconds();    
            		String duration = Utils.formatDuration(mediaDuration);
            		String artist = tag.getArtist();
        			String album = tag.getAlbum();
        			SongEntity song = new SongEntity(title, artist, album, duration, lyrics, audioPath, imagePath);
                    
                	addSong(song);
            	}
            	
            	else if (songFile.hasId3v1Tag()) {
            		
            		ID3v1 tag = songFile.getId3v1Tag();
            		String title = Utils.removeExtension(file.getName());
                	String lyrics = Utils.getLyricsPath(file.getAbsolutePath());
        			String audioPath = fileLocation;
        			String imagePath = Utils.getImagePath(file.getAbsolutePath());
        			double mediaDuration = songFile.getLengthInSeconds();    
            		String duration = Utils.formatDuration(mediaDuration);
            		String artist = tag.getArtist();
        			String album = tag.getAlbum();
        			SongEntity song = new SongEntity(title, artist, album, duration, lyrics, audioPath, imagePath);
                    
                	addSong(song);
            		
            	}
            	
    		
    	

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
    
    public void saveSaf(File file) throws FileNotFoundException, IOException {
    	
    	FileOutputStream fos = new FileOutputStream(file);
    	ObjectOutputStream oos = new ObjectOutputStream(fos);
    	
    	music = getAllSongs();
    	
    	SongEntity[] songs = music.toArray(new SongEntity[music.size()]);
    	oos.writeObject(songs);
    	
    	oos.close();
    	
    }
    
    
    
    public void loadSaf(File file) throws IOException, ClassNotFoundException {
    	
    	controller = new MusicController();
    	
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        SongEntity[] songs = (SongEntity[]) ois.readObject(); 
        
        List<SongEntity> test = controller.getAllSongs();
        
        if (test != null || !test.isEmpty()) {
        	
        	List<SongEntity> songList = new ArrayList<>(controller.getAllSongs());
        	List<SongEntity> bufferList = new ArrayList<>(Arrays.asList(songs));
        	
        	for (SongEntity s : songs) {

            	for (SongEntity a : songList) {
            		
            		if (s.getTitle().equalsIgnoreCase(a.getTitle())) {
            			
            			bufferList.remove(s);
            		}
            		
            	}
            	
            }
        	
        	for (SongEntity s: bufferList) {
        		addSong(s);
        	}
        	
        }
        
        else {
        	for (SongEntity s : songs) {
        		addSong(s);
        	}
        }

        

        ois.close();
    }


    /*

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
    } */

    public SongEntity getSongById(Long id) {
    	return em.find(SongEntity.class, id);
    }
	
    
}


