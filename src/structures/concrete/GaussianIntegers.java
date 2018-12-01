package structures.concrete;

import java.math.BigInteger;

import structures.basic.EuclideanDomain;
import utils.GaussianInteger;

/**
 * Represents the Gaussian integers Z[i].
 */
public class GaussianIntegers extends EuclideanDomain<GaussianInteger> {

	@Override
	public GaussianInteger quotient(GaussianInteger a, GaussianInteger b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GaussianInteger remainder(GaussianInteger a, GaussianInteger b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GaussianInteger getAddIdentity() {
		return new GaussianInteger(BigInteger.ZERO);
	}

	@Override
	public GaussianInteger getProductIdentity() {
		return new GaussianInteger(BigInteger.ZERO);
	}

	@Override
	public GaussianInteger getAddInverse(GaussianInteger a) {
		return new GaussianInteger(a.getRealPart().negate(), a.getImaginaryPart().negate());
	}

	@Override
	public GaussianInteger add(GaussianInteger a, GaussianInteger b) {
		return new GaussianInteger(a.getRealPart().add(b.getRealPart()),
				a.getImaginaryPart().add(b.getImaginaryPart()));
	}

	@Override
	public GaussianInteger multiply(GaussianInteger a, GaussianInteger b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean divides(GaussianInteger a, GaussianInteger b) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GaussianInteger divFactor(GaussianInteger a, GaussianInteger b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GaussianInteger parseElement(String s) {
		return null;
	}
}
