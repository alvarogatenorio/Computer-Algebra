package utils;

import java.util.ArrayList;
import java.util.List;

import structures.Ring;
import structures.UnitRing;

/*COMMENT AND TEST*/

public class Polynomials<T extends Polynomial<E>, E> implements Ring<T> {

	private Ring<E> baseRing;
	private char variable = 'T';

	public Polynomials(Ring<E> baseRing) {
		this.baseRing = baseRing;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getAddIdentity() {
		List<E> addIdentity = new ArrayList<E>();
		addIdentity.add(baseRing.getAddIdentity());
		return (T) new Polynomial<E>(addIdentity, baseRing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getAddInverse(T a) {
		List<E> addInverse = new ArrayList<E>();
		for (int i = 0; i < a.size(); i++) {
			addInverse.add(baseRing.getAddInverse(a.get(i)));
		}
		return (T) new Polynomial<E>(addInverse, baseRing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T add(T a, T b) {
		/* We create the result polynomial */
		List<E> sum = new ArrayList<E>();

		/*
		 * The result polynomial will have, at most, the degree of the greater degree
		 * polynomial
		 */
		int sizeUpperBound = Math.max(a.size(), b.size());

		/*
		 * We add the polynomials coefficient by coefficient, taking into account the
		 * cases when one polynomial is shorter than the other
		 */
		for (int i = 0; i < sizeUpperBound; i++) {
			E coefficientSum;

			if (a.size() <= i) {
				coefficientSum = baseRing.add(baseRing.getAddIdentity(), b.get(i));
			} else if (b.size() <= i) {
				coefficientSum = baseRing.add(a.get(i), baseRing.getAddIdentity());
			} else {
				coefficientSum = baseRing.add(a.get(i), b.get(i));
			}

			sum.add(coefficientSum);
		}

		/*
		 * We clean the last add identity elements from the result polynomial. We take
		 * care of the case in which the result polynomial is the add identity.
		 */
		while (sum.get(sum.size() - 1).equals(baseRing.getAddIdentity()) && sum.size() > 1) {
			sum.remove(sum.size() - 1);
		}

		/* Cast and return the result */
		return (T) new Polynomial<E>(sum, baseRing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T multiply(T a, T b) {
		/* We create the result polynomial */
		List<E> product = new ArrayList<E>();

		/*
		 * The result polynomial will have degree of, at most, the sum of both
		 * polynomials degrees
		 */
		int sizeUpperBound = (a.size() - 1) + (b.size() - 1);

		/* We just follow the general formula for the product of two polynomials */
		for (int k = 0; k <= sizeUpperBound; k++) {
			E coefficient = baseRing.getAddIdentity();
			for (int p = 0; p <= k; p++) {
				/* All of these is for taking care about the limits of the lists */
				E factor1;
				E factor2;

				if (a.size() <= p) {
					factor1 = baseRing.getAddIdentity();
				} else {
					factor1 = a.get(p);
				}

				if (b.size() <= k - p) {
					factor2 = baseRing.getAddIdentity();
				} else {
					factor2 = b.get(k - p);
				}

				coefficient = baseRing.add(coefficient, baseRing.multiply(factor1, factor2));
			}
			product.add(coefficient);
		}

		/*
		 * We clean the last add identity elements from the result polynomial. We take
		 * care of the case in which the result polynomial is the add identity.
		 */
		while (product.get(product.size() - 1).equals(baseRing.getAddIdentity()) && product.size() > 1) {
			product.remove(product.size() - 1);
		}

		/* Cast and return the result */
		return (T) new Polynomial<E>(product, baseRing);
	}

	/*
	 * It construct a polynomial from a valid LaTeX string, that is, something
	 * adjusting the following regular expression
	 * ((T(\^i)a|T(\^i)?|aT(\^i)?|a)\+)*(T(\^i)a|T(\^i)?|aT(\^i)?|a), being a an
	 * String representation of any element of the class parameter T, i an integer
	 * positive number (maybe zero), and T the variable.
	 * 
	 * To maintain the invariant of this class we need the ring in which the
	 * coefficients are considered to live, for example, we may have a parameter
	 * like "T+T", so we have to transform this to "2T" (in terms of our intern
	 * representation).
	 * 
	 * Important, we don´t check if the parameter string is a valid LaTeX string,
	 * this is, for the moment, responsibility of the programmer/client.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T parseElement(String latexString) {

		/*
		 * We know the implementation is a little bit "quick and dirty", but it was made
		 * like this for the sake of simplicity
		 */

		/* Initializing the list of coefficients of the polynomial */
		List<E> coefficients = new ArrayList<E>();

		/*
		 * First we split the string by the regular expression "+", so we should have
		 * something like (T(\^i)a|T(\^i)?|aT(\^i)?|a)
		 */
		String[] monomials = latexString.split("\\+");

		/* The we work on each token separately */
		for (int i = 0; i < monomials.length; i++) {
			/*
			 * We split by the variable, so we will get a single token like (\^i a|\^i|a),
			 * an empty string, or two tokens like [a,\^i]
			 */
			String[] monomial = monomials[i].split("" + variable);

			/* We consider every case */
			if (monomial.length == 0) {
				/* In this case, the monomial is T, so we know exactly what to insert */
				coefficients = updateCoefficients(coefficients, 1, ((UnitRing<E>) baseRing).getProductIdentity());
			} else if (monomial.length == 1) {
				/* DISTINGUIR LOS TRES CASOS DEL DEMONIO */
			} else {
				/*
				 * If we get two tokens, the first must be the actual coefficient token, and the
				 * last must be the degree token, we clean first the degree token
				 */
				int termDegree = Integer.parseInt(monomial[1].replaceFirst("\\^", ""));

				/* Then we parse the coefficient token */
				E coefficient = baseRing.parseElement(monomial[0]);

				/* And finally we update the list */
				coefficients = updateCoefficients(coefficients, termDegree, coefficient);
			}
		}

		/* Returns the polynomial */
		return (T) new Polynomial<E>(coefficients, baseRing);
	}

	/* Hacer un método privado con mierdecilla diversa */
	private List<E> updateCoefficients(List<E> coefficients, int termDegree, E coefficient) {
		/* Adding zeroes if necessary */
		while (coefficients.size() < termDegree) {
			coefficients.add(baseRing.getAddIdentity());
		}

		/* Adding the term to the polynomial */
		if (coefficients.size() == termDegree) {
			coefficients.add(coefficient);
		} else {
			coefficients.set(termDegree, baseRing.add(coefficients.get(termDegree), coefficient));
		}

		return coefficients;
	}
}