package cmpalg;

import utils.Integers;
import utils.Polynomial;
import utils.Polynomials;
import utils.Rationals;

public class Main {
	public static void main(String[] args) {
		System.out.println("A gato viejo rata tierna");
		Polynomial<Integer> pol;
		Integers Z = new Integers();
		Polynomials<Polynomial<Integer>, Integer> Zt = new Polynomials<Polynomial<Integer>, Integer>(Z);
		pol = Zt.parseElement("t^5+5t^6");
		Rationals Q = new Rationals();
		System.out.println(Q.add(Q.parseElement("1/45"), Q.parseElement("3/5")));
		Polynomial<Integer> pol2;
		pol2 = Zt.parseElement("t+1");
		System.out.println(Zt.multiply(pol, pol2));
	}
}
