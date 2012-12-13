package pl.project.android_opengl;

import pl.project.io.FilesManager;
import pl.project.io.OBJParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.app.Activity;

public class MainActivity extends Activity implements OnItemSelectedListener{
	
	private String [] items;
	private FilesManager filesManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        filesManager = new FilesManager();
                
        createListOfFiles();
        
        Button openFileButton = (Button) findViewById(R.id.loadMesh_button);
        
        openFileButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				
				filesManager.openFile(filesManager.getSelectedFile());
				OBJParser parser = new OBJParser(filesManager, getApplicationContext());
				parser.parse();
				
				Log.d("openFile", filesManager.getSelectedFile());	
			}
		});
    }

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		filesManager.setSelectedFile(items[position]);
		Log.d("LISTA ROZWIJALNA", filesManager.getSelectedFile());			
	}

	public void onNothingSelected(AdapterView<?> parent) {
		Log.d("LISTA ROZWIJALNA", "nie wybrano nic");	
	}
	
	private void createListOfFiles() {
		
        items = filesManager.getListOfFiles();
        
        Spinner spinner = (Spinner) findViewById(R.id.mesh_spinner);
        
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);        
	}
    
}
