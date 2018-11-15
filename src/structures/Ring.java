package structures;

/**
 * This will be the most primitive class of out project. It represents a ring,
 * we don't assume the ring has a product identity. But we will assume the
 * product is commutative.
 */
public interface Ring<T> {
	/* Returns the additive identity of the ring */
	public T getAddIdentity();

	/* Returns the additive inverse of an element of the ring */
	public T getAddInverse(T a);

	/* Returns the sum of two elements of the ring */
	public T add(T a, T b);

	/* Returns the product of two elements of the ring */
	public T multiply(T a, T b);

	/* Given a LaTeX-like format string, returns an element of the ring */
	public T parseElement(String s);

	/* Returns true if there is some element c in the ring such that ac = b */
	public boolean divides(T a, T b);

	public T multiply(T a, int k);

	public T power(T a, int k);
}
