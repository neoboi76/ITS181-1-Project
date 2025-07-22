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
    
	public void addSong(SongEvent e) {
		
		String title = e.getTitle();
		String artist = e.getArtist();
		String album = e.getAlbum();
		String duration = e.getDuration();
		String lyrics = e.getLyrics();
		String audioPath = e.getAudioPath();
		String imagePath = e.getImagePath();
		
		SongEntity song = new SongEntity(title, artist, album, duration, lyrics, audioPath, imagePath);
		db.addSong(song);
		
	}
	
	public void deleteSong(SongEvent e) {
		//Coming soon. . .
		
	}
	/*
	
	public void saveFile(File file) {
		if (Utils.getFileExtension(file) == songExtension) {
			//db.saveSongMp3(file);
		}
		else if (Utils.getFileExtension(file) == databaseExtension) {
			//db.saveSongDatabase(file);
		}
	}*/
	
	public void loadFile(File file, Runnable onFinish) {
	    new Thread(() -> {
	        db.loadSongMp3(file); // commits song
	        Platform.runLater(onFinish); // runs loadSongs after commit
	    }).start();
	}

    
    public SongEntity getSongById(Long id) {
    	return db.getSongById(id);
    }

    
}
