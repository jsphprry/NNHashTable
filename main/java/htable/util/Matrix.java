package htable.util;

import java.util.Random;

public class Matrix {
	
	private static Random RNG = new Random();
	
	public final int height;
	public final int width;
	private double[][] values;
	
	// constructor for matrix instance
	public Matrix(int height, int width) {
		
		// handle zero shape
		if (height < 1 || width < 1) throw new IllegalArgumentException("Neither axis can be less than 1.");
		
		this.height = height;
		this.width = width;
		this.values = new double[height][width];
	}
	
	
	// get the value at the index i j from this instance
	public double get(int i, int j) {
		return this.values[i][j];
	}
	
	
	// set the value at the index i j for this instance to value
	public void set(int i, int j, double value) {
		this.values[i][j] = value;
	}
	
	
	// initialise RNG with specific seed
	public static void randomSeed(long seed) {
		RNG = new Random(seed);
	}
	
	
	// initialise RNG with random seed
	public static void randomSeed() {
		RNG = new Random();
	}
	
	
	// create matrix populated with a constant value
	public static Matrix constant(int height, int width, double value) {
		Matrix m = new Matrix(height,width);
		for (int i=0; i < height; i++) {
			for (int j=0; j < width; j++) {
				m.set(i, j, value);
			}
		}
		return m;
	}
	
	
	// create matrix populated with the random values
	public static Matrix random(int height, int width) {
		Matrix m = new Matrix(height,width);
		for (int i=0; i < height; i++) {
			for (int j=0; j < width; j++) {
				m.set(i, j, RNG.nextGaussian());
			}
		}
		return m;
	}
	
	
	// create identity matrix
	public static Matrix identity(int size) {
		Matrix m = new Matrix(size,size);
		for (int i=0; i < size; i++) {
			m.set(i, i, 1);
		}
		return m;
	}
	
	
	// create a column vector
	public static Matrix column(double... values) {
		Matrix m = new Matrix(values.length,1);
		for (int i=0; i < values.length; i++) {
			m.set(i, 0, values[i]);
		}
		return m;
	}
	
	
	// result of addition of two matrices
	public Matrix add(Matrix m) {
		return add(this,m);
	}
	public static Matrix add(Matrix m1, Matrix m2) {
		
		// handle shape exception
		if (!m1.sameShape(m2)) throw new IllegalArgumentException("Incompatible matrix shapes.");
		
		Matrix m3 = new Matrix(m1.height, m1.width);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				m3.set(i, j, m1.get(i,j) + m2.get(i,j));
			}
		}
		return m3;
	}
	
	
	// result of element-wise addition of matrix and scalar
	public Matrix add(double scalar) {
		return add(this,scalar);
	}
	public static Matrix add(Matrix m1, double scalar) {
		Matrix m2 = new Matrix(m1.height, m1.width);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				m2.set(i, j, m1.get(i,j) + scalar);
			}
		}
		return m2;
	}
	

	// result of subtraction of two matrices
	public Matrix sub(Matrix m) {
		return sub(this,m);
	}
	public static Matrix sub(Matrix m1, Matrix m2) {
		
		// handle shape exception
		if (!m1.sameShape(m2)) throw new IllegalArgumentException("Incompatible matrix shapes.");
		
		Matrix m3 = new Matrix(m1.height, m1.width);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				m3.set(i, j, m1.get(i,j) - m2.get(i,j));
			}
		}
		return m3;
	}
	
	
	// result of element-wise subtraction of scalar from matrix
	public Matrix sub(double scalar) {
		return sub(this,scalar);
	}
	public static Matrix sub(Matrix m1, double scalar) {
		Matrix m2 = new Matrix(m1.height, m1.width);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				m2.set(i, j, m1.get(i,j) - scalar);
			}
		}
		return m2;
	}
	

	// result of element-wise multiplication of two matrices
	public Matrix multiply(Matrix m) {
		return multiply(this,m);
	}
	public static Matrix multiply(Matrix m1, Matrix m2) {
		
		// handle shape exception
		if (!m1.sameShape(m2)) throw new IllegalArgumentException("Incompatible matrix shapes.");
		
		Matrix m3 = new Matrix(m1.height, m1.width);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				m3.set(i, j, m1.get(i,j) * m2.get(i,j));
			}
		}
		return m3;
	}
	
	
	// result of element-wise multiplication of scalar and matrix
	public Matrix multiply(double scalar) {
		return multiply(this,scalar);
	}
	public static Matrix multiply(Matrix m1, double scalar) {
		Matrix m2 = new Matrix(m1.height, m1.width);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				m2.set(i, j, m1.get(i,j) * scalar);
			}
		}
		return m2;
	}
	

	// result of element-wise division of two matrices
	public Matrix div(Matrix m) {
		return div(this,m);
	}
	public static Matrix div(Matrix m1, Matrix m2) {
		
		// handle shape exception
		if (!m1.sameShape(m2)) throw new IllegalArgumentException("Incompatible matrix shapes.");
		
		Matrix m3 = new Matrix(m1.height, m1.width);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				m3.set(i, j, m1.get(i,j) / m2.get(i,j));
			}
		}
		return m3;
	}
	
	
	// result of element-wise division of matrix by scalar
	public Matrix div(double scalar) {
		return div(this,scalar);
	}
	public static Matrix div(Matrix m1, double scalar) {
		Matrix m2 = new Matrix(m1.height, m1.width);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				m2.set(i, j, m1.get(i,j) / scalar);
			}
		}
		return m2;
	}
	
	
	// result of dot product of two matrices
	public Matrix dot(Matrix m) {
		return dot(this,m);
	}
	public static Matrix dot(Matrix m1, Matrix m2) {
		
		// handle shape exception
		if (m1.width != m2.height) throw new IllegalArgumentException("Incompatible matrix shapes.");
		
		Matrix m3 = Matrix.constant(m1.height, m2.width, 0);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m2.width; j++) {
				for (int k=0; k < m1.width; k++) {
					m3.set(i, j, m3.get(i,j) + m1.get(i,k) * m2.get(k,j));
				}
			}
		}
		return m3;
	}
	
	
	// add the values of one matrix to the values of this instance
	public void sum(Matrix m) {
		
		// handle shape exception
		if (!this.sameShape(m)) throw new IllegalArgumentException("Incompatible matrix shapes.");
		
		for (int i=0; i < this.height; i++) {
			for (int j=0; j < this.width; j++) {
				this.set(i,j, this.get(i,j) + m.get(i,j));
			}
		}
	}
	
	
	// subtract the values of one matrix from the values of this instance
	public void dif(Matrix m) {
		
		// handle shape exception
		if (!this.sameShape(m)) throw new IllegalArgumentException("Incompatible matrix shapes.");
		
		for (int i=0; i < this.height; i++) {
			for (int j=0; j < this.width; j++) {
				this.set(i,j, this.get(i,j) - m.get(i,j));
			}
		}
	}
	
	
	// result of element-wise exponent
	public Matrix pow(double power) {
		return pow(this,power);
	}
	public static Matrix pow(Matrix m1, double power) {
		Matrix m2 = new Matrix(m1.height, m1.width);
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				m2.set(i, j, Math.pow(m1.get(i,j), power));
			}
		}
		return m2;
	}
	
	
	// result of element-wise natural logarithm
	public Matrix ln() {
		return ln(this);
	}
	public static Matrix ln(Matrix m) {
		Matrix lm = new Matrix(m.height, m.width);
		for (int i=0; i < m.height; i++) {
			for (int j=0; j < m.width; j++) {
				lm.set(i, j, Math.log(m.get(i,j)));
			}
		}
		return lm;
	}
	
	
	// mean value of matrix
	public double mean() {
		return mean(this);
	}
	public static double mean(Matrix m) {
		double sum = 0;
		for (int i=0; i < m.height; i++) {
			for (int j=0; j < m.width; j++) {
				sum += m.get(i,j);
			}
		}
		return sum / (m.height * m.width);
	}
	
	
	// transposed matrix
	public Matrix transpose() {
		return transpose(this);
	}
	public static Matrix transpose(Matrix m) {
		Matrix mt = new Matrix(m.width, m.height);
		for (int i=0; i < mt.height; i++) {
			for (int j=0; j < mt.width; j++) {
				mt.set(i,j, m.get(j,i));
			}
		}
		return mt;
	}
	
	
	// the index holding the maximum value in a matrix
	public int[] max() {
		return max(this);
	}
	public static int[] max(Matrix m1) {
		double max = Double.NEGATIVE_INFINITY;
		int[] max_index = new int[2];
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				if (m1.get(i,j) > max) {
					max = m1.get(i,j);
					max_index[0] = i;
					max_index[1] = j;
				}
			}
		}
		return max_index;
	}
	
	
	// the index holding the minimum value in a matrix
	public int[] min() {
		return min(this);
	}
	public static int[] min(Matrix m1) {
		double min = Double.POSITIVE_INFINITY;
		int[] min_index = new int[2];
		for (int i=0; i < m1.height; i++) {
			for (int j=0; j < m1.width; j++) {
				if (m1.get(i,j) < min) {
					min = m1.get(i,j);
					min_index[0] = i;
					min_index[1] = j;
				}
			}
		}
		return min_index;
	}
	
	
	// maximum value in a matrix
	public double maximum() {
		return maximum(this);
	}
	public static double maximum(Matrix m1) {
		int[] max_index = max(m1);
		return m1.get(max_index[0], max_index[1]);
	}
	
	
	// minimum value in a matrix
	public double minimum() {
		return minimum(this);
	}
	public static double minimum(Matrix m1) {
		int[] min_index = max(m1);
		return m1.get(min_index[0], min_index[1]);
	}
	
	
	// check if two matrices are the same shape
	public boolean sameShape(Matrix m) {
		return sameShape(this,m);
	}
	public static boolean sameShape(Matrix m1, Matrix m2) {
		return (m1.height == m2.height && m1.width == m2.width);
	}
	
	
	// represent matrix as string
	public String toString() {
		return toString(this);
	}
	public static String toString(Matrix m) {
		String repr = "[";
		for (int i=0; i < m.height; i++) {
			repr = repr + "[ ";
			for (int j=0; j < m.width; j++) {
				repr = repr + m.get(i,j) + " ";
			}
			repr = (i+1 < m.height) ? repr + "]\n" : repr + "]";
		}
		repr = repr + "]";
		return repr;
	}
}
