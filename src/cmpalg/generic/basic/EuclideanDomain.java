package cmpalg.generic.basic;

import java.math.BigInteger;
import java.util.List;

import utils.Pair;

/** Represents an Euclidean Domain (ED). */
public abstract class EuclideanDomain<E> extends UniqueFactorizationDomain<E> {

	/**
	 * Returns the quotient of the euclidean division. The denominator cannot be
	 * zero.
	 */
	public abstract E quotient(E a, E b);

	/**
	 * Returns the reminder of the euclidean division. The denominator cannot be
	 * zero.
	 */
	public abstract E remainder(E a, E b);

	/**
	 * Computes the greatest common divisor. See the documentation for details.
	 */
	public E gcd(E a, E b) {
		/* Euclid's algorithm. */
		while (!b.equals(getAddIdentity())) {
			E r = remainder(a, b);
			a = b;
			b = r;
		}
		return a;
	}

	/**
	 * Computes the coefficients of the Bezout identity of the elements a and b. See
	 * the documentation for details.
	 */
	public Pair<E, E> bezout(E a, E b) {
		/* Extended Euclid's algorithm. */
		E alphaMinus1 = getAddIdentity();
		E alphaMinus2 = getProductIdentity();
		E betaMinus1 = getProductIdentity();
		E betaMinus2 = getAddIdentity();
		while (!b.equals(getAddIdentity())) {
			/* Computes division of a and b */
			E q = quotient(a, b);
			E r = remainder(a, b);

			/*
			 * Just follow the formula for the new pseudo-bezout identity coefficients
			 */
			E alphaAux = add(alphaMinus2, getAddInverse(multiply(q, alphaMinus1)));
			E betaAux = add(betaMinus2, getAddInverse(multiply(q, betaMinus1)));

			/* Maintaining alphaMinusX and betaMinusX coherent */
			alphaMinus2 = alphaMinus1;
			betaMinus2 = betaMinus1;

			alphaMinus1 = alphaAux;
			betaMinus1 = betaAux;

			/* By euclid's lemma gcd(a,b) = gcd(b,r) */
			a = b;
			b = r;
		}
		return new Pair<E, E>(alphaMinus2, betaMinus2);
	}

	/**
	 * Computes the inverse function of the Chinese Reminder Theorem projection. See
	 * the documentation for details.
	 * 
	 * Both lists have to be the same size.
	 * 
	 * Ideals have to be two-by-two comaximal (coprime).
	 */
	public E chineseRemainderInverse(List<E> ideals, List<E> remainders) {

		/* m is a generator of the product of ideals. */
		E m = getProductIdentity();
		for (int i = 0; i < ideals.size(); i++) {
			m = multiply(m, ideals.get(i));
		}

		E inverse = getAddIdentity();
		for (int i = 0; i < remainders.size(); i++) {
			/* q is the generator of the product of ideals except the i-th one. */
			E q = quotient(m, ideals.get(i));
			E coefficient = bezout(q, ideals.get(i)).getFirst();
			inverse = add(inverse, multiply(multiply(remainders.get(i), coefficient), q));
		}
		return inverse;
	}

	/**
	 * Returns the power of an element of the euclidean domain with an integer
	 * exponent modulo another element of the euclidean domain. See the
	 * documentation for details.
	 */
	public E power(E a, BigInteger k, E module) {
		E result = remainder(getProductIdentity(), module);
		if (!k.equals(BigInteger.ZERO)) {
			/* Repeated squaring algorithm. */
			String binaryExponent = k.toString(2);
			result = remainder(a, module);
			for (BigInteger i = new BigInteger(Integer.toString(binaryExponent.length() - 2)); i
					.compareTo(BigInteger.ZERO) >= 0; i = i.subtract(BigInteger.ONE)) {
				if (binaryExponent.charAt(binaryExponent.length() - 1 - i.intValue()) == '1') {
					result = remainder(multiply(multiply(result, result), a), module);
				} else {
					result = remainder(multiply(result, result), module);
				}
			}
		}
		return result;
	}

}
