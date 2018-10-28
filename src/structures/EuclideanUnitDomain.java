package structures;

import utils.Pair;

public abstract class EuclideanUnitDomain<T> extends EuclideanDomain<T> implements UnitRing<T> {

	public Pair<T> bezout(T a, T b) {
		T alphaMinus1 = getAddIdentity();
		T alphaMinus2 = getProductIdentity();
		T betaMinus1 = getProductIdentity();
		T betaMinus2 = getAddIdentity();
		while (b != getAddIdentity()) {
			/* Computes division of a and b */
			T q = quotient(a, b);
			T r = reminder(a, b);

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
}
