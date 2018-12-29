package structures.concrete.euclideanDomains;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cmpalg.generic.basic.EuclideanDomain;

/** Represents the euclidean domain of integer numbers (Z). */
public class Integers extends EuclideanDomain<BigInteger> {
	@Override
	public BigInteger getAddIdentity() {
		return BigInteger.ZERO;
	}

	@Override
	public BigInteger getAddInverse(BigInteger a) {
		return a.negate();
	}

	@Override
	public BigInteger quotient(BigInteger a, BigInteger b) {
		return a.divide(b);
	}

	@Override
	public BigInteger remainder(BigInteger a, BigInteger b) {
		return a.mod(b);
	}

	@Override
	public BigInteger add(BigInteger a, BigInteger b) {
		return a.add(b);
	}

	@Override
	public BigInteger multiply(BigInteger a, BigInteger b) {
		return a.multiply(b);
	}

	@Override
	public BigInteger getProductIdentity() {
		return BigInteger.ONE;
	}

	@Override
	public BigInteger parseElement(String s) {
		return new BigInteger(s);
	}

	@Override
	public boolean divides(BigInteger a, BigInteger b) {
		return b.mod(a).equals(BigInteger.ZERO);
	}

	@Override
	public BigInteger divFactor(BigInteger a, BigInteger b) {
		return b.divide(a);
	}

	@Override
	public BigInteger intMultiply(BigInteger a, BigInteger k) {
		return a.multiply(k);
	}

	/** Returns true if a is prime. */
	@SuppressWarnings("null")
	public boolean isPrime(BigInteger a) {
		/* Agrawal-Kayal-Saxena (AKS) algorithm. */
		if (perfectPowerTest(a)) {
			return false;
		}
		BigInteger r = null;
		/* Find r */
		if (r.equals(a)) {
			return true;
		}
		if (gcd(a, r).compareTo(BigInteger.ONE) > 1) {
			return false;
		}
		for (BigInteger i = BigInteger.ZERO; i.compareTo(sqrtFloor(r)) <= 1; i = i.add(BigInteger.ONE)) {

		}
		return false;
	}

	/** Returns a list with the prime factors of the given number. */
	public List<BigInteger> factor(BigInteger a) {
		/* Just a really dumb algorithm. */
		ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
		for (BigInteger i = new BigInteger("2"); i.compareTo(a) <= 0; i = i.add(BigInteger.ONE)) {
			if (a.mod(i).equals(BigInteger.ZERO)) {
				factors.add(i);
				if (!i.equals(a.divide(i))) {
					factors.add(a.divide(i));
				}
			}
		}
		return factors;
	}

	public BigInteger sqrtFloor(BigInteger a) {
		BigInteger k = new BigInteger(Integer.toString(((a.toString(2).length() - 1) / 2)));

		BigInteger two = BigInteger.ONE.add(BigInteger.ONE);
		BigInteger result = power(two, k);
		for (BigInteger i = k.subtract(BigInteger.ONE); i.compareTo(BigInteger.ZERO) >= 0; i = i
				.subtract(BigInteger.ONE)) {
			BigInteger aux = add(result, power(two, i));
			if (power(aux, two).compareTo(a) <= 0) {
				result = aux;
			}
		}
		return result;
	}

	public BigInteger nthrootFloor() {
		return null;
	}

	public boolean perfectPowerTest(BigInteger a) {

		return false;
	}
}
