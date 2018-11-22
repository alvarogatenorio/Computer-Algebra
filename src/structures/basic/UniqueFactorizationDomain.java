package structures.basic;

/** Represents an unique factorization domain (UFD). */
public abstract class UniqueFactorizationDomain<T> extends Ring<T> {

	/**
	 * Returns the greatest common divisor, which is guaranteed to exist and to be
	 * unique (except units).
	 */
	public abstract T gcd(T a, T b);
}
