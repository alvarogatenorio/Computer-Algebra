package cmpalg;

import utils.FieldPolynomials;
import utils.Polynomial;
import utils.PrimeModuleIntegers;
import utils.PrimesQuotients;

public class Main {
	public static void main(String[] args) {
		System.out.println("A gato viejo rata tierna");
		/*FieldPolynomials<Polynomial<Rational>, Rational> QT = new FieldPolynomials<Polynomial<Rational>, Rational>(
				new Rationals());
		Polynomial<Rational> p1 = QT.parseElement("3t^3+2t^2+5t+1");
		Polynomial<Rational> p2 = QT.parseElement("4t+3");
		System.out.println(QT.quotient(p1, p2));
		System.out.println(QT.reminder(p1, p2));
		System.out.println(QT.add(QT.multiply(QT.quotient(p1, p2), p2), QT.reminder(p1, p2)));
*/
		FieldPolynomials<Polynomial<Integer>, Integer> ZpT = new FieldPolynomials<Polynomial<Integer>, Integer>(
				new PrimeModuleIntegers(5));
		PrimesQuotients Fq = new PrimesQuotients(5, ZpT.parseElement("t^3+-1t+2"));
		System.out.println(ZpT.gcd(ZpT.parseElement("45t^8+5t^7+t"), ZpT.parseElement("t^3+-1t+2")));
		System.out.println(Fq.getProductInverse(Fq.parseElement("t^2")));
	}
}
