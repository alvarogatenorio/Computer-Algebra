package structures.complex;

import java.math.BigInteger;

import structures.basic.Ring;

public class ModuleIntegers extends Ring<BigInteger> {

	BigInteger module;

	public ModuleIntegers(BigInteger module) {
		this.module = module;
	}

	@Override
	public BigInteger getAddIdentity() {
		return BigInteger.ZERO;
	}

	@Override
	public BigInteger getAddInverse(BigInteger a) {
		return a.negate().mod(module);
	}

	@Override
	public BigInteger add(BigInteger a, BigInteger b) {
		return a.add(b).mod(module);
	}

	@Override
	public BigInteger multiply(BigInteger a, BigInteger b) {
		return a.multiply(b).mod(module);
	}

	@Override
	public BigInteger parseElement(String s) {
		return (new BigInteger(s)).mod(module);
	}

	@Override
	public BigInteger getProductIdentity() {
		return BigInteger.ONE;
	}

	@Override
	public boolean divides(BigInteger a, BigInteger b) {
		return false;
	}

	@Override
	public BigInteger intMultiply(BigInteger a, BigInteger k) {
		return a.multiply(k).mod(module);
	}

	@Override
	public BigInteger divFactor(BigInteger a, BigInteger b) {
		return null;
	}

}
