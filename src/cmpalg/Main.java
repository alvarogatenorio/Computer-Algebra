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

		Polynomial<FiniteFieldElement> p1 = FqX.parseElement("(3,3)t^3+(1,1)");
		Polynomial<FiniteFieldElement> p2 = FqX.parseElement("(2,2)t^6+(1,1)+(4,4)t^3");

		System.out.println(FqX.gcd(p2, p1));
	}
}
