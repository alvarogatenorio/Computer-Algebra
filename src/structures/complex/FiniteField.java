package structures.complex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import structures.basic.Field;
import structures.concrete.Integers;
import structures.concrete.PrimeModuleIntegers;
import utils.FiniteFieldElement;
import utils.Polynomial;

public class FiniteField extends Field<FiniteFieldElement> {
	private Polynomial<BigInteger> irrPolMod;
	private PrimeModuleIntegers baseField;
	private FieldPolynomials<BigInteger> polyRing;
	private BigInteger primeIntMod;

	public FiniteField(BigInteger primeIntMod, Polynomial<BigInteger> irrPolMod) {
		this.irrPolMod = irrPolMod;
		this.primeIntMod = primeIntMod;
		this.baseField = new PrimeModuleIntegers(primeIntMod);
		polyRing = new FieldPolynomials<BigInteger>(baseField);
	}

	@Override
	public FiniteFieldElement getProductIdentity() {
		return new FiniteFieldElement(polyRing.getProductIdentity(), irrPolMod);
	}

	@Override
	public FiniteFieldElement getAddIdentity() {
		return new FiniteFieldElement(polyRing.getAddIdentity(), irrPolMod);
	}

	@Override
	public FiniteFieldElement getAddInverse(FiniteFieldElement a) {
		return new FiniteFieldElement(polyRing.remainder(polyRing.getAddInverse(a.getPolynomial()), irrPolMod),
				irrPolMod);
	}

	@Override
	public FiniteFieldElement add(FiniteFieldElement a, FiniteFieldElement b) {
		return new FiniteFieldElement(polyRing.remainder(polyRing.add(a.getPolynomial(), b.getPolynomial()), irrPolMod),
				irrPolMod);
	}

	@Override
	public FiniteFieldElement multiply(FiniteFieldElement a, FiniteFieldElement b) {
		return new FiniteFieldElement(
				polyRing.remainder(polyRing.multiply(a.getPolynomial(), b.getPolynomial()), irrPolMod), irrPolMod);
	}

	/**
	 * We will represent an element in a finite field as a n tuple, that is a string
	 * like "(a,...,a)". Being n the degree of the irreducible polynomial.
	 */
	@Override
	public FiniteFieldElement parseElement(String s) {
		s = s.replaceAll("[()]", "");
		String[] aux = s.split(",");
		List<BigInteger> coefficients = new ArrayList<BigInteger>();

		int i = aux.length - 1;
		while (i >= 0 && aux[i].equals("0")) {
			i--;
		}

		for (; i >= 0; i--) {
			coefficients.add(new BigInteger(aux[i]));
		}
		Polynomial<BigInteger> p = new Polynomial<BigInteger>(coefficients, baseField);
		return new FiniteFieldElement(polyRing.remainder(p, irrPolMod), irrPolMod);
	}

	@Override
	public FiniteFieldElement getProductInverse(FiniteFieldElement a) {
		/*
		 * The greater common divisor will always be a unit, we just normalize it to be
		 * the product identity, so the product inverse matches with the first
		 * coefficient of the Bezout's identity.
		 */
		BigInteger factor = baseField.getProductInverse(polyRing.gcd(a.getPolynomial(), irrPolMod).leading());
		return new FiniteFieldElement(polyRing.remainder(
				polyRing.intMultiply((polyRing.bezout(a.getPolynomial(), irrPolMod).getFirst()), factor), irrPolMod),
				irrPolMod);
	}

	@Override
	public FiniteFieldElement intMultiply(FiniteFieldElement a, BigInteger k) {
		return new FiniteFieldElement(polyRing.remainder(polyRing.intMultiply(a.getPolynomial(), k), irrPolMod),
				irrPolMod);
	}

	@Override
	public FiniteFieldElement divFactor(FiniteFieldElement a, FiniteFieldElement b) {
		return new FiniteFieldElement(
				polyRing.remainder(polyRing.divFactor(a.getPolynomial(), b.getPolynomial()), irrPolMod), irrPolMod);
	}

	/** Returns the order of the finite field */
	public BigInteger getOrder() {
		Integers Z = new Integers();
		return Z.power(primeIntMod, new BigInteger(Integer.toString(irrPolMod.degree())));
	}
}
