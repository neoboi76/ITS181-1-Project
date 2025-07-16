package model;

import jakarta.persistence.*;

//Model Class

@Entity
@Table(name = "songs")
public class SongEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String title;
	
	@Lob
	@Column(nullable = false)
	private String lyrics;
	
	@Column(name = "audio_path", nullable = false)
	private String audioPath;
	
	@Column(name = "image_path")
	private String imagePath;
	
	public SongEntity(Long id, String title, String audioPath, String imagePath) {

		super();
		this.id = id;
		this.title = title;
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
	
	public void setString(String title) {
		this.title = title;
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
