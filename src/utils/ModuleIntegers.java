package utils;

import structures.UnitRing;

public class ModuleIntegers implements UnitRing<Integer> {

	int module;

	/* EXCEPCIONES PARA NO HACER MÓDULO COSAS RARAS */
	public ModuleIntegers(int module) {
		this.module = module;
	}

	@Override
	public Integer getAddIdentity() {
		return 0;
	}

	@Override
	public Integer getAddInverse(Integer a) {
		return (-a) % module;
	}

	@Override
	public Integer add(Integer a, Integer b) {
		return (a + b) % module;
	}

	@Override
	public Integer multiply(Integer a, Integer b) {
		return (a * b) % module;
	}

	@Override
	public Integer parseElement(String s) {
		return Integer.parseInt(s) % module;
	}

	@Override
	public Integer getProductIdentity() {
		return 1;
	}

	@Override
	public boolean divides(Integer a, Integer b) {
		return false;
	}

	@Override
	public Integer multiply(Integer a, int k) {
		return (a * k) % module;
	}

	@Override
	public Integer power(Integer a, int k) {
		return null;
	}

}
