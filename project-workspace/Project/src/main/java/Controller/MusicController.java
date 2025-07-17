package Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import Model.Database;
import Model.SongEntity;
import View.SongEvent;

public class MusicController {
	
	Database db = new Database();
	
	public List<SongEntity> getAllSongs() {
		return db.getAllSongs();
	}
    
	public void addSong(SongEvent e) {
		
		String title = e.getTitle();
		String artist = e.getArtist();
		String album = e.getAlbum();
		double duration = e.getDuration();
		String lyrics = e.getLyrics();
		String audioPath = e.getAudioPath();
		String imagePath = e.getImagePath();
		
		SongEntity song = new SongEntity(title, artist, album, duration, lyrics, audioPath, imagePath);
		db.addSong(song);
		
	}
	
	public void deleteSong(SongEvent e) {
		//Coming soon. . .
		
	}
	
	public void saveFile(File file) {
		db.saveSong(file);
	}
	
    public void loadSong(File file) throws IOException {
    	
    	db.loadSong(file);
    	
    	
    }
    
}
