package pl.project.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class Model {
	
	private Vector<Float> vertices = new Vector<Float>();
	private List<FaceIndex> indices = new ArrayList<FaceIndex>();
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
		
	public List<FaceIndex> getIndices() {
		return indices;
	}
	public void setIndices(List<FaceIndex> indices) {
		this.indices = indices;
	}
	
	public Vector<Float> getVertices() {
		return vertices;
	}
	public void setVertices(Vector<Float> vertices) {
		this.vertices = vertices;
	}
	
	public void buildFaceBuffer() {
		System.out.println(indices.size());
		ByteBuffer fBuf = ByteBuffer.allocateDirect(indices.size() * 3 * 2);
		fBuf.order(ByteOrder.nativeOrder());
		indexBuffer = fBuf.asShortBuffer();
		indexBuffer.put(toPrimitiveArrayS(indices));
		indexBuffer.position(0);
	}
	
	private short[] toPrimitiveArrayS(List<FaceIndex> indices) {
		short [] s;
		Short [] tempS = new Short[3];
		int k = 0;
		
		s = new short[indices.size() * 3];
		for(int i = 0; i < indices.size() * 3; i += 3) {
			tempS = indices.get(k).getPositionIndex();
			for(int j = 0; j < 3; j++) {
				
				s[i + j] = tempS[j];
			}
			k++;
		}
		return s;
	}
	
	public void buildVertexBuffer() {
		ByteBuffer vBuf = ByteBuffer.allocateDirect(vertices.size() * 4);
		vBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = vBuf.asFloatBuffer();
		vertexBuffer.put(toPrimitiveArrayF(vertices));
		vertexBuffer.position(0);
	}
	
	private float[] toPrimitiveArrayF(Vector<Float> vertices) {
		float[] f;
		
		f = new float[vertices.size()];
		for(int i=0; i < vertices.size(); i++){
			f[i] = vertices.get(i);
		}
		
		return f;
	}
	
//	public void buildColorBuffer() {
//		
//		ByteBuffer byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
//		byteBuf.order(ByteOrder.nativeOrder());
//		colorBuffer = byteBuf.asFloatBuffer();
//		colorBuffer.put(colors);
//		colorBuffer.position(0);
//	}
	
	public void draw(GL10 gl) {
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
//		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.size() * 3, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

}
