package pl.project.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import pl.project.model.Model;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

public class MyRenderer implements Renderer {
	
	private Model model;
	private float rquad; 	

	public MyRenderer(Model model) {
		this.model = model;
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();				
				
		gl.glTranslatef(0.0f, -1.2f, -7.0f);	
		gl.glScalef(0.8f, 0.8f, 0.8f); 			
		gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f);	
		model.draw(gl);	
		
		gl.glLoadIdentity(); 
		
		rquad -= 0.15f; 
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

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);		
	}

}
