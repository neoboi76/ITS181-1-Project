package View;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import Model.SongEntity;

public class SongEntityTableModel extends AbstractTableModel {

	private List<SongEntity> db;
	
	private String[] colNames = {"ID", "Artist", "Title", "Album", "Duration" };

	
	public String getColumnName(int column) {
		return colNames[column];
	}
	
	public void setData(List<SongEntity> db) {
		this.db = db;
	}
	
	@Override
	public int getRowCount() {
		return db.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		SongEntity song = db.get(rowIndex);
		
		switch(columnIndex) {
			case 0: return song.getId();
			case 1: return song.getArtist();
			case 2: return song.getTitle();
			case 3: return song.getDuration();
			case 4: return song.getAlbum();
		}
		
		return null;
	}
	
	@Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; 
    }
	
	
	
}
