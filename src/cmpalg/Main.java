package cmpalg;

import java.util.ArrayList;
import java.util.List;

import utils.Integers;
import utils.Polynomials;

public class Main {
	public static void main(String[] args) {
		Integers Z = new Integers();
		Polynomials<List<Integer>, Integer> Zt = new Polynomials<List<Integer>, Integer>(Z);
		List<Integer> pol1 = new ArrayList<Integer>();
		List<Integer> pol2 = new ArrayList<Integer>();

		pol1.add(1);
		pol1.add(1);
		pol1.add(1);
		pol1.add(1);

		pol2.add(0);
		pol2.add(1);
		List<Integer> result = Zt.multiply(pol1, pol2);

		/* The following code prints a polynomial to a latex friendly format */

		// starts the math mode
		System.out.print("$");

		// if is 0 we don´t print it
		if (!result.get(0).equals(Z.getAddIdentity())) {
			System.out.print(result.get(0) + "+");
		}

		for (int i = 1; i < result.size() - 1; i++) {
			//if its 0 we don´t print it
			if (!result.get(i).equals(Z.getAddIdentity())) {
				System.out.print(result.get(i) + "T^" + i);
			}
		}

		if (result.get(result.size() - 1).equals(Z.getAddIdentity())) {
			System.out.print(result.get(result.size() - 1) + "T^" + (result.size() - 1));
		}

		// ends the math mode
		System.out.println("$");

		System.out.println("I am the yeast of thoughts and mind!");
	}
}
