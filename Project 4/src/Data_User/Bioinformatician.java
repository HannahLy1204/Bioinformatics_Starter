package Data_User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import Data_Genome.SNPAlignment;


public class Bioinformatician extends Users{
	SNPAlignment personalAlignment; //can be accessed from other classes (TeamLead, TechnicalSupport) of this package

	//default constructor
	public Bioinformatician() {
		super();
		leadBy = new TeamLead();
		personalAlignment = new SNPAlignment();
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

	public void setPersonalAlignment(HashMap<String, String>  currentData) {
		this.personalAlignment = new SNPAlignment(new LinkedHashMap<>(currentData));//create a copy for main source
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
					+ "Made by: %s%n",this.getLeadBy(), this.getName());
			pw.printf("Alignment Score:  %d%n"
					+ "SNiP alignment score: %d%n"
					, personalAlignment.alignmentScore(),personalAlignment.alignmentScore()); //Standard and SNP have identical alignment score!!
			pw.println();
		}
	}
}



