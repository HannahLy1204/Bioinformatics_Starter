package Constants;

public enum Address {//TO CHANGE THE CONSTANTS OF FILEPATH, ONLY ACESSING THIS ENUM CLASS
	//Package directory
	FILES_GENOME("Files_Genome/"),
	FILES_REPORT("Files_BioReports/"),
	BACK_UP("Files_BackUp/"),
	INPUT("InputFiles/"),
	PROPERTY("InputFiles/filename.properties");//properties file address
	
	public static final String SOURCE = "src/";//This constant is set public to be accessed in InputReader class.
	
	private final String DIR;

	private Address(String dir) {
		this.DIR = SOURCE + dir;
	}

	public String getDirectory() {
		return DIR;
	}

}
