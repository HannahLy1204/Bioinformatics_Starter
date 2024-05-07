package _RunMe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import Constants.Address;
import Data_Genome.SNPAlignment;
import Data_User.Bioinformatician;
import Data_User.TeamLead;
import Data_User.TechnicalSupport;
import Data_User.Users;

public class InputReader {
	private Properties p ;
	private HashMap<String,String> fileMap; 
	private HashMap<String,String> fasta;
	private List<Users> users;
	private boolean isFileInput;
	private boolean isFASTARead;
	private boolean isTeamFileRead;

	//These variables can be used in other classes in this package. All InputReader() instances has this variable.
	static String inputSource = Address.INPUT.getDirectory(); 
	static String fileProperties= Address.PROPERTY.getDirectory(); 

	//Constructor, setting all non-static variables to empty.
	public InputReader() {
		this.p = new Properties();
		this.fileMap = new LinkedHashMap<>();
		this.fasta = new LinkedHashMap<>();
		this.users = new ArrayList<>();

		this.isFileInput = false;
		this.isFASTARead = false;
		this.isTeamFileRead = false;
	}

	//Loading filename from properties file to HashMap. We can adjust filename or property name in properties file without interfering to the code
	//Private methods only to read Input files (fasta, team file, file properties)
	private HashMap<String,String> loadFilename() throws FileNotFoundException,IOException{

		//Using while loop with boolean condition to make sure file is input only once.
		while(!isFileInput) {
			try (FileInputStream input = new FileInputStream(fileProperties)){
				p.load(input);

				//Set fileMap empty, after for loop, fileMap only contains data from properties file
				this.fileMap = new LinkedHashMap<>();          
				for (String k: p.stringPropertyNames() ) {
					fileMap.put(k, p.getProperty(k));}
			}
			isFileInput = true;
		}
		return fileMap;		
	}

	private SNPAlignment readFASTA() throws IllegalArgumentException, FileNotFoundException, IOException{
		while(!isFASTARead) {
			//Checking if file properties is input into fileMap
			if(fileMap.isEmpty()) {
				fileMap = this.loadFilename();
			} else {
				String fastafile = null; 

				//After for loop, fastafile variable only contains name of FASTA file.
				for(String keys: fileMap.keySet()) {
					if(fileMap.get(keys).contains(".fasta")) {
						fastafile = fileMap.get(keys);
					}}
				//After fasta filename is set, start to read the file line by line using BufferedReader
				if (fastafile!=null) {
					try(BufferedReader bf = new BufferedReader(new FileReader(inputSource+fastafile))){
						String line = bf.readLine();
						while (line != null) {
							if(line.charAt(0) == '>') {//Identify the line contains genome name
								String identifier = line;
								String sequence = bf.readLine();//The following line is genome sequence 

								//Make sure no genome with the same name is added again into the HashMap
								if(!fasta.containsKey(identifier)) {
									fasta.put(identifier, sequence);
								}	
							}
							line = bf.readLine();
						}}
				} else {
					throw new IllegalArgumentException("There is no FASTA file in file properties!");
				}
				isFASTARead = true;
			}}//end of while loop
		return new SNPAlignment(fasta);
	}

	private List<Users> readTeam() throws IllegalArgumentException, FileNotFoundException, IOException{
		//This method both set users and get users at the end
		while(!isTeamFileRead) {
			if(fileMap.isEmpty()) {
				fileMap = this.loadFilename();
			} else {
				String teamfile = ""; 
				//After for loop, teamfile variable only contains name of team file.
				for(String keys: fileMap.keySet()) {
					if(fileMap.get(keys).contains(".txt")) {
						teamfile = fileMap.get(keys);
					}}
				//Read the file line by line using BufferedReader
				this.users = new ArrayList<>();
				if (teamfile!=null) {
					try(BufferedReader bf = new BufferedReader(new FileReader(inputSource+teamfile))){
						String line = bf.readLine();
						//Separate each word in line using StringTokenizer
						while (line != null) {
							StringTokenizer token = new StringTokenizer(line);
							String type = token.nextToken().trim();
							String name = token.nextToken() +" "+ token.nextToken();
							int years = Integer.parseInt(token.nextToken().trim());

							//Identify which type of Users
							switch (type) {
							case "TeamLead":
								users.add(new TeamLead(name,years));
								break;
							case "Bioinformatician":
								users.add(new Bioinformatician(name, years));
								break;
							case "TechnicalSupport":
								users.add(new TechnicalSupport(name,years));
								break;
							default:
								throw new IllegalArgumentException("Cannot identify the function of user!");
							}
							//next line:
							line = bf.readLine();
						}}
				} else {
					throw new IllegalArgumentException("There is no text file in file properties!");
				}
				isTeamFileRead = true;
			}}//end of while loop
		
		return users;

	}

	//Public methods
	public TeamLead getTeamLead() throws IllegalArgumentException, FileNotFoundException, IOException {
		TeamLead lead = new TeamLead();
		boolean isMethodExecute = false;
		//Use while loop to execute code with different conditions
		while(!isMethodExecute) {
			if(users.isEmpty()) {
				users = this.readTeam();	
			}else {
				for (Users i : users) {
					if(i instanceof TeamLead) {
						lead = (TeamLead) i;
					}
				}
				//To link Leader to their own User team
				lead.setMyTeam(users);
				//Only TeamLead can set original Alignment from fasta input file
				lead.setRepository(this.readFASTA());
				isMethodExecute = true;
			}
		}
		return lead;
	}

	public Bioinformatician getBioinformatician(int which) throws IllegalArgumentException, FileNotFoundException, IOException {
		Bioinformatician worker = new Bioinformatician();
		boolean isMethodExecute = false;
		//Use while loop to execute code with different conditions
		while(!isMethodExecute) {
			if(users.isEmpty()) {
				users = this.readTeam();	
			}else {
				if (users.get(which) instanceof Bioinformatician) {
					worker = (Bioinformatician) users.get(which);
					//worker.setMyTeam(users); //To link Bioinformatician to their own User team
				}
				isMethodExecute = true;
			}
		}
		return worker;
	}

	public TechnicalSupport getTechnicalSupport(int which) throws IllegalArgumentException, FileNotFoundException, IOException {
		TechnicalSupport tech = new TechnicalSupport();
		boolean isMethodExecute = false;
		//Use while loop to execute code with different conditions
		while(!isMethodExecute) {
			if(users.isEmpty()) {
				users = this.readTeam();	
			}else {
				if (users.get(which) instanceof TechnicalSupport) {
					tech = (TechnicalSupport) users.get(which);
					//tech.setMyTeam(users); //To link TechnicalSupport to their own User team
				}
				isMethodExecute = true;
			}
		}
		return tech;
	}
	
	//Only boolean getters are created to check if file is read! These variables cannot be set. 
	public boolean isFileInput() {
		return isFileInput;
	}

	public boolean isFASTARead() {
		return isFASTARead;
	}

	public boolean isTeamFileRead() {
		return isTeamFileRead;
	}
}
