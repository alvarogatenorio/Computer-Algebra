package utils;

import java.util.ArrayList;
import java.util.List;

import structures.Ring;
import structures.UnitRing;

/* 
 * A polynomial is just a list of several coefficients, formally, this coefficients have to belong to
 * a certain ring, but this class does not contain this information, we only define the concrete ring
 * when operating polynomials with the class Polynomials.
 * 
 * The following invariant must hold. The i-th coefficient is the i degree coefficient,
 * the last coefficient (in other words, the leading coefficient) has to be non-zero.
 * */

public class Polynomial<T> {
	private List<T> coefficients;

	public Polynomial(List<T> coefficients) {
		this.coefficients = coefficients;
	}

	public int size() {
		return this.coefficients.size();
	}

	public T get(int index) {
		return this.coefficients.get(index);
	}

	/*
	 * This method will print the polynomial in a LaTeX friendly format, we will
	 * print the coefficients from higher to lower degree
	 */
	public String toStringLaTeX(Ring<T> baseRing) {
		boolean unitRing = baseRing instanceof UnitRing;
		List<String> aux = new ArrayList<String>();
		String result = "";

		if (!unitRing) {
			for (int i = coefficients.size() - 1; i > 1; i--) {
				if (!coefficients.get(i).equals(baseRing.getAddIdentity())) {
					aux.add(coefficients.get(i).toString() + "T^" + i);
				}
			}
			if (!coefficients.get(1).equals(baseRing.getAddIdentity())) {
				aux.add(coefficients.get(0).toString() + "T");
			}
			if (!coefficients.get(0).equals(baseRing.getAddIdentity())) {
				aux.add(coefficients.get(0).toString());
			}
		} else {
			for (int i = coefficients.size() - 1; i > 1; i--) {
				if (!coefficients.get(i).equals(baseRing.getAddIdentity())) {
					if (coefficients.get(i).equals(((UnitRing<T>) (baseRing)).getProductIdentity())) {
						aux.add("T^" + i);
					} else {
						aux.add(coefficients.get(i).toString() + "T^" + i);
					}
				}
			}
			if (!coefficients.get(1).equals(baseRing.getAddIdentity())) {
				aux.add(coefficients.get(0).toString() + "T");
			}
			if (!coefficients.get(0).equals(baseRing.getAddIdentity())) {
				aux.add(coefficients.get(0).toString());
			}
		}

		result = aux.get(0);
		for (int i = 1; i < aux.size(); i++) {
			result = result + "+" + aux.get(i);
		}

		return result;
	}
}
