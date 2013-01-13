package pl.project.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import pl.project.render.MyRenderer;

import android.opengl.GLES20;

public class Model {
	
//	private Vector<Float> vertices = new Vector<Float>();
//	private List<FaceIndex> indices = new ArrayList<FaceIndex>();
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private ShortBuffer indexBuffer;
	private int numberOfFaces = DataStructure.getNumberOfFaces();
	private int mProgram;
	private int mPositionHandle;	
	private int mColorHandle;
	private int mMVPMatrixHandle;
	
	float color[] = { 0.63671875f, 0.76953125f, 0.78265625f, 1.0f };
	
	private final String vertexShaderCode =
	        "uniform mat4 uMVPMatrix;" +

	        "attribute vec4 vPosition;" +
	        "attribute vec4 a_Color;" +
	        "attribute vec3 a_Normal;" +
	        
	        "varying vec4 v_Color;" +
	        
	        "void main() {" +
	        "  gl_Position = uMVPMatrix * vPosition ;" +
	        "  gl_PointSize = 5.0 ;" +
	        "}";

	private final String fragmentShaderCode =
	    "precision mediump float;" +
	    "uniform vec4 vColor;" +
	    "void main() {" +
	    "  if( gl_FrontFacing == true )" +
	    "    gl_FragColor = vColor;" +
	    "  else" +
	    "    gl_FragColor = vColor * 0.5;" +
	    "}";
	
	public Model() {
		buildVertexBuffer();
		buildNormalsBuffer();
		buildFaceBuffer();
		prepare();
	}
				
	private void buildFaceBuffer() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
				numberOfFaces * 3 * 2);
		byteBuffer.order(ByteOrder.nativeOrder());
		indexBuffer = byteBuffer.asShortBuffer();
		indexBuffer.put(DataStructure.getIndicesArray());
		indexBuffer.position(0);
	}
		
	private void buildVertexBuffer() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
				DataStructure.getNumbersOfVertices() * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(DataStructure.getPositionsArray());
		vertexBuffer.position(0);
	}
	
	private void buildNormalsBuffer() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
				DataStructure.getNumberOfNormals() * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuffer.asFloatBuffer();
		normalBuffer.put(DataStructure.getNormalsArray());
		normalBuffer.position(0);
	}
	
	private void prepare() {
        int vertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                                                   vertexShaderCode);
        int fragmentShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                                                     fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
	}
			
	public void draw(float [] mvpMatrix) {
		
	    GLES20.glUseProgram(mProgram);
	    
	    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
	    GLES20.glEnableVertexAttribArray(mPositionHandle);
	    
	    GLES20.glVertexAttribPointer(mPositionHandle, 3,
	                                 GLES20.GL_FLOAT, false,
	                                 12, vertexBuffer);
	    	    
	    mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
	    GLES20.glUniform4fv(mColorHandle, 1, color, 0);
	    
	    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

	    GLES20.glDrawElements(GLES20.GL_TRIANGLES, numberOfFaces * 3, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
//	    GLES20.glDrawElements(GLES20.GL_LINES, numberOfFaces * 3, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
//	    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, DataStructure.getNumbersOfVertices());

	    GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
