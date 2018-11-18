package utils;

import java.util.ArrayList;
import java.util.List;

import structures.UniqueFactorizationDomain;

/**
 * Remember by the Gauss' theorem, if the base ring is an UFD, then the
 * polynomial ring is also a UFD. Also take into account that every Euclidean
 * domain is an UFD.
 */
public class UFDPolynomials<T extends Polynomial<E>, E> extends UniqueFactorizationDomain<T> {

	private Polynomials<T, E> polyRing;
	private UniqueFactorizationDomain<E> baseRing;

	public UFDPolynomials(UniqueFactorizationDomain<E> baseRing) {
		this.baseRing = baseRing;
		polyRing = new Polynomials<T, E>(baseRing);
	}

	@Override
	public T getAddIdentity() {
		return polyRing.getAddIdentity();
	}

	@Override
	public T getAddInverse(T a) {
		return polyRing.getAddInverse(a);
	}

	@Override
	public T add(T a, T b) {
		return polyRing.add(a, b);
	}

	@Override
	public T multiply(T a, T b) {
		return polyRing.multiply(a, b);
	}

	@Override
	public T parseElement(String latexString) {
		return polyRing.parseElement(latexString);
	}

	@Override
	public boolean divides(T a, T b) {
		return polyRing.divides(a, b);
	}

	/* Primitive euclid's algorithm. Read about Gauss lemma */
	@Override
	public T gcd(T a, T b) {
		while (!b.equals(getAddIdentity())) {
			T r = polyRing.pseudoDivision(a, b).getThird();
			E c = content(a);
			r = primitivePart(r, c);
			a = b;
			b = r;
		}
		return a;
	}

	@SuppressWarnings("unchecked")
	private T primitivePart(T r, E c) {
		List<E> coefficients = new ArrayList<E>();
		for (int i = 0; i < r.size(); i++) {
			coefficients.add(baseRing.exactQuotient(r.get(i), c));
		}
		return r = (T) new Polynomial<E>(coefficients, baseRing);
	}

	public E content(T a) {
		E c = a.leading();
		for (int i = a.size() - 2; i >= 0; i--) {
			c = baseRing.gcd(c, a.get(i));
		}
		return c;
	}

	@Override
	public T multiply(T a, int k) {
		return polyRing.multiply(a, k);
	}

	@Override
	public T power(T a, int k) {
		return polyRing.power(a, k);
	}

	@Override
	public T exactQuotient(T a, T b) {
		return polyRing.exactQuotient(a, b);
	}
}
