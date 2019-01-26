package cmpalg.concrete.polynomials;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cmpalg.generic.basic.UniqueFactorizationDomain;
import cmpalg.generic.finiteFields.PrimeField;
import cmpalg.generic.finiteFields.PrimeFieldElement;
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

	/** The polynomial must be primitive and square free. */
	public List<Polynomial<BigInteger>> modular(Polynomial<BigInteger> f) {
		BigInteger p = choosePrime(f);

		computeN(f, p);

		PrimeField Zp = new PrimeField(p);
		FiniteFieldPolynomials<PrimeFieldElement> ZpT = new FiniteFieldPolynomials<PrimeFieldElement>(Zp);
		ZpT.factor(modularize(f, Zp));

		// elevamos la factorizacion quiza varias veces
		multilift();

		// arrejuntamos
		trueFactors();
		return null;
	}

	private BigInteger choosePrime(Polynomial<BigInteger> f) {
		Integers Z = new Integers();
		BigInteger p;
		PrimeField Zp;
		Polynomial<PrimeFieldElement> ff;
		FiniteFieldPolynomials<PrimeFieldElement> ZpT;
		Polynomial<PrimeFieldElement> dff;
		do {
			p = BigInteger.probablePrime(8, new Random());
			Zp = new PrimeField(p);
			ff = modularize(f, Zp);
			ZpT = new FiniteFieldPolynomials<PrimeFieldElement>(Zp);
			dff = ZpT.derivative(ff);
		} while (!Z.isPrime(p) || dff.equals(ZpT.getAddIdentity())
				|| !ZpT.gcd(ff, dff).equals(ZpT.getProductIdentity()));
		return p;
	}

	private BigInteger computeN(Polynomial<BigInteger> f, BigInteger p) {
		Integers Z = new Integers();
		BigInteger bound = Z.multiply(computeNorm(f),
				Z.power(new BigInteger("2"), Z.add(new BigInteger(Integer.toString(f.degree())), BigInteger.ONE)));

		BigInteger N = Z.getAddIdentity();
		while (bound.compareTo(BigInteger.ZERO) >= 0) {
			bound = Z.quotient(bound, p);
			N = Z.add(N, BigInteger.ONE);
		}

		return N;
	}

	private BigInteger computeNorm(Polynomial<BigInteger> f) {
		Integers Z = new Integers();
		List<BigInteger> coefficients = f.getCoefficients();
		BigInteger norm = Z.getAddIdentity();

		for (int i = 0; i < f.size(); i++) {
			norm = Z.add(norm, Z.power(coefficients.get(i), new BigInteger("2")));
		}

		norm = Z.add(Z.sqrtFloor(norm), BigInteger.ONE);
		return norm;
	}

	private List<Polynomial<BigInteger>> lift(Polynomial<BigInteger> f, Polynomial<BigInteger> g,
			Polynomial<BigInteger> h, Polynomial<BigInteger> t, Polynomial<BigInteger> s) {

		// Delta = f-gh
		Polynomial<BigInteger> Delta = add(f, multiply(getAddInverse(g), h));
		// gg = g(1+(sDelta/h))+tDelta
		Polynomial<BigInteger> gg = add(
				multiply(g, (add(getProductIdentity(), pseudoDivision(multiply(s, Delta), h).getSecond()))),
				multiply(t, Delta));
		// hh=h+(sDelta rem h)
		Polynomial<BigInteger> hh = add(h, (pseudoDivision(multiply(s, Delta), h).getThird()));

		// delta = sgg+thh-1
		Polynomial<BigInteger> delta = add(add(multiply(s, gg), multiply(t, hh)), getAddInverse(getProductIdentity()));
		// ss = s-(sdelta rem hh)
		Polynomial<BigInteger> ss = add(s, getAddInverse((pseudoDivision(multiply(s, delta), hh).getThird())));
		// tt = (1-delta)t-gg(sdelta/hh)
		Polynomial<BigInteger> tt = add(multiply(add(getProductIdentity(), getAddInverse(delta)), t),
				getAddInverse(multiply(gg, (pseudoDivision(multiply(s, delta), hh).getSecond()))));

		List<Polynomial<BigInteger>> result = new ArrayList<Polynomial<BigInteger>>();
		result.add(gg);
		result.add(hh);
		result.add(ss);
		result.add(tt);
		return result;
	}

	private void multilift() {

	}

	private void trueFactors() {

	}

	private Polynomial<PrimeFieldElement> modularize(Polynomial<BigInteger> f, PrimeField Zp) {
		Integers Z = new Integers();
		List<BigInteger> old = f.getCoefficients();
		List<PrimeFieldElement> result = new ArrayList<PrimeFieldElement>();
		for (int i = 0; i < old.size(); i++) {
			result.add(new PrimeFieldElement(Z.remainder(old.get(i), Zp.getCharacteristic())));
		}
		return new Polynomial<PrimeFieldElement>(result, Zp);
	}
}
