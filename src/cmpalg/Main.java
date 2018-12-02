package cmpalg;

import java.math.BigInteger;

import structures.complex.UFDPolynomials;
import structures.concrete.Integers;
import utils.Polynomial;

public class Main {
	public static void main(String[] args) {
		Integers Z = new Integers();
		UFDPolynomials<BigInteger> Zt = new UFDPolynomials<BigInteger>(Z);

		Polynomial<BigInteger> p1 = Zt.parseElement("t^8+t");
		Polynomial<BigInteger> p2 = Zt.parseElement("t^7+t");

		System.out.println(Zt.gcd(p1, p2));
	}
}
