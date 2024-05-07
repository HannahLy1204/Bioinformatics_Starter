package _RunMe;

//import java.util.HashMap;

//import Data_Genome.Repository;
import Data_Genome.SNPAlignment;
import Data_User.Bioinformatician;
import Data_User.TeamLead;
//import Data_User.TechnicalSupport;

public class Main {
	static boolean isOriginalLoad = false;

	public static void main(String[] args) {
		try {
			InputReader in = new InputReader();
			TeamLead X = in.getTeamLead(); // At this point, original fasta file has been load to Repository
			Bioinformatician A = in.getBioinformatician(1);
			Bioinformatician B = in.getBioinformatician(2);
			Bioinformatician C = in.getBioinformatician(3);

			SNPAlignment original = X.getOptimal();
			// HashMap<String, String> origin = original.getAlignment();
			// int initial = original.alignmentScore();
			X.overwriteAlignment(original);// Original fasta file has been parse to personal Alignment
			// Immutable problem

			SNPAlignment alignmentA = A.getPersonalAlignment();
			A.setPersonalAlignment(alignmentA);
			int a1 = alignmentA.alignmentScore();

			SNPAlignment alignmentB = B.getPersonalAlignment();
			B.setPersonalAlignment(alignmentB);
			int b1 = alignmentB.alignmentScore();

			SNPAlignment alignmentC = C.getPersonalAlignment();
			C.setPersonalAlignment(alignmentC);
			int c1 = alignmentC.alignmentScore();

			// TechnicalSupport D = in.getTechnicalSupport(4);

			System.out.println(A.getPersonalAlignment());
			System.out.println(B.getPersonalAlignment());
			System.out.println(C.getPersonalAlignment());

			/*
			 * 
			 * System.out.printf("The team:%n%s. %n"
			 * + "Bioinformaticians: %s, %s, %s.%n"
			 * + "Technical Support: %s.%n%n",A.getLeadBy(), A.getName(), B.getName(),
			 * C.getName(),D.getName());
			 */
			System.out.printf("\t Personal alignment score: %n");
			System.out.printf("%s: %d.%n"
					+ "%s: %d.%n"
					+ "%s: %d.%n"
					+ "%n", A.getName(), a1, B.getName(), b1, C.getName(), c1);

			alignmentA.replaceOccurences(">2001.F1.BR.01.01BR087", "TGTCCTGGGG", "AAAAAAAAAA");
			A.writeDataToFile();
			// alignmentScore() return method is immutable, thus, it's needed to be set
			// again
			a1 = alignmentA.alignmentScore();

			alignmentB.replaceOccurences(">2001.F1.BR.01.01BR087", "TGTCCTGGGG", "TGTCCTGGGG");
			B.writeDataToFile();
			b1 = alignmentB.alignmentScore();

			// C.writeDataToFile();
			c1 = alignmentC.alignmentScore();
			System.out.printf("\t After modification: %n"
					+ "%s: %d.%n"
					+ "%s: %d.%n"
					+ "%s: %d.%n", A.getName(), a1, B.getName(), b1, C.getName(), c1);

			// Check if Bioinfomatician can access Repository
			// A.getRepository();
			// Repository rep = X.getRepository();// this Repository cannot be set in main
			// method

		} catch (Exception e) {
			System.out.println("INVALID!");
			e.printStackTrace();
		}

	}
}
