package Constants;

public enum Nucleotide {
	A(3),
	T(3),
	C(5),
	G(5);
	
	private final int mutationPoint;

	private Nucleotide(int point) {
		this.mutationPoint = point;
	}

	public int getMutation() {
		return mutationPoint;
	}
}
