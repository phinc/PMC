package by.phinc.pmc.util;

import static by.phinc.pmc.util.Constants.RESORCE_BUNDLE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.phinc.pmc.exception.PMCException;
import by.phinc.pmc.model.beans.Document;

public class DocumentUtil {
	
	public static final String REPOSITORY_PATH;
	
	public static final int ATTEMPT_NUM = 10;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle(
				RESORCE_BUNDLE);
		REPOSITORY_PATH = bundle.getString("upload.files.repository");
	}
	
	
	public static void saveDocument(File srcFile, Document document) {
	    try {
	        File destFile = new File(REPOSITORY_PATH + File.separator + 
	        		createFilename(document.getName()));
	        int i = 0;
	        while (destFile.exists() && i++ < ATTEMPT_NUM) {
	            destFile = new File(REPOSITORY_PATH + File.separator + 
	            		createFilename(document.getName()));
	        }
	        if (destFile.exists()) {
	        	throw new PMCException("Destination file already exists " 
	        							+ destFile.getName());
	        }
	        document.setPath(destFile.getPath());
	        document.setFilename(destFile.getName());
	        
	        //copy data to new file
	        FileChannel in = new FileInputStream(srcFile).getChannel();
	        FileChannel out = new FileOutputStream(destFile).getChannel();
	        in.transferTo(0, in.size(), out);
	    } catch (IOException e) {
	    	throw new PMCException(e);
	    } finally {
	          srcFile.delete();
	    }
	}
	
	private static String createFilename(String oldFilename) {
		Pattern pattern = Pattern.compile("(.*)(\\..+)");
		Matcher matcher = pattern.matcher(oldFilename);
		if (matcher.matches()) {
			String name = matcher.group(1);
			String postfix = matcher.group(2);
			return name + "_" + (new Date()).getTime() + postfix;
		}
		throw new PMCException("Filename format error: " + oldFilename);
	}
	
	public static void deleteDocument(Document document) {
		File file = new File(document.getPath());
		if (file.exists()) {
			if (!file.delete()) {
		           throw new PMCException(document.getName() + 
		        		   				" was not successfully deleted"); 
		       }
		}
	}
}
