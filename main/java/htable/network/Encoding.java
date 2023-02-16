package htable.network;

import htable.util.Matrix;

public final class Encoding {
	
	
	// convert label to one hot encoding column vector
	public static Matrix oneHot(int label, int size) {
		Matrix ohe = Matrix.constant(size, 1, 0.0);
		ohe.set(label, 0, 1.0);
		return ohe;
	}
	
	
	// convert string to column vector of normalised character values
	public static Matrix stringNorm(String key, int size, int minchar, int maxchar) {
		Matrix norm = Matrix.constant(size, 1, 0.0);
		for (int i=0; i < key.length(); i++) {
			norm.set(i, 0, (key.charAt(i) - minchar) / (double)(maxchar - minchar + 1));
		}
		return norm;
	}
}
