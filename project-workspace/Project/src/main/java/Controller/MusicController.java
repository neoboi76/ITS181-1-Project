package Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import Model.Database;
import Model.SongEntity;
import View.SongEvent;
import View.Utils;

public class MusicController {
	
	public static final String songExtension = "mp3";
	public static final String databaseExtension = "saf";
	
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
		if (Utils.getFileExtension(file) == songExtension) {
			db.saveSongMp3(file);
		}
		else if (Utils.getFileExtension(file) == databaseExtension) {
			db.saveSongDatabase(file);
		}
	}
	
    public void loadFile(File file) throws IOException {
    	if (Utils.getFileExtension(file) == songExtension) {
			db.loadSongMp3(file);
		}
		else if (Utils.getFileExtension(file) == databaseExtension) {
			db.loadSongDatabase(file);
		}
    	  	
    }

    
}
