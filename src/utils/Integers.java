package utils;

import structures.EuclideanUnitDomain;

public class Integers extends EuclideanUnitDomain<Integer> {
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

	@Override
	public boolean divides(Integer a, Integer b) {
		return b % a == 0;
	}
}
