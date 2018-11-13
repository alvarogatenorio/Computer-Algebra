package utils;

import java.util.ArrayList;
import java.util.List;

import structures.UnitRing;

public class UnitPolynomials<T extends Polynomial<E>, E> extends Polynomials<T, E> implements UnitRing<T> {

	public UnitPolynomials(UnitRing<E> baseRing) {
		super(baseRing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getProductIdentity() {
		List<E> addIdentity = new ArrayList<E>();
		addIdentity.add((((UnitRing<E>) baseRing).getProductIdentity()));
		return (T) new Polynomial<E>(addIdentity, baseRing);
	}
	
	/*El parser se debería hacer por aquí*/

}
