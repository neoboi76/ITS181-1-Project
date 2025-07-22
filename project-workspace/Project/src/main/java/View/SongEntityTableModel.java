package View;

import java.util.List;
import javax.swing.table.DefaultTableModel;

import Model.SongEntity;

public class SongEntityTableModel extends DefaultTableModel {
	
	private String[] colNames = {"ID", "Artist", "Title", "Album", "Duration" };

	
	public SongEntityTableModel() {
        super.setColumnIdentifiers(colNames);
    }
	
	@Override
	public String getColumnName(int column) {
	    return colNames[column];
	}

	 
	public void setData(List<SongEntity> db) {
		
		setRowCount(0);
		
		for (SongEntity s: db) {			
			
			addRow(new Object[] {
				s.getId(),
				s.getArtist(),
				s.getTitle(),
				s.getAlbum(),
				s.getDuration()
			});
			
		}
		
	}	
	
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; 
    }
	
	
}
