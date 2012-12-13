package pl.project.model;

public class FaceIndex {
	
	private int [] positionIndex;
	private int [] textureIndex;
	private int [] normalIndex;
	
	public FaceIndex() {
		
	}

	public int [] getPositionIndex() {
		return positionIndex;
	}

	public void setPositionIndex(int [] positionIndex) {
		this.positionIndex = positionIndex;
	}

	public int [] getTextureIndex() {
		return textureIndex;
	}

	public void setTextureIndex(int [] textureIndex) {
		this.textureIndex = textureIndex;
	}

	public int [] getNormalIndex() {
		return normalIndex;
	}

	public void setNormalIndex(int [] normalIndex) {
		this.normalIndex = normalIndex;
	}

}
