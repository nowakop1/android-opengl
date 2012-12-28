package pl.project.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.project.model.Model;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

public class MyRenderer extends GLSurfaceView implements Renderer {
		
	private Model model;
	
	private float xrot;					//obrót w osi x
	private float yrot;					//obrót w osi y

	private float xspeed;				//prêdkoœæ obortu x
	private float yspeed;				//prêdkoœæ obortu y
	
	private float oldX;
    private float oldY;
	private final float TOUCH_SCALE = 0.2f;
	
	private float z = -5.0f;
	
	public MyRenderer(Context context, Model model) {
		super(context);
		
		this.model = model;
		
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
				
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);		
	}

	public void onDrawFrame(GL10 gl) {
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();	
				
		gl.glTranslatef(-0.5f, -0.5f, z);	
		gl.glScalef(0.8f, 0.8f, 0.8f); 			

		//obrót wokó³ osi
		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
		
		model.draw(gl);	
		
		xrot += xspeed;
		yrot += yspeed;
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 					
			height = 1; 						
		}

		gl.glViewport(0, 0, width, height); 	
		gl.glMatrixMode(GL10.GL_PROJECTION); 	
		gl.glLoadIdentity();
		
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float x = event.getX();
        float y = event.getY();
        
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
        	//oblicza zmianê
        	float dx = x - oldX;
	        float dy = y - oldY;

        	int upperArea = this.getHeight() / 10;
        	
        	if(y < upperArea) {
        		z -= dx * TOUCH_SCALE / 2;
        	} else {        		
    	        xrot += dy * TOUCH_SCALE;
    	        yrot += dx * TOUCH_SCALE;
        	}        
        }
        
        oldX = x;
        oldY = y;
        
		return true;
	}
}
