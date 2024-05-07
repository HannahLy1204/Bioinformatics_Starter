package Data_User;


public class TechnicalSupport extends Users{
	//private SNPAlignment forAllUsers, original;


	//default constructor
	public TechnicalSupport() {
		super();
		leadBy = new TeamLead();
		this.isAccess();
	}

	//Constructor
	public TechnicalSupport(String name, int year){
		super("TechnicalSupport", name, year);
	}


	/*public void originalGenome() throws IllegalArgumentException, FileNotFoundException, IOException {
		try{
			original.setAlignment(in.readFASTA().getAlignment()); 


		}finally {

		}
	}*/



	/*@Override
	public void writeDataToFile() {
		throw new IllegalArgumentException("This user does not have this function!");
	}*/

}
