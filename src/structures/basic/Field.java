package structures.basic;

/** Represents a field. */
public abstract class Field<E> extends EuclideanDomain<E> {

	/**
	 * In a field, every element is a unit. Returns the product inverse of a given
	 * field element.
	 */
	public abstract E getProductInverse(E a);

	/** Multiply by the inverse of the divisor. */
	@Override
	public E quotient(E a, E b) {
		return multiply(a, getProductInverse(b));
	}

	/** The remainder is always null in a field. */
	@Override
	public E remainder(E a, E b) {
		return getAddIdentity();
	}

	/** Everyone divides everyone in a field. But no one divides 0. */
	@Override
	public boolean divides(E a, E b) {
		return b.equals(getAddIdentity()) ? false : true;
	}

	/** The factor is just b over a. */
	@Override
	public E divFactor(E a, E b) {
		return multiply(b, getProductInverse(a));
	}

}
