package View;

import Controller.MusicController;
import Controller.PlayerThread;
import Model.SongEntity;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.glass.events.KeyEvent;
import com.sun.glass.events.WindowEvent;




public class MusicPlayer {
	
	private SongPanel songPanel;
	private static TablePanel tablePanel;
	private ControlPanel controlPanel;
	private JFileChooser fileChooser;
	private MusicController controller;
	private static JFrame frm;
	private PlayerThread player;
	
	public MusicPlayer() {
		
		frm = new JFrame("Music Player");
		frm.setLayout(new BorderLayout());
		frm.setSize(1250, 700);
	//	frm.setResizable(false);
		
		
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.addChoosableFileFilter(new SongFileFilter());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 Files", "mp3");
		fileChooser.setFileFilter(filter);
		controller = new MusicController();
		songPanel = new SongPanel();
		tablePanel = new TablePanel(controller);
		
		controlPanel = new ControlPanel(controller, tablePanel, songPanel);
		
		tablePanel.setControlPanel(controlPanel);
		
		frm.setJMenuBar(createMenuBar());
		
		
		tablePanel.setSongListener(new SongListener() {
			
			public void songEventOccured(SongEvent e) {
				
			    songPanel.setSong(e);
			      
         				
			}
			
		});
		
		
		frm.add(songPanel, BorderLayout.WEST);
		frm.add(tablePanel, BorderLayout.CENTER);
		frm.add(controlPanel, BorderLayout.SOUTH);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		tablePanel.loadSongs();
		
		frm.setVisible(true);
	
		
	}
	
	public TablePanel getTablePanel() {
		return tablePanel;
	}
	
	public PlayerThread getPlayer() {
		return player;
	}
	
	private JMenuBar createMenuBar() {
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem exportDataItem = new JMenuItem("Export Data. . .");
		JMenuItem importDataItem = new JMenuItem("Import Data. . .");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		fileMenu.add(exportDataItem);
		fileMenu.add(importDataItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		
		JMenu window = new JMenu("Window");
		JMenu show = new JMenu("Show");
		
		JCheckBoxMenuItem showTableItem = new JCheckBoxMenuItem("Table");
		showTableItem.setSelected(true);
		show.add(showTableItem);
		window.add(show);
		
		menuBar.add(fileMenu);
		menuBar.add(window);
		
		showTableItem.addActionListener(new ActionListener() {
			

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
				
				tablePanel.setVisible(menuItem.isSelected());
				
			}
			
		});
		
		fileMenu.setMnemonic(KeyEvent.VK_F);
		exitItem.setMnemonic(KeyEvent.VK_X);
		
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		
		
		importDataItem.addActionListener(new ActionListener() {
			
			

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {

				
				if (fileChooser.showOpenDialog(frm) == JFileChooser.APPROVE_OPTION) {
					
					String extension = Utils.getFileExtension(fileChooser.getSelectedFile());
					
					File[] selectedFiles = fileChooser.getSelectedFiles(); 
				    List<SongEntity> songList = controller.getAllSongs();
				    
				    ArrayList<File> fileList = new ArrayList<>(Arrays.asList(selectedFiles));
					
					for (File file: fileList) {
						
						String fileExtension = Utils.getFileExtension(file);
						String fileName = Utils.removeExtension(file.getName());
						
						if ("mp3".equalsIgnoreCase(fileExtension)) {
													
							
							for (SongEntity s: songList) {
							
								
								if (s.getTitle().equalsIgnoreCase(fileName)) {
									
									fileList.remove(file);
									continue;
									
								}
							
						}	
							
						System.out.println(Utils.removeExtension(file.getName()));
						
						controller.loadFile(file, () -> {
						    SwingUtilities.invokeLater(() -> tablePanel.loadSongs());
						});
							
						
					}
						
				}
					
				if ("saf".equalsIgnoreCase(extension)) {
		
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
				int action = JOptionPane.showConfirmDialog(frm, "Are you sure?", "Confim Exit", JOptionPane.OK_CANCEL_OPTION);
				
				
				if (action == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
			
		});
		
		return menuBar;
		
	}
}