package pl.project.model;

import java.util.Vector;

public class Vertex {
	
	private Vector<Float> position;
	private Vector<Float> texture;
	private Vector<Float> normal;
	
	public Vertex() {
		
	}
	
	public Vector<Float> getPosition() {
		return position;
	}
	
	public void setPosition(Vector<Float> position) {
		this.position = position;
	}

	public Vector<Float> getTexture() {
		return texture;
	}

	public void setTexture(Vector<Float> texture) {
		this.texture = texture;
	}

	public Vector<Float> getNormal() {
		return normal;
	}

	public void setNormal(Vector<Float> normal) {
		this.normal = normal;
	}
	
}
