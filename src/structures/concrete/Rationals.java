package structures.concrete;

import java.math.BigInteger;

import structures.basic.Field;
import utils.Rational;

/**
 * Represents the field of rational numbers (Q). Notice that every operation
 * returns a rational in canonical form.
 */
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
		BigInteger commonDenominator = a.getDenominator().multiply(b.getDenominator())
				.divide(Z.gcd(a.getDenominator(), b.getDenominator()));
		BigInteger aFactor = commonDenominator.divide(a.getDenominator());
		BigInteger bFactor = commonDenominator.divide(b.getDenominator());
		return new Rational(a.getNumerator().multiply(aFactor).add(b.getNumerator().multiply(bFactor)),
				commonDenominator);
	}

	@Override
	public Rational multiply(Rational a, Rational b) {
		return new Rational(a.getNumerator().multiply(b.getNumerator()),
				a.getDenominator().multiply(b.getDenominator()));
	}

	@Override
	public Rational parseElement(String s) {
		if (s.contains("/")) {
			String[] splittedString = s.split("/");
			return new Rational(new BigInteger(splittedString[0]), new BigInteger(splittedString[1]));
		} else {
			return new Rational(new BigInteger(s));
		}
	}

	@Override
	public Rational getProductInverse(Rational a) {
		return new Rational(a.getDenominator(), a.getNumerator());
	}

	@Override
	public Rational intMultiply(Rational a, BigInteger k) {
		return multiply(a, new Rational(k));
	}
}
