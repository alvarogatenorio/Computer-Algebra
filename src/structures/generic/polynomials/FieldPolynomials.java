package structures.generic.polynomials;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cmpalg.generic.basic.EuclideanDomain;
import cmpalg.generic.basic.Field;
import cmpalg.generic.basic.Ring;
import structures.generic.matrixes.Matrix;
import structures.generic.matrixes.Matrixes;
import utils.Pair;

/** Represents the ring of polynomials over a field */
public class FieldPolynomials<E> extends EuclideanDomain<Polynomial<E>> {

	protected Polynomials<E> polyRing;
	protected Field<E> baseField;

	public FieldPolynomials(Field<E> baseRing) {
		polyRing = new Polynomials<E>(baseRing);
		this.baseField = baseRing;
	}

	@Override
	public Polynomial<E> getProductIdentity() {
		return polyRing.getProductIdentity();
	}

	@Override
	public Polynomial<E> getAddIdentity() {
		return polyRing.getAddIdentity();
	}

	@Override
	public Polynomial<E> getAddInverse(Polynomial<E> a) {
		return polyRing.getAddInverse(a);
	}

	@Override
	public Polynomial<E> add(Polynomial<E> a, Polynomial<E> b) {
		return polyRing.add(a, b);
	}

	@Override
	public Polynomial<E> multiply(Polynomial<E> a, Polynomial<E> b) {
		return polyRing.multiply(a, b);
	}

	@Override
	public Polynomial<E> parseElement(String s) {
		return polyRing.parseElement(s);
	}

	@Override
	public boolean divides(Polynomial<E> a, Polynomial<E> b) {
		return division(a, b).getSecond().equals(getAddIdentity());
	}

	@Override
	public Polynomial<E> intMultiply(Polynomial<E> a, BigInteger k) {
		return polyRing.intMultiply(a, k);
	}

	public Pair<Polynomial<E>, Polynomial<E>> division(Polynomial<E> a, Polynomial<E> b) {
		Polynomial<E> q = null;
		Polynomial<E> r = null;
		if (a.degree() < b.degree()) {
			q = getAddIdentity();
			r = a;
		} else {
			if (a.degree() == 0) {
				q = multiply(a, baseField.getProductInverse(b.independent()));
				r = getAddIdentity();
			} else {
				Polynomial<E> h = add(a, multiply(multiply(parseElement("t^" + (a.degree() - b.degree())), baseField
						.multiply(baseField.getAddInverse(a.leading()), baseField.getProductInverse(b.leading()))), b));
				if (h.degree() < b.degree()) {
					q = multiply(parseElement("t^" + (a.degree() - b.degree())),
							baseField.multiply(a.leading(), baseField.getProductInverse(b.leading())));
					r = h;
				} else {
					Pair<Polynomial<E>, Polynomial<E>> aux = division(h, b);
					q = add(multiply(parseElement("t^" + (a.degree() - b.degree())),
							baseField.multiply(a.leading(), baseField.getProductInverse(b.leading()))), aux.getFirst());
					r = aux.getSecond();
				}
			}
		}
		return new Pair<Polynomial<E>, Polynomial<E>>(q, r);
	}

	public Polynomial<E> multiply(Polynomial<E> a, E e) {
		return polyRing.multiply(a, e);
	}

	@Override
	public Polynomial<E> quotient(Polynomial<E> a, Polynomial<E> b) {
		return division(a, b).getFirst();
	}

	@Override
	public Polynomial<E> remainder(Polynomial<E> a, Polynomial<E> b) {
		return division(a, b).getSecond();
	}

	@Override
	public Polynomial<E> divFactor(Polynomial<E> a, Polynomial<E> b) {
		return quotient(a, b);
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
		Matrixes<E> M = new Matrixes<E>(baseField);
		Matrix<E> H = M.fill(hPowers.subList(0, hPowers.size() - 1), m, n);
		Matrix<E> G = M.fill(gPolynomials, m, m);
		Matrix<E> A = M.multiply(G, H);

		/* Building the huge polynomial... */
		Polynomial<Polynomial<E>> b = buildHugePoly(A);

		/* Evaluating the huge polynomial modulo f... */
		Polynomial<E> p = new Polynomial<E>(hPowers.get(hPowers.size() - 1), baseField);
		return evaluate(p, b, f);
	}

	/**
	 * Auxiliary private method for the modularComposition() method. Computes a
	 * pseudo matrix whose rows are the coefficients of the powers of h modulo f.
	 */
	private List<List<E>> computeHPowers(Polynomial<E> h, Polynomial<E> f, int m) {
		List<List<E>> coefficients = new ArrayList<List<E>>();
		coefficients.add(getProductIdentity().getCoefficients());
		coefficients.add(remainder(h, f).getCoefficients());
		Polynomial<E> hPower = h;
		for (int i = 2; i <= m; i++) {
			hPower = remainder(multiply(hPower, h), f);
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
		for (int i = 0; i < l % m; i++) {
			gCoefficients.add(baseField.getAddIdentity());
		}
		l = gCoefficients.size();
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
			/* Cleaning extra identities. */
			List<E> c = A.getCoefficients().get(i);
			int k = c.size() - 1;
			for (; k >= 0; k--) {
				if (!c.get(k).equals(baseField.getAddIdentity())) {
					c = c.subList(0, k + 1);
					break;
				}
			}
			if (k != -1) {
				coefficients.add(new Polynomial<E>(c, baseField));
			} else if (i != A.getRows() - 1) {
				coefficients.add(new Polynomial<E>(c.subList(0, 1), baseField));
			}
		}
		return new Polynomial<Polynomial<E>>(coefficients, (Ring<Polynomial<E>>) this);
	}

	/**
	 * Evaluates the polynomial g modulo f in the specified value. See the
	 * documentation for details.
	 */
	public Polynomial<E> evaluate(Polynomial<E> a, Polynomial<Polynomial<E>> g, Polynomial<E> f) {
		/* Horner's algorithm */
		Polynomial<E> evaluation = getAddIdentity();
		for (int i = g.degree(); i >= 0; i--) {
			evaluation = remainder(add(multiply(evaluation, a), g.get(i)), f);
		}
		return evaluation;
	}

	public Polynomial<E> derivative(Polynomial<E> f) {
		return polyRing.derivative(f);
	}

	@Override
	public Polynomial<E> gcd(Polynomial<E> a, Polynomial<E> b) {
		/* Euclid's algorithm with normal form. */
		if (!a.leading().equals(baseField.getAddIdentity())) {
			a = multiply(a, baseField.getProductInverse(a.leading()));
		}
		if (!b.leading().equals(baseField.getAddIdentity())) {
			b = multiply(b, baseField.getProductInverse(b.leading()));
		}

		while (!b.equals(getAddIdentity())) {
			Polynomial<E> r = remainder(a, b);

			E factor = baseField.getProductInverse(r.leading());
			r = multiply(r, factor);

			a = b;
			b = r;
		}
		return a;
	}

	@Override
	public Pair<Polynomial<E>, Polynomial<E>> bezout(Polynomial<E> a, Polynomial<E> b) {
		/* Extended Euclid's algorithm with normal form. */
		if (!a.leading().equals(baseField.getAddIdentity())) {
			a = multiply(a, baseField.getProductInverse(a.leading()));
		}
		if (!b.leading().equals(baseField.getAddIdentity())) {
			b = multiply(b, baseField.getProductInverse(b.leading()));
		}

		Polynomial<E> alphaMinus1 = getAddIdentity();
		Polynomial<E> alphaMinus2 = getProductIdentity();
		Polynomial<E> betaMinus1 = getProductIdentity();
		Polynomial<E> betaMinus2 = getAddIdentity();
		while (!b.equals(getAddIdentity())) {
			/* Computes division of a and b */
			Polynomial<E> q = quotient(a, b);
			Polynomial<E> r = remainder(a, b);

			E factor = baseField.getProductInverse(r.leading());
			r = multiply(r, factor);

			/*
			 * Just follow the formula for the new pseudo-bezout identity coefficients
			 */
			Polynomial<E> alphaAux = multiply(add(alphaMinus2, getAddInverse(multiply(q, alphaMinus1))), factor);
			Polynomial<E> betaAux = multiply(add(betaMinus2, getAddInverse(multiply(q, betaMinus1))), factor);

			/* Maintaining alphaMinusX and betaMinusX coherent */
			alphaMinus2 = alphaMinus1;
			betaMinus2 = betaMinus1;

			alphaMinus1 = alphaAux;
			betaMinus1 = betaAux;

			/* By euclid's lemma gcd(a,b) = gcd(b,r) */
			a = b;
			b = r;
		}
		return new Pair<Polynomial<E>, Polynomial<E>>(alphaMinus2, betaMinus2);
	}

}
