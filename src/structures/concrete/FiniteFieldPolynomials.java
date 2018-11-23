package structures.concrete;

import java.math.BigInteger;

import structures.complex.FieldPolynomials;
import structures.complex.FiniteField;
import utils.Polynomial;

/*¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡APAÑAR PARSING!!!!!!!!!!!*/

/** Represents the set of polynomials over Fq (Fq[t]). */
public class FiniteFieldPolynomials
		extends FieldPolynomials<Polynomial<Polynomial<BigInteger>>, Polynomial<BigInteger>> {
	
	private FiniteField baseField;
	
	public FiniteFieldPolynomials(FiniteField baseField) {
		super(baseField);
		this.baseField = baseField;
	}

	public boolean isIrreducible(Polynomial<Polynomial<BigInteger>> f) {
		/*REVISAR TOODO Y ARREGLAR PARSING DE POLINOMIOS SOBRE CUERPOS FINITOS, ELEMENTOS SOBRE CUERPOS FINITOS O LO QUE COÑO HAYA FALTA*/
		/*Compute x^q rem f (use power)*/
		/*Compute a = x^(q^n) rem f (modular comp)**********/
		/*if a!=x reducible*/
		/*Para todo primo t divisor de n (ni idea de como)****************/
			/*Calcular  b =  x^(q^(n/t)) rem f*/
			/*si gcd(b-x,f)!=1 reducible*/
		/*irreducible*/
		return false;
	}
}
