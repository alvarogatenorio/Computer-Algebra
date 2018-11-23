package structures.concrete;

import java.math.BigInteger;

import structures.basic.EuclideanDomain;
import utils.Polynomial;

/**
 * Represents the Gaussian integers Z[i]. (We will see how to do that, low
 * priority).
 */
public class GaussianIntegers extends EuclideanDomain<Polynomial<BigInteger>> {

	@Override
	public Polynomial<BigInteger> quotient(Polynomial<BigInteger> a, Polynomial<BigInteger> b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<BigInteger> remainder(Polynomial<BigInteger> a, Polynomial<BigInteger> b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<BigInteger> getAddIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<BigInteger> getProductIdentity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<BigInteger> getAddInverse(Polynomial<BigInteger> a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<BigInteger> add(Polynomial<BigInteger> a, Polynomial<BigInteger> b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<BigInteger> multiply(Polynomial<BigInteger> a, Polynomial<BigInteger> b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean divides(Polynomial<BigInteger> a, Polynomial<BigInteger> b) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Polynomial<BigInteger> divFactor(Polynomial<BigInteger> a, Polynomial<BigInteger> b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial<BigInteger> parseElement(String s) {
		// TODO Auto-generated method stub
		return null;
	}
}
