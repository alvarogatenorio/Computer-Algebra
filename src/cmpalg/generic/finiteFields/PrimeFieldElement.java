package cmpalg.generic.finiteFields;

import java.math.BigInteger;

public class PrimeFieldElement extends FiniteFieldElement {

	private BigInteger element;

	public PrimeFieldElement(BigInteger element) {
		this.element = element;
	}

	public BigInteger getElement() {
		return element;
	}

	public boolean equals(Object o) {
		PrimeFieldElement other = (PrimeFieldElement) (o);
		return element.equals(other.getElement());
	}

	public String toString() {
		return element.toString();
	}

}
