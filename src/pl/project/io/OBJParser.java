package pl.project.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.util.Log;

import pl.project.model.FaceIndex;

public class OBJParser {
	
	private FilesManager filesManager;
	private File openFile;
	
	List<Vector<Float>>	positions = new ArrayList<Vector<Float>>();
	List<Vector<Float>>	normals = new ArrayList<Vector<Float>>();
	List<FaceIndex> faces = new ArrayList<FaceIndex>();
	
	public OBJParser(FilesManager filesManager) {
		this.filesManager = filesManager;
		
		openFile = this.filesManager.getOpenFile();
	}
	
	public void parse() {
		
		BufferedReader reader;
		String line;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(openFile)));
			
			while((line = reader.readLine()) != null) {
				try {
					if(line.startsWith("vn")) {
						readNormals();
					} else if(line.startsWith("v")) {
						line = line.substring(1);
						readVertices(line.trim());
					} else if(line.startsWith("f")) {
						readFaces();
					}
				} catch(NullPointerException e) {
					e.printStackTrace();
					System.out.println("B³êdny format pliku");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Nie znaleziono pliku");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Nie mo¿na odczytaæ pliku");
		}
		
		System.out.println("Parsowanie zakoñczone");
	}

	private void readFaces() {
		Log.d("readFaces", "czytam");
		
	}

	private void readVertices(String line) {
		Log.d("readVertices", "czytam");
		
		String [] tokens = line.split("[ ]+");
		Vector<Float> vertex = new Vector<Float>();
		
		for(String token : tokens) {
			System.out.println(token);
			//vertex.add(Float.parseFloat(token));
		}
		
		positions.add(vertex);
		
		System.out.println(vertex.toString());
	}

	private void readNormals() {
		Log.d("readNormals", "czytam");
		
	}

}
