package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Controller.MusicController;
import Controller.PlayerThread;
import Model.SongEntity;
import Model.Database;

public class TablePanel extends JPanel{

	private JTable table;
	private SongEntityTableModel tableModel;
	private MusicController controller;
	private PlayerThread player;
	private SongPanel songPanel;
	private ControlPanel controlPanel;
	private SongListener songListener;
	
	private Long currentlyPlayingId = null;

	
	public TablePanel(MusicController controller) {
	    this.controller = controller;
		
		tableModel = new SongEntityTableModel();
		table = new JTable(tableModel);
		
		setLayout(new BorderLayout());
		
		table.setShowGrid(false);
		table.setRowHeight(30);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionBackground(new Color(200, 220, 255));

		table.setFillsViewportHeight(true);
		JScrollPane scp = new JScrollPane(table);
		scp.setBorder(BorderFactory.createEmptyBorder());
		
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		        if (!e.getValueIsAdjusting()) {
		            int selectedRow = table.getSelectedRow();

		            if (selectedRow != -1) {
		                Object idValue = tableModel.getValueAt(selectedRow, 0);

		                Long id = null;
		                if (idValue instanceof Integer) {
		                    id = ((Integer) idValue).longValue();
		                } else if (idValue instanceof Long) {
		                    id = (Long) idValue;
		                }

		                if (id == null) {
		                    JOptionPane.showMessageDialog(null, "Could not determine selected song ID.");
		                    return;
		                }		          
		                
		                if (id != currentlyPlayingId) {
		                	if (player != null) {
			                	player.stopPlayback();
			                	System.out.println("Song Stopped");
			                    controlPanel.btnPlayPause.setText("Play");
			                }
		                }
		               
		                SongEntity song = controller.getSongById(id);		                		            
		                
		               
		                SongEvent ev = controller.getSong(song);
		                
		                if (songListener != null)
		                	songListener.songEventOccured(ev);
		                	                

		                player = new PlayerThread(ev.getAudioPath(), false); 
		                
		                controlPanel.setId(ev.getId());
		                
		                controlPanel.setPlayer(player);
		                
		                controlPanel.enableControls(true);
		              
		                currentlyPlayingId = id;
		          		              
		            }
		            
		            else {
		             controlPanel.enableControls(false);
		            }
		        }
		    }
		});

		
		add(scp, BorderLayout.CENTER);
		
		return;
		
	}
	

	
	public void loadSongs() {
	    List<SongEntity> songs = controller.getAllSongs();
	    System.out.println("Songs from DB: " + songs.size());
	    for (SongEntity s : songs) {
	        System.out.println(s.getTitle());
	    }

	    SongEntity selectedSong = null;

	   
	    if (currentlyPlayingId != null) {

	        for (SongEntity s : songs) {
	            if (Long.valueOf(s.getId()).equals(currentlyPlayingId)) {
	                selectedSong = s;
	                break;
	            }
	        }
	    }
	    
	    System.out.println("Song Found");

	    tableModel.setData(songs);
	    tableModel.fireTableDataChanged();

	    if (selectedSong != null) {
	        for (int i = 0; i < tableModel.getRowCount(); i++) {
	            if (tableModel.getData().get(i).equals(selectedSong)) {
	                table.setRowSelectionInterval(i, i);
	                break;
	            }
	        }
	    }
	    
	    System.out.println("Song Regained");
	    
	    

	    System.out.println("Row count in table: " + table.getRowCount());
	}

	
	public SongEntityTableModel getTableModel() {
		return tableModel;
	}

	public void setSongListener(SongListener listener) {
    	this.songListener = listener;
    }
	
	public void setControlPanel(ControlPanel controlPanel)  {
		this.controlPanel = controlPanel;
	}
}
