package structures.complex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import structures.basic.EuclideanDomain;
import structures.basic.Field;
import structures.basic.Ring;
import utils.Matrix;
import utils.Matrixes;
import utils.Pair;
import utils.Polynomial;

/** Represents the ring of polynomials over a field */
public class FieldPolynomials<E> extends EuclideanDomain<Polynomial<E>> {

	protected Polynomials<E> polyRing;
	protected Field<E> baseRing;

	public FieldPolynomials(Field<E> baseRing) {
		polyRing = new Polynomials<E>(baseRing);
		this.baseRing = baseRing;
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

	public Pair<Polynomial<E>> division(Polynomial<E> a, Polynomial<E> b) {
		Polynomial<E> q = null;
		Polynomial<E> r = null;
		if (a.degree() < b.degree()) {
			q = getAddIdentity();
			r = a;
		} else {
			if (a.degree() == 0) {
				q = multiply(a, baseRing.getProductInverse(b.independent()));
				r = getAddIdentity();
			} else {
				Polynomial<E> h = add(a,
						multiply(
								multiply(parseElement("t^" + (a.degree() - b.degree())), baseRing.multiply(
										baseRing.getAddInverse(a.leading()), baseRing.getProductInverse(b.leading()))),
								b));
				if (h.degree() < b.degree()) {
					q = multiply(parseElement("t^" + (a.degree() - b.degree())),
							baseRing.multiply(a.leading(), baseRing.getProductInverse(b.leading())));
					r = h;
				} else {
					Pair<Polynomial<E>> aux = division(h, b);
					q = add(multiply(parseElement("t^" + (a.degree() - b.degree())),
							baseRing.multiply(a.leading(), baseRing.getProductInverse(b.leading()))), aux.getFirst());
					r = aux.getSecond();
				}
			}
		}
		return new Pair<Polynomial<E>>(q, r);
	}

	private Polynomial<E> multiply(Polynomial<E> a, E e) {
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
			evaluation = remainder(add(multiply(evaluation, a), g.get(i)), f);
		}
		return evaluation;
	}

}