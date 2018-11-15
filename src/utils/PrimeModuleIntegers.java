package utils;

import structures.Field;

public class PrimeModuleIntegers extends Field<Integer> {

	int primeModule;

	/* EXCEPTION (quizá meter aquí el test de primalidad aks) */
	public PrimeModuleIntegers(int primeModule) {
		this.primeModule = primeModule;
	}

	@Override
	public Integer getProductIdentity() {
		return 1;
	}

	@Override
	public Integer getAddIdentity() {
		return 0;
	}

	@Override
	public Integer getAddInverse(Integer a) {
		return (-a) % primeModule;
	}

	@Override
	public Integer add(Integer a, Integer b) {
		return (a + b) % primeModule;
	}

	@Override
	public Integer multiply(Integer a, Integer b) {
		return (a * b) % primeModule;
	}

	@Override
	public Integer parseElement(String s) {
		return Integer.parseInt(s) % primeModule;
	}

	/* PAG 73 */
	@Override
	public Integer getProductInverse(Integer a) {
		return bezout(a, primeModule).getFirst();
	}

	@Override
	public boolean divides(Integer a, Integer b) {
		// TODO Auto-generated method stub
		return false;
	}

}
