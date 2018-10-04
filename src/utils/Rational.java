package utils;

public class Rational {
	private int numerator;
	private int denominator;

	/* Sistema de excepciones con divisiones por cero y esas cosas */
	/* Decidir si da la simplificación directa o no */
	public Rational(int numerator, int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public int getNumerator() {
		return this.numerator;
	}

	public int getDenominator() {
		return this.denominator;
	}
}
