package View;

import java.util.EventObject;

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */

//Song event class for facilitating and handling custom events

public class SongEvent extends EventObject {

	private Long id;
	
	private String title;
	
	private String artist;
	
	private String album;
	
	private String duration;

	private String lyrics;
	
	private String audioPath;

	private String imagePath;
	
	public SongEvent(Object source, Long id, String title, String artist, String album, String duration, String lyrics, String audioPath, String imagePath) {

		super(source);
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.duration = duration;
		this.lyrics = lyrics;
		this.audioPath = audioPath;
		this.imagePath = imagePath;
		
	}
	
	public SongEvent(Object source) {
		super(source);
	}
	
	public Long getId() {	
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getLyrics() {
		return lyrics;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}
	
	public String getAudioPath() {
		return audioPath;
	}
	
	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
}
