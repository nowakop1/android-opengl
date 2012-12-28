package pl.project.model;

import java.util.Vector;

public class Face {
	
	private Vector<Short> vPointers = new Vector<Short>();
	private Vector<Short> vtPointers = new Vector<Short>();
	private Vector<Short> vnPointers = new Vector<Short>();
	
	public Vector<Short> getvPointers() {
		return vPointers;
	}
	
	public void addvPointer(Short pointer) {
		vPointers.add(pointer);
	}
	
	public Vector<Short> getVtPointers() {
		return vtPointers;
	}
	
	public void addVtPointer(Short pointer){
		vtPointers.add(pointer);
	}
	
	public Vector<Short> getVnPointers() {
		return vnPointers;
	}
	
	public void addVnPointer(Short pointer) {
		vnPointers.add(pointer);
	}	
}
