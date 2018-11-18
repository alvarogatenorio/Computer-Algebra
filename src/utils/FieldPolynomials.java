package utils;

import structures.EuclideanUnitDomain;
import structures.Field;

public class FieldPolynomials<T extends Polynomial<E>, E> extends EuclideanUnitDomain<T> {

	private UnitPolynomials<T, E> polyRing;
	private Field<E> baseRing;

	public FieldPolynomials(Field<E> baseRing) {
		polyRing = new UnitPolynomials<T, E>(baseRing);
		this.baseRing = baseRing;
	}

	@Override
	public T getProductIdentity() {
		return polyRing.getProductIdentity();
	}

	@Override
	public T getAddIdentity() {
		return polyRing.getAddIdentity();
	}

	@Override
	public T getAddInverse(T a) {
		return polyRing.getAddInverse(a);
	}

	@Override
	public T add(T a, T b) {
		return polyRing.add(a, b);
	}

	@Override
	public T multiply(T a, T b) {
		return polyRing.multiply(a, b);
	}

	@Override
	public T parseElement(String s) {
		return polyRing.parseElement(s);
	}

	@Override
	public boolean divides(T a, T b) {
		return division(a, b).getSecond().equals(getAddIdentity());
	}

	@Override
	public T multiply(T a, int k) {
		return polyRing.multiply(a, k);
	}

	@Override
	public T power(T a, int k) {
		return polyRing.power(a, k);
	}

	public Pair<T> division(T a, T b) {
		T q = null;
		T r = null;
		if (a.degree() < b.degree()) {
			q = getAddIdentity();
			r = a;
		} else {
			if (a.degree() == 0) {
				q = multiply(a, baseRing.getProductInverse(b.independent()));
				r = getAddIdentity();
			} else {
				T h = add(a,
						multiply(
								multiply(parseElement("t^" + (a.degree() - b.degree())), baseRing.multiply(
										baseRing.getAddInverse(a.leading()), baseRing.getProductInverse(b.leading()))),
								b));
				if (h.degree() < b.degree()) {
					q = multiply(parseElement("t^" + (a.degree() - b.degree())),
							baseRing.multiply(a.leading(), baseRing.getProductInverse(b.leading())));
					r = h;
				} else {
					Pair<T> aux = division(h, b);
					q = add(multiply(parseElement("t^" + (a.degree() - b.degree())),
							baseRing.multiply(a.leading(), baseRing.getProductInverse(b.leading()))), aux.getFirst());
					r = aux.getSecond();
				}
			}
		}
		return new Pair<T>(q, r);
	}

	private T multiply(T a, E e) {
		return polyRing.multiply(a, e);
	}

	@Override
	public T quotient(T a, T b) {
		return division(a, b).getFirst();
	}

	@Override
	public T reminder(T a, T b) {
		return division(a, b).getSecond();
	}

	@Override
	public T exactQuotient(T a, T b) {
		return quotient(a, b);
	}
}
