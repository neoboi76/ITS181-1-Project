package View;

import javax.swing.SwingUtilities;

import javafx.embed.swing.JFXPanel;



public class App {
	
    public static void main(String[] args) {
    	
    	 new JFXPanel(); // This initializes the JavaFX runtime.
    	 
    	
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
