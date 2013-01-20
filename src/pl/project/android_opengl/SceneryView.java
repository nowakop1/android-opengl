package pl.project.android_opengl;

import pl.project.model.DataStructure;
import pl.project.model.Model;
import pl.project.render.MyRenderer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class SceneryView extends Activity {
	
	private MyRenderer myRender;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		myRender = new MyRenderer(getApplicationContext());

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
	
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "punty");
		menu.add(0, 2, 0, "krawêdzie");
		menu.add(0, 3, 0, "elementy");
		
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		System.out.println(item.getItemId());
		switch (item.getItemId()) {
	    case 1:
	        myRender.setMode(1);
	        break;
	    case 2:
	    	myRender.setMode(2);
	        break;
	    case 3:
	    	myRender.setMode(3);
	        break;
	    default:
	    	myRender.setMode(3); 
	    }
		
		return true;
	}
}
