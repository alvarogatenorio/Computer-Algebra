# Computer Algebra
An experimental **CAS** ***(Computer Algebra System)*** writen in **Java**.

As we like memes very much, we should name it ***"AlJavra"*** (at least provisionally).

You can use this project as an extension of Java to make things easier when working with algebraic structures, specially polynomials, and in particular, polynomials over finite fields.

Some of the algorithms included are:

**General algebra algorithms**
- Euclid's algorithm for any eucliean domain (implemented by the user, or one of the implemented ones).
- Extended Euclid's algorithm for any euclidean domain (Bézout identity coefficients computation).
- Chinese remainder inverse algorithm.
- Repeated squaring algorithm.
- AKS primality testing (only recommended for small numbers).
- Element inversion over finite fields.
- Discrete logarithm over the multiplicative group of a finite field (baby step--giant step algorithm).

**Algorithms over polynomials**
- Primitive Euclid's algorithm.
- Fast modular composition.
- Irreducibility testing for polynomials over finite fields.
- Berlekamp's factoring algorithm for polynomials over finite fields.
- Cantor--Zassenhauss' factoring algorithm for polynomials over finite fields.
- Modular algorithm (Hensel lifting) for factoring polynomials with integer coefficients.
- Kronecker's algorithm for factoring polynomials over the rationals.

Some of the general algebraic structures implemented are:

- Euclidean domains.
- Unique factorization domains.
- Rings.
- Fields.
- Finite Fields.
- Univariate polynomials over rings in general.
- Integers.
- Rationals.
- Matrixes (very incomplete).

**Documentation**
There is an outdated .pdf file that pretends to explain some of the details in the code and in the future may be a little quick user guide.
