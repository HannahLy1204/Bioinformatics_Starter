package Data_Genome;

public class Repository {
	//Creating a different class to store ONLY one optimal alignment
	private static SNPAlignment storage ;
	
	//default constructor
	public Repository() {
		storage = new SNPAlignment();
	}
	//Constructor
	public Repository(SNPAlignment optimal){
		this.setStorage(optimal);
	}
	
	public void setStorage(SNPAlignment optimal) { 
		storage = optimal;
	}
	
	public SNPAlignment getStorage() {
		return storage;
	}
	
}
