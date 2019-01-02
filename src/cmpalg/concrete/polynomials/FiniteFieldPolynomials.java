package cmpalg.concrete.polynomials;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import cmpalg.generic.finiteFields.FiniteField;
import cmpalg.generic.finiteFields.FiniteFieldElement;
import structures.concrete.euclideanDomains.Integers;
import structures.generic.matrixes.FieldMatrixes;
import structures.generic.matrixes.Matrix;
import structures.generic.polynomials.FieldPolynomials;
import structures.generic.polynomials.Polynomial;
import utils.Pair;

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

	/**
	 * Returns true if the given polynomial is irreducible, and false otherwise. See
	 * the documentation for details.
	 */
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

	private Polynomial<E> computePower(String binaryN, List<Polynomial<E>> powers, Polynomial<E> f) {
		Polynomial<E> tqn = parseElement("t");

		for (int i = binaryN.length() - 1; i >= 0; i--) {
			if (binaryN.charAt(i) == '1') {
				tqn = modularComposition(f, powers.get(binaryN.length() - 1 - i), tqn);
			}
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
	public List<Pair<Polynomial<E>, Integer>> factor(Polynomial<E> f) {
		switch (factorAlgorithm) {
		case CANTOR:
			return cantor(f);
		case BERLEKAMP:
			return berlekamp(f);
		default:
			return null;
		}
	}

	public String printFactorization(List<Pair<Polynomial<E>, Integer>> f) {
		String result = "";
		for (int i = 0; i < f.size(); i++) {
			result = result + "(" + f.get(i).getFirst() + ")^" + f.get(i).getSecond() + "\n";
		}
		return result;
	}

	public String printFactorizationCheck(List<Pair<Polynomial<E>, Integer>> f) {
		Polynomial<E> result = getProductIdentity();

		for (int i = 0; i < f.size(); i++) {
			result = multiply(result,
					power(f.get(i).getFirst(), new BigInteger(Integer.toString(f.get(i).getSecond()))));
		}
		return result.toString();
	}

	public List<Pair<Polynomial<E>, Integer>> cantor(Polynomial<E> f) {
		List<Pair<Polynomial<E>, Integer>> factorization = new ArrayList<Pair<Polynomial<E>, Integer>>();

		/* Gets the square free decomposition. */
		List<Pair<Polynomial<E>, Integer>> sfd = squareFreeDecomposition(f);
		for (int i = 0; i < sfd.size(); i++) {
			/* For each square free factor, performs the distinct degree factorization. */
			List<Polynomial<E>> ddf = distinctDegreeFactorization(sfd.get(i).getFirst());
			/* For each distinct degree factor, gets the equal degree factorization. */
			for (int j = 0; j < ddf.size(); j++) {
				if (!ddf.get(j).equals(getProductIdentity())) {
					List<Polynomial<E>> edf = equalDegreeFactorization(ddf.get(j), j + 1);
					/* Append each true factor to the list. */
					for (int k = 0; k < edf.size(); k++) {
						factorization.add(new Pair<Polynomial<E>, Integer>(edf.get(k), sfd.get(i).getSecond()));
					}
				}
			}
		}
		return factorization;
	}

	public List<Pair<Polynomial<E>, Integer>> berlekamp(Polynomial<E> f) {
		List<Pair<Polynomial<E>, Integer>> factorization = new ArrayList<Pair<Polynomial<E>, Integer>>();

		/* Gets the square free decomposition. */
		List<Pair<Polynomial<E>, Integer>> sfd = squareFreeDecomposition(f);
		for (int i = 0; i < sfd.size(); i++) {
			List<Polynomial<E>> b = berlekampDecomposition(sfd.get(i).getFirst());
			for (int j = 0; j < b.size(); j++) {
				factorization.add(new Pair<Polynomial<E>, Integer>(b.get(j), sfd.get(i).getSecond()));
			}
		}
		return factorization;
	}

	private List<Polynomial<E>> berlekampDecomposition(Polynomial<E> f) {
		List<Polynomial<E>> result = new ArrayList<Polynomial<E>>();
		Stack<Polynomial<E>> toSplit = new Stack<Polynomial<E>>();
		toSplit.add(f);
		while (!toSplit.isEmpty()) {
			Polynomial<E> g = toSplit.pop();

			FieldMatrixes<E> MFq = new FieldMatrixes<E>(Fq);
			List<Polynomial<E>> B = MFq.hermite(MFq.transpose(buildBerlekampMatrix(g)));

			Polynomial<E> p;
			do {
				p = berlekampSplit(g, B);
			} while (p == null);
			if (p.equals(g)) {
				result.add(g);
			} else {
				toSplit.add(p);
				toSplit.add(quotient(g, p));
			}
		}
		return result;
	}

	/** Returns a proper factor of the given polynomial or null. */
	private Polynomial<E> berlekampSplit(Polynomial<E> f, List<Polynomial<E>> B) {
		if (B.size() == 1) {
			return f;
		}

		Polynomial<E> a = getAddIdentity();
		for (int j = 0; j < B.size(); j++) {
			E randomElement = Fq.getRandomElement();
			a = add(a, multiply(B.get(j), randomElement));
		}

		Polynomial<E> b = getMagicalPolynomial(a, f, 1);
		Polynomial<E> g = gcd(add(b, getAddInverse(getProductIdentity())), f);

		if (!g.equals(getProductIdentity()) && !g.equals(f)) {
			return g;
		}
		return null;
	}

	private Matrix<E> buildBerlekampMatrix(Polynomial<E> f) {
		List<Polynomial<E>> M = new ArrayList<Polynomial<E>>();
		Polynomial<E> tq = power(parseElement("t"), Fq.getOrder(), f);
		M.add(getProductIdentity());
		M.add(tq);
		for (int i = 2; i < f.degree(); i++) {
			M.add(remainder(multiply(M.get(i - 1), tq), f));
		}
		List<List<E>> coefficients = new ArrayList<List<E>>();
		for (int i = 0; i < f.degree(); i++) {
			List<E> c = M.get(i).getCoefficients();
			int toFill = f.degree() - c.size();
			for (int k = 0; k < toFill; k++) {
				c.add(Fq.getAddIdentity());
			}
			coefficients.add(c);
		}

		for (int i = 0; i < coefficients.size(); i++) {
			coefficients.get(i).set(i, Fq.add(coefficients.get(i).get(i), Fq.getAddInverse(Fq.getProductIdentity())));
		}
		return new Matrix<E>(coefficients);
	}

	/**
	 * Computes the square free decomposition of the given polynomial. Returns a
	 * list of pairs. The first element of the pair is a factor of the given
	 * polynomial, and the second, its multiplicity. See the documentation for
	 * details.
	 */
	public List<Pair<Polynomial<E>, Integer>> squareFreeDecomposition(Polynomial<E> f) {
		List<Pair<Polynomial<E>, Integer>> result = new ArrayList<Pair<Polynomial<E>, Integer>>();
		int s = 1;
		do {
			int j = 1;
			Polynomial<E> g = quotient(f, gcd(f, derivative(f)));
			while (!g.equals(getProductIdentity())) {
				f = quotient(f, g);
				Polynomial<E> h = gcd(f, g);
				Polynomial<E> m = quotient(g, h);
				if (!m.equals(getProductIdentity())) {
					result.add(new Pair<Polynomial<E>, Integer>(m, j * s));
				}
				g = h;
				j++;
			}
			if (!f.equals(getProductIdentity())) {
				f = pthRoot(f);
				s *= Fq.getCharacteristic().intValue();
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
	 * Computes the distinct degree factorization of the given polynomial. Returns a
	 * list of polynomials, it is guaranteed that the i-th element of the list is a
	 * polynomial such that all its factors have degree i+1. See the documentation
	 * for details.
	 */
	public List<Polynomial<E>> distinctDegreeFactorization(Polynomial<E> f) {
		List<Polynomial<E>> result = new ArrayList<Polynomial<E>>();
		Polynomial<E> h = remainder(parseElement("t"), f);
		Polynomial<E> g = f;
		int i = 0;
		do {
			i++;
			h = power(h, Fq.getOrder(), f);
			Polynomial<E> t = gcd(add(h, getAddInverse(parseElement("t"))), g);
			result.add(t);
			g = quotient(g, t);
		} while (g.degree() >= 2 * (i + 1));
		if (!g.equals(getProductIdentity())) {
			while (result.size() < g.degree() - 1) {
				result.add(getProductIdentity());
			}
			result.add(g);
		}
		return result;
	}

	/**
	 * Computes the equal degree factorization of the given polynomial, knowing that
	 * each factor has the specified degree. See the documentation for details.
	 */
	public List<Polynomial<E>> equalDegreeFactorization(Polynomial<E> f, int k) {
		List<Polynomial<E>> result = new ArrayList<Polynomial<E>>();

		Stack<Polynomial<E>> toSplit = new Stack<Polynomial<E>>();
		if (f.degree() == k) {
			result.add(f);
		} else {
			toSplit.add(f);
		}

		while (!toSplit.isEmpty()) {
			Polynomial<E> g = toSplit.pop();
			Polynomial<E> p;
			do {
				p = equalDegreeSplit(g, k);
			} while (p == null);
			if (p.degree() == k) {
				result.add(p);
			} else {
				toSplit.add(p);
			}
			Polynomial<E> h = quotient(g, p);
			if (h.degree() == k) {
				result.add(h);
			} else {
				toSplit.add(h);
			}
		}
		return result;
	}

	private Polynomial<E> equalDegreeSplit(Polynomial<E> f, int k) {
		Polynomial<E> a = getRandomMonicPolynomial(0, f.degree());
		if (a.degree() == 0) {
			return null;
		}
		Polynomial<E> g = gcd(a, f);
		if (!g.equals(getProductIdentity())) {
			return g;
		}
		Polynomial<E> b = getMagicalPolynomial(a, f, k);
		Polynomial<E> h = gcd(add(b, getAddInverse(getProductIdentity())), f);
		if (!h.equals(getProductIdentity()) && !h.equals(f)) {
			return h;
		}
		return null;
	}

	private Polynomial<E> getMagicalPolynomial(Polynomial<E> a, Polynomial<E> f, int k) {
		if (Fq.getCharacteristic().equals(new BigInteger("2"))) {
			int bound = 2 * ((Fq.getOrder().toString(2).length() - 1) * k) - 1;
			Polynomial<E> b = getAddIdentity();
			Polynomial<E> aux = getProductIdentity();
			for (int i = 0; i < bound; i++) {
				aux = multiply(aux, a);
				b = add(b, aux);
			}
			return b;
		} else {
			Integers Z = new Integers();
			return power(a, Z.quotient(Z.add(Z.power(Fq.getOrder(), new BigInteger(Integer.toString(k))),
					Z.getAddInverse(Z.getAddIdentity())), new BigInteger("2")), f);
		}
	}

	/** The maximum is exclusive and minimum is inclusive. */
	public Polynomial<E> getRandomMonicPolynomial(int minDegree, int maxDegree) {
		int n = ThreadLocalRandom.current().nextInt(minDegree, maxDegree);
		List<E> coefficients = new ArrayList<E>();
		for (int i = 0; i < n; i++) {
			coefficients.add(Fq.getRandomElement());
		}
		coefficients.add(Fq.getProductIdentity());
		return new Polynomial<E>(coefficients, Fq);
	}
}
