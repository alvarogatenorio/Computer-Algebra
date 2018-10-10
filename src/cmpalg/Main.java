package cmpalg;

import utils.Integers;
import utils.Polynomial;
import utils.Polynomials;

public class Main {
	public static void main(String[] args) {
		System.out.println("I am the yeast of thoughts and mind!");
		Polynomial<Integer> pol;
		Integers Z = new Integers();
		Polynomials<Polynomial<Integer>, Integer> Zt = new Polynomials<Polynomial<Integer>, Integer>(Z);
		pol = Zt.parseElement("6T^99+9T^0+8T^0");
		System.out.println(pol);
	}
}
