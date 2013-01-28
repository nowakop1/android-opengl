package pl.project.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import pl.project.model.DataStructure;
import pl.project.model.Face;
import pl.project.model.Model;

public class OBJParser {
		
	private FilesManager filesManager;
	private File openFile;
	
	private String [] tokens;
	private String [] tempTokens;
	private String fixToken;
	private Face face;
	private float value;
	
	private int numberOfVertices = 0;
	private int numberOfNormals = 0;
	private int numberOfFaces = 0;
	
	private float [] minValues = new float [3];
	private float [] maxValues = new float [3];
	
	private Pattern pattern = Pattern.compile("[ ]+");
	private Pattern pattern2 = Pattern.compile("/");
	
	Model model;
	
	public OBJParser(FilesManager filesManager) {
		this.filesManager = filesManager;
		
		openFile = this.filesManager.getOpenFile();
	}
	
	public boolean parse() {
		long time1 = System.currentTimeMillis();
		
		BufferedReader reader;
		String line;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(openFile)));
			
			while((line = reader.readLine()) != null) {
								
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
				} else {
					continue;
				}
			}	
			
			DataStructure.setNumbersOfVertices(numberOfVertices);
			DataStructure.setNumberOfNormals(numberOfNormals);
			DataStructure.setNumberOfFaces(numberOfFaces);
			
			DataStructure.minValues = minValues;
			DataStructure.maxValues = maxValues;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Nie znaleziono pliku");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Nie mo¿na odczytaæ pliku");
		}
		
		long time2 = System.currentTimeMillis() - time1;
		System.out.println("Parsowanie zakoñczone, czas: " + time2);
		System.out.println("Vertices: " + numberOfVertices / 3 + " " + "Faces: " + numberOfFaces);
		
		return true;
	}

	private void readFaces(String line) {
		
		tokens = pattern.split(line);
		face = new Face();
		
		for(String token : tokens) {
			fixToken = token.replaceAll("//", "/0/");
			tempTokens = pattern2.split(fixToken);
			
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
//		System.out.println("Positions: " + face.getvPointers());
//		System.out.println("Normals: " + face.getVnPointers());
		DataStructure.getFaces().add(numberOfFaces++, face);
	}

	private void readVertices(String line) {
		tokens = pattern.split(line);
		int size = tokens.length;
		
		for(int i = 0; i < size; i++) {
//			System.out.println("Position: " + tokens[i]);
			value = parseFloat(tokens[i]);
			DataStructure.getPositions().add(numberOfVertices++, value);		//d³ugi wektor (wszystkie pozycje)
			if(value < minValues[i])
				minValues[i] = value;
			else if(value > maxValues[i])
				maxValues[i] = value;
		}
	}

	private void readNormals(String line) {		
		tokens = pattern.split(line);
		int size = tokens.length;
		
		for(int i = 0; i < size; i++) {
//			System.out.println("Normal: " + tokens[i]);
			value = parseFloat(tokens[i]);
			DataStructure.getNormals().add(numberOfNormals++, value);		//d³ugi wektor (wszystkie normalne)
		}
	}
	
	//w³asna metoda parseFloat
	public static float parseFloat(String f) {
		float result = 0.0f;
		int position = 0;
		int intPart = 0;
		int floatPart = 0;
		int mul = 1;
		boolean negative = false;
		int length = f.length();
		
		if(f.charAt(position) == '-') {
			negative = true;
			position++;
		}
		
		while((position < length) && (f.charAt(position) != '.')) {
			intPart = intPart * 10 + (f.charAt(position++) - '0');
		}
		result = (float) intPart;
			
		if((position < length) && (f.charAt(position) == '.')) {
			position++;
			
			while(position < length) {
				floatPart = floatPart * 10 + (f.charAt(position++) - '0');
				mul *= 10;
			}
			result = (float) (intPart * mul + floatPart) / mul;
		}
		
		if(negative)
			result = result * -1;
		
		return result;
	}
}
