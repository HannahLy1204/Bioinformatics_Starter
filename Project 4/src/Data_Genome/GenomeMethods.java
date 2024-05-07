package Data_Genome;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface GenomeMethods {
	Set<String> getAllIdentifiers();
	String getOneSequence(String identifier);
	void addGenome(Genome newGenome);
	void removeGenome(String id);
	
	//Basic functionalities:
	List<String> searchGenome(String fragments);
	void replaceGenome(String toBeReplaced, Genome newGenome);
	HashMap<String, String> replaceOccurences(String toBeReplaced, String givenFragments, String newFragments);
	void replaceOccurencesAll(String givenFragments, String newFragments);
	int alignmentScore();
	HashMap<String,String> deleteFragments(String target, String removeFragment);
	
	//Other functionalities:
	//public boolean comparing two alignment

}
