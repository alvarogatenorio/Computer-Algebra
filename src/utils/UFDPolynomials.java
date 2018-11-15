package utils;

import structures.UniqueFactorizationDomain;

public class UFDPolynomials<T extends Polynomial<E>, E> extends UniqueFactorizationDomain<T> {

	private Polynomials<T, E> polyRing;

	public UFDPolynomials(UniqueFactorizationDomain<E> baseRing) {
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
		return false;
	}

	/* Primitive euclid's algorithm */
	@Override
	public T gcd(T a, T b) {
		return null;
	}
}