package structures.concrete;

import java.math.BigInteger;
import java.util.ArrayList;
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
	
	
	public boolean isIrreducible(Polynomial<FiniteFieldElement> f) {
		BigInteger n = new BigInteger(Integer.toString(f.degree()));
		BigInteger q = baseField.getOrder();

		Integers Z = new Integers();
		List<BigInteger> factors = Z.factor(n);

		Polynomial<FiniteFieldElement> tq = modularPower(parseElement("t"), q, f);
		String binaryN = n.toString(2);
		List<Polynomial<FiniteFieldElement>> powers = computePowersOfTwo(binaryN.length(), tq, f);
		Polynomial<FiniteFieldElement> tqn = computePower(binaryN, powers, f);

		if (tqn.equals(remainder(parseElement("t"), f))) {
			for (int i = 0; i < factors.size(); i++) {
				Polynomial<FiniteFieldElement> b = computePower(n.divide(factors.get(i)).toString(2), powers, f);
				if (!gcd(add(b, parseElement("-1t")), f).equals(getProductIdentity())) {
					return false;
				}
			}
			return true;
		}
		
		return false;
	}
	
	/*revisar*/
	private Polynomial<FiniteFieldElement> computePower(String binaryN, List<Polynomial<FiniteFieldElement>> powers,
			Polynomial<FiniteFieldElement> f) {
		Polynomial<FiniteFieldElement> tqn = powers.get(0);
		for (int i = 1; i < binaryN.length(); i++) {
			tqn = modularComposition(f, tqn, powers.get(i));
		}
		return tqn;
	}

	private List<Polynomial<FiniteFieldElement>> computePowersOfTwo(int k, Polynomial<FiniteFieldElement> tq,
			Polynomial<FiniteFieldElement> f) {
		List<Polynomial<FiniteFieldElement>> powers = new ArrayList<Polynomial<FiniteFieldElement>>();
		
		Polynomial<FiniteFieldElement> aux = tq;
		powers.add(aux);
		for (int i = 0; i < k; i++) {
			aux = modularComposition(f, aux, aux);
			powers.add(aux);
		}
		
		return powers;
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
