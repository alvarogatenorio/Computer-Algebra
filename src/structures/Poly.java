package structures;

import java.util.ArrayList;
import java.util.List;

/*COMMENT, TEST AND MULTIPLY*/

public class Poly<T extends List<E>, E> implements Ring<T> {

	private Ring<E> baseRing;

	public Poly(Ring<E> baseRing) {
		this.baseRing = baseRing;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getAddIdentity() {
		List<E> addIdentity = new ArrayList<E>();
		addIdentity.add(baseRing.getAddIdentity());
		return (T) (addIdentity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getAddInverse(T a) {
		List<E> addInverse = new ArrayList<E>();
		for (int i = 0; i < a.size(); i++) {
			addInverse.add(baseRing.getAddInverse(a.get(i)));
		}
		return (T) (addInverse);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T add(T a, T b) {
		List<E> sum = new ArrayList<E>();
		int sizeUpperBound = Math.max(a.size(), b.size());
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
		while (sum.get(sum.size() - 1).equals(baseRing.getAddIdentity())) {
			sum.remove(sum.size() - 1);
		}
		return (T) (sum);
	}

	@Override
	public T multiply(T a, T b) {
		return null;
	}

}