package pl.project.android_opengl;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.app.Activity;

public class MainActivity extends Activity implements OnItemSelectedListener{
	
	String [] items = {"cube.obj", "banana.obj", "test.obj"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Spinner spinner = (Spinner) findViewById(R.id.mesh_spinner);
        
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		Log.d("LISTA ROZWIJALNA", items[position]);			
	}

	public void onNothingSelected(AdapterView<?> parent) {
		Log.d("LISTA ROZWIJALNA", "nie wybrano nic");	
	}
    
}
