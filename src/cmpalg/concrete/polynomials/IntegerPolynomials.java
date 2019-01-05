package cmpalg.concrete.polynomials;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cmpalg.generic.basic.UniqueFactorizationDomain;
import structures.concrete.euclideanDomains.Integers;
import structures.concrete.rationals.Rational;
import structures.concrete.rationals.Rationals;
import structures.generic.polynomials.FieldPolynomials;
import structures.generic.polynomials.Polynomial;
import structures.generic.polynomials.UFDPolynomials;

public class IntegerPolynomials extends UFDPolynomials<BigInteger> {

	/**
	 * You are allowed to put the UFD you want, this will just ignore it and put the
	 * integers instead.
	 */
	@Deprecated
	public IntegerPolynomials(UniqueFactorizationDomain<BigInteger> baseRing) {
		super(new Integers());
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

	/** Computes the square free part of the given monic polynomial. */
	public Polynomial<BigInteger> squareFree(Polynomial<BigInteger> f) {
		return pseudoDivision(f, gcd(f, derivative(f))).getSecond();
	}

	public List<Polynomial<BigInteger>> kronecker(Polynomial<BigInteger> f) {
		return null;
	}

	public Polynomial<BigInteger> kroneckerSplit(Polynomial<BigInteger> f) {
		/* Auxiliary stuff. */
		Integers Z = new Integers();
		int bound = f.degree() / 2;
		FieldPolynomials<Rational> QT = new FieldPolynomials<Rational>(new Rationals());

		/* Choose these different and at random. */
		BigInteger a0 = null;
		BigInteger a1 = null;

		/* Check if we are lucky. (COMPROBAR LA COMPOSICIÓN MODULAR YA QUE ESTÁS) */

		/* Initializing loop stuff. */
		List<BigInteger> as = new ArrayList<BigInteger>();
		as.add(a0);
		as.add(a1);

		List<List<BigInteger>> M = new ArrayList<List<BigInteger>>();
		M.add(Z.divisors(a0));

		/* Try every single degree. */
		for (int e = 1; e <= bound; e++) {
			List<BigInteger> Me = Z.divisors(as.get(as.size() - 1));
			M = cartesianProduct(M, Me);
			List<List<BigInteger>> Mp = M;

			/* Try every single divisor. */
			while (Mp.size() > 0) {
				Mp.get(Mp.size() - 1);
				/* Solve the system over Q. (MODIFICAR HERMITE) */
				/* Mount g. */
				Polynomial<Rational> g = null;

				/* Discarding the divisor election. */
				Mp.remove(Mp.get(Mp.size() - 1));
			}

			/* Choosing another integer. */
			BigInteger aee = null;
			as.add(aee);
		}

		return null;
	}

	private List<List<BigInteger>> cartesianProduct(List<List<BigInteger>> M, List<BigInteger> Me) {
		List<List<BigInteger>> result = M;
		int k = 0;
		for (int i = 0; i < M.size(); i++) {
			for (int j = 0; j < Me.size(); j++) {
				result.add(M.get(i));
				result.get(k).add(Me.get(j));
				k++;
			}
		}
		return result;
	}

	public List<Polynomial<BigInteger>> modular(Polynomial<BigInteger> f) {
		return null;
	}

}
