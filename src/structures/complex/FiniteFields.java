package structures.complex;

import java.math.BigInteger;

import structures.basic.Field;
import structures.concrete.PrimeModuleIntegers;
import utils.Polynomial;

public class FiniteFields extends Field<Polynomial<BigInteger>> {
	private Polynomial<BigInteger> irrPolMod;
	private PrimeModuleIntegers baseField;
	private FieldPolynomials<Polynomial<BigInteger>, BigInteger> polyRing;

	public FiniteFields(BigInteger primeIntMod, Polynomial<BigInteger> irrPolMod) {
		this.irrPolMod = irrPolMod;
		this.baseField = new PrimeModuleIntegers(primeIntMod);
		polyRing = new FieldPolynomials<Polynomial<BigInteger>, BigInteger>(baseField);
	}

	@Override
	public Polynomial<BigInteger> getProductIdentity() {
		return polyRing.getProductIdentity();
	}

	@Override
	public Polynomial<BigInteger> getAddIdentity() {
		return polyRing.getAddIdentity();
	}

	@Override
	public Polynomial<BigInteger> getAddInverse(Polynomial<BigInteger> a) {
		return polyRing.remainder(polyRing.getAddInverse(a), irrPolMod);
	}

	@Override
	public Polynomial<BigInteger> add(Polynomial<BigInteger> a, Polynomial<BigInteger> b) {
		return polyRing.remainder(polyRing.add(a, b), irrPolMod);
	}

	@Override
	public Polynomial<BigInteger> multiply(Polynomial<BigInteger> a, Polynomial<BigInteger> b) {
		return polyRing.remainder(polyRing.multiply(a, b), irrPolMod);
	}

	@Override
	public Polynomial<BigInteger> parseElement(String s) {
		return polyRing.remainder(polyRing.parseElement(s), irrPolMod);
	}

	@Override
	public Polynomial<BigInteger> getProductInverse(Polynomial<BigInteger> a) {
		/*
		 * The greater common divisor will always be a unit, we just normalize it to be
		 * the product identity, so the product inverse matches with the first
		 * coefficient of the B�zout's identity
		 */
		BigInteger factor = baseField.getProductInverse(polyRing.gcd(a, irrPolMod).leading());
		return polyRing.remainder(polyRing.intMultiply((polyRing.bezout(a, irrPolMod).getFirst()), factor), irrPolMod);
	}

	@Override
	public Polynomial<BigInteger> intMultiply(Polynomial<BigInteger> a, BigInteger k) {
		return polyRing.remainder(polyRing.intMultiply(a, k), irrPolMod);
	}

	@Override
	public Polynomial<BigInteger> divFactor(Polynomial<BigInteger> a, Polynomial<BigInteger> b) {
		return polyRing.remainder(polyRing.divFactor(a, b), irrPolMod);
	}

}
