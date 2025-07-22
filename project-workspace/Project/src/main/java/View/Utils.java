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
	    return null; 
	}

    
    public static String removeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }
    
    public static String getMeta(Media media, String key) {
	    Object value = media.getMetadata().get(key);
	    return value != null ? value.toString() : "Unknown";
	}
    
    public static String formatDuration(double seconds) {
        int totalSeconds = (int) Math.round(seconds);
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%d:%02d", mins, secs);
    }


    
    public static String getImagePath(String fileLocation) {
    	
    	File file = new File(fileLocation);
    	File folder = file.getParentFile();
    	
    	if (folder != null && folder.isDirectory()) {
    		
    		File[] files = folder.listFiles();
    		
    		if (files != null) {
    			
    			for (File f : files) {
    				
    				if ("png".equalsIgnoreCase(getFileExtension(f))
    						|| "jpg".equalsIgnoreCase(getFileExtension(f))|| "jpeg".equalsIgnoreCase(getFileExtension(f))) {
    					
    					return f.getAbsolutePath();
    					
    				}
    					
    			}
    			
    		
    			
    		}

    	}
    	
		return "No path found";
    	
    }
    

	public static String getLyricsPath(String fileLocation) {
		File file = new File(fileLocation);
    	File folder = file.getParentFile();
    	
    	if (folder != null && folder.isDirectory()) {
    		
    		File[] files = folder.listFiles();
    		
    		if (files != null) {
    			
    			for (File f : files) {
    				
    				if ("txt".equalsIgnoreCase(getFileExtension(f))) {
    					
    					return f.getAbsolutePath();
    					
    				}
    					
    			}
    			
    		
    			
    		}

    	}
    	
		return "No path found";
    	
	}

}
