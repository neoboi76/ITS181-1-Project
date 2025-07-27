package View;

import javax.swing.SwingUtilities;

import javafx.embed.swing.JFXPanel;

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */

//Contains main method. Runs instance of MusicPlayer class

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
