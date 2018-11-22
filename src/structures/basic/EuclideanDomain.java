package structures.basic;

import java.util.List;

import structures.complex.Pair;

/** Represents an Euclidean Domain (ED). */
public abstract class EuclideanDomain<T> extends Ring<T> {

	/**
	 * Returns the quotient of the euclidean division. The denominator cannot be
	 * zero.
	 */
	public abstract T quotient(T a, T b);

	/**
	 * Returns the reminder of the euclidean division. The denominator cannot be
	 * zero.
	 */
	public abstract T remainder(T a, T b);

	/**
	 * Computes the greatest common divisor. See the documentation for details.
	 */
	public T gcd(T a, T b) {
		while (!b.equals(getAddIdentity())) {
			T r = remainder(a, b);
			a = b;
			b = r;
		}
		return a;
	}

	/**
	 * Computes the coefficients of the Bézout identity of the elements a and b. See
	 * the documentation for details.
	 */
	public Pair<T> bezout(T a, T b) {
		T alphaMinus1 = getAddIdentity();
		T alphaMinus2 = getProductIdentity();
		T betaMinus1 = getProductIdentity();
		T betaMinus2 = getAddIdentity();
		while (!b.equals(getAddIdentity())) {
			/* Computes division of a and b */
			T q = quotient(a, b);
			T r = remainder(a, b);

			/*
			 * Just follow the formula for the new pseudo-bezout identity coefficients
			 */
			T alphaAux = add(alphaMinus2, getAddInverse(multiply(q, alphaMinus1)));
			T betaAux = add(betaMinus2, getAddInverse(multiply(q, betaMinus1)));

			/* Maintaining alphaMinusX and betaMinusX coherent */
			alphaMinus2 = alphaMinus1;
			betaMinus2 = betaMinus1;

			alphaMinus1 = alphaAux;
			betaMinus1 = betaAux;

			/* By euclid's lemma gcd(a,b) = gcd(b,r) */
			a = b;
			b = r;
		}
		return new Pair<T>(alphaMinus2, betaMinus2);
	}

	/**
	 * Computes the inverse function of the Chinese Reminder Theorem projection.
	 * 
	 * Both lists have to be the same size.
	 * 
	 * Ideals have to be two-by-two comaximal (coprime).
	 */
	public T chineseReminderInverse(List<T> ideals, List<T> reminders) {
		T inverse = getAddIdentity();
		T m = getProductIdentity();
		for (int i = 0; i < ideals.size(); i++) {
			m = multiply(m, ideals.get(i));
		}
		for (int i = 0; i < reminders.size(); i++) {
			T q = quotient(m, ideals.get(i));
			T coefficient = bezout(q, ideals.get(i)).getFirst();
			/* Make sure of things */
			// inverse = add(inverse, multiply(reminder(multiply(reminders.get(i),
			// coefficient), ideals.get(i)), q));
			inverse = add(inverse, multiply(multiply(reminders.get(i), coefficient), q));
		}
		return inverse;
	}

}
