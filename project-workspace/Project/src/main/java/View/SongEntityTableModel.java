package View;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import Model.SongEntity;

public class SongEntityTableModel extends AbstractTableModel {
	
	private List<SongEntity> db;
	
	private String[] colNames = {"ID", "Artist", "Title", "Album", "Duration" };

	public SongEntityTableModel() {
        this.db = new ArrayList<>(); // ✅ Prevents null
    }
	
    @Override
    public String getColumnName(int column) {
        return colNames[column]; 
    }
   
    @Override
    public int getRowCount() {
    	return db.size();
    }

    @Override
    public int getColumnCount() {
    	return 5;
    }
	 
	public void setData(List<SongEntity> db) {
		
		this.db = db;
		
	}	
	
	public List<SongEntity> getData() {
		return db;
	}
	
	
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; 
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		SongEntity song = db.get(rowIndex);
		
		switch(columnIndex) {
			case 0: return song.getId();
			case 1: return song.getArtist();
			case 2: return song.getTitle();
			case 3: return song.getAlbum();
			case 4: return song.getDuration();
		}
		
		return null;
	}
	
	
}
