package pl.project.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.project.model.Model;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Debug;
import android.view.MotionEvent;

public class MyRenderer extends GLSurfaceView implements Renderer {
		
	private Model model;
		
	private float oldX;
    private float oldY;
	private final float TOUCH_SCALE = 0.2f;
		
	private float scale = 0.1f;
	
	private float rotateX = 0.0f;
	private float rotateY = 0.0f;
	
	private float translateX = 0.0f;
	private float translateY = 0.0f;
	
	private float left = -2.0f;
	private float right = 2.0f;
	private float bottom = -2.0f;
	private float top = 2.0f;
	private float near = 3.0f;
	private float far = 7.0f;
	
	private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
	
	public MyRenderer(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GL10.GL_LEQUAL);
		
		model = new Model();
		
		Debug.stopMethodTracing();
	}

	public void onDrawFrame(GL10 gl) {
		
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);        
        Matrix.setIdentityM(mVMatrix, 0);
        
        // przesuniêcie uk³adu wspó³rzêdnych obiektu do œrodka bry³y obcinania
        Matrix.translateM(mVMatrix, 0, 0.0f, 0.0f, -(near+far)/2.0f);
        
        Matrix.scaleM(mVMatrix, 0, scale, scale, scale);
        
        // przesuniêcie obiektu
        Matrix.translateM(mVMatrix, 0, translateX, translateY, 0.0f);
        
        Matrix.rotateM(mVMatrix, 0, rotateX, 0.0f, 1.0f, 0);
        Matrix.rotateM(mVMatrix, 0, rotateY, 1.0f, 0.0f, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        //rysowanie obiektu
        model.draw(mMVPMatrix);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		//ustawienie widoku w zale¿noœci od orientacji ekranu
        GLES20.glViewport(0, 0, width, height);	
        
        float ratio = (float) width / height;
        
        Matrix.setIdentityM(mProjMatrix, 0);
        
        if(width < height && width > 0)
        	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
        else if(width >= height && height > 0)
        	Matrix.frustumM(mProjMatrix, 0, left * ratio, right * ratio, bottom, top, near, far);
        else
        	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
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
        
         if (event.getAction() == MotionEvent.ACTION_MOVE) {
        	//oblicza zmianê
         	float dx = x - oldX;
 	        float dy = y - oldY;
	        
 	        rotateX += dx * TOUCH_SCALE;
 	        rotateY += dy * TOUCH_SCALE;
         }
         
        oldX = event.getX();
	    oldY = event.getY();
	    	 
         return true;
	}
}
