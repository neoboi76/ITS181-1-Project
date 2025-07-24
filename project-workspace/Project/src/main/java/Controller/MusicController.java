package Controller;

import java.io.File;
import java.io.FileNotFoundException;
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
	

	public void saveFile(File file) throws FileNotFoundException, IOException {

		db.saveSaf(file);
		
		//else if (databaseExtension.equalsIgnoreCase(Utils.getFileExtension(file))) {
	//		db.saveSongDatabase(file);
		//}
	}
	
	public void loadFile(File file, Runnable onFinish) {

		
		if ("mp3".equalsIgnoreCase(Utils.getFileExtension(file))) {
			
			new Thread(() -> {
		        db.loadSongMp3(file); 
		        Platform.runLater(onFinish); 
		    }).start();
		}
		
		else if ("saf".equalsIgnoreCase(Utils.getFileExtension(file))) {
			
			new Thread(() -> {
		        try {
					db.loadSaf(file);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		        Platform.runLater(onFinish); 
		    }).start();
			
		} 

	}

    
    public SongEntity getSongById(Long id) {
    	return db.getSongById(id);
    }

    
}
