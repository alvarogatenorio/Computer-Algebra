package cmpalg;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cmpalg.generic.finiteFields.PrimeField;
import cmpalg.generic.finiteFields.PrimeFieldElement;
import cmpalg.generic.finiteFields.PrimePowerField;
import cmpalg.generic.finiteFields.PrimePowerFieldElement;
import structures.concrete.polynomials.FiniteFieldPolynomials;
import structures.concrete.polynomials.FiniteFieldPolynomials.FactorAlgorithm;
import structures.concrete.rationals.Rational;
import structures.concrete.rationals.Rationals;
import structures.generic.polynomials.FieldPolynomials;
import structures.generic.polynomials.Polynomial;
import utils.Pair;

public class Main {
	public static void main(String[] args) {
		Rationals Q = new Rationals();
		PrimeField Z5 = new PrimeField(new BigInteger("5"));
		FieldPolynomials<Rational> QT = new FieldPolynomials<Rational>(Q);
		FieldPolynomials<PrimeFieldElement> Z5T = new FieldPolynomials<PrimeFieldElement>(Z5);
		PrimePowerField Fq = new PrimePowerField(new BigInteger("5"), Z5T.parseElement("t^3+4t+2"));

		System.out.println(Fq.multiply(Fq.parseElement("(4,1,4)"), Fq.parseElement("(4,1,1)")));

		// fastModularTesting(QT);
		cantorZassenhaussTesting(Fq);
	}

	public static void fastModularTesting(FieldPolynomials<Rational> QT) {
		Polynomial<Rational> f = QT.parseElement("t^60+t^5+t^4+3t^2+1");
		Polynomial<Rational> g = QT.parseElement("t^2+t");
		Polynomial<Rational> h = QT.parseElement("t^2");
		Polynomial<Rational> r = QT.modularComposition(f, g, h);
		System.out.println("f:= " + f);
		System.out.println("g:= " + g);
		System.out.println("h:= " + h);
		System.out.println("g(h) mod f:= " + r);
	}

	public static void cantorZassenhaussTesting(PrimePowerField Fq) {
		for (int i = 0; i < 10; i++) {
			FiniteFieldPolynomials<PrimePowerFieldElement> FqT = new FiniteFieldPolynomials<PrimePowerFieldElement>(Fq);
			FqT.setFactorAlgorithm(FactorAlgorithm.BERLEKAMP);
			int n = ThreadLocalRandom.current().nextInt(2, 40);
			Polynomial<PrimePowerFieldElement> ff;
			do {
				ff = FqT.getRandomMonicPolynomial(2, n);
			} while (ff.degree() < 1);

			//ff = FqT.parseElement("t^3+(1,3,1)t^2+(0,2,2)t+(4,2,2)");

			System.out.println("To factor: " + ff);
			List<Pair<Polynomial<PrimePowerFieldElement>, Integer>> factor = FqT.factor(ff);
			System.out.println("\nBerlekamp factorization: ");
			System.out.println(FqT.printFactorization(factor));
			System.out.println("Berlekamp factorization product: ");
			System.out.println(FqT.printFactorizationCheck(factor));

			FqT.setFactorAlgorithm(FactorAlgorithm.CANTOR);
			factor = FqT.factor(ff);
			System.out.println("\nCantor--Zassenhauss factorization: ");
			System.out.println(FqT.printFactorization(factor));
			System.out.println("Cantor--Zassenhauss factorization product: ");
			System.out.println(FqT.printFactorizationCheck(factor));
			System.out.println("\n......\n");
		}
	}

}
