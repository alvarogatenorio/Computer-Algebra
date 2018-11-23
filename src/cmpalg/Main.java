package cmpalg;

import java.math.BigInteger;

import structures.concrete.Integers;

public class Main {
	public static void main(String[] args) {
		System.out.println("A gato viejo rata tierna");
		/* A general testing here... */
		Integers Z = new Integers();
		BigInteger pow = Z.power(Z.parseElement("1000"), Z.parseElement("1000"));
		System.out.println(pow);
	}
}
