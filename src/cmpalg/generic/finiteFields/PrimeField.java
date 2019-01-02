package cmpalg.generic.finiteFields;

import java.math.BigInteger;
import java.util.Random;

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

	@Override
	public PrimeFieldElement getGenerator() {
		/*
		 * Checks every element in the field. The multiplicative identity is obviously
		 * not the generator.
		 */
		for (BigInteger i = new BigInteger("2"); i.compareTo(p) == -1; i = i.add(BigInteger.ONE)) {
			PrimeFieldElement g = new PrimeFieldElement(i);

			/* In the j-th iteration, aux equals g to the j-th power */
			PrimeFieldElement aux = g;
			for (BigInteger j = new BigInteger("2"); j.compareTo(p) <= 0; j = j.add(BigInteger.ONE)) {
				aux = multiply(aux, g);

				/* A generator is an element whose order equals the order of the group. */
				if (aux.equals(getProductIdentity())) {
					if (!j.equals(p.subtract(BigInteger.ONE))) {
						break;
					} else {
						return g;
					}
				}
			}

		}

		/*
		 * This should never be reached as it is guaranteed to be a generator in the
		 * group.
		 */
		return null;
	}

	@Override
	public PrimeFieldElement getRandomElement() {
		Random random = new Random();
		BigInteger r;
		do {
			r = new BigInteger(p.toString(2).length(), random);
		} while (r.compareTo(p) >= 0);

		return new PrimeFieldElement(r);
	}

}
