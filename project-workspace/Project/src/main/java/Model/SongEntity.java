package Model;

import java.io.Serializable;

import jakarta.persistence.*;
import javafx.scene.media.Media;

//Model Class

@Entity
@Table(name = "songs")
public class SongEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String title;
	
	@Column(nullable = false)
	private String artist;
	
	@Column(nullable = false)
	private String album;
	
	@Column(nullable = false)
	private double duration;
	
	@Lob
	@Column(nullable = false)
	private String lyrics;
	
	@Column(name = "audio_path", nullable = false)
	private String audioPath;
	
	@Column(name = "image_path")
	private String imagePath;
	
	public SongEntity(String title, String artist, String album, double duration, String lyrics, String audioPath, String imagePath) {

		super();
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.duration = duration;
		this.lyrics = lyrics;
		this.audioPath = audioPath;
		this.imagePath = imagePath;
		
	}
	
	public SongEntity() {
		
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
	
	public String getLyrics() {
		return lyrics;
	}
	
	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}
	
	public double getDuration() {
		return duration;
	}
	
	public void setDuration(double duration) {
		this.duration = duration;
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
	
	private String getMeta(Media media, String key) {
	    Object value = media.getMetadata().get(key);
	    return value != null ? value.toString() : "Unknown";
	}

    private static String formatDuration(double duration) {
        int minutes = (int) duration / 60;
        int seconds = (int) duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
	
	
}
