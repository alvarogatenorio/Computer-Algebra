package utils;

public class Rational {
	private int numerator;
	private int denominator;

	/* EXCEPCIÓN */
	public Rational(int numerator, int denominator) {
		Integers Z = new Integers();
		int scale = Z.gcd(numerator, denominator);
		if (scale != 0) {
			this.numerator = numerator / scale;
			this.denominator = denominator / scale;
		}
	}

	public int getNumerator() {
		return this.numerator;
	}

	public int getDenominator() {
		return this.denominator;
	}

	public String toString() {
		return "" + numerator + "/" + denominator;
	}
}
