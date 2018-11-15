package structures;

/**
 * This is a trivial extension of the Ring class, we now assume the ring has a
 * product identity
 */
public interface UnitRing<T> extends Ring<T> {

	/* Returns the product identity of the ring */
	public T getProductIdentity();
}
