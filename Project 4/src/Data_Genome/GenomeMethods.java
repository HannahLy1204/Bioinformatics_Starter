package Data_Genome;

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
	void replaceOccurences(String toBeReplaced, String givenFragments, String newFragments);
	void replaceOccurencesAll(String givenFragments, String newFragments);
	int alignmentScore();
	void deleteFragments(String target, String removeFragment);
}
