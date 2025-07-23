package View;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import Controller.MusicController;
import Controller.PlayerThread;
import Model.Database;

public class ControlPanel extends JPanel {

    private PlayerThread player;
    private MusicController controller;
    private Database database;
    private Long id;
    private JSlider seekSlider;
    private Timer seekTimer;
    private boolean isPlaying = false;
    private TablePanel tablePanel;
    private SongPanel songPanel;

    public JButton btnPlayPause, btnStop, btnDelete;

    public ControlPanel(MusicController controller, TablePanel tablePanel, SongPanel songPanel) {
    	
    	this.controller = controller;
    	this.tablePanel = tablePanel;
    	this.songPanel = songPanel;
    	
        btnPlayPause = new JButton("Play");
        btnStop = new JButton("Stop");
        btnDelete = new JButton("Delete");
        btnStop.setEnabled(false);
        btnPlayPause.setEnabled(false);
        btnDelete.setEnabled(false);

        setPreferredSize(new Dimension(40, 70));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnPlayPause.addActionListener(e -> {
            if (player == null) return;
            if (!isPlaying) {
                player.play();
                startSeekUpdater();
                btnPlayPause.setText("Pause");
                isPlaying = true;
            } else {
                player.pause();
                btnPlayPause.setText("Play");
                isPlaying = false;
            }
        });

        btnStop.addActionListener(e -> {
            if (isPlaying) {
                player.stop();
                stopSeekUpdater();
                btnPlayPause.setText("Play");
                isPlaying = false;
            }
        });
        
        btnDelete.addActionListener(e -> {
        	
        	
	    	int result = JOptionPane.showConfirmDialog(
	    	        null,
	    	        "Are you sure you want to delete this song?",
	    	        "Confirm Deletion",
	    	        JOptionPane.YES_NO_OPTION,
	    	        JOptionPane.WARNING_MESSAGE
	    	);

	    	if (result == JOptionPane.YES_OPTION) {
	    		if (isPlaying) {
	        		player.stop();
	        		stopSeekUpdater();
	        		btnPlayPause.setText("Play");
	        		controller.deleteSong(id);
	        		songPanel.clearSongPanel();
	        		tablePanel.loadSongs();
	        	}
	        	
	        	else {
	        		stopSeekUpdater();
	        		controller.deleteSong(id);
	        		songPanel.clearSongPanel();
	        		tablePanel.loadSongs();
	        	}
	        	
	    		
	    	}
	    	
	    	else {
	    	    System.out.println("Deletion canceled.");
	    	}
        	
        	
        });

        seekSlider = new JSlider(0, 100, 0);
        seekSlider.setPreferredSize(new Dimension(300, 20));
        seekSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (player != null) {
                    MediaPlayer mp = player.getMediaPlayer();
                    if (mp != null) {
                        Duration total = mp.getTotalDuration();
                        double percent = seekSlider.getValue() / 100.0;
                        mp.seek(total.multiply(percent));
                    }
                }
            }
        });

        JLabel volumeLabel = new JLabel("Volume:");
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setPreferredSize(new Dimension(100, 20));
        volumeSlider.addChangeListener(e -> {
            if (player != null) {
                int value = volumeSlider.getValue();
                player.setVolume(value / 100.0);
            }
        });

        add(btnPlayPause);
        add(Box.createHorizontalStrut(10));
        add(btnStop);
        add(Box.createHorizontalStrut(10));
        add(btnDelete);
        add(Box.createHorizontalStrut(20));
        add(new JLabel("Seek:"));
        add(seekSlider);
        add(Box.createHorizontalStrut(20));
        add(volumeLabel);
        add(volumeSlider);
    }

    private void startSeekUpdater() {
        stopSeekUpdater();
        seekTimer = new Timer();
        seekTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (player != null) {
                    MediaPlayer mp = player.getMediaPlayer();
                    if (mp != null) {
                        Duration current = mp.getCurrentTime();
                        Duration total = mp.getTotalDuration();
                        if (total.toMillis() > 0) {
                            int progress = (int) (100 * current.toMillis() / total.toMillis());
                            SwingUtilities.invokeLater(() -> seekSlider.setValue(progress));
                        }
                    }
                }
            }
        }, 0, 500);
    }

    private void stopSeekUpdater() {
        if (seekTimer != null) {
            seekTimer.cancel();
            seekTimer = null;
        }
    }

    public void setPlayer(PlayerThread player) {
        this.player = player;
        btnPlayPause.setEnabled(true);
        btnStop.setEnabled(true);
    }
    
    public void enableControls(boolean enabled) {
        btnPlayPause.setEnabled(enabled);
        btnStop.setEnabled(enabled);
        btnDelete.setEnabled(enabled);

    }
    
    public void setId(Long id) {
    	this.id = id;
    }



	public Object getPlayer() {

		return player;
	}

}
