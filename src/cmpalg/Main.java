package cmpalg;

import java.math.BigInteger;

import structures.complex.Integers;

public class Main {
	public static void main(String[] args) {
		System.out.println("A gato viejo rata tierna");
		Integers Z = new Integers();
		BigInteger r = Z.multiply(new BigInteger("123456789012345678900000000000999999999999999999999"), new BigInteger("1234567899999999999999999999999999999901234567890"));
		System.out.println(r);
	}
}
