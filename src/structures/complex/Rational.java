package structures.complex;

public class Rational {
	private Integer numerator;
	private Integer denominator;

	/* EXCEPCIÓN */
	public Rational(Integer numerator, Integer denominator) {
		Integers Z = new Integers();
		int scale = Z.gcd(numerator, denominator);
		if (scale != 0) {
			this.numerator = numerator / scale;
			this.denominator = denominator / scale;
		}
	}

	public Rational(Integer numerator) {
		this.numerator = numerator;
		this.denominator = 1;
	}

	public Integer getNumerator() {
		return this.numerator;
	}

	public Integer getDenominator() {
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
