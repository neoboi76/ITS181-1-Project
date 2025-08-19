package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Component;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import Controller.MusicController;
import Controller.PlayerThread;
import Model.SongEntity;

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */
 
//JPanel that displays lists of songs in a table format on the right

public class TablePanel extends JPanel {

	// Spotify-inspired colors
	private static final Color BACKGROUND_COLOR = new Color(18, 18, 18);
	private static final Color TABLE_BACKGROUND = new Color(25, 25, 25);
	private static final Color HEADER_COLOR = new Color(40, 40, 40);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color SECONDARY_TEXT = new Color(179, 179, 179);
	private static final Color SELECTION_COLOR = new Color(29, 185, 84, 50);
	private static final Color SPOTIFY_GREEN = new Color(29, 185, 84);

	private JTable table;
	private SongEntityTableModel tableModel;
	private MusicController controller;
	private PlayerThread player;
	private ControlPanel controlPanel;
	private SongListener songListener;
	private Long currentlyPlayingId = null;

	public TablePanel(MusicController controller) {
		this.controller = controller;

		initializeComponents();
		setupLayout();
		setupEventListeners();
	}

	private void initializeComponents() {
		tableModel = new SongEntityTableModel();
		table = new JTable(tableModel);

		// Modern table styling
		table.setBackground(TABLE_BACKGROUND);
		table.setForeground(TEXT_COLOR);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(45);
		table.setShowGrid(false);
		table.setIntercellSpacing(new java.awt.Dimension(0, 0));
		table.setSelectionBackground(SELECTION_COLOR);
		table.setSelectionForeground(TEXT_COLOR);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		// Custom table header
		JTableHeader header = table.getTableHeader();
		header.setBackground(HEADER_COLOR);
		header.setForeground(TEXT_COLOR);
		header.setFont(new Font("Segoe UI", Font.BOLD, 12));
		header.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
		header.setReorderingAllowed(false);

		// Custom header renderer
		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
														   boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				label.setBackground(HEADER_COLOR);
				label.setForeground(SECONDARY_TEXT);
				label.setFont(new Font("Segoe UI", Font.BOLD, 12));
				label.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 10));
				label.setOpaque(true);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;
			}
		});

		// Custom cell renderer for modern look
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
														   boolean isSelected, boolean hasFocus, int row, int column) {

				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				// Alternating row colors
				if (isSelected) {
					label.setBackground(SELECTION_COLOR);
					label.setForeground(TEXT_COLOR);
				} else if (row % 2 == 0) {
					label.setBackground(TABLE_BACKGROUND);
					label.setForeground(TEXT_COLOR);
				} else {
					label.setBackground(new Color(30, 30, 30));
					label.setForeground(TEXT_COLOR);
				}

				// Column-specific styling
				switch (column) {
					case 0: // ID
						label.setForeground(SECONDARY_TEXT);
						label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
						break;
					case 1: // Artist
						label.setForeground(SECONDARY_TEXT);
						label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
						break;
					case 2: // Title
						label.setForeground(TEXT_COLOR);
						label.setFont(new Font("Segoe UI", Font.BOLD, 14));
						break;
					case 3: // Album
						label.setForeground(SECONDARY_TEXT);
						label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
						break;
					case 4: // Duration
						label.setForeground(SPOTIFY_GREEN);
						label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
						label.setHorizontalAlignment(SwingConstants.CENTER);
						break;
				}

				label.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));
				label.setOpaque(true);
				return label;
			}
		});

		// Set column widths
		setColumnWidths();

		// Add hover effect
		addHoverEffect();
	}

	private void setupLayout() {
		setLayout(new BorderLayout());
		setBackground(BACKGROUND_COLOR);

		// Custom scroll pane
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBackground(BACKGROUND_COLOR);
		scrollPane.getViewport().setBackground(TABLE_BACKGROUND);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Style scroll bars
		styleScrollBar(scrollPane.getVerticalScrollBar());

		// Add header panel
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(BACKGROUND_COLOR);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		JLabel libraryTitle = new JLabel("Your Library");
		libraryTitle.setForeground(TEXT_COLOR);
		libraryTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		libraryTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

		JLabel songCount = new JLabel();
		songCount.setForeground(SECONDARY_TEXT);
		songCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		songCount.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

		headerPanel.add(libraryTitle, BorderLayout.WEST);
		headerPanel.add(songCount, BorderLayout.EAST);

		add(headerPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void setupEventListeners() {
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
							showStyledErrorMessage("Could not determine selected song ID.");
							return;
						}

						if (id != currentlyPlayingId) {
		                	if (player != null) {
			                	player.stopPlayback();
			                    controlPanel.btnPlayPause.setText("▶");
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

						
						player = new PlayerThread(song.getAudioPath(), false);

						if (controlPanel != null) {
							controlPanel.setPlayer(player);
							controlPanel.setNowPlaying(song.getTitle(), song.getArtist());
							controlPanel.enableControls(true);
						}
					} else {
						if (controlPanel != null) {
							controlPanel.enableControls(false);
							controlPanel.setNowPlaying("", "");
						}
					}
				}
			}
		});
	}

	private void setColumnWidths() {
		TableColumn column;

		// ID column - narrow
		column = table.getColumnModel().getColumn(0);
		column.setPreferredWidth(60);
		column.setMaxWidth(80);
		column.setMinWidth(50);

		// Artist column
		column = table.getColumnModel().getColumn(1);
		column.setPreferredWidth(200);

		// Title column - widest
		column = table.getColumnModel().getColumn(2);
		column.setPreferredWidth(300);

		// Album column
		column = table.getColumnModel().getColumn(3);
		column.setPreferredWidth(200);

		// Duration column - narrow
		column = table.getColumnModel().getColumn(4);
		column.setPreferredWidth(80);
		column.setMaxWidth(100);
		column.setMinWidth(60);
	}

	private void addHoverEffect() {
		table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			private int lastHoveredRow = -1;

			@Override
			public void mouseMoved(java.awt.event.MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				if (row != lastHoveredRow) {
					if (lastHoveredRow >= 0) {
						table.repaint(table.getCellRect(lastHoveredRow, 0, true));
					}
					if (row >= 0) {
						table.repaint(table.getCellRect(row, 0, true));
					}
					lastHoveredRow = row;
				}
			}
		});
	}

	private void styleScrollBar(javax.swing.JScrollBar scrollBar) {
		scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = new Color(100, 100, 100);
				this.trackColor = BACKGROUND_COLOR;
			}

			@Override
			protected javax.swing.JButton createDecreaseButton(int orientation) {
				javax.swing.JButton button = new javax.swing.JButton();
				button.setPreferredSize(new java.awt.Dimension(0, 0));
				return button;
			}

			@Override
			protected javax.swing.JButton createIncreaseButton(int orientation) {
				javax.swing.JButton button = new javax.swing.JButton();
				button.setPreferredSize(new java.awt.Dimension(0, 0));
				return button;
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle thumbBounds) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(thumbColor);
				g2d.fillRoundRect(thumbBounds.x + 2, thumbBounds.y, thumbBounds.width - 4, thumbBounds.height, 6, 6);
			}
		});
	}

	public void loadSongs() {
		List<SongEntity> songs = controller.getAllSongs();
		
		SongEntity selectedSong = null;

		   
	    if (currentlyPlayingId != null) {

	        for (SongEntity s : songs) {
	            if (Long.valueOf(s.getId()).equals(currentlyPlayingId)) {
	                selectedSong = s;
	                break;
	            }
	        }
	    }
	    
	    if (currentlyPlayingId != null) {

	        for (SongEntity s : songs) {
	            if (Long.valueOf(s.getId()).equals(currentlyPlayingId)) {
	                selectedSong = s;
	                break;
	            }
	        }
	    }

		tableModel.setData(songs);
		tableModel.fireTableDataChanged();

		
		Component[] components = ((JPanel) getComponent(0)).getComponents();
		for (Component comp : components) {
			if (comp instanceof JLabel && ((JLabel) comp).getForeground().equals(SECONDARY_TEXT)) {
				((JLabel) comp).setText(songs.size() + " songs");
				break;
			}
		}
		
	    if (selectedSong != null) {
	        for (int i = 0; i < tableModel.getRowCount(); i++) {
	            if (tableModel.getData().get(i).equals(selectedSong)) {
	                table.setRowSelectionInterval(i, i);
	                break;
	            }
	        }
	    }
	    
	    controlPanel.switchClear(this);

	}

	public SongEntityTableModel getTableModel() {
		return tableModel;
	}

	public void setSongListener(SongListener listener) {
		this.songListener = listener;
	}

	public void setControlPanel(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
	}

	private void showStyledErrorMessage(String message) {

		javax.swing.JDialog dialog = new javax.swing.JDialog(
				(java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
				"Error",
				true
		);
		dialog.setSize(300, 120);
		dialog.setLocationRelativeTo(this);
		dialog.getContentPane().setBackground(new Color(25, 25, 25));
		dialog.setLayout(new BorderLayout());

		JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
		messageLabel.setForeground(TEXT_COLOR);
		messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		javax.swing.JButton okButton = new javax.swing.JButton("OK");
		okButton.setBackground(SPOTIFY_GREEN);
		okButton.setForeground(TEXT_COLOR);
		okButton.setBorderPainted(false);
		okButton.setFocusPainted(false);
		okButton.addActionListener(e -> dialog.dispose());

		javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
		buttonPanel.setBackground(new Color(25, 25, 25));
		buttonPanel.add(okButton);

		dialog.add(messageLabel, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);

		dialog.setVisible(true);
	}
}