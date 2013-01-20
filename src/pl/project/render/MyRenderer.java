package pl.project.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.project.model.DataStructure;
import pl.project.model.Model;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Debug;
import android.os.SystemClock;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;

public class MyRenderer extends GLSurfaceView implements Renderer {
		
	private Model model;
		
	private float oldX;
    private float oldY;
	private final float TOUCH_SCALE = 0.2f;
	private int pointers;
	private float oldDist;
		
	private float scale = 0.4f;
	private float diam = 0.0f;
	
	private float ratio;
	private int width;
	private int height;
	
	private float rotateX = 0.0f;
	private float rotateY = 0.0f;
	
	private float translateX = 0.0f;
	private float translateY = 0.0f;
	
	private float centerX;
	private float centerY;
	private float centerZ;
	
	private float lengthX;
	private float lengthY;
	private float lengthZ;
	
	private float left;
	private float right;
	private float bottom;
	private float top;
	private float near;
	private float far;
	
	private int mode;
	
	private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    
    private float[] mLightModelMatrix = new float[16];
    
    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] mLightPosInWorldSpace = new float[4];
    private final float[] mLightPosInEyeSpace = new float[4];

	private int mPointProgramHandle;
	
	public MyRenderer(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		
		lengthX = DataStructure.maxValues[0] - DataStructure.minValues[0];
		lengthY = DataStructure.maxValues[1] - DataStructure.minValues[1];
		lengthZ = DataStructure.maxValues[2] - DataStructure.minValues[2];
		
		centerX = (DataStructure.minValues[0] + DataStructure.maxValues[0]) / 2;
		centerY = (DataStructure.minValues[1] + DataStructure.maxValues[1]) / 2;
		centerZ = (DataStructure.minValues[2] + DataStructure.maxValues[2]) / 2;
		
		if(lengthX > diam)
			diam = lengthX;
		if(lengthY > diam)
			diam = lengthY;
		if(lengthZ > diam)
			diam = lengthZ;
		
		if(diam < 2.0) 
			diam = 2.0f;
		else
			diam = diam / 2;
		
		left = -diam;
		right = diam;
		bottom = -diam;
		top = diam;
		near = diam;
		far =  2 * diam + near;
		
		mode = 3;
		
		System.out.println(lengthX + " " + lengthY + " " + lengthZ);
		System.out.println(diam);
		System.out.println(left + " " + right + " " + bottom + " " + top + " " + near + " " + far);
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        
//        GLES20.glEnable (GL10.GL_LIGHTING);
//        GLES20.glEnable (GL10.GL_LIGHT0);
		
		model = new Model();
		
		final String pointVertexShader =
	        	"uniform mat4 u_MVPMatrix;      \n"		
	          +	"attribute vec4 a_Position;     \n"		
	          + "void main()                    \n"
	          + "{                              \n"
	          + "   gl_Position = u_MVPMatrix   \n"
	          + "               * a_Position;   \n"
	          + "   gl_PointSize = 5.0;         \n"
	          + "}                              \n";
	        
        final String pointFragmentShader = 
        	"precision mediump float;       \n"					          
          + "void main()                    \n"
          + "{                              \n"
          + "   gl_FragColor = vec4(1.0,    \n" 
          + "   1.0, 1.0, 1.0);             \n"
          + "}                              \n";
        
        final int pointVertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(pointVertexShaderHandle, pointVertexShader);
        GLES20.glCompileShader(pointVertexShaderHandle);
        
        final int pointFragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(pointFragmentShaderHandle, pointFragmentShader);
        GLES20.glCompileShader(pointFragmentShaderHandle);
        
        int vertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER, pointVertexShader);
        int fragmentShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShader);

        mPointProgramHandle = GLES20.glCreateProgram();
        GLES20.glAttachShader(mPointProgramHandle, vertexShader);
        GLES20.glAttachShader(mPointProgramHandle, fragmentShader);
        
        GLES20.glBindAttribLocation(mPointProgramHandle, 0, "a_Position");
        
        GLES20.glLinkProgram(mPointProgramHandle);
		
		Debug.stopMethodTracing();
	}

	public void onDrawFrame(GL10 gl) {
		
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
                
        if(width < height && width > 0) {
//        	System.out.println(left * scale + " " + right * scale);
        	Matrix.frustumM(mProjMatrix, 0, left * scale, right * scale, (bottom / ratio) * scale, (top / ratio) * scale, near, far);
        }
        else if(width >= height && height > 0)
        	Matrix.frustumM(mProjMatrix, 0, left * ratio * scale, right * ratio * scale, bottom * scale, top * scale, near, far);
        else
        	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
                
        Matrix.setIdentityM(mVMatrix, 0);    
        Matrix.setLookAtM(mVMatrix, 0, 0.0f, 0.0f, (2 * diam) + centerZ, 0.0f, 0.0f, centerZ, 0.0f, 1.0f, 0.0f);
        
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, centerX + diam, centerY + diam, centerZ);        
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mVMatrix, 0, mLightPosInWorldSpace, 0);
        
        Matrix.translateM(mVMatrix, 0, centerX, centerY, centerZ);
        
        Matrix.rotateM(mVMatrix, 0, rotateX, 0.0f, 1.0f, 0);
        if(lengthY >= lengthX)
        	Matrix.rotateM(mVMatrix, 0, rotateY, 1.0f, 0.0f, 0.0f);
        else
        	Matrix.rotateM(mVMatrix, 0, -rotateY, 0.0f, 0.0f, 1.0f);
        
        Matrix.translateM(mVMatrix, 0, -centerX, -centerY, -centerZ);
        
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        
        //rysowanie obiektu
//        System.out.println(mode);
        model.draw(mMVPMatrix, mVMatrix, mLightPosInEyeSpace, mode);
        
        GLES20.glUseProgram(mPointProgramHandle);
        drawLight();
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		//ustawienie widoku w zale¿noœci od orientacji ekranu
        GLES20.glViewport(0, 0, width, height);	
        
        ratio = (float) width / height;
        this.width = width;
        this.height = height;
        
        Matrix.setIdentityM(mProjMatrix, 0);
	}
	
	private void drawLight() {
		final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
		final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position");
		
		GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

		GLES20.glDisableVertexAttribArray(pointPositionHandle);
		
//		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mLightModelMatrix, 0);
//		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
	}
	
    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float x = event.getX();
        float y = event.getY();
		
		switch(event.getAction() & MotionEvent.ACTION_MASK) {
		
		//je¿eli 1 wskaŸnik (palec) - pobieram wspó³rzêdne
		case MotionEvent.ACTION_DOWN :
			pointers = 1;
			
			x = event.getX();
			y = event.getY();
			
			break;
			
		//2 wskaŸniki - obliczam odleg³oœæ miêdzy nimi
		case MotionEvent.ACTION_POINTER_DOWN :
        	pointers = 2;
        	
        	x = event.getX(0) - event.getX(1);
       	 	y = event.getY(0) - event.getY(1);
       	 	
			oldDist = FloatMath.sqrt(x * x + y * y) + 10000;
       	 	       	 	
       	 	break;
       	 	
		case MotionEvent.ACTION_MOVE :
			//obracanie
			if(pointers == 1) {
	        	//oblicza zmianê po przesuniêciu wskaŸnika
	         	float dx = x - oldX;
	 	        float dy = y - oldY;
		        
	 	        //ustawiam o ile obróciæ
	 	        rotateX += dx * TOUCH_SCALE;
	 	        rotateY += dy * TOUCH_SCALE;
			} 
			//powiêkszanie/pomijszanie
			else if(pointers == 2) {
				//obliczam now¹ d³ugoœæ po przesuniêciu wskaŸników
				x = event.getX(0) - event.getX(1);
	        	y = event.getY(0) - event.getY(1);
	        	 
	        	float newDist = FloatMath.sqrt(x * x + y * y) + 10000;
	        	float newScale = scale;
	        	
	        	//zauwa¿alna odleg³oœæ miêdzy 2 wskaŸnikami
	        	if(newDist > 10.0f) {
	        		newScale = scale * ((oldDist / newDist));
	        		
	        		//nie powiêkszam dalej
	        		if(newScale <= 0.1f) 
	        			scale = 0.1f;
	        		//nie zmniejszam dalej
	        		else if(newScale >= 2.0f)
	        			scale = 2.0f;
	        		else
	        			scale = newScale;
	        	}
	        	 
//		        	System.out.println(Math.abs(oldDist - newDist));		        	
//		        	System.out.println(scale);
			}				
			break;
		}		
        
		oldX = event.getX();
		oldY = event.getY();
                                  
     	return true;
	}
		
	public void setMode(int mode) {
		this.mode = mode;
	}
}
