package cmpalg.generic.finiteFields;

import java.math.BigInteger;
import java.util.HashMap;

import cmpalg.generic.basic.Field;
import structures.concrete.euclideanDomains.Integers;

public abstract class FiniteField<E extends FiniteFieldElement> extends Field<E> {

	public abstract BigInteger getOrder();

	public abstract BigInteger getCharacteristic();

	/**
	 * Computes the discrete logarithm of an element, given a generator of the
	 * multiplicative group. See the documentation for details.
	 */
	public BigInteger discreteLogarithm(E generator, E a) {
		Integers Z = new Integers();

		BigInteger m = Z.add(Z.sqrtFloor(Z.add(getOrder(), Z.getAddInverse(Z.getProductIdentity()))),
				Z.getProductIdentity());

		HashMap<E, BigInteger> L = new HashMap<E, BigInteger>();
		E aux = getProductIdentity();
		L.put(aux, BigInteger.ZERO);
		for (BigInteger j = BigInteger.ONE; j.compareTo(m) < 0; j = j.add(BigInteger.ONE)) {
			aux = multiply(aux, generator);
			L.put(aux, j);
		}

		E gm = power(getProductInverse(generator), m);
		E w = a;
		for (BigInteger i = BigInteger.ZERO; i.compareTo(m) < 0; i = i.add(BigInteger.ONE)) {
			BigInteger match = L.get(w);
			if (match == null) {
				w = multiply(w, gm);
			} else {
				return Z.add(Z.multiply(i, m), match);
			}
		}

		/* This return is never reached. */
		return null;
	}

	/**
	 * As finite fields without the additive identity are cyclic multiplicative
	 * groups, this method returns a generator for this group.
	 */
	public abstract E getGenerator();

	public abstract E getRandomElement();
}
