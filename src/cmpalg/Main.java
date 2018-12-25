package cmpalg;

import java.math.BigInteger;

import cmpalg.generic.finiteFields.PrimeField;
import cmpalg.generic.finiteFields.PrimeFieldElement;
import cmpalg.generic.finiteFields.PrimePowerField;
import cmpalg.generic.finiteFields.PrimePowerFieldElement;
import structures.concrete.euclideanDomains.Integers;
import structures.concrete.polynomials.FiniteFieldPolynomials;
import structures.concrete.rationals.Rational;
import structures.concrete.rationals.Rationals;
import structures.generic.polynomials.FieldPolynomials;
import structures.generic.polynomials.Polynomial;

public class Main {
	public static void main(String[] args) {
		Integers Z = new Integers();
		Rationals Q = new Rationals();
		PrimeField Z5 = new PrimeField(new BigInteger("5"));

		System.out.println(Z.power(new BigInteger("2"), new BigInteger("15")));

		FieldPolynomials<Rational> QT = new FieldPolynomials<Rational>(Q);
		FieldPolynomials<PrimeFieldElement> Z5T = new FieldPolynomials<PrimeFieldElement>(Z5);

		Polynomial<Rational> f = QT.parseElement("t^6+t^5+t^4+3t^2+1");
		Polynomial<Rational> g = QT.parseElement("t^2+t");
		Polynomial<Rational> h = QT.parseElement("t^2");
		Polynomial<Rational> r = QT.modularComposition(f, g, h);
		System.out.println("f:= " + f);
		System.out.println("g:= " + g);
		System.out.println("h:= " + h);
		System.out.println("g(h) mod f:= " + r);

		PrimePowerField Fq = new PrimePowerField(new BigInteger("5"), Z5T.parseElement("t^3+4t+2"));
		FiniteFieldPolynomials<PrimePowerFieldElement> FqT = new FiniteFieldPolynomials<PrimePowerFieldElement>(Fq);
		Polynomial<PrimePowerFieldElement> ff = FqT.parseElement("t");
		System.out.println(FqT.isIrreducible(ff));
	}
}
