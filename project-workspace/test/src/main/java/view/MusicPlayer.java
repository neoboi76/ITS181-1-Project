package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import controller.MusicController;
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.stage.Stage;
import controller.PlayerThread;

//View Class

public class MusicPlayer implements MusicDataChangeListener{

	private MusicController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnPlay, btnPause, btnStop;
    private final List<MusicDataChangeListener> listeners = new ArrayList<>();
    private PlayerThread player;
    
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusicPlayer());
    }

    public MusicPlayer() {
    	this.controller = controller;
        addMusicDataChangeListener(this); //Adds CrudView() as an object to be listened to.
        gui();
    	
    }
	
    private void addMusicDataChangeListener(MusicDataChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyDataChangeListeners() {
        for (MusicDataChangeListener listener : listeners) {
            listener.onDataChanged();
        }
    }
    
    private void gui() {
    	
    	JFrame frm = new JFrame("test");
    	frm.setLayout(new FlowLayout(FlowLayout.CENTER));
    	frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(550, 450);
        frm.setResizable(false);
        
        btnPlay = new JButton("Play");
        btnPause = new JButton("Pause");
        btnStop = new JButton("Stop");
        
        frm.add(btnPlay);
        frm.add(btnPause);
        frm.add(btnStop);
        
        btnPlay.addActionListener(e -> playSong());
        btnPause.addActionListener(e -> pauseSong());
        btnStop.addActionListener(e -> stopSong());
        
        frm.setVisible(true);
    	
    }
    
    public void playSong() {
    	
    	player.play();
    	
    }
    
	public void pauseSong() {
	    	
    	player.pause();
	    	
    }
	
	public void stopSong() {
		
		player.stop();
		
	}


	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		
	}

}
