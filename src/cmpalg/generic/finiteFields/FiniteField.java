package cmpalg.generic.finiteFields;

import java.math.BigInteger;
import java.util.HashMap;

import cmpalg.generic.basic.Field;
import structures.concrete.euclideanDomains.Integers;

public abstract class FiniteField<E extends FiniteFieldElement> extends Field<E> {

	public abstract BigInteger getOrder();

	public abstract BigInteger getCharacteristic();

	public BigInteger discreteLogarithm(E generator, E a) {
		Integers Z = new Integers();

		BigInteger m = Z.add(Z.sqrtFloor(Z.add(getOrder(), Z.getAddInverse(Z.getProductIdentity()))),
				Z.getProductIdentity());

		HashMap<E, BigInteger> L = new HashMap<E, BigInteger>();
		E aux = getProductIdentity();
		for (BigInteger j = BigInteger.ZERO; j.compareTo(m) < 0; j = j.add(BigInteger.ONE)) {
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

	public abstract E getGenerator();

	public abstract E getRandomElement();
}
