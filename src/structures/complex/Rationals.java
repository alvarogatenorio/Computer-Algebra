package structures.complex;

import structures.basic.Field;

public class Rationals extends Field<Rational> {

	@Override
	public Rational getProductIdentity() {
		Integers Z = new Integers();
		return new Rational(Z.getProductIdentity(), Z.getProductIdentity());
	}

	@Override
	public Rational getAddIdentity() {
		Integers Z = new Integers();
		return new Rational(Z.getAddIdentity(), Z.getProductIdentity());
	}

	@Override
	public Rational getAddInverse(Rational a) {
		Integers Z = new Integers();
		return new Rational(Z.getAddInverse(a.getNumerator()), a.getDenominator());
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
		if (s.contains("/")) {
			String[] splittedString = s.split("/");
			return new Rational(Integer.parseInt(splittedString[0]), Integer.parseInt(splittedString[1]));
		} else {
			return new Rational(Integer.parseInt(s));
		}
	}

	@Override
	public Rational getProductInverse(Rational a) {
		return new Rational(a.getDenominator(), a.getNumerator());
	}

	@Override
	public Rational multiply(Rational a, int k) {
		return multiply(a, new Rational(k));
	}

	@Override
	public Rational power(Rational a, int k) {
		Integers Z = new Integers();
		return new Rational(Z.power(a.getNumerator(), k), Z.power(a.getDenominator(), k));
	}

	@Override
	public Rational divFactor(Rational a, Rational b) {
		return quotient(a, b);
	}
}
