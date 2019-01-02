package cmpalg.generic.finiteFields;

import java.math.BigInteger;

import structures.generic.polynomials.Polynomial;

public class PrimePowerFieldElement extends FiniteFieldElement {
	private Polynomial<PrimeFieldElement> element;
	private int tupleSize;

	public PrimePowerFieldElement(Polynomial<PrimeFieldElement> element, int tupleSize) {
		this.element = element;
		this.tupleSize = tupleSize;
	}

	public String toString() {
		String result = "(";
		for (int i = 0; i < tupleSize; i++) {
			if (i < element.size()) {
				result = result + element.get(i) + ",";
			} else {
				result = result + BigInteger.ZERO + ",";
			}
		}
		return result.substring(0, result.length() - 1) + ")";
	}

	public Polynomial<PrimeFieldElement> getElement() {
		return element;
	}

	@Override
	public boolean equals(Object o) {
		PrimePowerFieldElement other = (PrimePowerFieldElement) o;
		return other.getElement().equals(getElement());
	}
	
	public int hashCode() {
		return 0;
	}
}
