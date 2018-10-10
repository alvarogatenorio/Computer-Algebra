package structures;

public interface Ring<T> {
	/* Returns the additive identity of the ring */
	public T getAddIdentity();

	/* Returns the additive inverse of an element of the ring */
	public T getAddInverse(T a);

	/* Returns the sum of two elements of the ring */
	public T add(T a, T b);

	/* Returns the product of two elements of the ring */
	public T multiply(T a, T b);

	/**/
	public T parseElement(String s);
}
