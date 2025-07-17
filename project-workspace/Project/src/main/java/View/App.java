package View;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import Model.SongEntity;

public class App {
	
	private static EntityManagerFactory emf;
    private static EntityManager em;
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                 new MusicPlayer();
            	
            	
            	/*emf = Persistence.createEntityManagerFactory("musicdata");
                em = emf.createEntityManager();
                
	            em.getTransaction().begin();
            	SongEntity song = new SongEntity();
            	song.setTitle("Duvet");
            	song.setArtist("Boa");
            	song.setLyrics("Sample TextSample TextSample TextSample TextSample TextSample TextSample Text");
            	song.setAudioPath("src/main/java/resources/Your Relief.mp3");
            	song.setImagePath("c:/samplePath");
            	
            	em.persist(song);
            	em.getTransaction().commit();*/
            }
        
        
    });
        
    }
}
