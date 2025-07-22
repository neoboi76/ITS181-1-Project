package View;

import Model.SongEntity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * SongPanel.java
 *
 * Placed in BorderLayout.WEST.  
 * Shows the selected song's Artist, Album Cover, and Lyrics.
 *
 * Usage (in your existing selection listener):
 *   SongEntity s = controller.getSongById(selectedId);
 *   songPanel.setSong(s);
 */
public class SongPanel extends JPanel {
    private final JLabel artistLabel  = new JLabel("Artist: —", SwingConstants.CENTER);
    private final JLabel coverLabel   = new JLabel();               // displays album art
    private final JTextArea lyricsArea = new JTextArea();
    private SongListener songListener;

    public SongPanel() {
        setLayout(new BorderLayout(8,8));
        setPreferredSize(new Dimension(260, 0));  
        setBorder(new TitledBorder("Now Playing"));

        // Artist at top
        artistLabel.setFont(artistLabel.getFont().deriveFont(Font.BOLD, 14f));
        add(artistLabel, BorderLayout.NORTH);

        // Cover in the center
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coverLabel.setPreferredSize(new Dimension(220, 220));
        add(coverLabel, BorderLayout.CENTER);

        // Lyrics at bottom (scrollable)
        lyricsArea.setEditable(false);
        lyricsArea.setLineWrap(true);
        lyricsArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(lyricsArea);
        scroll.setPreferredSize(new Dimension(260, 150));
        add(scroll, BorderLayout.SOUTH);
    }

   
    public void setSong(SongEvent song) {
        if (song == null) {
            artistLabel.setText("Artist: —");
            coverLabel.setIcon(null);
            lyricsArea.setText("");
            return;
        }

        // 1) Artist name
        artistLabel.setText("Artist: " + song.getArtist());

        // 2) Album cover image
        String imgPath = song.getImagePath();
        if (imgPath != null && !imgPath.isBlank()) {
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage()
                           .getScaledInstance(220, 220, Image.SCALE_SMOOTH);
            coverLabel.setIcon(new ImageIcon(img));
        } else {
            coverLabel.setIcon(null);
        }

        // 3) Lyrics (load from SongEntity.getLyrics())
        //    If you store lyrics directly in the entity, just setText(song.getLyrics()).
        //    If you store a path, read the file here:
        if (song.getLyrics() != null) {
            // assume getLyrics() returns the full lyrics text
            lyricsArea.setText(song.getLyrics());
        } else {
            lyricsArea.setText("");
        }
        lyricsArea.setCaretPosition(0);
    }
    
    public void setSongListener(SongListener listener) {
    	this.songListener = listener;
    }
}
