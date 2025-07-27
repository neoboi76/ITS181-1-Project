package View;

import Controller.MusicController;
import Controller.PlayerThread;
import Model.SongEntity;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.sun.glass.events.KeyEvent;

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */

//Main JFrame view class. Houses all three main JPanels: ControlPanel, SongPanel, and TablePanel

public class MusicPlayer {

	// Spotify-inspired color scheme
	private static final Color BACKGROUND_COLOR = new Color(18, 18, 18);
	private static final Color SIDEBAR_COLOR = new Color(0, 0, 0);
	private static final Color SPOTIFY_GREEN = new Color(29, 185, 84);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color SECONDARY_TEXT = new Color(179, 179, 179);
	private static final Color PANEL_COLOR = new Color(25, 25, 25);

	private SongPanel songPanel;
	private static TablePanel tablePanel;
	private ControlPanel controlPanel;
	private JFileChooser fileChooser;
	private MusicController controller;
	private static JFrame frm;
	private PlayerThread player;
	
	public static final String songExtension = "mp3";
	public static final String databaseExtension = "saf";

	public MusicPlayer() {

		// Set dark theme for file chooser
		try {
			UIManager.setLookAndFeel(UIManager.getLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}

		frm = new JFrame("Music Player - Spotify Style");
		frm.setLayout(new BorderLayout());
		frm.setSize(1400, 900);
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setBackground(BACKGROUND_COLOR);

		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.addChoosableFileFilter(new SongFileFilter());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 Files", "mp3");
		controller = new MusicController();
		songPanel = new SongPanel();
		tablePanel = new TablePanel(controller);
		controlPanel = new ControlPanel(controller, tablePanel, songPanel);

		tablePanel.setControlPanel(controlPanel);

		frm.setJMenuBar(createStyledMenuBar());

		tablePanel.setSongListener(new SongListener() {
			public void songEventOccured(SongEvent e) {
				songPanel.setSong(e);
			}
		});

		// Create main content panel with modern layout
		JPanel mainContent = new JPanel(new BorderLayout());
		mainContent.setBackground(BACKGROUND_COLOR);

		// Left sidebar with song info
		songPanel.setPreferredSize(new Dimension(350, 0));

		// Center panel for table
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(BACKGROUND_COLOR);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centerPanel.add(tablePanel, BorderLayout.CENTER);

		mainContent.add(songPanel, BorderLayout.WEST);
		mainContent.add(centerPanel, BorderLayout.CENTER);

		frm.add(mainContent, BorderLayout.CENTER);
		frm.add(controlPanel, BorderLayout.SOUTH);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tablePanel.loadSongs();
		frm.setVisible(true);
	}

	public TablePanel getTablePanel() {
		return tablePanel;
	}

	private JMenuBar createStyledMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SIDEBAR_COLOR);
		menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));

		JMenu fileMenu = createStyledMenu("File");
		JMenuItem exportDataItem = createStyledMenuItem("Export Data...");
		JMenuItem importDataItem = createStyledMenuItem("Import Data...");
		JMenuItem exitItem = createStyledMenuItem("Exit");

		fileMenu.add(exportDataItem);
		fileMenu.add(importDataItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		JMenu window = createStyledMenu("Window");
		JMenu show = createStyledMenu("Show");

		JCheckBoxMenuItem showLyricsItem = new JCheckBoxMenuItem("Lyrics");
		showLyricsItem.setSelected(true);
		styleMenuItem(showLyricsItem);
		show.add(showLyricsItem);
		window.add(show);

		menuBar.add(fileMenu);
		menuBar.add(window);

		showLyricsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
				songPanel.setVisible(menuItem.isSelected());
			}
		});

		fileMenu.setMnemonic(KeyEvent.VK_F);
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));

		//Imports songs
		importDataItem.addActionListener(new ActionListener() {
			

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {

				styleFileChooser();
				
				
				if (fileChooser.showOpenDialog(frm) == JFileChooser.APPROVE_OPTION) {
					
					String extension = Utils.getFileExtension(fileChooser.getSelectedFile());
					
					File[] selectedFiles = fileChooser.getSelectedFiles(); 
				    List<SongEntity> songList = controller.getAllSongs();
				    
				    ArrayList<File> fileList = new ArrayList<>(Arrays.asList(selectedFiles));
					
				    for (File file : fileList) {
				        String fileExtension = Utils.getFileExtension(file);
				        String fileName = Utils.removeExtension(file.getName());

				        //Imports mp3 files (can either be single or multiple)
				        if (songExtension.equalsIgnoreCase(fileExtension)) {
				            boolean isDuplicate = false;

				            for (SongEntity s : songList) {
				                if (s.getTitle().equalsIgnoreCase(fileName)) {
				                    isDuplicate = true;
				                    break;
				                }
				            }

				            if (isDuplicate) {
				                JOptionPane.showMessageDialog(frm, "Song '" + fileName + "' has already been imported", "Error", JOptionPane.ERROR_MESSAGE);
				            } else {
				                controller.loadFile(file, () -> {
				                    SwingUtilities.invokeLater(() -> tablePanel.loadSongs());
				                });

				            }
				        }
				    }

					//Else, imports saf files (filetype unique to this application that contains mp3 songs)
					if (databaseExtension.equalsIgnoreCase(extension)) {
			
						controller.loadFile(fileChooser.getSelectedFile(), () -> {
						    SwingUtilities.invokeLater(() -> tablePanel.loadSongs());
						});
						
					}
					
					
					

					System.out.println("File loaded");
					System.out.println("yess");

				}
				
				
				else {
					JOptionPane.showMessageDialog(frm, "Could not load data from file", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
			
			
			
		});

		//Saves all songs locally that are currently in the table as a .saf file 
		exportDataItem.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (fileChooser.showSaveDialog(frm) == JFileChooser.APPROVE_OPTION) {
					try {
						controller.saveFile(fileChooser.getSelectedFile());
					} catch (Exception ex) {
						JOptionPane.showConfirmDialog(frm, ex.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}
			
			
		});

		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				int action = showStyledConfirmDialog("Are you sure you want to exit?", "Confirm Exit");
				if (action == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});

		return menuBar;
	}

	private JMenu createStyledMenu(String text) {
		JMenu menu = new JMenu(text);
		menu.setForeground(TEXT_COLOR);
		menu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		return menu;
	}

	private JMenuItem createStyledMenuItem(String text) {
		JMenuItem item = new JMenuItem(text);
		styleMenuItem(item);
		return item;
	}

	private void styleMenuItem(JMenuItem item) {
		item.setBackground(SIDEBAR_COLOR);
		item.setForeground(TEXT_COLOR);
		item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		item.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

		// Hover effect
		item.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				item.setBackground(new Color(40, 40, 40));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				item.setBackground(SIDEBAR_COLOR);
			}
		});
	}

	private void styleFileChooser() {
		// Apply dark theme to file chooser components
		Component[] components = fileChooser.getComponents();
		styleComponents(components);
	}

	private void styleComponents(Component[] components) {
		for (Component component : components) {
			if (component instanceof JPanel) {
				component.setBackground(PANEL_COLOR);
				styleComponents(((JPanel) component).getComponents());
			} else if (component instanceof JLabel) {
				component.setForeground(TEXT_COLOR);
			} else if (component instanceof JButton) {
				JButton button = (JButton) component;
				button.setBackground(SPOTIFY_GREEN);
				button.setForeground(TEXT_COLOR);
				button.setBorderPainted(false);
			}
		}
	}


	private int showStyledConfirmDialog(String message, String title) {
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		styleOptionPane(optionPane);
		JDialog dialog = optionPane.createDialog(frm, title);
		styleDialog(dialog);
		dialog.setVisible(true);

		Object result = optionPane.getValue();
		return result != null ? (Integer) result : JOptionPane.CANCEL_OPTION;
	}

	private void styleOptionPane(JOptionPane pane) {
		pane.setBackground(PANEL_COLOR);
		pane.setForeground(TEXT_COLOR);

		// Style buttons
		for (Component component : pane.getComponents()) {
			if (component instanceof JPanel) {
				styleOptionPaneComponents((JPanel) component);
			}
		}
	}

	private void styleOptionPaneComponents(JPanel panel) {
		panel.setBackground(PANEL_COLOR);
		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				JButton button = (JButton) component;
				button.setBackground(SPOTIFY_GREEN);
				button.setForeground(TEXT_COLOR);
				button.setBorderPainted(false);
				button.setFocusPainted(false);
			} else if (component instanceof JLabel) {
				component.setForeground(TEXT_COLOR);
			} else if (component instanceof JPanel) {
				styleOptionPaneComponents((JPanel) component);
			}
		}
	}

	private void styleDialog(JDialog dialog) {
		dialog.getContentPane().setBackground(PANEL_COLOR);
		dialog.setLocationRelativeTo(frm);
	}
}