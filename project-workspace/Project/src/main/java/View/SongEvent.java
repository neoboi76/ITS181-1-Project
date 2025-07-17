package View;


import java.util.EventObject;


public class SongEvent extends EventObject {

	private Long id;
	
	private String title;
	
	private String artist;
	
	private String album;
	
	private double duration;

	private String lyrics;
	
	private String audioPath;

	private String imagePath;
	
	public SongEvent(Object source, String title, String artist, private String album, double duration, String lyrics, String audioPath, String imagePath) {

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
	
	public double getDuration() {
		return duration;
	}
	
	public void setDuration(double duration) {
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
