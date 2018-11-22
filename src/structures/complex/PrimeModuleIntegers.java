package structures.complex;

import java.math.BigInteger;

import structures.basic.Field;

public class PrimeModuleIntegers extends Field<BigInteger> {

	BigInteger primeModule;
	private Integers Z;

	public PrimeModuleIntegers(BigInteger primeModule) {
		this.primeModule = primeModule;
		Z = new Integers();
	}

	@Override
	public BigInteger getProductIdentity() {
		return BigInteger.ONE;
	}

	@Override
	public BigInteger getAddIdentity() {
		return BigInteger.ZERO;
	}

	@Override
	public BigInteger getAddInverse(BigInteger a) {
		return a.negate().mod(primeModule);
	}

	@Override
	public BigInteger add(BigInteger a, BigInteger b) {
		return a.add(b).mod(primeModule);
	}

	@Override
	public BigInteger multiply(BigInteger a, BigInteger b) {
		return a.multiply(b).mod(primeModule);
	}

	@Override
	public BigInteger parseElement(String s) {
		return (new BigInteger(s)).mod(primeModule);
	}

	@Override
	public BigInteger getProductInverse(BigInteger a) {
		return Z.bezout(a, primeModule).getFirst().mod(primeModule);
	}

	@Override
	public BigInteger intMultiply(BigInteger a, BigInteger k) {
		return a.multiply(k).mod(primeModule);
	}

	@Override
	public BigInteger divFactor(BigInteger a, BigInteger b) {
		return Z.divFactor(a, b);
	}
}
