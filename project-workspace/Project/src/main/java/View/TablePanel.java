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
	private Database db;
	
	public TablePanel(MusicController controller, ControlPanel controlPanel) {
	    this.controller = controller;
	    this.controlPanel = controlPanel;

	    db = new Database();
		
		tableModel = new SongEntityTableModel();
		table = new JTable(tableModel);
		
		setLayout(new BorderLayout());
		
		table.setShowGrid(false);
		table.setRowHeight(30);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionBackground(new Color(200, 220, 255));
		//table.setTableHeader(null);

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
		                
		                
		                if (player != null) {
		                    player.stop(); // this should safely stop playback
		                    controlPanel.btnPlayPause.setText("Play");
		                    controlPanel.enableControls(false);
		                }

		                
		               
		                SongEntity song = controller.getSongById(id);
		      
		                player = new PlayerThread(song.getAudioPath(), false); 
		                
		                
		                controlPanel.setPlayer(player);
     
		                controlPanel.enableControls(true);
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
	    

	    tableModel.setData(songs);
	    
	    
	    System.out.println("Row count in table: " + table.getRowCount());

	  
	}
	
	public SongEntityTableModel getTableModel() {
		return tableModel;
	}

	
}
