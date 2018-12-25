package structures.concrete.rationals;

import java.math.BigInteger;

import structures.concrete.euclideanDomains.Integers;

/** Represents a rational number. */
public class Rational {

	/** Represents the numerator. */
	private BigInteger numerator;

	/** Represents the denominator. */
	private BigInteger denominator;

	/**
	 * Builds a rational number from a numerator and a denominator, it simplify them
	 * to the canonical form.
	 */
	public Rational(BigInteger numerator, BigInteger denominator) {
		Integers Z = new Integers();
		BigInteger scale = Z.gcd(numerator, denominator);
		if (!scale.equals(BigInteger.ZERO)) {
			this.numerator = numerator.divide(scale);
			this.denominator = denominator.divide(scale);
		}
	}

	/** Builds a rational number from an integer. */
	public Rational(BigInteger numerator) {
		this.numerator = numerator;
		this.denominator = BigInteger.ONE;
	}

	/** Returns the numerator. */
	public BigInteger getNumerator() {
		return this.numerator;
	}

	/** Returns the denominator. */
	public BigInteger getDenominator() {
		return this.denominator;
	}

	/** Prints the rational number. */
	public String toString() {
		return denominator.equals(BigInteger.ONE) ? "" + numerator : "" + numerator + "/" + denominator;
	}

	/**
	 * Two rational numbers are equal if they have the same canonical form. And they
	 * are always in the canonical form.
	 */
	public boolean equals(Object o) {
		Rational r = (Rational) (o);
		return (r.numerator.equals(numerator)) && (r.denominator.equals(denominator));
	}
}
