package structures.generic.matrixes;

import java.util.ArrayList;
import java.util.List;

import cmpalg.generic.basic.Field;
import structures.generic.polynomials.Polynomial;

public class FieldMatrixes<E> extends Matrixes<E> {

	Field<E> baseField;

	public FieldMatrixes(Field<E> baseField) {
		super(baseField);
		this.baseField = baseField;
	}

	public List<Polynomial<E>> hermite(Matrix<E> M) {
		List<Integer> badColumns = new ArrayList<Integer>();

		/* Number of columns with a found pivot. */
		int j = 0;

		/* Column in which we are searching for the pivot. */
		int i = 0;

		while (j < Math.max(M.getRows(), M.getColumns()) && i < M.getColumns()) {
			/* Search pivot in the from j-th row (included) in the i-th column. */
			int pivotRow = searchPivot(i, j, M.getRows(), M);

			/*
			 * If no pivot is found, we go to the next column, and add the current to the
			 * "bad columns" list.
			 */
			if (pivotRow == -1) {
				badColumns.add(i);
				i++;
				continue;
			}

			/* Interchange rows. */
			List<List<E>> coefficients = M.getCoefficients();
			List<E> aux = coefficients.get(j);
			coefficients.set(j, coefficients.get(pivotRow));
			coefficients.set(pivotRow, aux);

			/* Operate. */
			List<E> currentRow = coefficients.get(j);
			for (int k = 0; k < M.getColumns(); k++) {
				currentRow.set(k,
						baseField.multiply(currentRow.get(k), baseField.getProductInverse(currentRow.get(i))));
			}
			for (int l = 0; l < M.getRows(); l++) {
				if (l != j) {
					List<E> lthRow = coefficients.get(l);
					E factor = baseField.getAddInverse(lthRow.get(i));
					for (int k = 0; k < M.getColumns(); k++) {
						lthRow.set(k, baseField.add(lthRow.get(k), baseField.multiply(factor, currentRow.get(k))));
					}
				}
			}

			/* Re-creating the matrix. */
			M = new Matrix<E>(coefficients);

			/* Updating indexes. */
			i++;
			j++;
		}

		/* Adding the rest of the bad columns. */
		for (; i < M.getColumns(); i++) {
			badColumns.add(i);
		}

		int kernelDimension = badColumns.size();

		/* Building the polynomials. */
		List<Polynomial<E>> basis = new ArrayList<Polynomial<E>>();
		for (int k = 0; k < kernelDimension; k++) {
			int c = badColumns.get(k);
			List<E> coefficients = new ArrayList<E>();

			for (int l = 0; l < M.getColumns(); l++) {
				if (l < M.getColumns() - kernelDimension) {
					coefficients.add(M.get(l, c));
				} else {
					if (l == M.getColumns() - kernelDimension + k) {
						coefficients.add(baseField.getAddInverse(baseField.getProductIdentity()));
					} else {
						coefficients.add(baseField.getAddIdentity());
					}
				}
			}

			for (int l = coefficients.size() - 1; l > 0; l--) {
				if (coefficients.get(l).equals(baseField.getAddIdentity())) {
					coefficients.remove(l);
				}
			}
			basis.add(new Polynomial<E>(coefficients, baseField));
		}

		return basis;
	}

	public int searchPivot(int j, int i, int rows, Matrix<E> M) {
		for (int k = j; k < rows; k++) {
			if (!M.get(k, i).equals(baseField.getAddIdentity())) {
				return k;
			}
		}
		return -1;
	}

}
