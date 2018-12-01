package utils;

import java.math.BigInteger;

/** Represents a gaussian integer. */
public class GaussianInteger {

	private BigInteger realPart;
	private BigInteger imaginaryPart;

	public GaussianInteger(BigInteger realPart, BigInteger imaginaryPart) {
		this.realPart = realPart;
		this.imaginaryPart = imaginaryPart;
	}

	public GaussianInteger(BigInteger realPart) {
		this.realPart = realPart;
		this.imaginaryPart = BigInteger.ZERO;
	}

	public BigInteger getRealPart() {
		return realPart;
	}

	public BigInteger getImaginaryPart() {
		return imaginaryPart;
	}
}
