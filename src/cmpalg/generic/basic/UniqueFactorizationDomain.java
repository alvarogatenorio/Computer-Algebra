package cmpalg.generic.basic;

/** Represents an unique factorization domain (UFD). */
public abstract class UniqueFactorizationDomain<E> extends Ring<E> {

	/**
	 * Returns the greatest common divisor, which is guaranteed to exist and to be
	 * unique (except units).
	 */
	public abstract E gcd(E a, E b);
}
