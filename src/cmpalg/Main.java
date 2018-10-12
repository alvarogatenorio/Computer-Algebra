package cmpalg;

import utils.Integers;
import utils.Polynomial;
import utils.Polynomials;
import utils.Rationals;

public class Main {
	public static void main(String[] args) {
		System.out.println("I am the yeast of thoughts and mind!");
		Polynomial<Integer> pol;
		Integers Z = new Integers();
		Polynomials<Polynomial<Integer>, Integer> Zt = new Polynomials<Polynomial<Integer>, Integer>(Z);
		/* Problemas con la impresión (qué raro) */
		pol = Zt.parseElement("-15+0+9+8t+5t^8+4t^788+-8+900t^0");
		Rationals Q = new Rationals();
		System.out.println(Q.add(Q.parseElement("1/45"), Q.parseElement("3/5")));

		System.out.println(pol);
	}
}
