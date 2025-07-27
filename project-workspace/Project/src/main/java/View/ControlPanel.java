package View;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import Controller.MusicController;
import Controller.PlayerThread;

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */

//View class that displays music controls: play, pause, stop, delete, and clear
//As well as song playback slider and volume control slider

public class ControlPanel extends JPanel {

    // Spotify-inspired colors
    private static final Color BACKGROUND_COLOR = new Color(40, 40, 40);
    private static final Color SPOTIFY_GREEN = new Color(29, 185, 84);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color SECONDARY_TEXT = new Color(179, 179, 179);
    private static final Color BUTTON_COLOR = new Color(60, 60, 60);
    private static final Color BUTTON_DELETE = new Color(231, 76, 60);
    private static final Color SLIDER_COLOR = new Color(100, 100, 100);

    private PlayerThread player;
    private MusicController controller;
    private Long id;
    private JSlider seekSlider;
    private Timer seekTimer;
    private boolean isPlaying = false;
    private TablePanel tablePanel;
    private SongPanel songPanel;

    public JButton btnPlayPause, btnStop, btnDelete, btnClear;
    private JLabel currentTimeLabel, totalTimeLabel;
    private JLabel nowPlayingLabel;

    public ControlPanel(MusicController controller, TablePanel tablePanel, SongPanel songPanel) {

        this.controller = controller;
        this.tablePanel = tablePanel;
        this.songPanel = songPanel;
        

        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    private void initializeComponents() {
        // Control buttons with modern styling
        btnPlayPause = createStyledButton("▶", SPOTIFY_GREEN, 50, 50);
        btnStop = createStyledButton("⏹", BUTTON_COLOR, 40, 40);
        btnDelete = createStyledButton("🗑", BUTTON_DELETE, 40, 40);
        btnClear = createStyledButton("ℹ", new Color(173, 216, 230), 40, 40);
        
        enableControls(false);
        
        switchClear(tablePanel);

        // Time labels
        currentTimeLabel = new JLabel("0:00");
        currentTimeLabel.setForeground(SECONDARY_TEXT);
        currentTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        totalTimeLabel = new JLabel("0:00");
        totalTimeLabel.setForeground(SECONDARY_TEXT);
        totalTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Now playing label
        nowPlayingLabel = new JLabel("No song selected");
        nowPlayingLabel.setForeground(TEXT_COLOR);
        nowPlayingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Seek slider with modern styling
        seekSlider = new JSlider(0, 100, 0);
        seekSlider.setPreferredSize(new Dimension(400, 20));
        seekSlider.setBackground(BACKGROUND_COLOR);
        seekSlider.setForeground(SPOTIFY_GREEN);

        // Custom slider UI
        seekSlider.setUI(new javax.swing.plaf.basic.BasicSliderUI(seekSlider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Rectangle trackBounds = trackRect;
                int cy = (trackBounds.height / 2) - 2;
                int cw = trackBounds.width;

                // Background track
                g2d.setColor(SLIDER_COLOR);
                g2d.fillRoundRect(trackBounds.x, trackBounds.y + cy, cw, 4, 4, 4);

                // Progress track
                int progressWidth = (int) (cw * ((double) seekSlider.getValue() / seekSlider.getMaximum()));
                g2d.setColor(SPOTIFY_GREEN);
                g2d.fillRoundRect(trackBounds.x, trackBounds.y + cy, progressWidth, 4, 4, 4);
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(TEXT_COLOR);
                g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
            }
        });

        seekSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (player != null) {
                    MediaPlayer mp = player.getMediaPlayer();
                    if (mp != null) {
                        Duration total = mp.getTotalDuration();
                        double percent = seekSlider.getValue() / 100.0;
                        mp.seek(total.multiply(percent));
                    }
                }
            }
        });
    }

    private void setupLayout() {
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(0, 100));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)));

        // Left panel - Now playing info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        leftPanel.add(nowPlayingLabel);

        // Center panel - Controls and seek bar
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Control buttons panel
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controlsPanel.setBackground(BACKGROUND_COLOR);
        controlsPanel.add(btnDelete);
        controlsPanel.add(btnPlayPause);
        controlsPanel.add(btnStop);
        controlsPanel.add(btnClear);

        // Seek panel
        JPanel seekPanel = new JPanel(new BorderLayout(10, 0));
        seekPanel.setBackground(BACKGROUND_COLOR);
        seekPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        seekPanel.add(currentTimeLabel, BorderLayout.WEST);
        seekPanel.add(seekSlider, BorderLayout.CENTER);
        seekPanel.add(totalTimeLabel, BorderLayout.EAST);

        centerPanel.add(controlsPanel);
        centerPanel.add(seekPanel);

        // Right panel - Volume control
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel volumeLabel = new JLabel("🔊");
        volumeLabel.setForeground(TEXT_COLOR);
        volumeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setPreferredSize(new Dimension(100, 20));
        volumeSlider.setBackground(BACKGROUND_COLOR);
        volumeSlider.setForeground(SPOTIFY_GREEN);

        // Apply same custom UI to volume slider
        volumeSlider.setUI(new javax.swing.plaf.basic.BasicSliderUI(volumeSlider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Rectangle trackBounds = trackRect;
                int cy = (trackBounds.height / 2) - 2;
                int cw = trackBounds.width;

                g2d.setColor(SLIDER_COLOR);
                g2d.fillRoundRect(trackBounds.x, trackBounds.y + cy, cw, 4, 4, 4);

                int progressWidth = (int) (cw * ((double) volumeSlider.getValue() / volumeSlider.getMaximum()));
                g2d.setColor(SPOTIFY_GREEN);
                g2d.fillRoundRect(trackBounds.x, trackBounds.y + cy, progressWidth, 4, 4, 4);
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(TEXT_COLOR);
                g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
            }
        });

        volumeSlider.addChangeListener(e -> {
            if (player != null) {
                int value = volumeSlider.getValue();
                player.setVolume(value / 100.0);
            }
        });

        rightPanel.add(volumeLabel);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(volumeSlider);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private void setupEventListeners() {
        btnPlayPause.addActionListener(e -> {
            if (player == null) return;
            if (!isPlaying) {
                player.play();
                startSeekUpdater();
                btnPlayPause.setText("⏸");
                isPlaying = true;
            } else {
                player.pause();
                btnPlayPause.setText("▶");
                isPlaying = false;
            }
        });

        btnStop.addActionListener(e -> {
            if (isPlaying) {
                player.stopPlayback();
                stopSeekUpdater();
                btnPlayPause.setText("▶");
                isPlaying = false;
                seekSlider.setValue(0);
                currentTimeLabel.setText("0:00");
            }
            
            else {
            	player.stopPlayback();
                stopSeekUpdater();
                btnPlayPause.setText("▶");
                seekSlider.setValue(0);
                currentTimeLabel.setText("0:00");
            }
        });

        btnDelete.addActionListener(e -> {
            int result = showStyledConfirmDialog(
                    "Are you sure you want to delete this song?",
                    "Confirm Deletion"
            );

            if (result == JOptionPane.YES_OPTION) {
                if (isPlaying) {
                    player.stopPlayback();
                    stopSeekUpdater();
                    btnPlayPause.setText("▶");
                    controller.deleteSong(id);
                    tablePanel.loadSongs();
                } else {
                    stopSeekUpdater();
                    controller.deleteSong(id);
                    tablePanel.loadSongs();
                }

                // Reset UI
                nowPlayingLabel.setText("No song selected");
                enableControls(false);
            }
        });
        
        btnClear.addActionListener(e -> {
            int result = showStyledConfirmDialog(
                    "Are you sure you want to delete all songs?",
                    "Confirm Deletion"
            );

            if (result == JOptionPane.YES_OPTION) {
            	if (player != null) {
        			player.stopPlayback();
	        		stopSeekUpdater();
	        		btnPlayPause.setText("▶");
        		}
    
            	controller.deleteAll();
        		songPanel.clearSongPanel();
        		tablePanel.loadSongs();
        		enableControls(false);
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE); // or any preferred text color

        // Use emoji-compatible font with fallback
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 18);
        if (!emojiFont.canDisplay(text.charAt(0))) {
            emojiFont = new Font("Dialog", Font.PLAIN, 18);
        }
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(width, height));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }


    private void startSeekUpdater() {
        stopSeekUpdater();
        seekTimer = new Timer();
        seekTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (player != null) {
                    MediaPlayer mp = player.getMediaPlayer();
                    if (mp != null) {
                        Duration current = mp.getCurrentTime();
                        Duration total = mp.getTotalDuration();
                        if (total.toMillis() > 0) {
                            int progress = (int) (100 * current.toMillis() / total.toMillis());
                            SwingUtilities.invokeLater(() -> {
                                seekSlider.setValue(progress);
                                currentTimeLabel.setText(formatTime(current));
                                totalTimeLabel.setText(formatTime(total));
                            });
                        }
                    }
                }
            }
        }, 0, 500);
    }

    private void stopSeekUpdater() {
        if (seekTimer != null) {
            seekTimer.cancel();
            seekTimer = null;
        }
    }

    private String formatTime(Duration duration) {
        int totalSeconds = (int) duration.toSeconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void setPlayer(PlayerThread player) {
        this.player = player;
        btnPlayPause.setEnabled(true);
        btnStop.setEnabled(true);
        btnPlayPause.setText("▶");
        isPlaying = false;
    }

    public void enableControls(boolean enabled) {
        btnPlayPause.setEnabled(enabled);
        btnStop.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
    }
    

    public void setId(Long id) {
        this.id = id;
    }

    public void setNowPlaying(String songTitle, String artist) {
        if (songTitle != null && !songTitle.isEmpty()) {
            nowPlayingLabel.setText(songTitle + " - " + artist);
        } else {
            nowPlayingLabel.setText("No song selected");
        }
    }
    
    public void switchClear(TablePanel tablePanel) {
   	 if (tablePanel != null && tablePanel.getTableModel() != null &&
        	    tablePanel.getTableModel().getRowCount() > 0) {
        		btnClear.setEnabled(true);
        }
        
        else {
        	btnClear.setEnabled(false);
        }
   }

    public Object getPlayer() {
        return player;
    }

    private int showStyledConfirmDialog(String message, String title) {
        // Create custom dialog with dark theme
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(25, 25, 25));
        dialog.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(TEXT_COLOR);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(25, 25, 25));

        JButton yesButton = createStyledButton("Yes", new Color(231, 76, 60), 80, 35);
        JButton noButton = createStyledButton("No", BUTTON_COLOR, 80, 35);

        final int[] result = {JOptionPane.CANCEL_OPTION};

        yesButton.addActionListener(e -> {
            result[0] = JOptionPane.YES_OPTION;
            dialog.dispose();
        });

        noButton.addActionListener(e -> {
            result[0] = JOptionPane.NO_OPTION;
            dialog.dispose();
        });

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
        return result[0];
    }
}