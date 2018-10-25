package structures;

public abstract class EuclideanUnitDomain<T> extends EuclideanDomain<T> implements UnitRing<T> {
	public T bezout(T a, T b) {
		return getAddIdentity();
	}
}
