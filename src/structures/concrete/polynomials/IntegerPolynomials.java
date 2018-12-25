package structures.concrete.polynomials;

import java.math.BigInteger;
import java.util.List;

import cmpalg.generic.basic.UniqueFactorizationDomain;
import structures.concrete.euclideanDomains.Integers;
import structures.generic.polynomials.Polynomial;
import structures.generic.polynomials.UFDPolynomials;

public class IntegerPolynomials extends UFDPolynomials<BigInteger> {

	@Deprecated
	public IntegerPolynomials(UniqueFactorizationDomain<BigInteger> baseRing) {
		super(baseRing);
	}

	public IntegerPolynomials() {
		super(new Integers());
	}

	private FactorAlgorithm factorAlgorithm = FactorAlgorithm.MODULAR;

	public enum FactorAlgorithm {
		KRONECKER, MODULAR
	};

	public void setFactorAlgorithm(FactorAlgorithm factorAlgorithm) {
		this.factorAlgorithm = factorAlgorithm;
	}

	/**
	 * Returns the factors list of the given polynomial. See the documentation for
	 * details.
	 */
	public List<Polynomial<BigInteger>> factor(Polynomial<BigInteger> f) {
		switch (factorAlgorithm) {
		case KRONECKER:
			return kronecker(f);
		case MODULAR:
			return modular(f);
		default:
			return null;
		}
	}

	public List<Polynomial<BigInteger>> kronecker(Polynomial<BigInteger> f) {
		return null;
	}

	public List<Polynomial<BigInteger>> modular(Polynomial<BigInteger> f) {
		return null;
	}

}
