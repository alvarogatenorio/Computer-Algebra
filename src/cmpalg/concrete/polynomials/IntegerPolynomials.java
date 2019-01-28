package cmpalg.concrete.polynomials;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
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
	public List<Polynomial<Rational>> factor(Polynomial<BigInteger> f) {
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

	public List<Polynomial<Rational>> kronecker(Polynomial<BigInteger> f) {
		FieldPolynomials<Rational> QT = new FieldPolynomials<Rational>(new Rationals());
		Polynomial<Rational> h = rationalize(f);
		List<Polynomial<Rational>> result = new ArrayList<Polynomial<Rational>>();
		Stack<Polynomial<BigInteger>> s = new Stack<Polynomial<BigInteger>>();
		s.push(f);
		while (!s.empty()) {
			Polynomial<BigInteger> aux = s.peek();
			Polynomial<Rational> g = kroneckerSplit(s.pop());
			result.add(g);
			if (!rationalize(aux).equals(g)) {
				h = QT.quotient(h, g);
				s.push(integerize(h));
				s.push(integerize(g));
			}
		}
		return result;
	}

	public Polynomial<Rational> kroneckerSplit(Polynomial<BigInteger> f) {
		/* Auxiliary stuff. */
		HashMap<BigInteger, BigInteger> blackList = new HashMap<BigInteger, BigInteger>();
		Integers Z = new Integers();
		int bound = f.degree() / 2;
		FieldPolynomials<Rational> QT = new FieldPolynomials<Rational>(new Rationals());

		/* Choose these different and at random. */
		BigInteger a0 = getRandomNumber(blackList);
		blackList.put(a0, null);
		BigInteger a1 = getRandomNumber(blackList);
		blackList.put(a1, null);

		/* Check if we are lucky. */
		if (f.evaluate(a0).equals(Z.getAddIdentity())) {
			Rational aux = new Rational(a0);
			return QT.parseElement("t+-" + aux);
		}
		if (f.evaluate(a1).equals(Z.getAddIdentity())) {
			Rational aux = new Rational(a1);
			return QT.parseElement("t+-" + aux);
		}

		/* Initializing loop stuff. */
		List<BigInteger> as = new ArrayList<BigInteger>();
		as.add(f.evaluate(a0));
		as.add(f.evaluate(a1));

		List<List<BigInteger>> M = new ArrayList<List<BigInteger>>();
		M.add(Z.divisors(as.get(0)));

		/* Try every single degree. */
		for (int e = 1; e <= bound; e++) {
			List<BigInteger> Me = Z.divisors(as.get(as.size() - 1));
			M = cartesianProduct(M, Me);
			List<List<BigInteger>> Mp = M;

			/* Try every single divisor. */
			while (Mp.size() > 0) {
				Mp.get(Mp.size() - 1);
				/* Mount and solve the system over Q. */

				/* Mount g. */
				Polynomial<Rational> g = null;
				if (QT.divides(g, rationalize(f))) {
					return g;
				}

				/* Discarding the divisor election. */
				Mp.remove(Mp.get(Mp.size() - 1));
			}

			/* Choosing another integer. */
			BigInteger aee = getRandomNumber(blackList);
			blackList.put(aee, null);
			as.add(f.evaluate(aee));
		}

		return rationalize(f);
	}

	private BigInteger getRandomNumber(HashMap<BigInteger, BigInteger> blackList) {
		Random random = new Random();
		BigInteger r;
		do {
			r = new BigInteger(10, random);
		} while (blackList.containsKey(r));
		return r;
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
	public List<Polynomial<Rational>> modular(Polynomial<BigInteger> f) {

		BigInteger p = choosePrime(f);

		BigInteger N = computeN(f, p);

		PrimeField Zp = new PrimeField(p);
		FiniteFieldPolynomials<PrimeFieldElement> ZpT = new FiniteFieldPolynomials<PrimeFieldElement>(Zp);
		Polynomial<PrimeFieldElement> ff = modularize(f, Zp);
		List<Pair<Polynomial<PrimeFieldElement>, Integer>> factors = ZpT
				.factor(ZpT.multiply(ff, Zp.getProductInverse(ff.leading())));

		Integers Z = new Integers();
		List<Polynomial<BigInteger>> l;
		List<Polynomial<BigInteger>> fact = new ArrayList<Polynomial<BigInteger>>();
		for (int i = 0; i < factors.size(); i++) {
			fact.add(modularize(toIntegerPol(factors.get(i).getFirst()), p));
		}
		l = multilift(p, fact, f, N);

		Polynomial<BigInteger> product = getProductIdentity();
		for (int i = 0; i < l.size(); i++) {
			product = multiply(product, l.get(i));
		}
		System.out.println("FINAL TEST");
		System.out.println(
				modularize(f, Z.power(p, N)).equals(modularize(intMultiply(product, f.leading()), Z.power(p, N))));
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
		// return new BigInteger("41");
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

	/* sg+th cong 1 mod q */
	private List<Polynomial<BigInteger>> lift(Polynomial<BigInteger> f, Polynomial<BigInteger> g,
			Polynomial<BigInteger> h, Polynomial<BigInteger> s, Polynomial<BigInteger> t, BigInteger q2) {

		// Delta = f-gh
		Polynomial<BigInteger> Delta = modularize(add(f, multiply(getAddInverse(g), h)), q2);
		// gg = g(1+(sDelta/h))+tDelta
		Polynomial<BigInteger> gg = modularize(
				add(multiply(g, (add(getProductIdentity(), pseudoDivision(multiply(s, Delta), h).getSecond()))),
						multiply(t, Delta)),
				q2);
		// hh=h+(sDelta rem h)
		Polynomial<BigInteger> hh = modularize(add(h, (pseudoDivision(multiply(s, Delta), h).getThird())), q2);

		// delta = sgg+thh-1
		Polynomial<BigInteger> delta = modularize(
				add(add(multiply(s, gg), multiply(t, hh)), getAddInverse(getProductIdentity())), q2);
		// ss = s-(sdelta rem hh)
		Polynomial<BigInteger> ss = modularize(
				add(s, getAddInverse((pseudoDivision(multiply(s, delta), hh).getThird()))), q2);
		// tt = (1-delta)t-gg(sdelta/hh)
		Polynomial<BigInteger> tt = modularize(add(multiply(add(getProductIdentity(), getAddInverse(delta)), t),
				getAddInverse(multiply(gg, (pseudoDivision(multiply(s, delta), hh).getSecond())))), q2);

		List<Polynomial<BigInteger>> result = new ArrayList<Polynomial<BigInteger>>();
		result.add(modularize(gg, q2));
		result.add(modularize(hh, q2));
		result.add(modularize(ss, q2));
		result.add(modularize(tt, q2));

		return result;
	}

	private List<Polynomial<BigInteger>> multilift(BigInteger p, List<Polynomial<BigInteger>> factors,
			Polynomial<BigInteger> f, BigInteger l) {

		Integers Z = new Integers();
		List<Polynomial<BigInteger>> result = new ArrayList<Polynomial<BigInteger>>();

		int r = factors.size();

		/* Easy case... */
		if (r == 1) {
			// calcular el inverso de leading
			BigInteger pl = Z.power(p, l);
			BigInteger aux = Z.gcd(f.leading(), pl);
			if (aux.equals(Z.getProductIdentity())) {
				result.add(modularize(intMultiply(f, Z.bezout(f.leading(), pl).getFirst()), pl));
			} else {
				System.out.println("!!!");
				result.add(f);
			}
			return result;
		}

		int k = r / 2;
		BigInteger d = new BigInteger(Integer.toString(l.toString(2).length()));

		// computar el producto de la mitad1
		Polynomial<BigInteger> g = getProductIdentity();
		for (int i = 0; i < k; i++) {
			g = modularize(multiply(g, factors.get(i)), p);
		}
		g = modularize(intMultiply(g, f.leading()), p);

		// computar el producto de la mitad2
		Polynomial<BigInteger> h = getProductIdentity();
		for (int i = k; i < factors.size(); i++) {
			h = modularize(multiply(h, factors.get(i)), p);
		}

		// computar los coeficientillos
		PrimeField Zp = new PrimeField(p);
		FiniteFieldPolynomials<PrimeFieldElement> ZpT = new FiniteFieldPolynomials<PrimeFieldElement>(Zp);
		Pair<Polynomial<PrimeFieldElement>, Polynomial<PrimeFieldElement>> bez = ZpT.bezout(modularize(g, Zp),
				modularize(h, Zp));
		Polynomial<BigInteger> s = modularize(toIntegerPol(bez.getFirst()), p);
		Polynomial<BigInteger> t = modularize(toIntegerPol(bez.getSecond()), p);

		// computar la elevacion
		for (BigInteger j = BigInteger.ONE; j.compareTo(d) <= 0; j = j.add(BigInteger.ONE)) {
			BigInteger q = Z.power(p, Z.power(new BigInteger("2"), j.subtract(BigInteger.ONE)));
			List<Polynomial<BigInteger>> lifting = lift(f, g, h, s, t, q);
			g = lifting.get(0);
			h = lifting.get(1);
			s = lifting.get(2);
			t = lifting.get(3);
		}

		// llamada reursiva1
		result.addAll(multilift(p, factors.subList(0, k), g, l));

		// llamada recursiva2
		result.addAll(multilift(p, factors.subList(k, factors.size()), h, l));
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

	private List<Polynomial<Rational>> trueFactors(Polynomial<BigInteger> f, List<Polynomial<BigInteger>> factors,
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

				if (divides(intMultiply(h, f.leading()), product)) {
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
			}
			d++;
		}

		if (h.degree() > 0) {
			result.add(primitivePart(h, content(h)));
		}

		List<Polynomial<Rational>> finalResult = new ArrayList<Polynomial<Rational>>();
		for (int i = 0; i < result.size(); i++) {
			finalResult.add(rationalize(result.get(i)));
		}
		return finalResult;
	}

	private Polynomial<Rational> rationalize(Polynomial<BigInteger> f) {
		List<BigInteger> coeff = f.getCoefficients();
		List<Rational> result = new ArrayList<Rational>();
		for (int i = 0; i < coeff.size(); i++) {
			result.add(new Rational(coeff.get(i)));
		}
		return new Polynomial<Rational>(result, new Rationals());
	}

	private Polynomial<PrimeFieldElement> modularize(Polynomial<BigInteger> f, PrimeField Zp) {
		Integers Z = new Integers();
		List<BigInteger> old = f.getCoefficients();
		List<PrimeFieldElement> result = new ArrayList<PrimeFieldElement>();
		for (int i = 0; i < old.size(); i++) {
			result.add(new PrimeFieldElement(Z.remainder(old.get(i), Zp.getCharacteristic())));
		}
		for (int i = old.size() - 1; i > 0; i--) {
			if (result.get(i).equals(Zp.getAddIdentity())) {
				result.remove(result.size() - 1);
			} else {
				break;
			}
		}
		return new Polynomial<PrimeFieldElement>(result, Zp);
	}

	private Polynomial<BigInteger> modularize(Polynomial<BigInteger> f, BigInteger q) {
		Integers Z = new Integers();
		List<BigInteger> old = f.getCoefficients();
		List<BigInteger> result = new ArrayList<BigInteger>();
		for (int i = 0; i < old.size(); i++) {
			BigInteger rep = Z.remainder(old.get(i), q);
			if (rep.compareTo(Z.add(Z.quotient(q, new BigInteger("2")), BigInteger.ONE)) >= 0) {
				rep = Z.add(rep, Z.getAddInverse(q));
			}
			result.add(rep);
		}
		for (int i = old.size() - 1; i > 0; i--) {
			if (result.get(i).equals(Z.getAddIdentity())) {
				result.remove(result.size() - 1);
			} else {
				break;
			}
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

	@SuppressWarnings("unused")
	private List<List<Rational>> rationalize(List<List<BigInteger>> a) {
		List<List<Rational>> result = new ArrayList<List<Rational>>();
		for (int i = 0; i < a.size(); i++) {
			result.add(new ArrayList<Rational>());
			for (int j = 0; j < a.get(i).size(); j++) {
				result.get(i).add(new Rational(a.get(i).get(j)));
			}
		}
		return result;
	}

	private Polynomial<BigInteger> integerize(Polynomial<Rational> a) {
		Integers Z = new Integers();
		List<Rational> coeffs = a.getCoefficients();
		BigInteger k = Z.getProductIdentity();
		List<BigInteger> result = new ArrayList<BigInteger>();
		for (int i = 0; i < coeffs.size(); i++) {
			k = Z.multiply(k, coeffs.get(i).getDenominator());
		}
		for (int i = 0; i < coeffs.size(); i++) {
			result.add(Z.quotient(Z.multiply(coeffs.get(i).getNumerator(), k), coeffs.get(i).getDenominator()));
		}
		return new Polynomial<BigInteger>(result, Z);
	}
}
