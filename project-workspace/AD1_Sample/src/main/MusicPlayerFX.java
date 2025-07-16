package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MusicPlayerFX extends Application {

    private PlayerThreadFX player;

    @Override
    public void start(Stage stage) {
        String filePath = "src/resources/Tokyo Neon Signs.mp3";
        player = new PlayerThreadFX(filePath, true);

        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button resumeButton = new Button("Resume");
        Button stopButton = new Button("Stop");

        // 🎚 Volume Slider (0.0 to 1.0)
        Slider volumeSlider = new Slider(0, 1, 0.5); // default at 50%
        volumeSlider.setPrefWidth(200);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(0.25);
        volumeSlider.setBlockIncrement(0.1);

        // 🔄 Set volume when slider moves
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            player.setVolume(newVal.doubleValue());
        });

        playButton.setOnAction(e -> player.play());
        pauseButton.setOnAction(e -> player.pause());
        resumeButton.setOnAction(e -> player.resume());
        stopButton.setOnAction(e -> player.stop());

        HBox controls = new HBox(10, playButton, pauseButton, resumeButton, stopButton);
        VBox layout = new VBox(20, controls, volumeSlider);
        layout.setStyle("-fx-padding: 20");

        Scene scene = new Scene(layout, 450, 150);
        stage.setScene(scene);
        stage.setTitle("JavaFX Music Player with Volume");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
