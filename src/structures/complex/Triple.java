package structures.complex;

public class Triple<T1, T2> {
	private T1 first;
	private T2 second;
	private T2 third;

	public Triple(T1 first, T2 second, T2 third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

	public T2 getThird() {
		return third;
	}
}
