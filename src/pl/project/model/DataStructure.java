package pl.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DataStructure {
	
	private static Model model;
	private static Vector<Float> positions = new Vector<Float>();
	private static Vector<Float> textures = new Vector<Float>();
	private static Vector<Float> normals = new Vector<Float>();
	private static List<Face> faces = new ArrayList<Face>();
	
	public static void setModel(Model model) {
		DataStructure.model = model;
	}
	
	public static Model getModel() {
		return model;
	}

	public static Vector<Float> getPositions() {
		return positions;
	}
	
	public static float [] getPositionsArray() {
		int size = positions.size();
		float [] array = new float[size];
		
		for(int i = 0; i < size; i++)
			array[i] = positions.get(i);
		
		return array;
	}

	public static Vector<Float> getTextures() {
		return textures;
	}
	
	public static float [] getTexturesArray() {
		int size = textures.size();
		float [] array = new float[size];
		
		for(int i = 0; i < size; i++)
			array[i] = textures.get(i);
		
		return array;
	}

	public static Vector<Float> getNormals() {
		return normals;
	}
	
	public static float [] getNormalsArray() {
		int size = normals.size();
		float [] array = new float[size];
		
		for(int i = 0; i < size; i++)
			array[i] = normals.get(i);
		
		return array;
	}

	public static List<Face> getFaces() {
		return faces;
	}
	
	public static short [] getIndicesArray() {
		int size = faces.size() * 3;
		short array [] = new short[size];
		Vector<Short> tmp;
		int k = 0;
		short tmpValue;
		
		for(int i = 0; i < faces.size() * 3; i += 3) {
			tmp = faces.get(k).getvPointers();
			for(int j = 0; j < 3; j++) {
				tmpValue = tmp.get(j);
				array[i + j] = --tmpValue;			//indeskowanie w talbicy od 0 (w pliku od 1)
			}
			k++;
		}
				
		return array;
	}
}
