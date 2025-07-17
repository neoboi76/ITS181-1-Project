package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        // Initialize JavaFX environment (no UI shown, just enables MediaPlayer)
        new JFXPanel();

        SwingUtilities.invokeLater(() -> new MusicPlayer().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("JavaFX Audio Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);
        frame.setLayout(new FlowLayout());

        JButton btnPlay = new JButton("Play");
        JButton btnPause = new JButton("Pause");
        JButton btnStop = new JButton("Stop");

        frame.add(btnPlay);
        frame.add(btnPause);
        frame.add(btnStop);

        // ⚠️ CHANGE THIS TO A VALID AUDIO FILE ON YOUR PC
        String songPath = "src/resources/01. Duvet.mp3";

        File audioFile = new File(songPath);
        if (!audioFile.exists()) {
            JOptionPane.showMessageDialog(frame, "Audio file not found:\n" + songPath, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Media media = new Media(audioFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        btnPlay.addActionListener(e -> mediaPlayer.play());
        btnPause.addActionListener(e -> mediaPlayer.pause());
        btnStop.addActionListener(e -> mediaPlayer.stop());

        frame.setVisible(true);
    }
}
