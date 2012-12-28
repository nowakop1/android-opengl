package pl.project.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Model {
	
//	private Vector<Float> vertices = new Vector<Float>();
//	private List<FaceIndex> indices = new ArrayList<FaceIndex>();
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private int numberOfFaces = DataStructure.getFaces().size();
			
	public void buildFaceBuffer() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
				numberOfFaces * 3 * 2);
		byteBuffer.order(ByteOrder.nativeOrder());
		indexBuffer = byteBuffer.asShortBuffer();
		indexBuffer.put(DataStructure.getIndicesArray());
		indexBuffer.position(0);
	}
		
	public void buildVertexBuffer() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
				DataStructure.getPositions().size() * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(DataStructure.getPositionsArray());
		vertexBuffer.position(0);
	}
		
	public void draw(GL10 gl) {
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

}
