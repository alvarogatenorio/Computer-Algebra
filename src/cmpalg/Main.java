package cmpalg;

import java.math.BigInteger;

import structures.complex.FiniteField;
import structures.complex.UFDPolynomials;
import structures.concrete.FiniteFieldPolynomials;
import structures.concrete.Integers;
import utils.FiniteFieldElement;
import utils.Polynomial;

public class Main {
	public static void main(String[] args) {
		Integers Z = new Integers();
		UFDPolynomials<BigInteger> ZX = new UFDPolynomials<BigInteger>(Z);
		FiniteField Fq = new FiniteField(new BigInteger("5"), ZX.parseElement("t^2+-1t+1"));
		FiniteFieldPolynomials FqX = new FiniteFieldPolynomials(Fq);

		Polynomial<FiniteFieldElement> p1 = FqX.parseElement("t");
		Polynomial<FiniteFieldElement> p2 = FqX.parseElement("(1,0)t^2");

		System.out.println(FqX.isIrreducible(p1));
	}
}
