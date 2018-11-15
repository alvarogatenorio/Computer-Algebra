package cmpalg;

import java.util.ArrayList;
import java.util.List;

import utils.Integers;
import utils.Pair;

public class Main {
	public static void main(String[] args) {
		System.out.println("A gato viejo rata tierna");
		/* Testing bezout */

		Integers Z = new Integers(); // declaring the integers
		Pair<Integer> p = Z.bezout(33, 7);
		System.out.println(Z.gcd(33, 7));
		System.out.println(p.getFirst() + "," + p.getSecond());
		System.out.println(p.getFirst() * 33 + p.getSecond() * 7);
		List<Integer> ideals = new ArrayList<Integer>();
		ideals.add(6);
		ideals.add(7);
		ideals.add(11);
		List<Integer> reminders = new ArrayList<Integer>();
		reminders.add(1);
		reminders.add(3);
		reminders.add(2);
		Integer a = Z.chineseReminderInverse(ideals, reminders);
		System.out.println(a);
	}
}
