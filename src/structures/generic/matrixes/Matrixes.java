package structures.generic.matrixes;

import java.util.ArrayList;
import java.util.List;

import cmpalg.generic.basic.Ring;

public class Matrixes<T> {

	private Ring<T> baseRing;

	public Matrixes(Ring<T> baseRing) {
		this.baseRing = baseRing;
	}

	public Matrix<T> fill(List<List<T>> coefficients, int rows, int columns) {
		while (coefficients.size() < rows) {
			coefficients.add(new ArrayList<T>());
		}
		for (int i = 0; i < coefficients.size(); i++) {
			while (coefficients.get(i).size() < columns) {
				coefficients.get(i).add(baseRing.getAddIdentity());
			}
		}
		return new Matrix<T>(coefficients);
	}

	public Matrix<T> multiply(Matrix<T> a, Matrix<T> b) {
		List<List<T>> product = new ArrayList<List<T>>();
		for (int i = 0; i < a.getRows(); i++) {
			List<T> productRow = new ArrayList<T>();
			for (int j = 0; j < b.getColumns(); j++) {
				T aux = baseRing.getAddIdentity();
				for (int k = 0; k < a.getColumns(); k++) {
					aux = baseRing.add(aux, baseRing.multiply(a.get(i, k), b.get(k, j)));
				}
				productRow.add(aux);
			}
			product.add(productRow);
		}
		return new Matrix<T>(product);
	}

	public Matrix<T> transpose(Matrix<T> M) {
		List<List<T>> transposeCoefficients = new ArrayList<List<T>>();
		for (int i = 0; i < M.getColumns(); i++) {
			transposeCoefficients.add(new ArrayList<T>());
			for (int j = 0; j < M.getRows(); j++) {
				transposeCoefficients.get(i).add(M.get(j, i));
			}
		}
		return new Matrix<T>(transposeCoefficients);
	}
}
