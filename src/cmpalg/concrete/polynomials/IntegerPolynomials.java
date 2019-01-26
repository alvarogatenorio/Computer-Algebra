package cmpalg.concrete.polynomials;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import cmpalg.generic.basic.UniqueFactorizationDomain;
import cmpalg.generic.finiteFields.PrimeField;
import cmpalg.generic.finiteFields.PrimeFieldElement;
import structures.concrete.euclideanDomains.Integers;
import structures.concrete.rationals.Rational;
import structures.concrete.rationals.Rationals;
import structures.generic.polynomials.FieldPolynomials;
import structures.generic.polynomials.Polynomial;
import structures.generic.polynomials.UFDPolynomials;
import utils.Pair;
import utils.Triple;

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

		BigInteger N = computeN(f, p);

		PrimeField Zp = new PrimeField(p);
		FiniteFieldPolynomials<PrimeFieldElement> ZpT = new FiniteFieldPolynomials<PrimeFieldElement>(Zp);
		Polynomial<PrimeFieldElement> ff = modularize(f, Zp);
		List<Pair<Polynomial<PrimeFieldElement>, Integer>> factors = ZpT
				.factor(ZpT.multiply(ff, Zp.getProductInverse(ff.leading())));

		List<Polynomial<BigInteger>> l = multilift(factors, ZpT);

		return trueFactors(f, l, p, N);
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
		while (bound.compareTo(BigInteger.ZERO) > 0) {
			bound = Z.quotient(bound, p);
			N = Z.add(N, BigInteger.ONE);
		}

		return Z.add(N, BigInteger.ONE);
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

	/* sg+th cong 1 mod q */
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

	private List<Polynomial<BigInteger>> multilift(List<Pair<Polynomial<PrimeFieldElement>, Integer>> factors,
			FiniteFieldPolynomials<PrimeFieldElement> ZpT) {
		List<Polynomial<BigInteger>> result = new ArrayList<Polynomial<BigInteger>>();

		Polynomial<PrimeFieldElement> g = factors.get(0).getFirst();
		Polynomial<PrimeFieldElement> h = factors.get(1).getFirst();
		Polynomial<PrimeFieldElement> product = ZpT.multiply(g, h);
		Pair<Polynomial<PrimeFieldElement>, Polynomial<PrimeFieldElement>> bez = ZpT.bezout(h, g);

		List<Polynomial<BigInteger>> lifting = lift(toIntegerPol(product), toIntegerPol(g), toIntegerPol(h),
				toIntegerPol(bez.getFirst()), toIntegerPol(bez.getSecond()));

		result.add(lifting.get(0));
		result.add(lifting.get(1));

		for (int i = 2; i < factors.size(); i++) {
			g = product;
			h = factors.get(i).getFirst();
			product = ZpT.multiply(g, h);
			bez = ZpT.bezout(h, g);
			lifting = lift(toIntegerPol(product), toIntegerPol(g), toIntegerPol(h), toIntegerPol(bez.getFirst()),
					toIntegerPol(bez.getSecond()));
			result.add(lifting.get(1));
		}

		return result;
	}

	private Polynomial<BigInteger> toIntegerPol(Polynomial<PrimeFieldElement> f) {
		List<PrimeFieldElement> oldCoeffs = f.getCoefficients();
		List<BigInteger> newCoeffs = new ArrayList<BigInteger>();
		for (int i = 0; i < oldCoeffs.size(); i++) {
			newCoeffs.add(oldCoeffs.get(i).getElement());
		}
		return new Polynomial<BigInteger>(newCoeffs, new Integers());
	}

	private List<Polynomial<BigInteger>> trueFactors(Polynomial<BigInteger> f, List<Polynomial<BigInteger>> factors,
			BigInteger p, BigInteger N) {
		int d = 1;
		Polynomial<BigInteger> h = f;
		List<Polynomial<BigInteger>> result = new ArrayList<Polynomial<BigInteger>>();

		List<Integer> I = new ArrayList<Integer>();
		for (int i = 0; i < factors.size(); i++) {
			I.add(i);
		}

		while (2 * d <= I.size()) {
			int oldD = d;
			List<List<Boolean>> L = subsets(I.size(), d);
			while (!L.isEmpty() && 2 * d <= I.size()) {
				List<Boolean> S = L.get(L.size() - 1);
				L.remove(L.size() - 1);

				Polynomial<BigInteger> product = getProductIdentity();
				for (int i = 0; i < I.size(); i++) {
					if (S.get(i)) {
						product = multiply(product, factors.get(I.get(i)));
					}
				}
				Integers Z = new Integers();
				product = intMultiply(product, f.leading());
				product = modularize(product, Z.power(p, N));

				if (divides(product, intMultiply(h, f.leading()))) {
					BigInteger c = content(product);
					Polynomial<BigInteger> pp = primitivePart(product, c);
					result.add(pp);
					h = pseudoDivision(h, pp).getSecond();

					for (int i = 0; i < I.size(); i++) {
						if (S.get(i)) {
							I.remove(i);
						}
					}

					L = subsets(I.size(), oldD);
				}
				d++;
			}
		}

		if (h.degree() > 0) {
			result.add(h);
		}
		return result;
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

	private Polynomial<BigInteger> modularize(Polynomial<BigInteger> f, BigInteger q) {
		Integers Z = new Integers();
		List<BigInteger> old = f.getCoefficients();
		List<BigInteger> result = new ArrayList<BigInteger>();
		for (int i = 0; i < old.size(); i++) {
			BigInteger rep = Z.remainder(old.get(i), q);
			if (rep.compareTo(Z.add(Z.quotient(q, new BigInteger("2")), BigInteger.ONE)) == 1) {
				rep = Z.add(rep, Z.getAddInverse(q));
			}
			result.add(rep);
		}
		return new Polynomial<BigInteger>(result, Z);
	}

	private List<List<Boolean>> subsets(int n, int d) {
		List<List<Boolean>> result = new ArrayList<List<Boolean>>();
		Stack<Triple<Integer, Integer, List<Boolean>>> s = new Stack<Triple<Integer, Integer, List<Boolean>>>();
		// first we represent the decision
		Triple<Integer, Integer, List<Boolean>> root = new Triple<Integer, Integer, List<Boolean>>(0, 0,
				new ArrayList<Boolean>());
		s.push(root);
		while (!s.isEmpty()) {
			Triple<Integer, Integer, List<Boolean>> node = s.pop();
			List<Boolean> list1 = new ArrayList<Boolean>(node.getThird());
			List<Boolean> list2 = new ArrayList<Boolean>(node.getThird());
			// checking if we have to drop things out
			if (node.getSecond() >= d) {
				int diff = n - node.getThird().size();
				for (int i = 0; i < diff; i++) {
					node.getThird().add(false);
				}
				result.add(node.getThird());
			} else if (node.getFirst() >= n) {
				continue;
			} else {
				// taken
				list1.add(true);
				s.push(new Triple<Integer, Integer, List<Boolean>>(node.getFirst() + 1, node.getSecond() + 1, list1));
				// not taken
				list2.add(false);
				s.push(new Triple<Integer, Integer, List<Boolean>>(node.getFirst() + 1, node.getSecond(), list2));
			}
		}
		return result;
	}

}
