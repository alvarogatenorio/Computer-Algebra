package utils;

import structures.Field;

public class Rationals extends Field<Rational> {

	@Override
	public Rational getProductIdentity() {
		return new Rational(1, 1);
	}

	@Override
	public Rational getAddIdentity() {
		return new Rational(0, 1);
	}

	@Override
	public Rational getAddInverse(Rational a) {
		return new Rational(-a.getNumerator(), a.getDenominator());
	}

	@Override
	public Rational add(Rational a, Rational b) {
		Integers Z = new Integers();
		int commonDenominator = (a.getDenominator() * b.getDenominator())
				/ Z.gcd(a.getDenominator(), b.getDenominator());
		int aFactor = commonDenominator / a.getDenominator();
		int bFactor = commonDenominator / b.getDenominator();
		return new Rational(a.getNumerator() * aFactor + b.getNumerator() * bFactor, commonDenominator);
	}

	@Override
	public Rational multiply(Rational a, Rational b) {
		return new Rational(a.getNumerator() * b.getNumerator(), a.getDenominator() * b.getDenominator());
	}

	@Override
	public Rational parseElement(String s) {
		String[] splittedString = s.split("/");
		return new Rational(Integer.parseInt(splittedString[0]), Integer.parseInt(splittedString[1]));
	}

	@Override
	public Rational getProductInverse(Rational a) {
		return new Rational(a.getDenominator(), a.getNumerator());
	}

	@Override
	public Rational multiply(Rational a, int k) {
		return null;
	}

	@Override
	public Rational power(Rational a, int k) {
		// TODO Auto-generated method stub
		return null;
	}

}
