package structures.generic.matrixes;

import java.util.List;

public class Matrix<E> {
	private List<List<E>> coefficients;

	public Matrix(List<List<E>> coefficients) {
		this.coefficients = coefficients;
	}

	public List<List<E>> getCoefficients() {
		return coefficients;
	}

	public int getRows() {
		return coefficients.size();
	}

	public int getColumns() {
		return coefficients.get(0).size();
	}

	public E get(int i, int j) {
		return coefficients.get(i).get(j);
	}
}
