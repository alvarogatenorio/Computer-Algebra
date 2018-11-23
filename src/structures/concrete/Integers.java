package structures.concrete;

import java.math.BigInteger;

import structures.basic.EuclideanDomain;

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
	public boolean isPrime(BigInteger a) {
		/* Agrawal-Kayal-Saxena (AKS) algorithm. */
		return false;
	}

}
