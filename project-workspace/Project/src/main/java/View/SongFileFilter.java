package View;

import java.io.File;
import javax.swing.filechooser.FileFilter; 

/*
 * Project Created by Group 6:
 * 	Kenji Mark Alan Arceo
 *  Ryonan Owen Ferrer
 *  Dino Alfred Timbol
 *  Mike Emil Vocal
 */

//SongFile filter for JFileChooser

public class SongFileFilter extends FileFilter {
	
	public static final String songExtension = "mp3";
	public static final String databaseExtension = "saf";

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) return true;

        String extension = Utils.getFileExtension(file);
        if (extension == null) return false;

        return extension.equals(songExtension) || extension.equals(databaseExtension);
    }

    @Override
    public String getDescription() {
        return "Audio files (*.mp3) and Song database files (*.saf)";
    }
}
