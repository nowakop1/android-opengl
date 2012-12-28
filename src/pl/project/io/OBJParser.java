package pl.project.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Debug;
import android.widget.Toast;

import pl.project.model.DataStructure;
import pl.project.model.Face;
import pl.project.model.Model;

public class OBJParser {
	
	private Context context;
	
	private FilesManager filesManager;
	private File openFile;
	
	Model model;
	
	public OBJParser(FilesManager filesManager, Context context) {
		this.filesManager = filesManager;
		this.context = context;
		
		openFile = this.filesManager.getOpenFile();
	}
	
	public void parse() {
		Debug.startMethodTracing();
		
		BufferedReader reader;
		String line;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(openFile)));
			
			Toast toast = Toast.makeText(context, "Trwa parsowanie", Toast.LENGTH_SHORT);
			while((line = reader.readLine()) != null) {
				toast.show();
				try {
					if(line.startsWith("vn")) {
						line = line.substring(2);
						readNormals(line.trim());
					} else if(line.startsWith("vt")) {

					} else if(line.startsWith("v")) {
						line = line.substring(1);
						readVertices(line.trim());
					} else if(line.startsWith("f")) {
						line = line.substring(1);
						readFaces(line.trim());
					}
					
					model = new Model();
					DataStructure.setModel(model);
					
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
		
		Toast toast = Toast.makeText(context, "Parsowanie zakoñczone!", Toast.LENGTH_SHORT);
		toast.show();
		System.out.println("Parsowanie zakoñczone");
		Debug.stopMethodTracing();
	}

	private void readFaces(String line) {
		
		String [] tokens = line.split("[ ]+");
		Face face = new Face();
		
		for(String token : tokens) {
			String fixToken = token.replaceAll("//", "/0/");
			String [] tempTokens = fixToken.split("/");
			
			if(tempTokens.length == 1) {
				face.addvPointer(Short.parseShort(tempTokens[0]));
			} else if(tempTokens.length == 2) {
				face.addvPointer(Short.parseShort(tempTokens[0]));
				face.addVtPointer(Short.parseShort(tempTokens[1]));
				
			} else if(tempTokens.length == 3){
				face.addvPointer(Short.parseShort(tempTokens[0]));
				face.addVtPointer(Short.parseShort(tempTokens[1]));
				face.addVnPointer(Short.parseShort(tempTokens[2]));
			}
		}
		System.out.println("Positions: " + face.getvPointers());
		System.out.println("Normals: " + face.getVnPointers());
		DataStructure.getFaces().add(face);
	}

	private void readVertices(String line) {			
		String [] tokens = line.split("[ ]+");
		
		for(String token : tokens) {
			System.out.println("Position: " + token);
			DataStructure.getPositions().add(Float.parseFloat(token));		//d³ugi wektor (wszystkie pozycje)
		}
	}

	private void readNormals(String line) {		
		String [] tokens = line.split("[ ]+");
		
		for(String token : tokens) {
			System.out.println("Normal: " + token);
			DataStructure.getNormals().add(Float.parseFloat(token));		//d³ugi wektor (wszystkie normalne)
		}
	}
}
