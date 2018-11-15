package utils;

import structures.Field;

public class PrimesQuotients extends Field<Polynomial<Integer>> {

	private int primeIntMod;
	private Polynomial<Integer> irrPolMod;

	public PrimesQuotients(int primeIntMod, Polynomial<Integer> irrPolMod) {
		this.primeIntMod = primeIntMod;
		this.irrPolMod = irrPolMod;
	}

	@Override
	public Polynomial<Integer> getProductIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<Integer> getAddIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<Integer> getAddInverse(Polynomial<Integer> a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<Integer> add(Polynomial<Integer> a, Polynomial<Integer> b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<Integer> multiply(Polynomial<Integer> a, Polynomial<Integer> b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<Integer> parseElement(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<Integer> getProductInverse(Polynomial<Integer> a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<Integer> multiply(Polynomial<Integer> a, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<Integer> power(Polynomial<Integer> a, int k) {
		// TODO Auto-generated method stub
		return null;
	}

}
