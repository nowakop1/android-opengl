package pl.project.model;

public class Vertex {
	
	private float [] position = new float[3];
	private float [] texture =  new float[3];
	private float [] normal = new float[3];
	
	public float [] getPosition() {
		return position;
	}

	public float [] getTexture() {
		return texture;
	}

	public float [] getNormal() {
		return normal;
	}
		
}
