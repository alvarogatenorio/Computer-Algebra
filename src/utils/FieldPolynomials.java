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
		return polyRing.divides(a, b);
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
		int k = 0;
		T q = null;
		T r = null;
		if (a.degree() < b.degree()) {
			k = 0;
			q = getAddIdentity();
			r = a;
		} else {
			if (a.degree() == 0) {
				k = 0;
				q = multiply(a, baseRing.getProductInverse(b.independent()));
				r = getAddIdentity();
			} else {
				T h = add(multiply(a, b.leading()), multiply(
						multiply(parseElement("t^" + (a.degree() - b.degree())), baseRing.getAddInverse(a.leading())),
						b));
				if (h.degree() < b.degree()) {
					k = 1;
					q = multiply(parseElement("t^" + (a.degree() - b.degree())), a.leading());
					r = h;
				} else {
					Triple<Integer, T> aux = division(h, b);
					k = aux.getFirst() + 1;
					// errors here
					q = add(aux.getSecond(), multiply(parseElement("t^" + (a.degree() - b.degree())),
							baseRing.multiply((baseRing.power(b.leading(), aux.getFirst())), a.leading())));
					r = aux.getThird();
				}
			}
		}
		return null;
	}

	private T multiply(T a, E e) {
		return polyRing.multiply(a, e);
	}

	@Override
	public T quotient(T a, T b) {

		return null;
	}

	@Override
	public T reminder(T a, T b) {
		return null;
	}
}
