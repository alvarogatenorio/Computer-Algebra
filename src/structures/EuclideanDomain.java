package structures;

/**
 * This class represents an euclidean domain, that is, a ring equipped with an
 * euclidean division.
 */
public abstract class EuclideanDomain<T> extends UniqueFactorizationDomain<T> implements Ring<T> {

	/* Returns the quotient of the euclidean division. b can't equal zero. */
	public abstract T quotient(T a, T b);

	/* Returns the reminder of the euclidean division. b can' t equal zero. */
	public abstract T reminder(T a, T b);

	/*
	 * Computes the greater common divisor by the Euclid's basic algorithm. See the
	 * docs for details.
	 */
	public T gcd(T a, T b) {
		while (!b.equals(getAddIdentity())) {
			T r = reminder(a, b);
			a = b;
			b = r;
		}
		return a;
	}
}
