package cmpalg;

import utils.Integers;
import utils.Pair;

public class Main {
	public static void main(String[] args) {
		System.out.println("A gato viejo rata tierna");
		/* Testing bezout */

		Integers Z = new Integers(); // declaring the integers
		Pair<Integer> p = Z.bezout(33, 7);
		System.out.println(p.getFirst()+","+p.getSecond());
	}
}
