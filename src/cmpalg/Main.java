package cmpalg;

import java.math.BigInteger;

import structures.complex.FieldPolynomials;
import structures.complex.FiniteField;
import structures.complex.UFDPolynomials;
import structures.concrete.FiniteFieldPolynomials;
import structures.concrete.Integers;
import structures.concrete.Rationals;
import utils.FiniteFieldElement;
import utils.Polynomial;
import utils.Rational;

public class Main {
	public static void main(String[] args) {
		Integers Z = new Integers();
		Rationals Q = new Rationals();
		
		System.out.println(Z.power(new BigInteger("2"), new BigInteger("15")));
		
		FieldPolynomials<Rational> QT = new FieldPolynomials<Rational>(Q);
		UFDPolynomials<BigInteger> ZT = new UFDPolynomials<BigInteger>(Z);

		FiniteField Fq = new FiniteField(new BigInteger("5"), ZT.parseElement("t^3+4t+2"));

		Polynomial<Rational> f = QT.parseElement("t^6+t^5+t^4+3t^2+1");
		Polynomial<Rational> g = QT.parseElement("t^2+t");
		Polynomial<Rational> h = QT.parseElement("t^2");
		Polynomial<Rational> r = QT.modularComposition(f, g, h);
		System.out.println("f:= " + f);
		System.out.println("g:= " + g);
		System.out.println("h:= " + h);
		System.out.println("g(h) mod f:= " + r);

		FiniteFieldPolynomials FqT = new FiniteFieldPolynomials(Fq);
		Polynomial<FiniteFieldElement> ff = FqT.parseElement("t");
		System.out.println(FqT.isIrreducible(ff));
	}
}
