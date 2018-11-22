package structures.complex;

import structures.basic.Field;

public class PrimeModuleIntegers extends Field<Integer> {

	int primeModule;

	/* EXCEPTION (quiz� meter aqu� el test de primalidad aks) */
	public PrimeModuleIntegers(int primeModule) {
		this.primeModule = primeModule;
	}

	@Override
	public Integer getProductIdentity() {
		return 1;
	}

	@Override
	public Integer getAddIdentity() {
		return 0;
	}

	@Override
	public Integer getAddInverse(Integer a) {
		Integer result = (-a) % primeModule;
		return result < 0 ? result + primeModule : result;
	}

	@Override
	public Integer add(Integer a, Integer b) {
		Integer result = (a + b) % primeModule;
		return result < 0 ? result + primeModule : result;
	}

	@Override
	public Integer multiply(Integer a, Integer b) {
		Integer result = (a * b) % primeModule;
		return result < 0 ? result + primeModule : result;
	}

	@Override
	public Integer parseElement(String s) {
		Integer result = Integer.parseInt(s) % primeModule;
		return result < 0 ? result + primeModule : result;
	}

	/* PAG 73 */
	/* a not equals 0 */
	@Override
	public Integer getProductInverse(Integer a) {
		Integers Z = new Integers();
		Integer result = Z.bezout(a, primeModule).getFirst() % primeModule;
		return result < 0 ? result + primeModule : result;
	}

	@Override
	public Integer multiply(Integer a, int k) {
		Integer result = (a * k) % primeModule;
		return result < 0 ? result + primeModule : result;
	}

	@Override
	public Integer power(Integer a, int k) {
		Integers Z = new Integers();
		Integer result = Z.power(a, k);
		return result < 0 ? result + primeModule : result;
	}

	@Override
	public Integer divFactor(Integer a, Integer b) {
		Integers Z = new Integers();
		return Z.divFactor(a, b);
	}
}
