package structures.complex;

import java.math.BigInteger;

public class Rational {
	private BigInteger numerator;
	private BigInteger denominator;

	public Rational(BigInteger numerator, BigInteger denominator) {
		Integers Z = new Integers();
		BigInteger scale = Z.gcd(numerator, denominator);
		if (!scale.equals(BigInteger.ZERO)) {
			this.numerator = numerator.divide(scale);
			this.denominator = denominator.divide(scale);
		}
	}

	public Rational(BigInteger numerator) {
		this.numerator = numerator;
		this.denominator = BigInteger.ONE;
	}

	public BigInteger getNumerator() {
		return this.numerator;
	}

	public BigInteger getDenominator() {
		return this.denominator;
	}

	public String toString() {
		return "" + numerator + "/" + denominator;
	}

	public boolean equals(Object o) {
		Integers Z = new Integers();
		Rational r = (Rational) (o);
		Rationals Q = new Rationals();
		Rational result = Q.add(Q.getAddInverse(r), this);
		if (result.getNumerator().equals(Z.getAddIdentity())) {
			return true;
		}
		return false;
	}
}
