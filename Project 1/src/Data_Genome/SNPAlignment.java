package Data_Genome;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SNPAlignment extends StandardAlignment{
	private HashMap<String,String> snip;

	public SNPAlignment(HashMap<String,String> map) {
		super(map);
		this.convertSNiP();
		//The SNPAlignment contains the standard HashMap in its superclass and its SNP HashMap.
	}

	public SNPAlignment() {
		super();
	}

	private HashMap<String,String> convertSNiP(){//The conversion should be set only in SNPAlignment class
		Genome firstGenome = this.referenceGenome(getAlignment());

		HashMap<String,String> newMap = new LinkedHashMap<>();
		newMap.put(firstGenome.getID(), firstGenome.getSequence()); //Adding reference Genome to the newMap

		for(String x: this.getAllIdentifiers()) {
			if(!x.equals(firstGenome.getID())) {//Skip the first genome reference in standard
				String seq = this.getOneSequence(x);
				String newSeq = "";

				//Conversion of each nucleotide in genome Sequence
				for(int i = 0; i< seq.length();i++) {
					//Converting each common nucleotide into "."
					if(seq.charAt(i) == firstGenome.getSequence().charAt(i)) {
						newSeq += ".";
					}else {
						newSeq += seq.charAt(i);
					}
				}
				newMap.put(x, newSeq);
			}
		}

		this.snip = newMap;
		return snip;
	}

	public HashMap<String, String> getSNiPMap() {
		return snip;
	}

	//The output from this class of the void method from StandardAlignment (superclass) is not adapted to SNiP pattern!
	//Thus, void methods of superclass are needed to be overwritten
	@Override
	public void addGenome(Genome newGenome) {
		super.addGenome(newGenome);
		this.convertSNiP();
	}

	@Override
	public void removeGenome(String id) {
		super.removeGenome(id);
		this.convertSNiP();
	}

	@Override
	public HashMap<String, String> deleteFragments(String target, String removeFragment) {
		super.deleteFragments(target,removeFragment);
		return this.convertSNiP();
	}

	@Override
	public void replaceGenome(String toBeReplaced, Genome newGenome) throws IllegalArgumentException{//CHECKED
		super.replaceGenome(toBeReplaced,newGenome);
		this.convertSNiP();
	}

	@Override
	public HashMap<String, String> replaceOccurences(String toBeReplaced, String givenFragments, String newFragments) throws IllegalArgumentException {
		super.replaceOccurences(toBeReplaced,givenFragments, newFragments);
		return this.convertSNiP();

	}

	@Override
	public void replaceOccurencesAll(String givenFragments, String newFragments) throws IllegalArgumentException {
		super.replaceOccurencesAll(givenFragments, newFragments);
		this.convertSNiP();
	}

	//Alignment score of StandardAlignment is identical to SNPAlignment
	
	@Override
	public int alignmentScore() {
		Genome firstGenome = this.referenceGenome(snip);
		int score = 0;

		for(String x: snip.keySet()) {
			if(!x.equals(firstGenome.getID())) {//Skip the first genome 
				String seq = snip.get(x);
				for(int i = 0; i< seq.length();i++) {
					if(seq.charAt(i) != '.') {
						score ++;
					}else {
						score += 0;
					}}}}
		return score;
	}
}
