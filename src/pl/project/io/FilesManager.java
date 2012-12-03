package pl.project.io;

import java.io.File;

import android.os.Environment;

public class FilesManager {
	
	private boolean isStorageAvailable = false;
	private boolean isStorageWriteable = false;
	
	private String selectedFile;
	private File openFile;
	private String filesPath;
	
	public FilesManager() {
		checkStorage();
	}
	
	public String [] getListOfFiles() {
		/*
		 * TODO
		 */
		String [] list = null;
		
		if(isStorageAvailable) {		
			File sdCard = Environment.getExternalStorageDirectory();
			filesPath = sdCard.getPath() + "/obj_files/";
			
			File fileList = new File(filesPath);
			list = fileList.list();
		}

		return list;
	}
	
	private void checkStorage() {
		
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    isStorageAvailable = isStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    isStorageAvailable = true;
		    isStorageWriteable = false;
		} else {
		   isStorageAvailable = isStorageWriteable = false;
		}
	}

	public String getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(String selectedFile) {
		this.selectedFile = selectedFile;
	}

	public File getOpenFile() {
		return openFile;
	}

	public void openFile(String fileName) {
		openFile = new File(filesPath, fileName);
	}

}
