package View;

import java.io.File;
import javax.swing.filechooser.FileFilter; 

public class SongFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) return true;

        String extension = Utils.getFileExtension(file);
        if (extension == null) return false;

        return extension.equals(Reference.songExtension) || extension.equals(Reference.databaseExtension);
    }

    @Override
    public String getDescription() {
        return "Audio files (*.mp3) and Song database files (*.saf)";
    }
}
