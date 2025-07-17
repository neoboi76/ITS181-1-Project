package View;

import Controller.MusicController;
import javafx.event.ActionEvent;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.IOException;

import javax.swing.*;

import com.sun.glass.events.KeyEvent;


public class MusicPlayer {
	
	private SongPanel songPanel;
	private TablePanel tablePanel;
	private JFileChooser fileChooser;
	private MusicController controller;
	private static JFrame frm;
	
	public MusicPlayer() {
		
		frm = new JFrame("Music Player");
		frm.setLayout(new BorderLayout());
		frm.setSize(1050, 450);
		frm.setResizable(false);
		
		songPanel = new SongPanel();
		tablePanel = new TablePanel();
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new SongFileFilter());
		controller = new MusicController();
		
		tablePanel.setData(controller.getAllSongs());
		
		frm.setJMenuBar(createMenuBar());
		
		//songPanel.setFormListener()
		
		
		frm.add(songPanel, BorderLayout.WEST);
		frm.add(tablePanel, BorderLayout.CENTER);
		
		frm.setVisible(true);
		
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
		
		JCheckBoxMenuItem showLyricsItem = new JCheckBoxMenuItem("Lyrics");
		showLyricsItem.setSelected(true);
		show.add(showLyricsItem);
		window.add(show);
		
		menuBar.add(fileMenu);
		menuBar.add(window);
		
		showLyricsItem.addActionListener(new ActionListener() {
			

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
				
				//songPanel.setVisible(menuItem.isSelected());
				
			}
			
		});
		
		fileMenu.setMnemonic(KeyEvent.VK_F);
		exitItem.setMnemonic(KeyEvent.VK_X);
		
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		
		
		importDataItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				
				if (fileChooser.showOpenDialog(frm) == JFileChooser.APPROVE_OPTION) {
					try {
						controller.loadSong(fileChooser.getSelectedFile());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					tablePanel.update();
					
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
						controller.saveSong(fileChooser.getSelectedFile());
						tablePanel.update();
					}
				}
			}
			
			
		});
		
	}
}