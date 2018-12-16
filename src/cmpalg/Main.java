package cmpalg;

import structures.complex.FieldPolynomials;
import structures.concrete.Rationals;
import utils.Polynomial;
import utils.Rational;

public class Main {
	public static void main(String[] args) {
		FieldPolynomials<Rational> QT = new FieldPolynomials<Rational>(new Rationals());
		Polynomial<Rational> f = QT.parseElement("t^6+t^5+t^4+3t^2+1");
		Polynomial<Rational> g = QT.parseElement("t^2+t");
		Polynomial<Rational> h = QT.parseElement("t^2");
		Polynomial<Rational> r = QT.modularComposition(f, g, h);
		System.out.println(f);
		System.out.println(g);
		System.out.println(h);
		System.out.println(r);
	}
}
