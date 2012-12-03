package pl.project.io;

import java.io.File;

public class OBJParser {
	
	private FilesManager filesManager;
	private File openFile;
	
	public OBJParser(FilesManager filesManager) {
		this.filesManager = filesManager;
		
		openFile = this.filesManager.getOpenFile();
	}

}
