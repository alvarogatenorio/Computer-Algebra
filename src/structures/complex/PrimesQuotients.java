package structures.complex;

import structures.basic.Field;

public class PrimesQuotients extends Field<Polynomial<Integer>> {
	private Polynomial<Integer> irrPolMod;
	private PrimeModuleIntegers baseField;
	private FieldPolynomials<Polynomial<Integer>, Integer> polyRing;

	public PrimesQuotients(int primeIntMod, Polynomial<Integer> irrPolMod) {
		this.irrPolMod = irrPolMod;
		this.baseField = new PrimeModuleIntegers(primeIntMod);
		polyRing = new FieldPolynomials<Polynomial<Integer>, Integer>(baseField);
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

	@Override
	public Polynomial<Integer> getProductInverse(Polynomial<Integer> a) {
		/*
		 * The greater common divisor will always be a unit, we just normalize it to be
		 * the product identity, so the product inverse matches with the first
		 * coefficient of the B�zout's identity
		 */
		Integer factor = baseField.getProductInverse(polyRing.gcd(a, irrPolMod).leading());
		return polyRing.reminder(polyRing.multiply((polyRing.bezout(a, irrPolMod).getFirst()), factor), irrPolMod);
	}

	@Override
	public Polynomial<Integer> multiply(Polynomial<Integer> a, int k) {
		return polyRing.reminder(polyRing.multiply(a, k), irrPolMod);
	}

	@Override
	public Polynomial<Integer> power(Polynomial<Integer> a, int k) {
		return polyRing.reminder(polyRing.power(a, k), irrPolMod);
	}

	@Override
	public Polynomial<Integer> divFactor(Polynomial<Integer> a, Polynomial<Integer> b) {
		return polyRing.reminder(polyRing.divFactor(a, b), irrPolMod);
	}

}
