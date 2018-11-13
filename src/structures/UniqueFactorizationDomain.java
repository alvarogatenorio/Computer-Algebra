package structures;

public abstract class UniqueFactorizationDomain<T> implements Ring<T> {
	public abstract T gcd(T a, T b);
}
