package View;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */

//JPanel that displays selected song information on the left 

public class SongPanel extends JPanel {

    // Spotify-inspired colors
    private static final Color BACKGROUND_COLOR = new Color(18, 18, 18);
    private static final Color PANEL_COLOR = new Color(25, 25, 25);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color SECONDARY_TEXT = new Color(179, 179, 179);
    private static final Color SPOTIFY_GREEN = new Color(29, 185, 84);

    private final JLabel titleLabel = new JLabel("No song selected", SwingConstants.CENTER);
    private final JLabel artistLabel = new JLabel("Unknown Artist", SwingConstants.CENTER);
    private final JLabel albumLabel = new JLabel("Unknown Album", SwingConstants.CENTER);
    private final JLabel durationLabel = new JLabel("0:00", SwingConstants.CENTER);
    private final JLabel coverLabel = new JLabel();
    private final JTextArea lyricsArea = new JTextArea();
    private final JScrollPane lyricsScroll = new JScrollPane(lyricsArea);

    public SongPanel() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(350, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(40, 40, 40)));

        // Song title
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);

        // Artist name
        artistLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        artistLabel.setForeground(SECONDARY_TEXT);

        // Album name
        albumLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        albumLabel.setForeground(SECONDARY_TEXT);

        // Duration
        durationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        durationLabel.setForeground(SPOTIFY_GREEN);

        // Album cover
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverLabel.setPreferredSize(new Dimension(280, 280));
        coverLabel.setBackground(PANEL_COLOR);
        coverLabel.setOpaque(true);
        coverLabel.setBorder(BorderFactory.createBevelBorder(8));

        // Default album cover
        setDefaultAlbumCover();

        // Lyrics area
        lyricsArea.setEditable(false);
        lyricsArea.setLineWrap(true);
        lyricsArea.setWrapStyleWord(true);
        lyricsArea.setBackground(PANEL_COLOR);
        lyricsArea.setForeground(TEXT_COLOR);
        lyricsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lyricsArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        lyricsArea.setText("Select a song to view lyrics...");

        //lyricsScroll = new JScrollPane(lyricsArea);
        lyricsScroll.setBackground(PANEL_COLOR);
        lyricsScroll.getViewport().setBackground(PANEL_COLOR);
        lyricsScroll.setBorder(BorderFactory.createBevelBorder(8));
        lyricsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        lyricsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Style scrollbar
        styleScrollBar(lyricsScroll.getVerticalScrollBar());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Song info panel (header)
        JPanel songInfoPanel = new JPanel();
        songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
        songInfoPanel.setBackground(BACKGROUND_COLOR);
        songInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel nowPlayingHeader = new JLabel("NOW PLAYING", SwingConstants.CENTER);
        nowPlayingHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nowPlayingHeader.setForeground(SPOTIFY_GREEN);
        nowPlayingHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        songInfoPanel.add(nowPlayingHeader);
        songInfoPanel.add(Box.createVerticalStrut(10));
        songInfoPanel.add(titleLabel);
        songInfoPanel.add(Box.createVerticalStrut(5));
        songInfoPanel.add(artistLabel);
        songInfoPanel.add(Box.createVerticalStrut(5));
        songInfoPanel.add(albumLabel); // newly added
        songInfoPanel.add(Box.createVerticalStrut(10));
        songInfoPanel.add(durationLabel);

        // Album cover panel
        JPanel coverPanel = new JPanel(new BorderLayout());
        coverPanel.setBackground(BACKGROUND_COLOR);
        coverPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        coverPanel.add(coverLabel, BorderLayout.CENTER);

        // Combine song info and cover into one top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(songInfoPanel, BorderLayout.NORTH);
        topPanel.add(coverPanel, BorderLayout.CENTER);

        // Lyrics panel (scrolling)
        JPanel lyricsPanel = new JPanel(new BorderLayout());
        lyricsPanel.setBackground(BACKGROUND_COLOR);

        JLabel lyricsHeader = new JLabel("LYRICS", SwingConstants.LEFT);
        lyricsHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lyricsHeader.setForeground(SPOTIFY_GREEN);
        lyricsHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Set preferred height for lyricsScroll
        lyricsScroll.setPreferredSize(new Dimension(lyricsScroll.getPreferredSize().width, 400));

        lyricsPanel.add(lyricsHeader, BorderLayout.NORTH);
        lyricsPanel.add(lyricsScroll, BorderLayout.CENTER);

        // Apply layout
        add(topPanel, BorderLayout.NORTH);
        add(lyricsPanel, BorderLayout.CENTER); // Correct panel here, not lyricsScroll directly
    }



    private void setDefaultAlbumCover() {
        // Create a default album cover with music note icon
        ImageIcon defaultIcon = createDefaultCoverIcon();
        coverLabel.setIcon(defaultIcon);
    }

    private ImageIcon createDefaultCoverIcon() {
        int size = 280;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background gradient
        GradientPaint gradient = new GradientPaint(0, 0, PANEL_COLOR, size, size, PANEL_COLOR.darker());
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, size, size, 8, 8);

        // Music note icon
        g2d.setColor(SECONDARY_TEXT);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        FontMetrics fm = g2d.getFontMetrics();
        String musicNote = "♪";
        int x = (size - fm.stringWidth(musicNote)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(musicNote, x, y);

        g2d.dispose();
        return new ImageIcon(img);
    }

    private void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 100, 100);
                this.trackColor = PANEL_COLOR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(thumbColor);
                g2d.fillRoundRect(thumbBounds.x + 2, thumbBounds.y, thumbBounds.width - 4, thumbBounds.height, 6, 6);
            }
        });
    }

    public void setSong(SongEvent song) {
        if (song == null) {
            titleLabel.setText("No song selected");
            artistLabel.setText("Unknown Artist");
            albumLabel.setText("Unknown Album");
            durationLabel.setText("0:00");
            setDefaultAlbumCover();
            lyricsArea.setText("Select a song to view lyrics...");
            return;
        }

        // Set song information
        titleLabel.setText(song.getTitle() != null ? song.getTitle() : "Unknown Title");
        artistLabel.setText(song.getArtist() != null ? song.getArtist() : "Unknown Artist");
        albumLabel.setText(song.getAlbum() != null ? song.getAlbum() : "Unknown Album");
        durationLabel.setText(song.getDuration() != null ? song.getDuration() : "0:00");

        //// 1) Artist name
        artistLabel.setText("Artist: " + song.getArtist());

	     // 2) Album name
	     albumLabel.setText("Album: " + song.getAlbum());
	
	     // 3) Album cover image
	     String imgPath = song.getImagePath();
	     if (imgPath != null && !imgPath.isBlank()) {
	         ImageIcon icon = new ImageIcon(imgPath);
	         Image img = icon.getImage().getScaledInstance(310, 295, Image.SCALE_SMOOTH);
	         coverLabel.setIcon(new ImageIcon(img));
	     } else {
	         coverLabel.setIcon(null);
	     }
	
	     // 4) Lyrics (load from SongEntity.getLyrics())
	     try {
	         String lyricsPath = song.getLyrics();
	         if (lyricsPath != null) {
	             String lyrics = new String(Files.readAllBytes(Paths.get(lyricsPath)));
	             lyricsArea.setText(lyrics);
	         } else {
	             lyricsArea.setText("No Lyrics Found");
	         }
	     } catch (IOException e) {
	         lyricsArea.setText("No Lyrics Found");
	     }
	
	     lyricsArea.setCaretPosition(0);

    }
    
    public void clearSongPanel() {
    	titleLabel.setText("No song selected");
        artistLabel.setText("Unknown Artist");
        albumLabel.setText("Unknown Album");
        durationLabel.setText("0:00");
        setDefaultAlbumCover();
        lyricsArea.setText("Select a song to view lyrics...");
    }
}