package structures;

public interface Ring<T> {
	/* Returns the additive identity of the ring */
	public T getAddIdentity();

	/* Returns the additive inverse of an element of the ring */
	public T getAddInverse(T a);
}
