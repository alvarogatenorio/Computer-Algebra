package structures.generic.polynomials;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cmpalg.generic.basic.Ring;
import structures.generic.matrixes.Matrix;
import structures.generic.matrixes.Matrixes;
import utils.Triple;

/**
 * Represents the ring of polynomials over a certain ring. T represents a
 * polynomial over a ring E.
 */
public class Polynomials<E> extends Ring<Polynomial<E>> {

	protected Ring<E> baseRing;
	protected char variable = 't';

	/** Constructs the polynomial ring, given the base ring. */
	public Polynomials(Ring<E> baseRing) {
		this.baseRing = baseRing;
	}

	@Override
	public Polynomial<E> getAddIdentity() {
		List<E> addIdentity = new ArrayList<E>();
		addIdentity.add(baseRing.getAddIdentity());
		return new Polynomial<E>(addIdentity, baseRing);
	}

	@Override
	public Polynomial<E> getProductIdentity() {
		List<E> addIdentity = new ArrayList<E>();
		addIdentity.add(baseRing.getProductIdentity());
		return new Polynomial<E>(addIdentity, baseRing);
	}

	@Override
	public Polynomial<E> getAddInverse(Polynomial<E> a) {
		List<E> addInverse = new ArrayList<E>();
		for (int i = 0; i < a.size(); i++) {
			addInverse.add(baseRing.getAddInverse(a.get(i)));
		}
		return new Polynomial<E>(addInverse, baseRing);
	}

	@Override
	public Polynomial<E> add(Polynomial<E> a, Polynomial<E> b) {
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
		return new Polynomial<E>(sum, baseRing);
	}

	@Override
	public Polynomial<E> multiply(Polynomial<E> a, Polynomial<E> b) {
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
		return new Polynomial<E>(product, baseRing);
	}

	@Override
	public Polynomial<E> intMultiply(Polynomial<E> a, BigInteger k) {
		List<E> result = new ArrayList<E>();
		for (int i = 0; i < a.size(); i++) {
			result.add(baseRing.intMultiply(a.get(i), k));
		}
		return new Polynomial<E>(result, baseRing);
	}

	@Override
	public boolean divides(Polynomial<E> a, Polynomial<E> b) {
		Polynomial<E> r = pseudoDivision(a, b).getThird();
		Polynomial<E> s = getAddIdentity();
		return r.equals(s);
	}

	@Override
	public Polynomial<E> divFactor(Polynomial<E> a, Polynomial<E> b) {
		return pseudoDivision(a, b).getSecond();
	}

	/**
	 * It construct a polynomial from a valid string, that is, something adjusting
	 * the following regular expression:
	 * 
	 * ((T(\^i)\*a|T(\^i)?|a(\*)?T(\^i)?|a)\+)*(T(\^i)\*a|T(\^i)?|a(\*)?T(\^i)?|a)
	 * 
	 * Being "a" an String representation of any element of the class parameter E,
	 * "i" a positive integer (maybe zero), and "T" the variable.
	 */
	@Override
	public Polynomial<E> parseElement(String latexString) {

		/* Cleaning white characters... */
		latexString = latexString.replaceAll("( |\\t|\\n)", "");

		/* Initializing the list of coefficients... */
		List<E> coefficients = new ArrayList<E>();

		/*
		 * Splitting the string by the regular expression "+" we should have something
		 * like ( T(\^i)\*a | T(\^i)? | a(\*)?T(\^i)? |a ).
		 */
		String[] monomials = latexString.split("\\+");

		/* Working on each token separately... */
		for (int i = 0; i < monomials.length; i++) {

			int termDegree;
			E coefficient;

			/*
			 * Splitting by the variable we will get a single token like ( \^i\*a | \*a |
			 * \^i | a | a\*), an empty string, or two tokens like ([a,\^i] | [a\*,\^i]).
			 * 
			 * We also need here to know, before splitting the string, if it actually
			 * contains the variable, in order to compute the term degrees correctly in the
			 * "a" cases.
			 */
			boolean containsVariable = monomials[i].contains("" + variable);
			String[] monomial = monomials[i].split("" + variable);

			/* Cleaning the possible empty strings generated by the splitting... */
			monomial = cleanEmptyStrings(monomial);

			/* Considering every case... */
			if (monomial.length == 0) {
				/* In this case, the monomial is "T", so we know exactly what to insert */
				termDegree = 1;
				coefficient = baseRing.getProductIdentity();
			} else if (monomial.length == 1) {

				if (monomial[0].startsWith("^")) { /* Cases beginning with \^ */

					/*
					 * We take out the exponent symbol, so the possible strings are now (i\*a) and
					 * (i).
					 */
					String exponentCleaned = monomial[0].replaceFirst("\\^", "");

					/* Splitting by \* we may get two tokens, [i,a] or one token (i) */
					String[] productSplit = exponentCleaned.split("\\*");
					if (productSplit.length == 1) {
						termDegree = Integer.parseInt(productSplit[0]);
						coefficient = baseRing.getProductIdentity();
					} else {
						termDegree = Integer.parseInt(productSplit[0]);
						coefficient = baseRing.parseElement(productSplit[1]);
					}
				} else { /* "a" cases */
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
		return new Polynomial<E>(coefficients, baseRing);
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

	/**
	 * Auxiliary private method for the parseElement() method, it just sums up some
	 * coefficient in the right position of the coefficients list.
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

	/**
	 * Auxiliary private method for the parseElement() method, it just removes the
	 * empty strings of an String array.
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

	public Polynomial<E> add(Polynomial<E> a, E e) {
		List<E> sum = new ArrayList<E>();
		sum.add(baseRing.add(a.get(0), e));
		for (int i = 1; i < a.size(); i++) {
			sum.add(a.get(i));
		}
		return new Polynomial<E>(sum, baseRing);
	}

	public Polynomial<E> multiply(Polynomial<E> a, E e) {
		List<E> product = new ArrayList<E>();
		for (int i = 0; i < a.size(); i++) {
			product.add(baseRing.multiply(a.get(i), e));
		}
		return new Polynomial<E>(product, baseRing);
	}

	/**
	 * Returns a triple representing the pseudo division of two polynomials over an
	 * arbitrary ring. See the documentation for details.
	 */
	public Triple<BigInteger, Polynomial<E>> pseudoDivision(Polynomial<E> a, Polynomial<E> b) {
		BigInteger k = BigInteger.ZERO;
		Polynomial<E> q = null;
		Polynomial<E> r = null;
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
				Polynomial<E> h = add(multiply(a, b.leading()),
						multiply(multiply(parseElement(variable + "^" + (a.degree() - b.degree())),
								baseRing.getAddInverse(a.leading())), b));
				if (h.degree() < b.degree()) {
					k = BigInteger.ONE;
					q = multiply(parseElement(variable + "^" + (a.degree() - b.degree())), a.leading());
					r = h;
				} else {
					Triple<BigInteger, Polynomial<E>> aux = pseudoDivision(h, b);
					k = aux.getFirst().add(BigInteger.ONE);
					q = add(aux.getSecond(), multiply(parseElement(variable + "^" + (a.degree() - b.degree())),
							baseRing.multiply((baseRing.power(b.leading(), aux.getFirst())), a.leading())));
					r = aux.getThird();
				}
			}
		}
		return new Triple<BigInteger, Polynomial<E>>(k, q, r);
	}

	/**
	 * Returns remainder(g(h),f). f has to be a monic polynomial such that deg(g),
	 * deg(h)<deg(f).
	 */
	public Polynomial<E> modularComposition(Polynomial<E> f, Polynomial<E> g, Polynomial<E> h) {
		/* Fast modular composition algorithm. */

		int n = f.degree();
		int m = (int) Math.ceil(Math.sqrt(n));

		/* Building the auxiliary matrixes... */
		List<List<E>> hPowers = computeHPowers(h, f, m);
		List<List<E>> gPolynomials = buildGPolynomials(g, m);

		/* Computing the matrix product... */
		Matrixes<E> M = new Matrixes<E>(baseRing);
		Matrix<E> H = M.fill(hPowers.subList(0, hPowers.size() - 1), m, n);
		Matrix<E> G = M.fill(gPolynomials, m, m);
		Matrix<E> A = M.multiply(G, H);

		/* Building the huge polynomial... */
		Polynomial<Polynomial<E>> b = buildHugePoly(A);

		/* Evaluating the huge polynomial modulo f... */
		Polynomial<E> p = new Polynomial<E>(hPowers.get(hPowers.size() - 1), baseRing);
		return evaluate(p, b, f);
	}

	/**
	 * Auxiliary private method for the modularComposition() method. Computes a
	 * pseudo matrix whose rows are the coefficients of the powers of h modulo f.
	 */
	private List<List<E>> computeHPowers(Polynomial<E> h, Polynomial<E> f, int m) {
		List<List<E>> coefficients = new ArrayList<List<E>>();
		coefficients.add(getProductIdentity().getCoefficients());
		coefficients.add(pseudoDivision(h, f).getThird().getCoefficients());
		Polynomial<E> hPower = h;
		for (int i = 2; i <= m; i++) {
			hPower = pseudoDivision(multiply(hPower, h), f).getThird();
			coefficients.add(hPower.getCoefficients());
		}
		return coefficients;
	}

	/**
	 * Auxiliary private method for the modularComposition() method. Computes a
	 * pseudo matrix whose rows are the coefficients of g packaged in groups of m
	 * coefficients.
	 */
	private List<List<E>> buildGPolynomials(Polynomial<E> g, int m) {
		List<List<E>> coefficients = new ArrayList<List<E>>();
		List<E> gCoefficients = g.getCoefficients();
		int l = gCoefficients.size();
		for (int i = 0; i < m; i++) {
			if (m * i >= l || m * (1 + i) > l) {
				break;
			}
			coefficients.add(gCoefficients.subList(m * i, m * (1 + i)));
		}
		return coefficients;
	}

	private Polynomial<Polynomial<E>> buildHugePoly(Matrix<E> A) {
		List<Polynomial<E>> coefficients = new ArrayList<Polynomial<E>>();
		for (int i = 0; i < A.getRows(); i++) {
			coefficients.add(new Polynomial<E>(A.getCoefficients().get(i), baseRing));
		}
		return new Polynomial<Polynomial<E>>(coefficients, (Ring<Polynomial<E>>) this);
	}

	/**
	 * Evaluates the polynomial g modulo f in the specified value. See the
	 * documentation for details.
	 */
	private Polynomial<E> evaluate(Polynomial<E> a, Polynomial<Polynomial<E>> g, Polynomial<E> f) {
		/* Horner's algorithm */
		Polynomial<E> evaluation = getAddIdentity();
		for (int i = g.degree(); i >= 0; i--) {
			evaluation = pseudoDivision(add(multiply(evaluation, a), g.get(i)), f).getThird();
		}
		return evaluation;
	}

	@Deprecated
	public Polynomial<E> bruteForceModularComposition(Polynomial<E> f, Polynomial<E> g, Polynomial<E> h) {
		/* This is just the brute force algorithm. */
		Polynomial<E> composition = getAddIdentity();
		int n = g.degree();
		for (int i = 0; i <= n; i++) {
			composition = add(composition, multiply(power(h, new BigInteger(Integer.toString(i))), g.get(i)));
		}
		return pseudoDivision(composition, f).getThird();
	}

}