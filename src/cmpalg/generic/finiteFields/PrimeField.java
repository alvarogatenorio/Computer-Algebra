package cmpalg.generic.finiteFields;

import java.math.BigInteger;

import structures.concrete.euclideanDomains.Integers;

public class PrimeField extends FiniteField<PrimeFieldElement> {

	private BigInteger p;
	private Integers Z;

	public PrimeField(BigInteger p) {
		this.p = p;
		Z = new Integers();
	}

	@Override
	public PrimeFieldElement getProductInverse(PrimeFieldElement a) {
		return new PrimeFieldElement(Z.bezout(a.getElement(), p).getFirst().mod(p));
	}

	@Override
	public PrimeFieldElement getAddIdentity() {
		return new PrimeFieldElement(BigInteger.ZERO);
	}

	@Override
	public PrimeFieldElement getProductIdentity() {
		return new PrimeFieldElement(BigInteger.ONE);
	}

	@Override
	public PrimeFieldElement getAddInverse(PrimeFieldElement a) {
		return new PrimeFieldElement(a.getElement().negate().mod(p));
	}

	@Override
	public PrimeFieldElement add(PrimeFieldElement a, PrimeFieldElement b) {
		return new PrimeFieldElement(a.getElement().add(b.getElement()).mod(p));
	}

	@Override
	public PrimeFieldElement multiply(PrimeFieldElement a, PrimeFieldElement b) {
		return new PrimeFieldElement(a.getElement().multiply(b.getElement()).mod(p));
	}

	@Override
	public PrimeFieldElement parseElement(String s) {
		return new PrimeFieldElement((new BigInteger(s)).mod(p));
	}

	@Override
	public PrimeFieldElement intMultiply(PrimeFieldElement a, BigInteger k) {
		return new PrimeFieldElement(a.getElement().multiply(k).mod(p));
	}

	@Override
	public BigInteger getOrder() {
		return p;
	}

	@Override
	public BigInteger getCharacteristic() {
		return p;
	}

}
