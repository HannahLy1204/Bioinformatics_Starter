package Data_User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

import Data_Genome.SNPAlignment;


public class Bioinformatician extends Users{
	SNPAlignment personalAlignment; //can be accessed from other classes (TeamLead, TechnicalSupport) of this package

	//default constructor
	public Bioinformatician() {
		super();
		leadBy = new TeamLead();
		personalAlignment = new SNPAlignment();
		this.isAccess();
	}

	//Constructor
	public Bioinformatician(String name, int year){
		super("Bioinformatician", name, year);
		leadBy = new TeamLead();
		personalAlignment = new SNPAlignment();
	}

	public SNPAlignment getPersonalAlignment() {
		if (!personalAlignment.getAlignment().isEmpty()) {
			return this.personalAlignment;
		} else {
			throw new IllegalArgumentException("Personal Alignment has not been set!");
		}
	}

	//Using overloading method to set personalAligment by read text file to SNPAlignment object class or by using alignment from outside
	public void setPersonalAlignment(SNPAlignment personalAlignment) {
		this.personalAlignment = personalAlignment;
	}

	//Bioinformatician convert their own adjusted genome file to their own personal alignment
	public void setPersonalAlignment() throws FileNotFoundException, IOException {
		HashMap<String,String> fasta = personalAlignment.getAlignment();

		try(BufferedReader bf = new BufferedReader(new FileReader(genomeOutput()))){
			//since in template, there may be a null line, thus, for loop is used instead of while loop
			//(depends on how template is designed)
			String line = bf.readLine();
			for (int j = 0; j<250; j++ ) {//Assuming 250 lines is enough to capture the necessary alignment
				if(line.charAt(0) == '>') {//Identify the line contains genome name
					String identifier = line;
					String sequence = bf.readLine();//The following line is genome sequence 

					//Make sure no genome with the same name is added again into the HashMap
					if(!fasta.containsKey(identifier)) {
						fasta.put(identifier, sequence);
					}}
			}
			personalAlignment = new SNPAlignment(fasta);
		}
	}

	public void writeDataToFile() throws IOException {//From personalAlignment to text file
		Set<String> IDs = personalAlignment.getAllIdentifiers();
		
		//User PrintWriter to easily format, combine with BufferedWriter to deal with large amount of data
		try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(genomeOutput())));){
			pw.printf("\t GENOME ALIGNMENT %n "
					+ "Lead by: %s%n "
					+ "Made by: %s%n"
					+ "Standard Alignment: %n", this.getLeadBy(),this.getName());
			for (String id : IDs) {
				pw.printf("%s%n%s%n", id, personalAlignment.getOneSequence(id));
			}
			pw.printf("%nSNiP Alignment: %n");
			for (String id : IDs) {
				pw.printf("%s%n%s%n", id, personalAlignment.getSNiPMap().get(id));
			}
			pw.println();
		}
	}

	public void writeReportToFile() throws IOException {
		//User PrintWriter to easily format, combine with BufferedWriter to deal with large amount of data
		try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(reportOutput())));){
			pw.printf("\t BIO-REPORT %n "
					+"Lead by: %s%n "
					+ "Made by: %s%n%n",this.getLeadBy(), this.getName());
			pw.printf("Alignment Score:  %d%n"
					+ "SNiP alignment score: %d%n"
					, personalAlignment.alignmentScore(),personalAlignment.alignmentScore()); //Standard and SNP have identical alignment score!!
			pw.println();
		}
	}
	


}



