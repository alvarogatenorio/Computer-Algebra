package structures.complex;

import java.util.ArrayList;
import java.util.List;

import structures.basic.Ring;

/* 
 * A polynomial is just a list of several coefficients, formally, this coefficients have to belong to
 * a certain ring, so we must specify it in the constructor.
 * 
 * The following invariant must hold. The i-th coefficient is the i degree coefficient,
 * the last coefficient (in other words, the leading coefficient) has to be non-zero.
 * */

public class Polynomial<T> {

	/*
	 * This represents the polynomial coefficients, following the previously defined
	 * invariant
	 */
	private List<T> coefficients;

	/*
	 * This attribute is only relevant for printing issues it represents the name of
	 * the polynomial variable, by default, it will be 'T'
	 */
	private char variable = 't';

	private Ring<T> baseRing;

	/*
	 * Just sets the coefficients to define the polynomial and sets the ring so we
	 * can parse it from a LaTeX string
	 */
	public Polynomial(List<T> coefficients, Ring<T> baseRing) {
		this.coefficients = coefficients;
		this.baseRing = baseRing;
	}

	/* Sets a custom variable */
	public void setVariable(char variable) {
		this.variable = variable;
	}

	/* Sets a custom ring */
	public void setRing(Ring<T> baseRing) {
		this.baseRing = baseRing;
	}

	/*
	 * Returns the size of the coefficients list, in terms of degrees is the
	 * polynomial degree plus one (with the exception of a zero polynomial)
	 */
	public int size() {
		return this.coefficients.size();
	}

	public int degree() {
		return size() - 1;
	}

	public T leading() {
		return this.coefficients.get(degree());
	}

	public T independent() {
		return this.coefficients.get(0);
	}

	/* Returns the coefficient corresponding to the index degree term */
	public T get(int index) {
		return this.coefficients.get(index);
	}

	/*
	 * This method will print the polynomial in a LaTeX friendly format, we will
	 * print the coefficients from higher to lower degree. We need the ring in which
	 * the polynomial coefficients are considered to live so we can omit zeroes and
	 * ones in the final printing
	 */
	@Override
	public String toString() {
		/* We only take care of product identities in the case this actually exists */

		/*
		 * We will use an auxiliary list to print every term separately so we can avoid
		 * problems of printing more or less additive symbols than necessary
		 */
		List<String> aux = new ArrayList<String>();

		/*
		 * The actual printing of the polynomial will be a proper concatenation of the
		 * auxiliary list
		 */
		String result = "";

		/*
		 * We know this is kind of repeating a big chunk of code, but we think is more
		 * efficient than other options we have came across, first we consider the case
		 * in which multiplicative identity does not exists, then, the other case
		 */
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
		 * Properly join the auxiliary list
		 * 
		 * Not sure about the complexity of this little chunk of code, hope its not
		 * quadratic
		 */
		result = aux.get(0);
		for (int i = 1; i < aux.size(); i++) {
			result = result + "+" + aux.get(i);
		}

		return result;
	}

	public Ring<T> getBaseRing() {
		return baseRing;
	}

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
