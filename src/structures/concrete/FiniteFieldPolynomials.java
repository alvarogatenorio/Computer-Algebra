package structures.concrete;

import java.math.BigInteger;
import java.util.List;

import structures.complex.FieldPolynomials;
import structures.complex.FiniteField;
import utils.FiniteFieldElement;
import utils.Polynomial;

/** Represents the set of polynomials over Fq (Fq[t]). */
public class FiniteFieldPolynomials extends FieldPolynomials<FiniteFieldElement> {

	private FiniteField baseField;

	public enum FactorAlgorithm {
		CANTOR, BERLEKAMP
	};

	private FactorAlgorithm factorAlgorithm = FactorAlgorithm.CANTOR;

	public FiniteFieldPolynomials(FiniteField baseField) {
		super(baseField);
		this.baseField = baseField;
	}

	/**
	 * EXPLODES!!
	 * 
	 * NEW POLYNOMIAL REPRESENTATION PROBABLY REQUIRED FOR SPEEDING UP ISSUES
	 * 
	 * FAST MODULAR COMPOSITION FOR SURE REQUIRED
	 */
	public boolean isIrreducible(Polynomial<FiniteFieldElement> f) {
		BigInteger n = new BigInteger(Integer.toString(f.degree()));
		BigInteger q = baseField.getOrder();

		Integers Z = new Integers();
		List<BigInteger> factors = Z.factor(n);

		if (modularPower(parseElement("t"), Z.power(q, n), f).equals(parseElement("t"))) {
			for (int i = 0; i < factors.size(); i++) {
				Polynomial<FiniteFieldElement> b = remainder(parseElement("t^" + Z.power(q, n.divide(factors.get(i)))),
						f);
				if (!gcd(add(b, parseElement("-1t")), parseElement("t")).equals(baseField.getProductIdentity())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public void setFactorAlgorithm(FactorAlgorithm factorAlgorithm) {
		this.factorAlgorithm = factorAlgorithm;
	}

	/**
	 * Returns the factors list of the given polynomial. See the documentation for
	 * details.
	 */
	public List<Polynomial<FiniteFieldElement>> factor(Polynomial<FiniteFieldElement> f) {
		switch (factorAlgorithm) {
		case CANTOR:
			return cantor(f);
		case BERLEKAMP:
			return berlekamp(f);
		default:
			return null;
		}
	}

	public List<Polynomial<FiniteFieldElement>> cantor(Polynomial<FiniteFieldElement> f) {
		return null;
	}

	public List<Polynomial<FiniteFieldElement>> berlekamp(Polynomial<FiniteFieldElement> f) {
		return null;
	}
}
