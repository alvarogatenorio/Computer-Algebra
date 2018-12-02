package cmpalg;

import java.math.BigInteger;

import structures.complex.UFDPolynomials;
import structures.concrete.Integers;
import utils.Polynomial;

public class Main {
	public static void main(String[] args) {
		Integers Z = new Integers();
		UFDPolynomials<BigInteger> Zt = new UFDPolynomials<BigInteger>(Z);

		Polynomial<BigInteger> p1 = Zt.parseElement("t^3+1");
		Polynomial<BigInteger> p2 = Zt.parseElement("t^6+1+2t^3");

		System.out.println(Zt.gcd(p1, p2));
	}
}
