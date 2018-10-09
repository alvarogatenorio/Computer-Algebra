package utils;

import java.util.ArrayList;
import java.util.List;

import structures.Ring;

/*COMMENT AND TEST*/

public class Polynomials<T extends Polynomial<E>, E> implements Ring<T> {

	private Ring<E> baseRing;

	public Polynomials(Ring<E> baseRing) {
		this.baseRing = baseRing;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getAddIdentity() {
		List<E> addIdentity = new ArrayList<E>();
		addIdentity.add(baseRing.getAddIdentity());
		return (T) new Polynomial<E>(addIdentity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getAddInverse(T a) {
		List<E> addInverse = new ArrayList<E>();
		for (int i = 0; i < a.size(); i++) {
			addInverse.add(baseRing.getAddInverse(a.get(i)));
		}
		return (T) new Polynomial<E>(addInverse);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T add(T a, T b) {
		/* We create the result polynomial */
		List<E> sum = new ArrayList<E>();

		/*
		 * The result polynomial will have, at most, the degree of the greater degree
		 * polynomial
		 */
		int sizeUpperBound = Math.max(a.size(), b.size());

		/*
		 * We add the polynomials coefficient by coefficient, taking into account the
		 * cases when one polynomial is shorter than the other
		 */
		for (int i = 0; i < sizeUpperBound; i++) {
			E coefficientSum;

			if (a.size() <= i) {
				coefficientSum = baseRing.add(baseRing.getAddIdentity(), b.get(i));
			} else if (b.size() <= i) {
				coefficientSum = baseRing.add(a.get(i), baseRing.getAddIdentity());
			} else {
				coefficientSum = baseRing.add(a.get(i), b.get(i));
			}

			sum.add(coefficientSum);
		}

		/*
		 * We clean the last add identity elements from the result polynomial. We take
		 * care of the case in which the result polynomial is the add identity.
		 */
		while (sum.get(sum.size() - 1).equals(baseRing.getAddIdentity()) && sum.size() > 1) {
			sum.remove(sum.size() - 1);
		}

		/* Cast and return the result */
		return (T) new Polynomial<E>(sum);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T multiply(T a, T b) {
		/* We create the result polynomial */
		List<E> product = new ArrayList<E>();

		/*
		 * The result polynomial will have degree of, at most, the sum of both
		 * polynomials degrees
		 */
		int sizeUpperBound = (a.size() - 1) + (b.size() - 1);

		/* We just follow the general formula for the product of two polynomials */
		for (int k = 0; k <= sizeUpperBound; k++) {
			E coefficient = baseRing.getAddIdentity();
			for (int p = 0; p <= k; p++) {
				/* All of these is for taking care about the limits of the lists */
				E factor1;
				E factor2;

				if (a.size() <= p) {
					factor1 = baseRing.getAddIdentity();
				} else {
					factor1 = a.get(p);
				}

				if (b.size() <= k - p) {
					factor2 = baseRing.getAddIdentity();
				} else {
					factor2 = b.get(k - p);
				}

				coefficient = baseRing.add(coefficient, baseRing.multiply(factor1, factor2));
			}
			product.add(coefficient);
		}

		/*
		 * We clean the last add identity elements from the result polynomial. We take
		 * care of the case in which the result polynomial is the add identity.
		 */
		while (product.get(product.size() - 1).equals(baseRing.getAddIdentity()) && product.size() > 1) {
			product.remove(product.size() - 1);
		}

		/* Cast and return the result */
		return (T) new Polynomial<E>(product);
	}

}