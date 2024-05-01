package Data_Genome;

public class Repository {
	//Creating a different class to store ONLY one optimal alignment
	private SNPAlignment storage ;
	
	//default constructor
	public Repository() {
		this.storage = new SNPAlignment();
	}
	//Constructor
	public Repository(SNPAlignment optimal){
		this.setStorage(optimal);
	}
	
	//this method cannot be used outside of this package. 
	//Thus, Repository data can not be set nor retrieved directly in main method, only via TeamLead user 
	void setStorage(SNPAlignment optimal) { 
		this.storage = optimal;
	}
	
	public SNPAlignment getStorage() {
		return storage;
	}
	
}
