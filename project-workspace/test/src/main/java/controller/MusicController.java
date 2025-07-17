package controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.SongEntity;

import java.util.List;

public class MusicController {
	
	private EntityManagerFactory emf;
    private EntityManager em;
    
    public MusicController() {
    	emf = Persistence.createEntityManagerFactory("musicdata");
    	em = emf.createEntityManager();
   			
    }
    
    public List<SongEntity> getAllSongs() {
    	return em.createQuery("SELECT s FROM SongEntity s", SongEntity.class).getResultList();
    }
    
    public void addSong(String title, String lyrics, String audiopath, String imagePath) {
    	
    	em.getTransaction().begin();
    	SongEntity song = new SongEntity();
    	song.setTitle(title);
    	song.setLyrics(lyrics);
    	song.setAudioPath(audiopath);
    	song.setImagePath(imagePath);
    	em.persist(song);
    	em.getTransaction().commit();
    	
    }
    
    public void deleteSong(Long id) {
    	
    	em.getTransaction().begin();
    	SongEntity song = em.find(SongEntity.class, id);
    	if (song != null) {
    		em.remove(song);
    	}
    	
    	em.getTransaction().commit();
    	
    }
    
}
