package pl.project.android_opengl;

import pl.project.model.DataStructure;
import pl.project.model.Model;
import pl.project.render.MyRenderer;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class SceneryView extends Activity {
	
	private GLSurfaceView glSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Model model = DataStructure.getModel();
		model.buildVertexBuffer();
//		model.buildColorBuffer();
		model.buildFaceBuffer();
		
		MyRenderer myRender = new MyRenderer(model);

		glSurface = new GLSurfaceView(this);
		glSurface.setRenderer(myRender);
		setContentView(glSurface);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		glSurface.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurface.onPause();
	}
}
