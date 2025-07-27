package View;

import java.io.File;

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */

//Utilities class that serves various functions throughout the project

public class Utils {
	
	//Returns file extension as string
	public static String getFileExtension(File file) {
	    String name = file.getName();
	    int lastDot = name.lastIndexOf('.');
	    if (lastDot > 0 && lastDot < name.length() - 1) {
	        return name.substring(lastDot + 1).toLowerCase(); 
	    }
	    return null; 
	}

    //Returns filename without extension as string
    public static String removeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }
    
    //Formats duration as "minutes:seconds" (e.g. "3:16")
    public static String formatDuration(double seconds) {
        int totalSeconds = (int) Math.round(seconds);
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%d:%02d", mins, secs);
    }


    //Returns path of album image as string
    public static String getImagePath(String fileLocation) {
        File file = new File(fileLocation);
        File folder = file.getParentFile();
        
        File placeholder = new File("C:\\Users\\Timbo\\OneDrive\\Desktop\\Documents\\Test\\placeholder.jpg");

        if (folder != null && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File f : files) {
                    String name = removeExtension(f.getName()).toLowerCase();
                    String ext = getFileExtension(f);

                    if (name.equals("album") && (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png"))) {
                        return f.getAbsolutePath();
                    }
                }
            }
        }

        return placeholder.getAbsolutePath(); // return placeholder if nothing found
    }

    //Returns path of lyrics text document as string
    public static String getLyricsPath(String fileLocation) {
        File file = new File(fileLocation);
        File folder = file.getParentFile();

        if (folder != null && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File f : files) {
                    String name = removeExtension(f.getName()).toLowerCase();
                    String ext = getFileExtension(f);

                    if (name.equals("lyrics") && ext.equals("txt")) {
                        return f.getAbsolutePath();
                    }
                }
            }
        }

        return "No path found";
    }

}