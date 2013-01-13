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
	
	private String [] tokens;
	private Face face;
	private float value;
	
	private int numberOfVertices = 0;
	private int numberOfNormals = 0;
	private int numberOfFaces = 0;
	
	Model model;
	
	public OBJParser(FilesManager filesManager, Context context) {
		this.filesManager = filesManager;
		this.context = context;
		
		openFile = this.filesManager.getOpenFile();
	}
	
	public void parse() {
		long time1 = System.currentTimeMillis();
		
		BufferedReader reader;
		String line;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(openFile)));
			
//			Toast toast = Toast.makeText(context, "Trwa parsowanie", Toast.LENGTH_SHORT);
			while((line = reader.readLine()) != null) {
//				toast.show();
//				try {
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
					
					DataStructure.setNumbersOfVertices(numberOfVertices);
					DataStructure.setNumberOfNormals(numberOfNormals);
					DataStructure.setNumberOfFaces(numberOfFaces);
//					model = new Model();
//					DataStructure.setModel(model);
					
//				} catch(NullPointerException e) {
//					e.printStackTrace();
//					System.out.println("B³êdny format pliku");
//				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Nie znaleziono pliku");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Nie mo¿na odczytaæ pliku");
		}
		
//		Toast toast = Toast.makeText(context, "Parsowanie zakoñczone!", Toast.LENGTH_SHORT);
//		toast.show();
		long time2 = System.currentTimeMillis() - time1;
		System.out.println("Parsowanie zakoñczone, czas: " + time2);
		System.out.println("Vertices: " + numberOfVertices / 3 + " " + "Faces: " + numberOfFaces);
	}

	private void readFaces(String line) {
		
		tokens = line.split("[ ]+");
		face = new Face();
		
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
		DataStructure.getFaces()[numberOfFaces++] = face;
	}

	private void readVertices(String line) {			
		tokens = line.split("[ ]+");
		int size = tokens.length;
		
		for(int i = 0; i < size; i++) {
			//System.out.println("Position: " + tokens[i]);
			value = Float.parseFloat(tokens[i]);
			DataStructure.getPositions()[numberOfVertices++] = value;		//d³ugi wektor (wszystkie pozycje)
		}
	}

	private void readNormals(String line) {		
		tokens = line.split("[ ]+");
		int size = tokens.length;
		
		for(int i = 0; i < size; i++) {
			System.out.println("Normal: " + tokens[i]);
			value = Float.parseFloat(tokens[i]);
			DataStructure.getNormals()[numberOfNormals++] = value;		//d³ugi wektor (wszystkie normalne)
		}
	}
	
	public static float parseFloat(String f) {
		final int len   = f.length();
		float     ret   = 0f;         // return value
		int       pos   = 0;          // read pointer position
		int       part  = 0;          // the current part (int, float and sci parts of the number)
		boolean   neg   = false;      // true if part is a negative number
	 
		// find start
		while (pos < len && (f.charAt(pos) < '0' || f.charAt(pos) > '9') && f.charAt(pos) != '-' && f.charAt(pos) != '.')
			pos++;
	 
		// sign
		if (f.charAt(pos) == '-') { 
			neg = true; 
			pos++; 
		}
	 
		// integer part
		while (pos < len && !(f.charAt(pos) > '9' || f.charAt(pos) < '0'))
			part = part*10 + (f.charAt(pos++) - '0');
		ret = neg ? (float)(part*-1) : (float)part;
	 
		// float part
		if (pos < len && f.charAt(pos) == '.') {
			pos++;
			int mul = 1;
			part = 0;
			while (pos < len && !(f.charAt(pos) > '9' || f.charAt(pos) < '0')) {
				part = part*10 + (f.charAt(pos) - '0'); 
				mul*=10; pos++;
			}
			ret = neg ? ret - (float)part / (float)mul : ret + (float)part / (float)mul;
		}
	 
		// scientific part
		if (pos < len && (f.charAt(pos) == 'e' || f.charAt(pos) == 'E')) {
			pos++;
			neg = (f.charAt(pos) == '-'); pos++;
			part = 0;
			while (pos < len && !(f.charAt(pos) > '9' || f.charAt(pos) < '0')) {
				part = part*10 + (f.charAt(pos++) - '0'); 
			}
			if (neg)
				ret = ret / (float)Math.pow(10, part);
			else
				ret = ret * (float)Math.pow(10, part);
		}	
		return ret;
	}
}
