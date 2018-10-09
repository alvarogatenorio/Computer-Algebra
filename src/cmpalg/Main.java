package cmpalg;

import java.util.ArrayList;
import java.util.List;

import structures.UnitRing;
import utils.Integers;
import utils.Polynomial;

public class Main {
	public static void main(String[] args) {
		System.out.println("I am the yeast of thoughts and mind!");
		List<Integer> p = new ArrayList<Integer>();
		p.add(1);
		p.add(1);
		p.add(1);
		Polynomial<Integer> pol = new Polynomial<Integer>(p);
		System.out.println((new Integers())instanceof UnitRing);
		System.out.println(pol.toStringLaTeX(new Integers()));
	}
}
