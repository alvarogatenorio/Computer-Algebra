package structures.concrete.euclideanDomains;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cmpalg.generic.basic.EuclideanDomain;
import cmpalg.generic.finiteFields.PrimeField;
import cmpalg.generic.finiteFields.PrimeFieldElement;
import structures.generic.polynomials.Polynomial;
import structures.generic.polynomials.Polynomials;

/** Represents the euclidean domain of integer numbers (Z). */
public class Integers extends EuclideanDomain<BigInteger> {
	@Override
	public BigInteger getAddIdentity() {
		return BigInteger.ZERO;
	}

	@Override
	public BigInteger getAddInverse(BigInteger a) {
		return a.negate();
	}

	@Override
	public BigInteger quotient(BigInteger a, BigInteger b) {
		return a.divide(b);
	}

	@Override
	public BigInteger remainder(BigInteger a, BigInteger b) {
		return a.mod(b);
	}

	@Override
	public BigInteger add(BigInteger a, BigInteger b) {
		return a.add(b);
	}

	@Override
	public BigInteger multiply(BigInteger a, BigInteger b) {
		return a.multiply(b);
	}

	@Override
	public BigInteger getProductIdentity() {
		return BigInteger.ONE;
	}

	@Override
	public BigInteger parseElement(String s) {
		return new BigInteger(s);
	}

	@Override
	public boolean divides(BigInteger a, BigInteger b) {
		return b.mod(a).equals(BigInteger.ZERO);
	}

	@Override
	public BigInteger divFactor(BigInteger a, BigInteger b) {
		return b.divide(a);
	}

	@Override
	public BigInteger intMultiply(BigInteger a, BigInteger k) {
		return a.multiply(k);
	}

	/**
	 * Returns true if a is prime and false otherwise. See the documentation for
	 * details.
	 */
	public boolean isPrime(BigInteger a) {
		/* Agrawal-Kayal-Saxena (AKS) algorithm. */
		if (perfectPowerTest(a)) {
			return false;
		}
		BigInteger r = findR(a);
		if (r.equals(a)) {
			return true;
		}
		if (gcd(a, r).compareTo(getProductIdentity()) == 1) {
			return false;
		}

		BigInteger bound = multiply(new BigInteger("2"),
				multiply(new BigInteger(Integer.toString(a.toString(2).length())), sqrtFloor(r))).add(BigInteger.ONE);

		/*
		 * A little trick just to save us from implementing the modular integers in
		 * general.
		 */
		Polynomials<PrimeFieldElement> ZNT = new Polynomials<PrimeFieldElement>(new PrimeField(a));
		Polynomial<PrimeFieldElement> T = ZNT.parseElement("t");
		Polynomial<PrimeFieldElement> mod = ZNT.add(ZNT.power(T, r),
				new PrimeFieldElement(getAddInverse(getAddIdentity())));
		for (BigInteger i = BigInteger.ZERO; i.compareTo(bound) <= 0; i = i.add(BigInteger.ONE)) {
			Polynomial<PrimeFieldElement> aux1 = ZNT.add(ZNT.power(T, a), new PrimeFieldElement(i.mod(a)));
			Polynomial<PrimeFieldElement> aux2 = ZNT.power(ZNT.add(T, new PrimeFieldElement(i)), a);
			if (!ZNT.pseudoDivision(aux1, mod).getThird().equals(ZNT.pseudoDivision(aux2, mod).getThird())) {
				return false;
			}
		}
		return true;
	}

	private BigInteger findR(BigInteger n) {

		BigInteger r = BigInteger.ONE.add(BigInteger.ONE);
		BigInteger bound = multiply(
				power(new BigInteger(Integer.toString(n.toString(2).length())), new BigInteger("2")),
				new BigInteger("4"));

		while (r.compareTo(n) <= 0) {
			if (gcd(n, r).compareTo(getProductIdentity()) == 1) {
				return r;
			} else {
				BigInteger aux = remainder(n, r);
				BigInteger order = getProductIdentity();
				while (!aux.equals(getProductIdentity())) {
					aux = remainder(multiply(aux, n), r);
					order = order.add(BigInteger.ONE);
				}
				if (order.compareTo(bound) == 1) {
					return r;
				}
			}
			r = add(r, getProductIdentity());
		}

		/* This should never be reached. */
		return null;
	}

	/** Returns a list with the prime factors of the given number. */
	public List<BigInteger> factor(BigInteger a) {
		/* Just a really dumb algorithm. */
		ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
		BigInteger aux = a;
		for (BigInteger i = new BigInteger("2"); i.compareTo(a) <= 0; i = i.add(BigInteger.ONE)) {
			if (aux.mod(i).equals(BigInteger.ZERO)) {
				factors.add(i);
				aux = aux.divide(i);
			}
			while (aux.mod(i).equals(BigInteger.ZERO)) {
				aux = aux.divide(i);
			}
		}
		return factors;
	}

	public BigInteger sqrtFloor(BigInteger a) {
		BigInteger k = new BigInteger(Integer.toString(((a.toString(2).length() - 1) / 2)));

		BigInteger two = BigInteger.ONE.add(BigInteger.ONE);
		BigInteger result = power(two, k);
		for (BigInteger i = k.subtract(BigInteger.ONE); i.compareTo(BigInteger.ZERO) >= 0; i = i
				.subtract(BigInteger.ONE)) {
			BigInteger aux = add(result, power(two, i));
			if (power(aux, two).compareTo(a) <= 0) {
				result = aux;
			}
		}
		return result;
	}

	public BigInteger nthrootFloor() {
		return null;
	}

	/**
	 * Returns true if the given number is a perfect power. See the documentation
	 * for details.
	 */
	public boolean perfectPowerTest(BigInteger n) {
		int bound = n.toString(2).length();
		BigInteger two = BigInteger.ONE.add(BigInteger.ONE);
		for (int b = 2; b < bound; b++) {
			/* Binary search over the base. */
			BigInteger left = BigInteger.ONE;
			BigInteger right = n;
			BigInteger middle = quotient(add(left, right), two);
			while (left.compareTo(right) <= 0) {
				BigInteger p = power(middle, new BigInteger(Integer.toString(b)));
				int compare = p.compareTo(n);
				if (compare == 0) {
					return true;
				} else if (compare == -1) {
					left = middle.add(BigInteger.ONE);
					middle = quotient(add(left, right), two);
				} else {
					right = middle.subtract(BigInteger.ONE);
					middle = quotient(add(left, right), two);
				}
			}
		}
		return false;
	}
}
