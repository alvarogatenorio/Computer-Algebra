package structures.complex;

import java.math.BigInteger;

import structures.basic.Ring;
import utils.Polynomial;

/** low priority */
public class PolynomialQuotients<T> extends Ring<Polynomial<T>> {

	private Polynomials<Polynomial<T>, T> polyRing;
	private Polynomial<T> module;

	public PolynomialQuotients(Polynomials<Polynomial<T>, T> polyRing, Polynomial<T> module) {
		this.polyRing = polyRing;
		this.module = module;
	}

	@Override
	public Polynomial<T> getAddIdentity() {
		return polyRing.getAddIdentity();
	}

	@Override
	public Polynomial<T> getProductIdentity() {
		return polyRing.getProductIdentity();
	}

	@Override
	public Polynomial<T> getAddInverse(Polynomial<T> a) {
		return polyRing.pseudoDivision(polyRing.getAddInverse(a), module).getThird();
	}

	@Override
	public Polynomial<T> add(Polynomial<T> a, Polynomial<T> b) {
		return polyRing.pseudoDivision(polyRing.add(a, b), module).getThird();
	}

	@Override
	public Polynomial<T> multiply(Polynomial<T> a, Polynomial<T> b) {
		return polyRing.pseudoDivision(polyRing.multiply(a, b), module).getThird();
	}

	@Override
	public Polynomial<T> intMultiply(Polynomial<T> a, BigInteger k) {
		return polyRing.pseudoDivision(polyRing.intMultiply(a, k), module).getThird();
	}

	@Override
	public boolean divides(Polynomial<T> a, Polynomial<T> b) {
		return polyRing.divides(a, b);
	}

	@Override
	public Polynomial<T> divFactor(Polynomial<T> a, Polynomial<T> b) {
		return polyRing.pseudoDivision(polyRing.divFactor(a, b), module).getThird();
	}

	@Override
	public Polynomial<T> parseElement(String s) {
		return polyRing.pseudoDivision(polyRing.parseElement(s), module).getThird();
	}

}
