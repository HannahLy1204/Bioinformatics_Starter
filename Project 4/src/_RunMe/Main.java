package _RunMe;

import Data_Genome.Repository;
//import Data_Genome.Repository;
import Data_Genome.SNPAlignment;
import Data_User.Bioinformatician;
import Data_User.TeamLead;
import Data_User.TechnicalSupport;
import _GUIProcessing.GUIProcess;

public class Main {

	public static void main(String[] args) {
		try {
			new GUIProcess();
			
			InputReader in = new InputReader();
			TeamLead X = in.getTeamLead(); //At this point, original fasta file has been load to Repository
			Bioinformatician A = in.getBioinformatician(1);
			Bioinformatician B = in.getBioinformatician(2);
			Bioinformatician C = in.getBioinformatician(3);
			TechnicalSupport D = in.getTechnicalSupport(4);
			
			Repository original = X.getRepository();//change in Repository does not affect the current alignment
			SNPAlignment current=  original.getStorage();
			
			int initial = current.alignmentScore();
			//Parsing original FASTA file to personal alignment
			X.overwriteAlignment(current, A);
			X.overwriteAlignment(current, B);
			X.overwriteAlignment(current, C);
			
			D.backUpData();//Backup
			
			SNPAlignment alignmentA = A.getPersonalAlignment();
			//Change in alignmentA directly affect to personalAlignment of A, since they have the same reference
			int a1 = alignmentA.alignmentScore();
			
			
			SNPAlignment alignmentB = B.getPersonalAlignment();
			int b1 = alignmentB.alignmentScore();
			
			
			SNPAlignment alignmentC = C.getPersonalAlignment();
			int c1 = alignmentC.alignmentScore();
			
			System.out.printf("Original alignment score: %d.%n%n", initial);
			
			//Modifying alignment and writing data to text files
			alignmentA.replaceOccurencesAll("TGTCCTGGGG", "TGTCCTAAAA");
			A.writeDataToFile();
			A.writeReportToFile();
			a1 = alignmentA.alignmentScore(); 
			
			alignmentB.deleteFragments(">2001.F1.BR.01.01BR087", "TGTCCTGGGG");
			B.writeDataToFile();
			B.writeReportToFile();
			b1 = alignmentB.alignmentScore(); 
			
			alignmentC.replaceOccurences(">2001.F1.BR.01.01BR087", "TGTCCTGGGG", "AAAAAAAAAA");
			C.writeDataToFile();
			C.writeReportToFile();
			c1 = alignmentC.alignmentScore();
			
			X.writeDataToFile();
			X.writeReportToFile();
			
			System.out.printf("\t Alignment score after modification: %n"
					+ "%s.%n"
					+ "%s: %d.%n"
					+ "%s: %d.%n"
					+ "%s: %d.%n",A.getLeadBy(),A.getName(), a1, B.getName(), b1, C.getName(), c1);
			
			System.out.printf("Bioinformatician has lowest score: %s.%n", X.lowestScoreAlignment().getName());
			
			//copying the optimal alignment to Repository
			//current variable need to be refered again because of immutablity
			current = X.copyAlignment(); 
			System.out.printf("Optimal alignment score: %d.%n%n",current.alignmentScore());
			
			//Restoring repository from back up data
			current = D.restoreData();
			System.out.printf("After restore, alignment score: %d.%n",current.alignmentScore());
			System.out.printf("Is alignment score of user equals to current? %b.%n%n",alignmentB.alignmentScore()==current.alignmentScore());
			
			//Clearing data
			D.clearingData(current);
			System.out.printf("After clearing data, current alignment is empty: %b%n", current.getAlignment().isEmpty());
			System.out.printf("After clearing data, users' alignment is empty: %b%n", alignmentB.getAlignment().isEmpty());
			
			//Check if Bioinfomatician can access Repository
			A.getRepository();

		} catch (Exception e) {
			System.out.println("INVALID!");
			e.printStackTrace();
		}

	}
}
