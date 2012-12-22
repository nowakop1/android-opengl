package pl.project.model;

public class DataStructure {
	
	private static Model model;
	
	public static void setModel(Model model) {
		DataStructure.model = model;
	}
	
	public static Model getModel() {
		return model;
	}

}
