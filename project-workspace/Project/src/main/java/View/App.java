package View;

import javax.swing.SwingUtilities;

import javafx.embed.swing.JFXPanel;



public class App {
	
    public static void main(String[] args) {
    	
    	 new JFXPanel();
    	 
    	
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                 new MusicPlayer();
            	
            }
        
        
    });
        
    }
}
