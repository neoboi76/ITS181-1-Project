package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import Controller.MusicController;

import Model.SongEntity;

public class TablePanel extends JPanel{

	private JTable table;
	private SongEntityTableModel tableModel;
	private MusicController controller;
	
	public TablePanel() {
		
		
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
		
		add(scp, BorderLayout.CENTER);
		
	}
	
	public void setData(List<SongEntity> db) {
		tableModel.setData(db);
	}
	
	public void update() {
		tableModel.setData(controller.getAllSongs());
		tableModel.fireTableDataChanged();
	}
	
}
