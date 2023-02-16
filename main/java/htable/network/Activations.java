package htable.network;

import htable.util.Matrix;

public final class Activations {
	
	public static double sigmoid(double x) {
		return 1.0 / (1.0 + Math.pow(Math.E, -x));
	}
	
	public static Matrix sigmoid(Matrix m) {
		Matrix sm = new Matrix(m.height, m.width);
		for (int i=0; i < m.height; i++) {
			for (int j=0; j < m.width; j++) {
				sm.set(i, j, sigmoid(m.get(i, j)));
			}
		}
		return sm;
	}
	
	public static double sigmoidDerivative(double x) {
		return sigmoid(x) * (1.0 - sigmoid(x));
	}
	
	public static Matrix sigmoidDerivative(Matrix m) {
		Matrix sm = new Matrix(m.height, m.width);
		for (int i=0; i < m.height; i++) {
			for (int j=0; j < m.width; j++) {
				sm.set(i, j, sigmoidDerivative(m.get(i, j)));
			}
		}
		return sm;
	}
}
