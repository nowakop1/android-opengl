package pl.project.model;

public class FaceIndex {
	
	private Short [] positionIndex;
	private Short [] textureIndex;
	private Short [] normalIndex;
	
	public FaceIndex() {
		
	}

	public Short [] getPositionIndex() {
		return positionIndex;
	}

	public void setPositionIndex(Short [] positionIndex) {
		this.positionIndex = positionIndex;
	}

	public Short [] getTextureIndex() {
		return textureIndex;
	}

	public void setTextureIndex(Short [] textureIndex) {
		this.textureIndex = textureIndex;
	}

	public Short [] getNormalIndex() {
		return normalIndex;
	}

	public void setNormalIndex(Short [] normalIndex) {
		this.normalIndex = normalIndex;
	}

}
