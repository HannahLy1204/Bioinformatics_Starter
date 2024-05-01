package Data_Genome;

public class Genome {
	private String id;
	private String sequence;
	
	//Set default constructor
	public Genome() {
		
	}
	
	//Constructor
	public Genome(String name, String gene) {
		this.setID(name);
		this.setSequence(gene);
	}

	//Getters and setters for private variables
	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	

}
