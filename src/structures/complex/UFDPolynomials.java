package structures.complex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import structures.basic.UniqueFactorizationDomain;
import utils.Polynomial;

/**
 * Represents the ring of polynomials over an UFD. By Gauss' lemma, this will be
 * as well an UFD.
 */
public class UFDPolynomials<E> extends UniqueFactorizationDomain<Polynomial<E>> {

	private Polynomials<E> polyRing;
	private UniqueFactorizationDomain<E> baseRing;

	public UFDPolynomials(UniqueFactorizationDomain<E> baseRing) {
		this.baseRing = baseRing;
		polyRing = new Polynomials<E>(baseRing);
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
	public Polynomial<E> parseElement(String latexString) {
		return polyRing.parseElement(latexString);
	}

	@Override
	public boolean divides(Polynomial<E> a, Polynomial<E> b) {
		return polyRing.divides(a, b);
	}

	/**
	 * Given two primitive polynomials, computes the greatest common divisor. See
	 * the documentation for details.
	 */
	@Override
	public Polynomial<E> gcd(Polynomial<E> a, Polynomial<E> b) {
		/* Primitive euclid's algorithm */
		while (!b.equals(getAddIdentity())) {
			Polynomial<E> r = polyRing.pseudoDivision(a, b).getThird();
			E c = content(a);
			r = primitivePart(r, c);
			a = b;
			b = r;
		}
		return a;
	}

	/** Computes the primitive part of a polynomial. */
	public Polynomial<E> primitivePart(Polynomial<E> r, E c) {
		List<E> coefficients = new ArrayList<E>();
		for (int i = 0; i < r.size(); i++) {
			E aux = r.get(i);
			if (!aux.equals(baseRing.getAddIdentity())) {
				coefficients.add(baseRing.divFactor(c, aux));
			} else {
				coefficients.add(aux);
			}
		}
		return r = new Polynomial<E>(coefficients, baseRing);
	}

	/**
	 * Computes the polynomial content, i.e. the greatest common divisor of its
	 * coefficients.
	 */
	public E content(Polynomial<E> a) {
		E c = a.leading();
		for (int i = a.size() - 2; i >= 0; i--) {
			c = baseRing.gcd(c, a.get(i));
		}
		return c;
	}

	@Override
	public Polynomial<E> intMultiply(Polynomial<E> a, BigInteger k) {
		return polyRing.intMultiply(a, k);
	}

	@Override
	public Polynomial<E> divFactor(Polynomial<E> a, Polynomial<E> b) {
		return polyRing.divFactor(a, b);
	}

	@Override
	public Polynomial<E> getProductIdentity() {
		return polyRing.getProductIdentity();
	}
}
