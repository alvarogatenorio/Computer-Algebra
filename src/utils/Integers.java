package utils;

import structures.EuclideanDomain;

public class Integers extends EuclideanDomain<Integer> {

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

}
