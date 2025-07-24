package Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import Model.Database;
import Model.SongEntity;
import View.SongEvent;
import View.Utils;
import javafx.application.Platform;

public class MusicController {
	
	public static final String songExtension = "mp3";
	public static final String databaseExtension = "saf";
	
	Database db = new Database();
	
	PlayerThread player;
	
	public List<SongEntity> getAllSongs() {
		return db.getAllSongs();
	}
    
	public SongEvent getSong(SongEntity e) {
		
		String title = e.getTitle();
		Long id = e.getId();
		String artist = e.getArtist();
		String album = e.getAlbum();
		String duration = e.getDuration();
		String lyrics = e.getLyrics();
		String audioPath = e.getAudioPath();
		String imagePath = e.getImagePath();
		
		SongEvent song = new SongEvent(this, id, title, artist, album, duration, lyrics, audioPath, imagePath);
		
		return song;
		
	}
	
	public void deleteSong(Long id) {
		
		db.deleteSongMp3(id);
		
	}
	
	/*
	public void saveFile(File file) {

		else if (databaseExtension.equalsIgnoreCase(Utils.getFileExtension(file))) {
			db.saveSongDatabase(file);
		}
	}*/
	
	public void loadFile(File file, Runnable onFinish) {

		
		if ("mp3".equalsIgnoreCase(Utils.getFileExtension(file))) {
			
		
			new Thread(() -> {
		        db.loadSongMp3(file); 
		        Platform.runLater(onFinish); 
		    }).start();
		}
		
		/*else (databaseExtension.equalsIgnoreCase(Utils.getFileExtension(file))) {
			//db.loadSongDatabase(file);
		} */

	}

    
    public SongEntity getSongById(Long id) {
    	return db.getSongById(id);
    }

    
}
