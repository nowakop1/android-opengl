package pl.project.android_opengl;

import pl.project.model.DataStructure;
import pl.project.model.Model;
import pl.project.render.MyRenderer;

import android.app.Activity;
import android.os.Bundle;

public class SceneryView extends Activity {
	
	private MyRenderer myRender;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Model model = DataStructure.getModel();
		model.buildVertexBuffer();
		model.buildFaceBuffer();
		
		myRender = new MyRenderer(getApplicationContext(), model);

		setContentView(myRender);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		myRender.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		myRender.onPause();
	}
}
