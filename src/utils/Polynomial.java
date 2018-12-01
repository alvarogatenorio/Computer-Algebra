package utils;

import java.util.ArrayList;
import java.util.List;

import structures.basic.Ring;

/**
 * A polynomial is just a list of several coefficients, this coefficients have
 * to belongs to a ring, specified it in the constructor. This is only relevant
 * for printing issues.
 * 
 * The following invariant must hold. The i-th coefficient is the i degree
 * coefficient, the last coefficient (in other words, the leading coefficient)
 * has to be non-zero.
 */
public class Polynomial<T> {

	/**
	 * Represents the polynomial coefficients, following the previously defined
	 * invariant.
	 */
	private List<T> coefficients;

	/**
	 * Only relevant for printing issues it represents the name of the polynomial
	 * variable, by default, it will be 't'.
	 */
	private char variable = 't';

	/** Represents the ring in which the coefficients live. */
	private Ring<T> baseRing;

	/**
	 * Sets the coefficients to define the polynomial and sets the ring so we can
	 * print it.
	 */
	public Polynomial(List<T> coefficients, Ring<T> baseRing) {
		this.coefficients = coefficients;
		this.baseRing = baseRing;
	}

	/** Returns the ring in which the coefficients live. */
	public Ring<T> getBaseRing() {
		return baseRing;
	}

	/** Sets a custom variable. */
	public void setVariable(char variable) {
		this.variable = variable;
	}

	/**
	 * Returns the size of the coefficients list, in terms of degrees is the
	 * polynomial degree plus one (with the exception of the zero polynomial).
	 */
	public int size() {
		return this.coefficients.size();
	}

	/**
	 * Returns the degree of the polynomial. The programmer must take into account
	 * the special case of the zero polynomial.
	 */
	public int degree() {
		return size() - 1;
	}

	/** Returns the leading coefficient of the polynomial. */
	public T leading() {
		return this.coefficients.get(degree());
	}

	/** Returns the independent coefficient of the polynomial. */
	public T independent() {
		return this.coefficients.get(0);
	}

	/** Returns the coefficient corresponding to the index degree term. */
	public T get(int index) {
		return this.coefficients.get(index);
	}

	/**
	 * Prints the polynomial in a LaTeX friendly format, the coefficients will be
	 * printed from higher to lower degree.
	 */
	@Override
	public String toString() {
		/*
		 * We will use an auxiliary list to print every term separately so we can avoid
		 * problems of printing more or less additive symbols than necessary.
		 */
		List<String> aux = new ArrayList<String>();

		/*
		 * The actual printing of the polynomial will be a proper concatenation of the
		 * auxiliary list.
		 */
		String result = "";

		/* Notice the 0 and 1 degree terms are special */
		for (int i = coefficients.size() - 1; i > 1; i--) {
			if (!coefficients.get(i).equals(baseRing.getAddIdentity())) {
				if (coefficients.get(i).equals(baseRing.getProductIdentity())) {
					aux.add(variable + "^" + i);
				} else {
					aux.add(coefficients.get(i).toString() + variable + "^" + i);
				}
			}
		}
		if ((coefficients.size() >= 2) && coefficients.get(1).equals(baseRing.getProductIdentity())) {
			aux.add("" + variable);
		} else if ((coefficients.size() >= 2) && !coefficients.get(1).equals(baseRing.getAddIdentity())) {
			aux.add(coefficients.get(1).toString() + variable);
		}
		if (!coefficients.get(0).equals(baseRing.getAddIdentity()) || (coefficients.size() == 1)) {
			aux.add(coefficients.get(0).toString());
		}

		/*
		 * Properly joins the auxiliary list
		 * 
		 * Not sure about the complexity of this little chunk of code, hope its not
		 * quadratic...
		 */
		result = aux.get(0);
		for (int i = 1; i < aux.size(); i++) {
			result = result + "+" + aux.get(i);
		}

		return result;
	}

	/**
	 * Evaluates the polynomial in the specified value. See the documentation for
	 * details.
	 */
	public T evaluate(T a) {
		/* Horner's algorithm */
		T evaluation = baseRing.getAddIdentity();
		for (int i = degree(); i >= 0; i--) {
			evaluation = baseRing.add(baseRing.multiply(evaluation, a), get(i));
		}
		return evaluation;
	}

	public List<T> getCoefficients() {
		return coefficients;
	}

	/**
	 * Two polynomials are equal if they have the same degree, and the same
	 * coefficients.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		Polynomial<T> p = (Polynomial<T>) o;
		if (p.degree() == degree()) {
			for (int i = 0; i < size(); i++) {
				if (!get(i).equals(p.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
