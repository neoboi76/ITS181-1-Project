package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusicPlayerFX extends Application {

    private PlayerThreadFX player;

    @Override
    public void start(Stage stage) {
        String filePath = "src/resources/01. Duvet.mp3";
        player = new PlayerThreadFX(filePath, true);

        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button resumeButton = new Button("Resume");
        Button stopButton = new Button("Stop");

        // 🎚 Volume Slider (0.0 to 1.0)
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setPrefWidth(200);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(0.25);
        volumeSlider.setBlockIncrement(0.1);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            player.setVolume(newVal.doubleValue());
        });

        // 📍 Playback slider (seek bar)
        Slider seekSlider = new Slider();
        seekSlider.setPrefWidth(400);

        // When user drags the slider
        seekSlider.setOnMousePressed(e -> {
            player.getMediaPlayer().pause();
        });
        seekSlider.setOnMouseReleased(e -> {
            double percent = seekSlider.getValue() / 100;
            Duration total = player.getMediaPlayer().getTotalDuration();
            player.getMediaPlayer().seek(total.multiply(percent));
            player.getMediaPlayer().play();
        });

        // Keep updating the slider while the song is playing
        player.getMediaPlayer().currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            Duration total = player.getMediaPlayer().getTotalDuration();
            if (total.toMillis() > 0) {
                double progress = newTime.toMillis() / total.toMillis() * 100;
                seekSlider.setValue(progress);
            }
        });

        playButton.setOnAction(e -> player.play());
        pauseButton.setOnAction(e -> player.pause());
        resumeButton.setOnAction(e -> player.resume());
        stopButton.setOnAction(e -> player.stop());

        HBox controls = new HBox(10, playButton, pauseButton, resumeButton, stopButton);
        VBox layout = new VBox(20, controls, volumeSlider, seekSlider);
        layout.setStyle("-fx-padding: 20");

        Scene scene = new Scene(layout, 500, 200);
        stage.setScene(scene);
        stage.setTitle("JavaFX Music Player with Seek Bar");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}