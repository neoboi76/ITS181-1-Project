package View;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class SongFileFilter extends FileFilter{

	@Override
	public boolean accept(File file) {
		
		if (file.isDirectory()) 
			return true;
		
		
		String name = file.getName();
		String extension = Utils.getFileExtension(name);
		
		if (extension == null) {
			return false;
		}
		
		if (extension.equals("saf") || extension.equals("mp3"))
			return true;
		
		return false;
		
		
	}

	@Override
	public String getDescription() {
		return "Song database files(*.saf)";
	}

}
