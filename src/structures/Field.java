package structures;

public abstract class Field<T> extends EuclideanUnitDomain<T> implements UnitRing<T> {

	@Override
	public T quotient(T a, T b) {
		return multiply(a, getProductInverse(b));
	}

	@Override
	public T reminder(T a, T b) {
		return getAddIdentity();
	}

	public abstract T getProductInverse(T a);

}
