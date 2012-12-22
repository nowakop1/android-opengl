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

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import pl.project.model.DataStructure;
import pl.project.model.FaceIndex;
import pl.project.model.Model;
import pl.project.model.Vertex;

public class OBJParser {
	
	private Context context;
	
	private FilesManager filesManager;
	private File openFile;
	
	Vector<Float> positions = new Vector<Float>();
	List<Vector<Float>>	normals = new ArrayList<Vector<Float>>();
	List<FaceIndex> faces = new ArrayList<FaceIndex>();
	Model model = new Model();
	
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
					
					model.setVertices(positions);
					model.setIndices(faces);
					
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
	}

	private void readFaces(String line) {
		Log.d("readFaces", "czytam");
		
		String [] tokens = line.split("[ ]+");
		List<Short> verticesIndex = new ArrayList<Short>();
		List<Short> texturesIndex = new ArrayList<Short>();
		List<Short> normalsIndex = new ArrayList<Short>();
		FaceIndex faceIndex = new FaceIndex();
		
		for(String token : tokens) {
			String fixToken = token.replaceAll("//", "/0/");
			String [] tempTokens = fixToken.split("/");
			
			if(tempTokens.length == 1) {
				verticesIndex.add(Short.parseShort(tempTokens[0]));
			} else if(tempTokens.length == 2) {
				verticesIndex.add(Short.parseShort(tempTokens[0]));
				texturesIndex.add(Short.parseShort(tempTokens[1]));
				
			} else if(tempTokens.length == 3){
				verticesIndex.add(Short.parseShort(tempTokens[0]));
				texturesIndex.add(Short.parseShort(tempTokens[1]));
				normalsIndex.add(Short.parseShort(tempTokens[2]));
				
				System.out.println(tempTokens[0] + ", " + tempTokens[1] + ", " + tempTokens[2]);
			}
		}
		Short [] temp = new Short[tokens.length];
		
		temp = copyToIntArray(verticesIndex);
		faceIndex.setPositionIndex(temp);
		temp = copyToIntArray(texturesIndex);
		faceIndex.setTextureIndex(temp);
		temp = copyToIntArray(normalsIndex);
		faceIndex.setNormalIndex(temp);
		
		System.out.println(faceIndex.getPositionIndex()[0] + " " + faceIndex.getPositionIndex()[1] + " " + faceIndex.getPositionIndex()[2]);
		
		faces.add(faceIndex);
	}

	private void readVertices(String line) {
		Log.d("readVertices", "czytam");
			
		String [] tokens = line.split("[ ]+");
		
		for(String token : tokens) {
			System.out.println(token);
			positions.add(Float.parseFloat(token));
		}
			
		//System.out.println(vertex.toString());
	}

	private void readNormals(String line) {
		Log.d("readNormals", "czytam");
		
		String [] tokens = line.split("[ ]+");
		Vector<Float> normal = new Vector<Float>();
		
		for(String token : tokens) {
			normal.add(Float.parseFloat(token));
		}
		
		normals.add(normal);
		System.out.println(normal.toString());
	}
	
	private Short[] copyToIntArray(List<Short> list) {
		
		Short [] intArray = new Short[list.size()];
		
		for(int i = 0; i < list.size(); i++) {
			intArray[i] = list.get(i);
		}
		
		return intArray;
	}

}
