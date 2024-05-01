package Data_User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import Data_Genome.Repository;
import Data_Genome.SNPAlignment;

public class TeamLead extends Users{
	private Repository optimal;

	//default constructor
	public TeamLead() {
		super();
	}

	//Constructor
	public TeamLead(String name, int year) {
		super("TeamLead", name, year);
		this.optimal = new Repository();
		this.isAccess();
	}

	@Override
	public String getLeadBy() {
		return this.getName() + " is the Leader!";
	}

	public List<String> getMyBioinformaticians() {
		List<String> team = new ArrayList<>();
		if(!myTeam.isEmpty()) {
			for(Users i : myTeam) {
				if (i instanceof Bioinformatician) {
					team.add(i.getName());
				}
			}
		}
		return team;
	}

	public List<String> getMyTechnicalSupports() {
		List<String> team = new ArrayList<>();
		if(!myTeam.isEmpty()) {
			for(Users i : myTeam) {
				if (i instanceof TechnicalSupport) {
					team.add(i.getName());
				}
			}
		}
		return team;
	}

	//Dealing with Repository...
	public void setRepository(SNPAlignment good){
		optimal = new Repository(good);
	}

	//Repository should not be access directly in main method. 
	//Data from Repository can be accessed via TeamLead
	public SNPAlignment getOptimal(){
		return optimal.getStorage();
	}

	//Dealing with data towards Bioinformaticians
	public Bioinformatician lowestScoreAlignment() {
		List<Bioinformatician> compare = new ArrayList<>();
		List<Integer> listScore = new ArrayList<>();
		int minScore = 0;
		Bioinformatician chosenOne = new Bioinformatician();
		//Adding alignment Score to the list
		if(!myTeam.isEmpty()) {
			for(Users i : myTeam) {
				if (i instanceof Bioinformatician) {
					SNPAlignment fasta= ((Bioinformatician) i).getPersonalAlignment();
					if(fasta != null) {
						listScore.add(fasta.alignmentScore());
						compare.add((Bioinformatician) i);
					} else {
						throw new IllegalArgumentException("Personal Alignment has not been set!");
					}}
			}
			//To choose the lowest Alignment score:
			minScore = listScore.get(0);
			for(int i = 0; i < listScore.size(); i++) {
				if(listScore.get(i) <= minScore) {
					minScore = listScore.get(i);
					chosenOne = compare.get(i);
				}
			}
		}
		return chosenOne;
	}

	public void copyAlignment(Bioinformatician optimal) {	//Set Repository to optimal alignment
		this.optimal = new Repository(optimal.getPersonalAlignment());
	}

	public void overwriteAlignment(SNPAlignment optimal) throws IOException, IllegalArgumentException {
		HashMap<String, String> origin = optimal.getAlignment();
		//Overwrite all alignment of all bioinformaticians (from SNPAlingment object class to text file)
		if (!myTeam.isEmpty()) {
			for(Users i : myTeam) {
				if(i instanceof Bioinformatician) {
					try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(i.genomeOutput())));){
						((Bioinformatician) i).setPersonalAlignment(new SNPAlignment(new LinkedHashMap<>(origin))); //set personalAlignment
						((Bioinformatician) i).writeDataToFile(); //overwrite to text file
					}} }
		} else {
			throw new IllegalArgumentException("The team is empty!");
		}
	}
	
	public void writeDataToFile(List<Users> myTeam) throws IOException {
		//User PrintWriter to easily format, combine with BufferedWriter to deal with large amount of data
		try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(genomeOutput())));){
			//Use .printf() method for formating text
			pw.printf("\t TEAM ALIGNMENT %n "
					+ "Made by: %s%n"
					+ "All alignments of bioinformaticians in the team: %n%n", this.getName());

			for(Users i: myTeam) {//Read alignment file of each bioinformatician then write it all together to one file.
				if(i instanceof Bioinformatician) {
					try(BufferedReader bf = new BufferedReader(new FileReader(i.genomeOutput()))){
						String content = bf.readLine();
						while (content != null) {
							pw.printf("%s%n", content);
							content = bf.readLine();
						}}
					catch (FileNotFoundException e) {
						System.out.println("File is not found! Bioinformaticians have not run writeDataToFile method!");
					}}}
		}}

	public void writeReportToFile(List<Users> myTeam) throws IOException {
		try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(reportOutput())));){
			//Use .printf() method for formating text
			pw.printf("\t TEAM REPORT %n "
					+ "Made by: %s%n"
					+ "All alignments of bioinformaticians in the team: %n%n", this.getName());

			for(Users i: myTeam) {
				if(i instanceof Bioinformatician) {
					try(BufferedReader bf = new BufferedReader(new FileReader(i.reportOutput()))){
						String content = bf.readLine();
						while (content != null) {
							pw.printf("%s%n", content);
							content = bf.readLine();
						}}
					catch (FileNotFoundException e) {
						System.out.println("File is not found! Bioinformaticians have not run writeReportToFile method!");
						e.printStackTrace();
					}}}
		}}

}
