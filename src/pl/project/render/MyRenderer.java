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
import android.util.FloatMath;
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
    
    private final float[] mLightPos = new float[3];
	
	public MyRenderer(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		
		//obliczenie d³ugoœci obiektu
		lengthX = DataStructure.maxValues[0] - DataStructure.minValues[0];
		lengthY = DataStructure.maxValues[1] - DataStructure.minValues[1];
		lengthZ = DataStructure.maxValues[2] - DataStructure.minValues[2];
		
		//obliczenie punktu œrodkowego
		centerX = (DataStructure.minValues[0] + DataStructure.maxValues[0]) / 2;
		centerY = (DataStructure.minValues[1] + DataStructure.maxValues[1]) / 2;
		centerZ = (DataStructure.minValues[2] + DataStructure.maxValues[2]) / 2;
		
		//wyznaczenie wartoœci diam
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
		
		//wyznaczenie wartoœci do ustawienia perspektywy
		left = -diam;
		right = diam;
		bottom = -diam;
		top = diam;
		near = diam;
		far =  2 * diam + near;
		
		//domyœlny tryb - wyœwietalnie ca³ego obiektu
		mode = 3;
		
		//ustawienie pozycji œwiat³a
		mLightPos[0] = centerX + diam;
		mLightPos[1] = centerY + diam;
		mLightPos[2] = centerZ;
		
		System.out.println(lengthX + " " + lengthY + " " + lengthZ);
		System.out.println(diam);
		System.out.println(left + " " + right + " " + bottom + " " + top + " " + near + " " + far);
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        		
		model = new Model();
	}

	public void onDrawFrame(GL10 gl) {
		
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
          
        //ustawienie perspektywy
        if(width < height && width > 0) {
        	Matrix.frustumM(mProjMatrix, 0, left * scale, right * scale, (bottom / ratio) * scale, (top / ratio) * scale, near, far);
        }
        else if(width >= height && height > 0)
        	Matrix.frustumM(mProjMatrix, 0, left * ratio * scale, right * ratio * scale, bottom * scale, top * scale, near, far);
        else
        	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
           
        //ustawienie kamery
        Matrix.setIdentityM(mVMatrix, 0);    
        Matrix.setLookAtM(mVMatrix, 0, centerX, centerY, (2 * diam) + centerZ, centerX, centerY, centerZ, 0.0f, 1.0f, 0.0f);
        
        //rotacja obiektu
        Matrix.translateM(mVMatrix, 0, centerX, centerY, centerZ);        
        Matrix.rotateM(mVMatrix, 0, rotateX, 0.0f, 1.0f, 0);
        if(lengthY >= lengthX)
        	Matrix.rotateM(mVMatrix, 0, rotateY, 1.0f, 0.0f, 0.0f);
        else
        	Matrix.rotateM(mVMatrix, 0, -rotateY, 0.0f, 0.0f, 1.0f);        
        Matrix.translateM(mVMatrix, 0, -centerX, -centerY, -centerZ);
        
        //wymno¿enie macierzy widoku i perspektywy
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        
        //rysowanie obiektu
        model.draw(mMVPMatrix, mVMatrix, mLightPos, mode);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		//ustawienie widoku w zale¿noœci od orientacji ekranu
        GLES20.glViewport(0, 0, width, height);	
        
        ratio = (float) width / height;
        this.width = width;
        this.height = height;
        
        Matrix.setIdentityM(mProjMatrix, 0);
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
