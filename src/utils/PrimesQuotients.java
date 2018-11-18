package utils;

import structures.Field;

public class PrimesQuotients extends Field<Polynomial<Integer>> {

	private int primeIntMod;
	private Polynomial<Integer> irrPolMod;

	private FieldPolynomials<Polynomial<Integer>, Integer> polyRing;

	public PrimesQuotients(int primeIntMod, Polynomial<Integer> irrPolMod) {
		this.primeIntMod = primeIntMod;
		this.irrPolMod = irrPolMod;
		polyRing = new FieldPolynomials<Polynomial<Integer>, Integer>(new PrimeModuleIntegers(primeIntMod));
	}

	@Override
	public Polynomial<Integer> getProductIdentity() {
		return polyRing.getProductIdentity();
	}

	@Override
	public Polynomial<Integer> getAddIdentity() {
		return polyRing.getAddIdentity();
	}

	@Override
	public Polynomial<Integer> getAddInverse(Polynomial<Integer> a) {
		return polyRing.reminder(polyRing.getAddInverse(a), irrPolMod);
	}

	@Override
	public Polynomial<Integer> add(Polynomial<Integer> a, Polynomial<Integer> b) {
		return polyRing.reminder(polyRing.add(a, b), irrPolMod);
	}

	@Override
	public Polynomial<Integer> multiply(Polynomial<Integer> a, Polynomial<Integer> b) {
		return polyRing.reminder(polyRing.multiply(a, b), irrPolMod);
	}

	@Override
	public Polynomial<Integer> parseElement(String s) {
		return polyRing.reminder(polyRing.parseElement(s), irrPolMod);
	}

	/* Waiting for algorithm */
	@Override
	public Polynomial<Integer> getProductInverse(Polynomial<Integer> a) {
		return polyRing.reminder(polyRing.bezout(a, irrPolMod).getFirst(), irrPolMod);
	}

	@Override
	public Polynomial<Integer> multiply(Polynomial<Integer> a, int k) {
		return polyRing.reminder(polyRing.multiply(a, k), irrPolMod);
	}

	@Override
	public Polynomial<Integer> power(Polynomial<Integer> a, int k) {
		return polyRing.reminder(polyRing.power(a, k), irrPolMod);
	}

}
