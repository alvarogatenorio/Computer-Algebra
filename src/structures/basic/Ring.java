package structures.basic;

import java.math.BigInteger;

/** Represents a commutative ring with product identity. */
public abstract class Ring<T> {

	/** Returns the additive identity of the ring. */
	public abstract T getAddIdentity();

	/** Returns the product identity of the ring. */
	public abstract T getProductIdentity();

	/** Returns the additive inverse of an element of the ring. */
	public abstract T getAddInverse(T a);

	/** Returns the sum of two elements of the ring. */
	public abstract T add(T a, T b);

	/** Returns the product of two elements of the ring. */
	public abstract T multiply(T a, T b);

	/**
	 * Returns the product of an element of the ring and an integer. For efficiency
	 * purposes, it is highly recommended to override this method.
	 */
	public T intMultiply(T a, BigInteger k) {
		T result = getAddIdentity();
		for (BigInteger i = BigInteger.ZERO; i.compareTo(k) == -1; i.add(BigInteger.ONE)) {
			result = add(result, a);
		}
		return result;
	}

	/**
	 * Returns the power of an element of the ring with an integer exponent. See the
	 * documentation for details.
	 */
	public T power(T a, BigInteger k) {
		String binaryExponent = k.toString(2);
		T result = a;
		for (BigInteger i = (new BigInteger(Integer.toString(binaryExponent.length()))).subtract(BigInteger.ONE); i
				.compareTo(BigInteger.ZERO) >= 0; i.subtract(BigInteger.ONE)) {
			if (true) {
				result = add(multiply(result, result), a);
			} else {
				result = multiply(result, result);
			}
		}
		return result;
	}

	/** Returns true if a divides b. */
	public abstract boolean divides(T a, T b);

	/** Assuming a divides b, returns the corresponding factor. */
	public abstract T divFactor(T a, T b);

	/** Given a correctly formatted string, returns an element of the ring. */
	public abstract T parseElement(String s);

}
