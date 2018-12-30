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
			int pivotRow = searchPivot(j, i, M.getRows(), M);

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
			List<List<E>> coefficients = interchangeRows(M, j, pivotRow);

			/* Operate. */
			M = operate(coefficients, M, j, i);

			/* Updating indexes. */
			i++;
			j++;
		}

		/* Adding the rest of the bad columns. */
		badColumns = addBadColumns(i, M.getColumns(), badColumns);

		/* Building the polynomials. */
		int kernelDimension = badColumns.size();
		return buildBerlekampBasis(kernelDimension, badColumns, M);
	}

	private List<List<E>> interchangeRows(Matrix<E> M, int j, int i) {
		List<List<E>> coefficients = M.getCoefficients();
		List<E> aux = coefficients.get(j);
		coefficients.set(j, coefficients.get(i));
		coefficients.set(i, aux);
		return coefficients;
	}

	private Matrix<E> operate(List<List<E>> coefficients, Matrix<E> M, int j, int i) {
		List<E> currentRow = coefficients.get(j);
		E inverse = baseField.getProductInverse(currentRow.get(i));
		for (int k = 0; k < M.getColumns(); k++) {
			currentRow.set(k, baseField.multiply(currentRow.get(k), inverse));
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
		return new Matrix<E>(coefficients);
	}

	private int searchPivot(int j, int i, int rows, Matrix<E> M) {
		for (int k = j; k < rows; k++) {
			if (!M.get(k, i).equals(baseField.getAddIdentity())) {
				return k;
			}
		}
		return -1;
	}

	private List<Integer> addBadColumns(int i, int columns, List<Integer> badColumns) {
		for (; i < columns; i++) {
			badColumns.add(i);
		}
		return badColumns;
	}

	private List<Polynomial<E>> buildBerlekampBasis(int kernelDimension, List<Integer> badColumns, Matrix<E> M) {
		List<Polynomial<E>> basis = new ArrayList<Polynomial<E>>();

		/* There are exactly kernelDimension vectors in the base. */
		for (int k = 0; k < kernelDimension; k++) {

			/* This vectors are, in part, the bad columns. */
			int c = badColumns.get(k);
			List<E> coefficients = new ArrayList<E>();

			/* The vectors in the base have the same number of coordinates as columns. */
			int bads = 0;
			for (int l = 0; l < M.getColumns(); l++) {
				if (bads < badColumns.size() && badColumns.get(bads) == l && l != c) {
					coefficients.add(baseField.getAddIdentity());
					bads++;
				} else if (l == c) {
					coefficients.add(baseField.getAddInverse(baseField.getProductIdentity()));
					bads++;
				} else {
					if (l - bads < M.getRows()) {
						coefficients.add(M.get(l - bads, c));
					} else {
						coefficients.add(baseField.getAddIdentity());
					}
				}
			}

			for (int l = coefficients.size() - 1; l > 0; l--) {
				if (coefficients.get(l).equals(baseField.getAddIdentity())) {
					coefficients.remove(l);
				} else {
					break;
				}
			}
			basis.add(new Polynomial<E>(coefficients, baseField));
		}
		return basis;
	}

}
