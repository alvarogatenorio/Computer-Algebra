package structures.concrete.polynomials;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cmpalg.generic.finiteFields.FiniteField;
import cmpalg.generic.finiteFields.FiniteFieldElement;
import structures.concrete.euclideanDomains.Integers;
import structures.generic.polynomials.FieldPolynomials;
import structures.generic.polynomials.Polynomial;

/** Represents the set of polynomials over Fq (Fq[t]). */
public class FiniteFieldPolynomials<E extends FiniteFieldElement> extends FieldPolynomials<E> {

	private FiniteField<E> Fq;

	public FiniteFieldPolynomials(FiniteField<E> Fq) {
		super(Fq);
		this.Fq = Fq;
	}

	public enum FactorAlgorithm {
		CANTOR, BERLEKAMP
	};

	private FactorAlgorithm factorAlgorithm = FactorAlgorithm.CANTOR;

	public boolean isIrreducible(Polynomial<E> f) {
		BigInteger n = new BigInteger(Integer.toString(f.degree()));
		BigInteger q = Fq.getOrder();

		Integers Z = new Integers();
		List<BigInteger> factors = Z.factor(n);

		Polynomial<E> tq = power(parseElement("t"), q, f);
		String binaryN = n.toString(2);
		List<Polynomial<E>> powers = computePowersOfTwo(binaryN.length(), tq, f);
		Polynomial<E> tqn = computePower(binaryN, powers, f);

		if (tqn.equals(remainder(parseElement("t"), f))) {
			for (int i = 0; i < factors.size(); i++) {
				Polynomial<E> b = computePower(n.divide(factors.get(i)).toString(2), powers, f);
				if (!gcd(add(b, parseElement("-1t")), f).equals(getProductIdentity())) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	/* revisar */
	private Polynomial<E> computePower(String binaryN, List<Polynomial<E>> powers, Polynomial<E> f) {
		Polynomial<E> tqn = powers.get(0);
		for (int i = 1; i < binaryN.length(); i++) {
			tqn = modularComposition(f, tqn, powers.get(i));
		}
		return tqn;
	}

	private List<Polynomial<E>> computePowersOfTwo(int k, Polynomial<E> tq, Polynomial<E> f) {
		List<Polynomial<E>> powers = new ArrayList<Polynomial<E>>();

		Polynomial<E> aux = tq;
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
	public List<Polynomial<E>> factor(Polynomial<E> f) {
		switch (factorAlgorithm) {
		case CANTOR:
			return cantor(f);
		case BERLEKAMP:
			return berlekamp(f);
		default:
			return null;
		}
	}

	public List<Polynomial<E>> cantor(Polynomial<E> f) {
		return null;
	}

	public List<Polynomial<E>> berlekamp(Polynomial<E> f) {
		return null;
	}

	/**
	 * Computes the square free decomposition of the given polynomial. See the
	 * documentation for details.
	 */
	public List<Polynomial<E>> squareFreeDecomposition(Polynomial<E> f) {
		List<Polynomial<E>> result = new ArrayList<Polynomial<E>>();
		do {
			Polynomial<E> g = quotient(f, gcd(f, derivative(f)));
			while (!g.equals(getProductIdentity())) {
				f = quotient(f, g);
				Polynomial<E> h = gcd(f, g);
				Polynomial<E> m = quotient(g, h);
				if (!m.equals(getProductIdentity())) {
					result.add(m);
				}
				g = h;
			}
			if (!f.equals(getProductIdentity())) {
				f = pthRoot(f);
			}
		} while (!f.equals(getProductIdentity()));
		return result;
	}

	/**
	 * Computes the exact p-th root of the given polynomial, being p the
	 * characteristic of the field. See the documentation for details.
	 */
	public Polynomial<E> pthRoot(Polynomial<E> f) {
		int p = Fq.getCharacteristic().intValue();
		List<E> root = new ArrayList<E>();
		for (int i = 0; i <= f.degree(); i += p) {
			root.add(f.get(i));
		}
		return new Polynomial<E>(root, Fq);
	}

	/**
	 * Computes the distinct degree decomposition of the given polynomial. See the
	 * documentation for details.
	 */
	public List<Polynomial<E>> distinctDegreeFactorization(Polynomial<E> f) {
		List<Polynomial<E>> result = new ArrayList<Polynomial<E>>();
		Polynomial<E> h = remainder(parseElement("t"), f);
		Polynomial<E> g = f;
		do {
			h = power(h, Fq.getOrder(), f);
			Polynomial<E> t = gcd(add(h, getAddInverse(parseElement("t"))), g);
			result.add(t);
			g = quotient(g, t);
		} while (!g.equals(getProductIdentity()));
		return result;
	}

}
