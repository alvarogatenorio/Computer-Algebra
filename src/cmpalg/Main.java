package cmpalg;

import structures.EuclideanUnitDomain;
import structures.UnitRing;
import utils.Integers;
import utils.Polynomial;
import utils.UnitPolynomials;

public class Main {
	public static void main(String[] args) {
		System.out.println("A gato viejo rata tierna");

		EuclideanUnitDomain<Integer> Z = new Integers();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		UnitPolynomials<Polynomial<Integers>, Integers> ZT = new UnitPolynomials<Polynomial<Integers>, Integers>(
				(UnitRing) Z);
		Polynomial<Integers> f = ZT.parseElement("8t^3+t+1");
		Polynomial<Integers> g = ZT.parseElement("t^2+t+1");
		System.out.println(ZT.pseudoDivision(f, g).getFirst() + " " + ZT.pseudoDivision(f, g).getSecond() + " "
				+ ZT.pseudoDivision(f, g).getThird());
		System.out.println(ZT.divides(f, g));
		System.out.println(
				ZT.add(ZT.multiply(ZT.pseudoDivision(f, g).getSecond(), g), ZT.pseudoDivision(f, g).getThird()));
	}
}
