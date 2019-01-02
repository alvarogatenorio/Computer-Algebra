package cmpalg.generic.finiteFields;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import structures.concrete.euclideanDomains.Integers;
import structures.generic.polynomials.FieldPolynomials;
import structures.generic.polynomials.Polynomial;

public class PrimePowerField extends FiniteField<PrimePowerFieldElement> {

	private PrimeField Zp;
	private BigInteger p;
	private FieldPolynomials<PrimeFieldElement> ZpX;
	private Polynomial<PrimeFieldElement> f;
	private int tupleSize;

	public PrimePowerField(BigInteger p, Polynomial<PrimeFieldElement> f) {
		Zp = new PrimeField(p);
		this.p = p;
		ZpX = new FieldPolynomials<PrimeFieldElement>(Zp);
		this.f = f;
		tupleSize = f.degree();
	}

	@Override
	public PrimePowerFieldElement getProductInverse(PrimePowerFieldElement a) {
		return new PrimePowerFieldElement(ZpX.remainder(
				ZpX.multiply(ZpX.bezout(a.getElement(), f).getFirst(), Zp.getProductInverse(a.getElement().leading())),
				f), tupleSize);
	}

	@Override
	public PrimePowerFieldElement getAddIdentity() {
		return new PrimePowerFieldElement(ZpX.getAddIdentity(), tupleSize);
	}

	@Override
	public PrimePowerFieldElement getProductIdentity() {
		return new PrimePowerFieldElement(ZpX.getProductIdentity(), tupleSize);
	}

	@Override
	public PrimePowerFieldElement getAddInverse(PrimePowerFieldElement a) {
		return new PrimePowerFieldElement(ZpX.remainder(ZpX.getAddInverse(a.getElement()), f), tupleSize);
	}

	@Override
	public PrimePowerFieldElement add(PrimePowerFieldElement a, PrimePowerFieldElement b) {
		return new PrimePowerFieldElement(ZpX.remainder(ZpX.add(a.getElement(), b.getElement()), f), tupleSize);
	}

	@Override
	public PrimePowerFieldElement multiply(PrimePowerFieldElement a, PrimePowerFieldElement b) {
		return new PrimePowerFieldElement(ZpX.remainder(ZpX.multiply(a.getElement(), b.getElement()), f), tupleSize);
	}

	/**
	 * We will represent an element in a finite field as a n tuple, that is a string
	 * like "(a,...,a)". Being n the degree of the irreducible polynomial.
	 */
	@Override
	public PrimePowerFieldElement parseElement(String s) {
		s = s.replaceAll("[()]", "");
		String[] aux = s.split(",");
		List<PrimeFieldElement> coefficients = new ArrayList<PrimeFieldElement>();

		int i = aux.length - 1;
		while (i >= 0 && aux[i].equals("0")) {
			i--;
		}

		i = i == -1 ? 0 : i;

		for (int j = 0; j <= i; j++) {
			coefficients.add(new PrimeFieldElement(new BigInteger(aux[j])));
		}
		Polynomial<PrimeFieldElement> pol = new Polynomial<PrimeFieldElement>(coefficients, Zp);
		return new PrimePowerFieldElement(ZpX.remainder(pol, f), tupleSize);
	}

	@Override
	public PrimePowerFieldElement intMultiply(PrimePowerFieldElement a, BigInteger k) {
		return new PrimePowerFieldElement(ZpX.remainder(ZpX.intMultiply(a.getElement(), k), f), tupleSize);
	}

	public BigInteger getOrder() {
		Integers Z = new Integers();
		return Z.power(p, new BigInteger(Integer.toString(tupleSize)));
	}

	@Override
	public BigInteger getCharacteristic() {
		return p;
	}

	@Override
	public PrimePowerFieldElement getGenerator() {
		do {
			PrimePowerFieldElement a = getRandomElement();

			/* At the end of the j-th iteration, aux equals the j-th power of a. */
			PrimePowerFieldElement aux = getProductIdentity();
			for (BigInteger j = BigInteger.ONE; j.compareTo(getOrder()) <= 0; j = j.add(BigInteger.ONE)) {
				aux = multiply(aux, a);
				if (aux.equals(getProductIdentity())) {
					if (!j.equals(getOrder().subtract(BigInteger.ONE))) {
						break;
					} else {
						return a;
					}
				}
			}

		} while (true);
	}

	@Override
	public PrimePowerFieldElement getRandomElement() {
		List<PrimeFieldElement> coefficients = new ArrayList<PrimeFieldElement>();
		for (int i = 0; i < tupleSize; i++) {
			coefficients.add(Zp.getRandomElement());
		}
		for (int i = coefficients.size() - 1; i > 0; i--) {
			if (!coefficients.get(i).equals(Zp.getAddIdentity())) {
				break;
			}
			coefficients.remove(i);
		}
		return new PrimePowerFieldElement(new Polynomial<PrimeFieldElement>(coefficients, Zp), tupleSize);
	}
}
