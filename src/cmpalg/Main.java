package cmpalg;

import java.math.BigInteger;

import structures.complex.FiniteField;
import structures.complex.Polynomials;
import structures.concrete.FiniteFieldPolynomials;
import structures.concrete.Integers;

/*
 * REPRESENTACION NUEVA POLINOMIOS
 * COMPOSICION MODULAR RAPIDA**
 * IRREDUCIBILIDAD EN FQ
 * HAY FALLOS EN EL PARSING/PRINTING DE LOS FQ
 * PREGUNTAR EN MATHSTACK LO DEL IRREDUCIBILITY TEST
 * METER STRINGS A SACO PARA QUE NO SEA UN COÑAZO TESTEAR
 * */

public class Main {
	public static void main(String[] args) {
		System.out.println("A gato viejo rata tierna");
		Integers Z = new Integers();
		Polynomials<BigInteger> ZX = new Polynomials<BigInteger>(Z);
		FiniteField Fq = new FiniteField(new BigInteger("5"), ZX.parseElement("t^3+-1t+2"));
		FiniteFieldPolynomials FqX = new FiniteFieldPolynomials(Fq);
		FqX.isIrreducible(FqX.parseElement("t"));
	}
}
