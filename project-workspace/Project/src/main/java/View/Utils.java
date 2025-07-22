package View;

import java.io.File;

import javafx.scene.media.Media;

public class Utils {
	
	public static String getFileExtension(File file) {
	    String name = file.getName();
	    int lastDot = name.lastIndexOf('.');
	    if (lastDot > 0 && lastDot < name.length() - 1) {
	        return name.substring(lastDot + 1).toLowerCase(); // returns "mp3", "saf", etc.
	    }
	    return null; // no extension found
	}

    
    public static String removeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }
    
    public static String getMeta(Media media, String key) {
	    Object value = media.getMetadata().get(key);
	    return value != null ? value.toString() : "Unknown";
	}
    
    public static String formatDuration(double duration) {
        int minutes = (int) duration;
        int seconds = (int) Math.round((duration - minutes) * 60);

        // Handle rounding cases (e.g., 2.999 rounds to 3:00)
        if (seconds == 60) {
            minutes += 1;
            seconds = 0;
        }

        return String.format("%d:%02d", minutes, seconds);
    }

}
