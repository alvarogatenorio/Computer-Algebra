package structures;

public abstract class EuclideanDomain<T> implements Ring<T> {

	public abstract T quotient(T a, T b);

	public abstract T reminder(T a, T b);

	/* Euclid's basic algorithm */
	public T gcd(T a, T b) {
		while (!b.equals(getAddIdentity())) {
			T r = reminder(a, b);
			a = b;
			b = r;
		}
		return a;
	}
}
