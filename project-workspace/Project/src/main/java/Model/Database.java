package Controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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

import java.util.logging.Level;

public class Database {
	
	private EntityManagerFactory emf;
    private EntityManager em;
    
    private ArrayList<SongEntity> songs;
    
    public Database() {
    	emf = Persistence.createEntityManagerFactory("musicdata");
    	em = emf.createEntityManager();
    }
    
    public List<SongEntity> getAllSongs() {
    	return em.createQuery("SELECT s FROM SongEntity s", SongEntity.class).getResultList();
    }
    
    public void addSong(SongEntity song) {
    	
    	em.getTransaction().begin();
    	SongEntity dbSong = new SongEntity();
    	dbSong.setTitle(song.getTitle());
    	dbSong.setLyrics(song.getLyrics());
    	dbSong.setAudioPath(song.getAudioPath());
    	dbSong.setImagePath(song.getImagePath());
    	em.persist(song);
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
    
    public void saveSong(File file) {
    	FileOutputStream fos = new FileOutputStream(file);
    	ObjectOutputStream oos = new ObjectOutputStream(fos);
    	
    	SongEntity[] music = songs.toArray(new SongEntity[songs.size()]);
    	oos.writeObject(music);
    	
    	oos.close();
    }
    
    public void loadSong(File file) {
    	
    	try {
    		AudioFile audioFile = AudioFileIO.read(file);
    		Tag tag = audioFile.getTag();
    		
    		String title = tag.getFirst(FieldKey.TITLE);
    		String artist = tag.getFirst(FieldKey.ARTIST);
    		String album = tag.getFirst(FieldKey.ALBUM);
    		double duration = audioFile.getAudioHeader().getTrackLength();
    		String audioPath = file.getAbsolutePath();
    		
    		File lyricsFile = new File(file.getParent(), removeExtension(file.getName()) + ".txt");

    		String lyrics = "";
    		if (lyricsFile.exists()) {
    		    lyrics = Files.readString(lyricsFile.toPath(), StandardCharsets.UTF_8);
    		}
    		
    		File imageFile = new File(file.getParent(), removeExtension(file.getName()) + ".jpg");
    		String imagePath = imageFile.exists() ? imageFile.getAbsolutePath() : null;

    		JOptionPane.showMessageDialog(null, 
				 "🎵 Title: " + title +
		            "\n🎤 Artist: " + artist +
		            "\n💿 Album: " + album +
		            "\n⏱ Duration: " + formatDuration(duration),
		            "MP3 Info",
		            JOptionPane.INFORMATION_MESSAGE
    				
			);

    	} catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading MP3 file.");
        }
    	
    	/*
    	 import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FXMP3Reader {

    public void readMp3File(File file) {
        try {
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnReady(() -> {
                String title = getMeta(media, "title");
                String artist = getMeta(media, "artist");
                String album = getMeta(media, "album");
                Duration fxDuration = media.getDuration();
                double duration = fxDuration.toSeconds();
                String audioPath = file.getAbsolutePath();

                // Read lyrics file
                File lyricsFile = new File(file.getParent(), removeExtension(file.getName()) + ".txt");
                String lyrics = "";
                try {
                    if (lyricsFile.exists()) {
                        lyrics = Files.readString(lyricsFile.toPath(), StandardCharsets.UTF_8);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Read image path
                File imageFile = new File(file.getParent(), removeExtension(file.getName()) + ".jpg");
                String imagePath = imageFile.exists() ? imageFile.getAbsolutePath() : null;

                // Show JavaFX Alert with info
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("MP3 Info");
                alert.setHeaderText("Loaded Metadata");
                alert.setContentText(
                    "🎵 Title: " + title +
                    "\n🎤 Artist: " + artist +
                    "\n💿 Album: " + album +
                    "\n⏱ Duration: " + formatDuration(duration)
                );
                alert.showAndWait();
            });

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Could not load MP3");
            errorAlert.setContentText("Error reading MP3 file.");
            errorAlert.showAndWait();
        }
    }

    private String getMeta(Media media, String key) {
        Object value = media.getMetadata().get(key);
        return value != null ? value.toString() : "Unknown";
    }

    private String removeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }

    private String formatDuration(double seconds) {
        int min = (int) seconds / 60;
        int sec = (int) seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
*/
    	
    	FileInputStream fis = new FileInputStream(file);
    	ObjectInputStream ois = new ObjectInputStream(fis);
    	
    	try {
    		SongEntity[] music = (SongEntity[]) ois.readObject();
    		songs.clear();
    		songs.addAll(Arrays.asList(music));
    	} catch(ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	ois.close();
    	
    }
    
    private static String removeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }
    
    private static String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        return String.format("%d:%02d", minutes, sec);
    }

    
}
