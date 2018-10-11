package utils;

import structures.Field;

public class PrimeModuleIntegers extends Field<Integer> {

	int primeModule;

	/* EXCEPTION */
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

	/* Esto es precisamente uno de los algoritmos (creo) */
	@Override
	public Integer getProductInverse(Integer a) {
		// TODO Auto-generated method stub
		return null;
	}

}
