package cmpalg.generic.finiteFields;

import java.math.BigInteger;

import cmpalg.generic.basic.Field;

public abstract class FiniteField<E extends FiniteFieldElement> extends Field<E> {

	public abstract BigInteger getOrder();

	public abstract BigInteger getCharacteristic();

	public FiniteFieldElement discreteLogarithm(FiniteFieldElement generator, FiniteFieldElement a) {
		return null;
	}

	public abstract FiniteFieldElement getGenerator();

	public abstract FiniteFieldElement getRandomElement();
}
