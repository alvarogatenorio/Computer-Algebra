package utils;

import structures.EuclideanDomain;
import structures.UnitRing;

public class Integers extends EuclideanDomain<Integer> implements UnitRing<Integer> {

	@Override
	public Integer getAddIdentity() {
		return 0;
	}

	@Override
	public Integer getAddInverse(Integer a) {
		return -a;
	}

	@Override
	public Integer quotient(Integer a, Integer b) {
		return a / b;
	}

	@Override
	public Integer reminder(Integer a, Integer b) {
		return a % b;
	}

	@Override
	public Integer add(Integer a, Integer b) {
		return a + b;
	}

	@Override
	public Integer multiply(Integer a, Integer b) {
		return a * b;
	}

	@Override
	public Integer getProductIdentity() {
		return 1;
	}

	@Override
	public Integer parseElement(String s) {
		return Integer.parseInt(s);
	}

}
