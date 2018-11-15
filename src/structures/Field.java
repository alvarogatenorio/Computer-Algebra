package structures;

/**
 * Every field is a unit ring, and an euclidean domain
 */
public abstract class Field<T> extends EuclideanUnitDomain<T> implements UnitRing<T> {

	/*
	 * In a field, every element is a unit. Returns the product inverse of a given
	 * field element
	 */
	public abstract T getProductInverse(T a);

	/* We just have to multiply by the inverse of the divisor */
	@Override
	public T quotient(T a, T b) {
		return multiply(a, getProductInverse(b));
	}

	/* The reminder is always null in a field */
	@Override
	public T reminder(T a, T b) {
		return getAddIdentity();
	}

	/* Everyone divides everyone in a field */
	@Override
	public boolean divides(T a, T b) {
		return true;
	}

}
