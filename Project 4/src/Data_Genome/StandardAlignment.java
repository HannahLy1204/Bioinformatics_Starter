package Data_Genome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class StandardAlignment implements GenomeMethods{
	private HashMap<String,String> alignment;
	protected List<Genome> collection; //Both StandardAlignment and SNPAlignment should have the same collection of Genome

	//Default constructor
	public StandardAlignment() {
		this.alignment= new LinkedHashMap<String,String>();
		this.collection = new ArrayList<>();
	}

	public StandardAlignment(HashMap<String,String> map) {
		this.setAlignment(map);
		this.setGenomeCollection();
	}

	public HashMap<String, String> getAlignment() {
		return alignment;
	}
	public void setAlignment(HashMap<String, String> alignment) {
		this.alignment = alignment;
	}
	public List<Genome> getGenomeCollection(){
		return this.collection;
	}
	public void setGenomeCollection() {
		this.collection = new ArrayList<>();
		for (String x: alignment.keySet()) {
			Genome g = new Genome(x, alignment.get(x));
			this.collection.add(g);
		}
	}
	
	public Genome referenceGenome(HashMap<String,String> standard){
		Genome reference = new Genome();
		List<String> listID = new ArrayList<>();

		//Transfer key set to list, in order to extract the first genomeID:
		Set<String> ids = standard.keySet();
		for(String i: ids) {
			listID.add(i);
		}

		//Setting the first genome as reference:
		String referenceID = listID.get(0);//first Genome identifier
		String referenceSeq = standard.get(listID.get(0));//first Genome sequence

		reference.setID(referenceID);//newMap containing the reference sequence
		reference.setSequence(referenceSeq);

		return reference;
	}

	//FUNCTION.......................................
	@Override
	public Set<String> getAllIdentifiers(){
		return alignment.keySet();
	}

	@Override
	public String getOneSequence(String identifier) throws IllegalArgumentException{
		if (alignment.containsKey(identifier)) {
			return alignment.get(identifier);
		}else {
			throw new IllegalArgumentException(identifier);
		}
	}

	@Override
	public void addGenome(Genome newGenome) {
		alignment.put(newGenome.getID(), newGenome.getSequence());
	}

	@Override
	public void removeGenome(String id) {
		alignment.remove(id);
	}

	@Override
	public void deleteFragments(String target, String removeFragment) {
		if(alignment.containsKey(target)) {
			String genomeSequence = alignment.get(target);
			if(genomeSequence.contains(removeFragment)) {
				String newGenome = genomeSequence.replace(removeFragment, "");//Create new String variable as it is immutable
				alignment.replace(target, newGenome);
			}else {
				throw new IllegalArgumentException(target + " does not contain this sequence!");
			}
		}else {
			throw new IllegalArgumentException(target + " is not available in Alignment!");}
	}

	@Override
	public List<String> searchGenome(String fragments){//CHECKED
		List<String> identifier = new ArrayList<>();
		if(alignment!=null) {
			Set<String> set = this.getAllIdentifiers();
			for (String s: set) {
				String sequence = alignment.get(s);
				if(sequence.contains(fragments)) {
					identifier.add(s);
				}
			}
		}
		return identifier;
	}

	@Override
	public void replaceGenome(String toBeReplaced, Genome newGenome) throws IllegalArgumentException{//CHECKED
		HashMap<String,String> newMap = new LinkedHashMap<String,String>();
		String newID = newGenome.getID();
		String newSequence = newGenome.getSequence();
		if(alignment!=null) {
			Set<String> set = this.getAllIdentifiers();
			if(!alignment.containsKey(newID)) {
				if(!alignment.containsValue(newSequence)) {
					for (String w:set) {
						String nu = alignment.get(w);
						if (w.equals(toBeReplaced)) {
							w = newID;
							nu = newSequence;
						}
						newMap.put(w, nu);
					}
					this.setAlignment(newMap);
				}else {newMap = alignment;
				throw new IllegalArgumentException("The provided sequence is");}
			}else {newMap = alignment;
			throw new IllegalArgumentException(newID+" is");}
		}else {newMap = alignment;
		throw new IllegalArgumentException("Null Alignment Collection!");}
	}

	@Override
	public void replaceOccurences(String toBeReplaced, String givenFragments, String newFragments) throws IllegalArgumentException {

		int oldLength = givenFragments.length();
		int newLength = newFragments.length();

		if(alignment.containsKey(toBeReplaced)) {
			String genomeSequence = alignment.get(toBeReplaced);
			if(genomeSequence.contains(givenFragments)) {//not implement IllegalArgumentException in order to apply this method for entire Alignment
				if (oldLength==newLength) {
					String newGenome = genomeSequence.replace(givenFragments, newFragments);//Create new String variable as it is immutable
					alignment.replace(toBeReplaced, newGenome);
				}else {
					throw new IllegalArgumentException("The two provided sequences are not the same length!");}	
			}	
		}else {
			throw new IllegalArgumentException(toBeReplaced + " is not available in Alignment!");}

	}

	@Override
	public void replaceOccurencesAll(String givenFragments, String newFragments) throws IllegalArgumentException {
		Set<String> whole = this.getAllIdentifiers();
		for (String x: whole) {
			this.replaceOccurences(x, givenFragments, newFragments);
		}

	}

	@Override
	public int alignmentScore() {
		Genome firstGenome = this.referenceGenome(alignment);
		int score = 0;

		for(String x: alignment.keySet()) {
			String seq = alignment.get(x);
			for(int i = 0; i< seq.length();i++) {
				if(seq.charAt(i) != firstGenome.getSequence().charAt(i)) {
					score ++;
				}else {
					score += 0;
				}
			}
		}
		return score;
	}

}
