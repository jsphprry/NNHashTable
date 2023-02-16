package htable.network;

import htable.util.Matrix;

public final class Metrics {
	
	
	// mean squared error cost function
	public static double mse(Matrix y, Matrix t) {
		
		// handle shape exception
		if (y.width != 1 || t.width != 1) throw new IllegalArgumentException("Inputs must be column vector format");
		
		// mean((y-t)^2)
		return Matrix.pow(y.sub(t), 2).mean();
	}
	
	
	// MSE derivative
	public static Matrix mseDerivative(Matrix y, Matrix t) {
		
		// handle shape exception
		if (y.width != 1 || t.width != 1) throw new IllegalArgumentException("Inputs must be column vector format");
		
		// 2(y-t)
		return Matrix.multiply(y.sub(t), 2);
	}
	
	
	// cross entropy cost function
	public static double crossEntropy(Matrix y, Matrix t) {
		
		// handle shape exception
		if (y.width != 1 || t.width != 1) throw new IllegalArgumentException("Inputs must be column vector format");
		
		// mean( -1 * ( t*log(y) + (1-t)*log(1-y) ) )
		Matrix t_lny = t.multiply(Matrix.ln(y));
		Matrix one_minus_t = t.multiply(-1).add(1);
		Matrix ln_one_minus_y = Matrix.ln(y.multiply(-1).add(1));
		return Matrix.multiply(Matrix.add(t_lny, Matrix.multiply(one_minus_t, ln_one_minus_y)), -1.0).mean();
	}
	
	
	// cross entropy derivative
	public static Matrix crossEntropyDerivative(Matrix y, Matrix t) {
		
		// handle shape exception
		if (y.width != 1 || t.width != 1) throw new IllegalArgumentException("Inputs must be column vector format");
		
		// ( ((1-t) / (1-y)) - (t/y)) / length(t)
		Matrix one_minus_t = t.multiply(-1).add(1);
		Matrix one_minus_y = y.multiply(-1).add(1);
		Matrix t_over_y = t.div(y);
		return Matrix.div(Matrix.sub(Matrix.div(one_minus_t, one_minus_y), t_over_y), (double)t.height);
	}
	
	
	// determine if a prediction is an accurate prediction
	// return 1 if accurate or 0 if inaccurate
	public static int classificationAccuracy(Matrix y, Matrix t) {
		
		// handle shape exception
		if (y.width != 1 || t.width != 1) throw new IllegalArgumentException("Inputs must be column vector format");
		
		int[] ymax = y.max();
		int[] tmax = t.max();
		return (ymax[0] == tmax[0] && ymax[1] == tmax[1]) ? 1 : 0;
	}
}
