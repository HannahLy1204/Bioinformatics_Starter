package Data_User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import Data_Genome.Repository;
import Data_Genome.SNPAlignment;
import _RunMe.Address;

public class TechnicalSupport extends Users{
	private HashMap<String, SNPAlignment> backUpAlignment;
	private SNPAlignment backUp;
	private LocalDateTime timeBackUp;
	private final String backUpDirectory = Address.BACK_UP.getDirectory();
	

	//default constructor
	public TechnicalSupport() {
		super();
	}

	//Constructor
	public TechnicalSupport(String name, int year){
		super("TechnicalSupport", name, year);
		leadBy = new TeamLead();
		this.backUpAlignment = new LinkedHashMap<>();
		this.backUp = new SNPAlignment();
	}

	//Back up repository data and personal alignment
	public SNPAlignment backUpData() throws IOException {
		//Repository back up data
		SNPAlignment store = super.getRepository().getStorage();
		HashMap<String, String> copy = store.getAlignment();
		timeBackUp = LocalDateTime.now();
		Repository data = new Repository(new SNPAlignment(new LinkedHashMap<>(copy)));//Back up Repository
		backUp = data.getStorage();
		//Back up personal alignment
		for (Users i: myTeam) {
			if (i instanceof Bioinformatician) {
				this.backUpAlignment.put(i.getName(),((Bioinformatician) i).getPersonalAlignment());
				//Copy .alignment.txt to another folder (reference from the internet)
				Path source = Paths.get(i.genomeOutput());
				Path target = Paths.get(backUpDirectory+i.getName()+ALIGNMENT_EXTENSION);
				Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		System.out.println("All data has been back up at " +this.getTimeBackUp()); 
		return backUp;
	}

	public SNPAlignment restoreData() {
		SNPAlignment current = this.getBackUp(); //for current alignment
		for (Users i: myTeam) {
			if (i instanceof Bioinformatician) {
				((Bioinformatician) i).setPersonalAlignment(current.getAlignment()); //for all bioinformaticians in the team
			}
		}
		return current;
	}
	
	public void clearingData(SNPAlignment currentAlignment) {
		currentAlignment.setAlignment(new LinkedHashMap<>());
		for (Users i: myTeam) {
			if (i instanceof Bioinformatician) {
				((Bioinformatician) i).setPersonalAlignment(new LinkedHashMap<>()); //for all bioinformaticians in the team
			}
		}
	}

	public SNPAlignment getBackUp() {
		return this.backUp;
	}

	public HashMap<String, SNPAlignment> getPersonalBackUp(){
		return backUpAlignment;
	}

	public String getTimeBackUp() {
		return timeBackUp.format(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss"));
	}



}
