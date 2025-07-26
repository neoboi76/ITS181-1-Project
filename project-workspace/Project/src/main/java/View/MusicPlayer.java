package View;

import Controller.MusicController;
import Controller.PlayerThread;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;

import javax.swing.*;

import com.sun.glass.events.KeyEvent;
import com.sun.glass.events.WindowEvent;

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

	public MusicPlayer() {

		// Set dark theme for file chooser
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}

		frm = new JFrame("Music Player - Spotify Style");
		frm.setLayout(new BorderLayout());
		frm.setSize(1400, 800);
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setBackground(BACKGROUND_COLOR);

		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new SongFileFilter());
		controller = new MusicController();
		songPanel = new SongPanel();
		tablePanel = new TablePanel(controller, songPanel);
		controlPanel = new ControlPanel(controller, tablePanel);

		tablePanel.setControlPanel(controlPanel);

		frm.setJMenuBar(createStyledMenuBar());

		tablePanel.setSongListener(new SongListener() {
			public void songEventOccured(SongEvent e) {
				tablePanel.loadSongs();
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

		importDataItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				// Style the file chooser
				styleFileChooser();

				if (fileChooser.showOpenDialog(frm) == JFileChooser.APPROVE_OPTION) {
					// Show loading dialog
					JDialog loadingDialog = createLoadingDialog();
					loadingDialog.setVisible(true);

					controller.loadFile(fileChooser.getSelectedFile(), () -> {
						SwingUtilities.invokeLater(() -> {
							tablePanel.loadSongs();
							loadingDialog.dispose();
							showSuccessMessage("File loaded successfully!");
						});
					});
				}
			}
		});

		exportDataItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				styleFileChooser();
				if (fileChooser.showSaveDialog(frm) == JFileChooser.APPROVE_OPTION) {
					try {
						tablePanel.loadSongs();
						showSuccessMessage("Data exported successfully!");
					} catch (Exception ex) {
						showErrorMessage("Error exporting data: " + ex.getMessage());
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

	private JDialog createLoadingDialog() {
		JDialog dialog = new JDialog(frm, "Loading", true);
		dialog.setSize(300, 120);
		dialog.setLocationRelativeTo(frm);
		dialog.getContentPane().setBackground(PANEL_COLOR);
		dialog.setLayout(new BorderLayout());

		JLabel loadingLabel = new JLabel("Loading file...", SwingConstants.CENTER);
		loadingLabel.setForeground(TEXT_COLOR);
		loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBackground(PANEL_COLOR);
		progressBar.setForeground(SPOTIFY_GREEN);

		dialog.add(loadingLabel, BorderLayout.CENTER);
		dialog.add(progressBar, BorderLayout.SOUTH);

		return dialog;
	}

	private void showSuccessMessage(String message) {
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
		styleOptionPane(optionPane);
		JDialog dialog = optionPane.createDialog(frm, "Success");
		styleDialog(dialog);
		dialog.setVisible(true);
	}

	private void showErrorMessage(String message) {
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
		styleOptionPane(optionPane);
		JDialog dialog = optionPane.createDialog(frm, "Error");
		styleDialog(dialog);
		dialog.setVisible(true);
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