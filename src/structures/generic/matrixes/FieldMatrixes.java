package structures.generic.matrixes;

import java.util.ArrayList;
import java.util.List;

import cmpalg.generic.basic.Field;

public class FieldMatrixes<E> extends Matrixes<E> {

	Field<E> baseField;

	public FieldMatrixes(Field<E> baseField) {
		super(baseField);
	}

	public Matrix<E> hermite(Matrix<E> M) {
		int m = Math.max(M.getRows(), M.getColumns());

		List<List<E>> coefficients = new ArrayList<List<E>>();
		
		//hemos encontrado un pivote
		int pivotRow = findPivot(M.getCoefficients(), 1);
		// operamos con magia oscura.
		coefficients.add(M.getCoefficients().get(pivotRow));

		/*
		 * The the number of pivots is upper bounded by m.
		 */
		int foundPivots = 0;

		return null;
	}

	/*
	 * Auxiliary private method for the hermite() method.
	 * 
	 * Returns the row in which the first non zero element of the specified column
	 * is. Returns -1 if the column is only made of zeroes.
	 */
	private int findPivot(List<List<E>> coefficients, int column) {
		int pivotRow = 0;
		int rows = coefficients.size();
		for (pivotRow = 0; pivotRow < rows; pivotRow++) {
			if (!coefficients.get(pivotRow).get(column).equals(baseField.getAddIdentity())) {
				break;
			}
		}
		return pivotRow == rows ? -1 : pivotRow;
	}

}
