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

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */

//Mediates between Database and View Classes

public class MusicController {
	
	public static final String songExtension = "mp3";
	public static final String databaseExtension = "saf";
	
	Database db = new Database();
	
	PlayerThread player;
	
	//Returns all songs from MySQL database
	public List<SongEntity> getAllSongs() {
		return db.getAllSongs();
	}
    
	//Add returns SongEvent object and takes ass parameter SongEntity
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
	
	//Delets song in the MySQL database
	public void deleteSong(Long id) {
		
		db.deleteSongMp3(id);
		
	}
	
	//Saves all songs currently imported in the table as a saf file 
	//Note: User manually adds ".saf" extension when exporting, preceeded by preferred 
	//file name (e.g. "sample.saf").
	public void saveFile(File file) throws FileNotFoundException, IOException {

		db.saveSaf(file);

	}
	
	//Loads file locally either a saf or mp3. 
	public void loadFile(File file, Runnable onFinish) {

		
		if (songExtension.equalsIgnoreCase(Utils.getFileExtension(file))) {
			
			new Thread(() -> {
		        db.loadSongMp3(file); 
		        Platform.runLater(onFinish); 
		    }).start();
		}
		
		else if (databaseExtension.equalsIgnoreCase(Utils.getFileExtension(file))) {
			
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

    
	//Returns SongEntity object from MySQL database by id
    public SongEntity getSongById(Long id) {
    	return db.getSongById(id);
    }

    //Deletes all songs in the MySQL database
    public void deleteAll() {
    	db.clearDB();
    }
    
}
