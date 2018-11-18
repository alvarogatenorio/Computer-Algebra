package cmpalg;

import utils.Integers;
import utils.Polynomial;
import utils.UFDPolynomials;

public class Main {
	public static void main(String[] args) {
		System.out.println("A gato viejo rata tierna");
		/*
		 * FieldPolynomials<Polynomial<Rational>, Rational> QT = new
		 * FieldPolynomials<Polynomial<Rational>, Rational>( new Rationals());
		 * Polynomial<Rational> p1 = QT.parseElement("3t^3+2t^2+5t+1");
		 * Polynomial<Rational> p2 = QT.parseElement("4t+3");
		 * System.out.println(QT.quotient(p1, p2)); System.out.println(QT.reminder(p1,
		 * p2)); System.out.println(QT.add(QT.multiply(QT.quotient(p1, p2), p2),
		 * QT.reminder(p1, p2)));
		 */
		
		/*FieldPolynomials<Polynomial<Integer>, Integer> ZpT = new FieldPolynomials<Polynomial<Integer>, Integer>(
				new PrimeModuleIntegers(5));
		PrimesQuotients Fq = new PrimesQuotients(5, ZpT.parseElement("t^3+-1t+2"));
		System.out.println(ZpT.gcd(ZpT.parseElement("45t^8+5t^7+t"), ZpT.parseElement("t^3+-1t+2")));
		System.out.println(Fq.multiply(Fq.getProductInverse(Fq.parseElement("t^2")), Fq.parseElement("t^2")));*/
		
		UFDPolynomials<Polynomial<Integer>,Integer> ZT = new UFDPolynomials<Polynomial<Integer>,Integer>(new Integers());
		Polynomial<Integer> f = ZT.parseElement("t^2+2t+1");
		Polynomial<Integer> g = ZT.parseElement("t^3+t+2t^2+t^2+1+2t");
		Polynomial<Integer> h = ZT.gcd(f, g);
		System.out.println(h);
	}
}
