package pl.project.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import pl.project.render.MyRenderer;

import android.opengl.GLES20;
import android.util.FloatMath;
import android.util.Log;

public class Model {
	
//	private Vector<Float> vertices = new Vector<Float>();
//	private List<FaceIndex> indices = new ArrayList<FaceIndex>();
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private ShortBuffer indexBuffer;
	private int numberOfFaces = DataStructure.getNumberOfFaces();
	private int numberOfVertices = DataStructure.getNumberOfNormals();
	
	private int mProgram;
	private int mPositionHandle;	
	private int mColorHandle;
	private int mLightPosHandle;
	private int mNormalHandle;
	
	private int mMVPMatrixHandle;
	private int mMVMatrixHandle;
	
	float color[] = { 0.82f, 0.82f, 0.82f, 1.0f };
	
	private final String vertexShaderCode =
	        "uniform mat4 uMVPMatrix;" +
	        "uniform mat4 u_MVMatrix;" +
	        "uniform vec3 u_LightPos;" +

	        "attribute vec4 vPosition;" +
	        "uniform vec4 a_Color;" +
	        "attribute vec3 a_Normal;" +
	        
	        "varying vec4 v_Color;" +
	        
	        "void main() {" +
	        "  vec3 modelViewVertex = vec3(u_MVMatrix * vPosition);" +
	        "  vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));" +
	        "  float distance = length(u_LightPos - modelViewVertex);" +
	        "  vec3 lightVector = normalize(u_LightPos - modelViewVertex);" +
	        "  float diffuse = max(dot(modelViewNormal, lightVector), 0.1);" +
//	        "  diffuse = diffuse * (1.0 / (1.0 + (0.02 * distance)));" +
	        "  diffuse = diffuse + 0.5;" +
//	        "  diffuse = diffuse * (100.0 / (1.0 + (0.25 * distance * distance)));" +
			"  v_Color = a_Color * diffuse;" +
	        
	        "  gl_Position = uMVPMatrix * vPosition ;" +
	        "  gl_PointSize = 5.0 ;" +
	        "}";

	private final String fragmentShaderCode =
	    "precision mediump float;" +
	    "varying vec4 v_Color;" +
	    		
	    "void main() {" +
	    "  gl_FragColor = v_Color;" +
	    "}";
	
	public Model() {
		buildVertexBuffer();
		System.out.println(DataStructure.getNumberOfNormals());
		if(DataStructure.getNumberOfNormals() == 0)
			normalize();
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
        GLES20.glBindAttribLocation(mProgram, 0, "vPosition");
//        GLES20.glBindAttribLocation(mProgram, 1, "a_Color");
        GLES20.glLinkProgram(mProgram);
        
        final int[] linkStatus = new int[1];
		GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);

		if (linkStatus[0] == 0) 
		{				
			Log.e("ERROR", "Error compiling program: " + GLES20.glGetProgramInfoLog(mProgram));
			GLES20.glDeleteProgram(mProgram);
			mProgram = 0;
		}
	}
			
	public void draw(float [] mvpMatrix, float [] mvMatrix, float [] mLightPos, int mode) {
		
	    GLES20.glUseProgram(mProgram);
		    
	    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	    mMVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
	    mLightPosHandle = GLES20.glGetUniformLocation(mProgram, "u_LightPos");
	    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
	    mColorHandle = GLES20.glGetUniformLocation(mProgram, "a_Color");
	    mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");
	    
	    GLES20.glEnableVertexAttribArray(mPositionHandle);  
	    GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
	    
	    GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
	    GLES20.glEnableVertexAttribArray(mNormalHandle);
	    	    
//	    mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
	    GLES20.glUniform4fv(mColorHandle, 1, color, 0);
	    
	    GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mvMatrix, 0);
//	    for(int i = 0; i < 4; i++)
//	    	System.out.println(mLightPosInEyeSpace[i]);
	    
	    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
	    
	    GLES20.glUniform3f(mLightPosHandle, mLightPos[0], mLightPos[1], mLightPos[2]);

	    switch(mode) {
	    case 1 :
		    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, DataStructure.getNumbersOfVertices());
	    	break;
	    case 2 :
		    GLES20.glDrawElements(GLES20.GL_LINE_STRIP, numberOfFaces * 3, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	    	break;
	    case 3 :
	    	GLES20.glDrawElements(GLES20.GL_TRIANGLES, numberOfFaces * 3, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	    	break;
	    }
	    
	    //GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
	
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
        	GLES20.glFlush();
            Log.e("ERROR", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    
    private void normalize() {
    	float l;
    	float [] v = DataStructure.getPositionsArray();
    	
    	for(int i = 0; i < numberOfVertices; i +=3) {
    		l = FloatMath.sqrt(v[i] * v[i] + v[i+1] * v[i+1] + v[i+2] * v[i+2]);
    		
    		v[0] /= l;
    		v[1] /= l;
    		v[2] /= l;
		}
    	
    	DataStructure.setNormals(v);
    }
}
