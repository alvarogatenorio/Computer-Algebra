package cmpalg.generic.basic;

import java.math.BigInteger;

/** Represents a commutative ring with product identity. */
public abstract class Ring<E> {

	/** Returns the additive identity of the ring. */
	public abstract E getAddIdentity();

	/** Returns the product identity of the ring. */
	public abstract E getProductIdentity();

	/** Returns the additive inverse of an element of the ring. */
	public abstract E getAddInverse(E a);

	/** Returns the sum of two elements of the ring. */
	public abstract E add(E a, E b);

	/** Returns the product of two elements of the ring. */
	public abstract E multiply(E a, E b);

	/**
	 * Returns the product of an element of the ring and an integer. For efficiency
	 * purposes, it is highly recommended to override this method. See the
	 * documentation for details.
	 */
	public E intMultiply(E a, BigInteger k) {
		E result = getAddIdentity();
		for (BigInteger i = BigInteger.ZERO; i.compareTo(k) == -1; i = i.add(BigInteger.ONE)) {
			result = add(result, a);
		}
		return result;
	}

	/**
	 * Returns the power of an element of the ring with an integer exponent. See the
	 * documentation for details.
	 */
	public E power(E a, BigInteger k) {
		E result = getProductIdentity();
		if (!k.equals(BigInteger.ZERO)) {
			/* Repeated squaring algorithm. */
			String binaryExponent = k.toString(2);
			result = a;
			for (BigInteger i = new BigInteger(Integer.toString(binaryExponent.length() - 2)); i
					.compareTo(BigInteger.ZERO) >= 0; i = i.subtract(BigInteger.ONE)) {
				if (binaryExponent.charAt(binaryExponent.length() - 1 - i.intValue()) == '1') {
					result = multiply(multiply(result, result), a);
				} else {
					result = multiply(result, result);
				}
			}
		}
		return result;
	}

	/** Returns true if a divides b. */
	public abstract boolean divides(E a, E b);

	/** Assuming a divides b, returns the corresponding factor. */
	public abstract E divFactor(E a, E b);

	/** Given a correctly formatted string, returns an element of the ring. */
	public abstract E parseElement(String s);

}
