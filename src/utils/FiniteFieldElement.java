package utils;

import java.math.BigInteger;

import structures.concrete.Integers;

public class FiniteFieldElement {
	private Polynomial<BigInteger> element;
	private int tupleSize;

	/* asume que el polinomio que se le pase está en forma canónica */
	public FiniteFieldElement(Polynomial<BigInteger> element, Polynomial<BigInteger> irrPolMod) {
		this.element = element;
		this.tupleSize = irrPolMod.degree();
	}

	/* arreglar la representación */
	public String toString() {
		Integers Z = new Integers();
		String result = "(";
		for (int i = 0; i < tupleSize; i++) {
			if (i < element.size()) {
				result = result + element.get(i) + ",";
			} else {
				result = result + Z.getAddIdentity() + ",";
			}
		}
		return result.substring(0, result.length() - 1) + ")";
	}

	public Polynomial<BigInteger> getPolynomial() {
		return element;
	}

	@Override
	public boolean equals(Object o) {
		FiniteFieldElement e = (FiniteFieldElement) o;
		if (e.getPolynomial().equals(getPolynomial())) {
			return true;
		}
		return false;
	}
}
