package structures.complex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import structures.basic.Ring;
import utils.Polynomial;
import utils.Triple;

/*COMMENT AND TEST*/

/*MODULAR COMPOSITION*/

public class Polynomials<T extends Polynomial<E>, E> extends Ring<T> {

	protected Ring<E> baseRing;
	protected char variable = 't';

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

	/* Maybe a more efficient implementation, when we have time... */
	@SuppressWarnings("unchecked")
	public T add(T a, E e) {
		List<E> sum = new ArrayList<E>();
		sum.add(baseRing.add(a.get(0), e));
		for (int i = 1; i < a.size(); i++) {
			sum.add(a.get(i));
		}
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

	@SuppressWarnings("unchecked")
	public T multiply(T a, E e) {
		List<E> product = new ArrayList<E>();
		for (int i = 0; i < a.size(); i++) {
			product.add(baseRing.multiply(a.get(i), e));
		}
		return (T) new Polynomial<E>(product, baseRing);
	}

	/*
	 * It construct a polynomial from a valid LaTeX string, that is, something
	 * adjusting the following regular expression
	 * ((T(\^i)\*a|T(\^i)?|a(\*)?T(\^i)?|a)\+)*(T(\^i)\*a|T(\^i)?|a(\*)?T(\^i)?|a),
	 * being a an String representation of any element of the class parameter T, i
	 * an integer positive number (maybe zero), and T the variable.
	 * 
	 * To maintain the invariant of this class we need the ring in which the
	 * coefficients are considered to live, for example, we may have a parameter
	 * like "T+T", so we have to transform this to "2T" (in terms of our intern
	 * representation).
	 * 
	 * Important, we don�t check if the parameter string is a valid LaTeX string,
	 * this is, for the moment, responsibility of the programmer/client.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T parseElement(String latexString) {

		/*
		 * We know the implementation is a little bit "quick and dirty", but it was made
		 * like this for the sake of simplicity
		 */

		/* We clean the string of white characters */
		latexString = latexString.replaceAll("( |\\t|\\n)", "");

		/* Initializing the list of coefficients of the polynomial */
		List<E> coefficients = new ArrayList<E>();

		/*
		 * First we split the string by the regular expression "+", so we should have
		 * something like ( T(\^i)\*a | T(\^i)? | a(\*)?T(\^i)? |a )
		 */
		String[] monomials = latexString.split("\\+");

		/* The we work on each token separately */
		for (int i = 0; i < monomials.length; i++) {

			int termDegree;
			E coefficient;

			/*
			 * We split by the variable, so we will get a single token like ( \^i\*a | \*a |
			 * \^i | a | a\*), an empty string, or two tokens like ([a,\^i] | [a\*,\^i])
			 * 
			 * We also need here to know, before splitting the string, if it actually
			 * contains the variable, in order to compute the term degrees correctly in the
			 * "a" cases
			 */
			boolean containsVariable = monomials[i].contains("" + variable);
			String[] monomial = monomials[i].split("" + variable);

			/* Cleaning the possible empty strings generated by the splitting */
			monomial = cleanEmptyStrings(monomial);

			/* We consider every case, first, the empty string case */
			if (monomial.length == 0) {

				/* In this case, the monomial is T, so we know exactly what to insert */
				termDegree = 1;
				coefficient = baseRing.getProductIdentity();
			} else if (monomial.length == 1) {

				if (monomial[0].startsWith("^")) { /* Cases beginning with \^ */

					/*
					 * We take out the exponent symbol, so the possible strings are now (i\*a) and
					 * (i)
					 */
					String exponentCleaned = monomial[0].replaceFirst("\\^", "");

					/* We split by \*, so we may get two tokens, [i,a] or one token (i) */
					String[] productSplit = exponentCleaned.split("\\*");
					if (productSplit.length == 1) {
						termDegree = Integer.parseInt(productSplit[0]);
						coefficient = baseRing.getProductIdentity();
					} else {
						termDegree = Integer.parseInt(productSplit[0]);
						coefficient = baseRing.parseElement(productSplit[1]);
					}
				} else { /* a cases */
					if (containsVariable) {
						termDegree = 1;
					} else {
						termDegree = 0;
					}
					coefficient = baseRing.parseElement(monomial[0].replaceFirst("\\*", ""));
				}

			} else {

				/*
				 * If we get two tokens, the first must be the actual coefficient token, and the
				 * last must be the degree token, we clean first the degree token
				 */
				termDegree = Integer.parseInt(monomial[1].replaceFirst("\\^", ""));

				/* Then we parse the coefficient token */
				coefficient = baseRing.parseElement(monomial[0].replaceAll("\\*", ""));
			}

			/* Finally we update the list */
			coefficients = updateCoefficients(coefficients, termDegree, coefficient);
		}

		coefficients = cleanIdentities(coefficients);

		/* Returns the polynomial */
		return (T) new Polynomial<E>(coefficients, baseRing);
	}

	private List<E> cleanIdentities(List<E> coefficients) {
		for (int i = coefficients.size() - 1; i >= 0; i--) {
			if (coefficients.get(i).equals(baseRing.getAddIdentity())) {
				coefficients.remove(i);
			} else {
				break;
			}
		}
		return coefficients;
	}

	/*
	 * Auxiliary private method for the parseElement() method, it just sums up some
	 * coefficient in the right position of the coefficients list
	 */
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

	/*
	 * Auxiliary private method for the parseElement() method, it just removes the
	 * empty strings of an String array
	 */
	private String[] cleanEmptyStrings(String[] strings) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < strings.length; i++) {
			if (!strings[i].equals("")) {
				result.add(strings[i]);
			}
		}
		return (String[]) result.toArray(new String[0]);
	}

	/*
	 * Returns a triple representing the pseudo division of two polynomials over an
	 * arbitrary ring. For details, see the docs.
	 * 
	 * If the leading coefficient of b is a unit, then k can be supposed to be 0.
	 * 
	 * If the ring is a field, the pseudo division is actually an euclidean division
	 */
	public Triple<BigInteger, T> pseudoDivision(T a, T b) {
		BigInteger k = BigInteger.ZERO;
		T q = null;
		T r = null;
		if (a.degree() < b.degree()) {
			k = BigInteger.ZERO;
			q = getAddIdentity();
			r = a;
		} else {
			if (a.degree() == 0) {
				k = BigInteger.ONE;
				q = a;
				r = getAddIdentity();
			} else {
				T h = add(multiply(a, b.leading()), multiply(
						multiply(parseElement("t^" + (a.degree() - b.degree())), baseRing.getAddInverse(a.leading())),
						b));
				if (h.degree() < b.degree()) {
					k = BigInteger.ONE;
					q = multiply(parseElement("t^" + (a.degree() - b.degree())), a.leading());
					r = h;
				} else {
					Triple<BigInteger, T> aux = pseudoDivision(h, b);
					k = aux.getFirst().add(BigInteger.ONE);
					// errors here
					q = add(aux.getSecond(), multiply(parseElement("t^" + (a.degree() - b.degree())),
							baseRing.multiply((baseRing.power(b.leading(), aux.getFirst())), a.leading())));
					r = aux.getThird();
				}
			}
		}
		return new Triple<BigInteger, T>(k, q, r);
	}

	@Override
	public boolean divides(T a, T b) {
		T r = pseudoDivision(a, b).getThird();
		T s = getAddIdentity();
		return r.equals(s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T intMultiply(T a, BigInteger k) {
		List<E> result = new ArrayList<E>();
		for (int i = 0; i < a.size(); i++) {
			result.add(baseRing.intMultiply(a.get(i), k));
		}
		return (T) new Polynomial<E>(result, baseRing);
	}

	/* We assume a divides b and k = 0 */
	@Override
	public T divFactor(T a, T b) {
		return pseudoDivision(a, b).getSecond();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getProductIdentity() {
		List<E> addIdentity = new ArrayList<E>();
		addIdentity.add(baseRing.getProductIdentity());
		return (T) new Polynomial<E>(addIdentity, baseRing);
	}

	/** Returns remainder(g(h),f). */
	public T modularComposition(T f, T g, T h) {
		/* Fast modular composition algorithm. */
		return null;
	}

}