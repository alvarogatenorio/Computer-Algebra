package utils;

import java.math.BigInteger;

public class FiniteFieldElement {
	private Polynomial<BigInteger> element;

	public FiniteFieldElement(Polynomial<BigInteger> element) {
		this.element = element;
	}

	/* arreglar la representación */
	public String toString() {
		String result = "(";
		for (int i = 0; i < element.size(); i++) {
			result = result + element.get(i) + ",";
		}
		return result.substring(0, result.length() - 1) + ")";
	}

	public Polynomial<BigInteger> getPolynomial() {
		return element;
	}
}
