package htable.network;

import htable.util.Matrix;

// Fully connected neural network with sigmoid activation
// functions and cross entropy cost function. Parameters are 
// initialised with random values sampled from the standard
// normal distribution and optimised by gradient descent.
// 
public class DenseNetwork {
	
	// network parameters
	private int depth;
	private Matrix[] weights;
	private Matrix[] biases;
	
	public DenseNetwork(int... layers) {
		
		// setup layers
		this.depth = layers.length-1;
		this.weights = new Matrix[this.depth];
		this.biases = new Matrix[this.depth];
		
		// setup layer parameters with random values
		for (int i=0; i < this.depth; i++) {
			this.weights[i] = Matrix.random(layers[i+1], layers[i]);
			this.biases[i] = Matrix.random(layers[i+1], 1);
		}
	}
	
	
	// predict the label of x
	public Matrix predict(Matrix x) {
		
		// handle shape exception
		if (x.width != 1) throw new IllegalArgumentException("Inputs must be column vector form");
		
		// forward propagate x
		Matrix activation = x;
		for (int i=0; i < this.depth; i++) {
			activation = Activations.sigmoid(Matrix.dot(this.weights[i], activation).add(this.biases[i]));
		}
		
		return activation;
	}
	
	
	// optimise network with gradient descent for target data
	//
	// to speed up the tests, replace Metrics.crossEntropy and 
	// Metrics.crossEntropyDerivative with Metrics.mse and 
	// Metrics.mseDerivative respectively. mse is less expensive
	// to compute but does not converge as quickly so can cause
	// more failures with low max_step
	//
	public void fit(Matrix[] target_x, Matrix[] target_y, double target_accuracy, int max_step, int buffer_steps, double learning_rate) {
		
		// handle malformed data
		if (target_x.length != target_y.length) throw new IllegalArgumentException("Malformed data, target_x.length != target_y.length");
		
		// setup constants
		int batch_size = target_x.length; // number of x,y pairs
		int prop_depth = this.depth+1;    // network depth including input
		int output = prop_depth-1;        // index of output layer including input
		
		// setup variables
		double accuracy = 0.0;
		double cost;
		int convStep = 0;
		
		// train the network until convergence or max step
		for (int step=0; step < max_step; step++) {
			
			// setup metrics and gradient sums
			double costSum = 0;
			double accuracySum = 0;
			Matrix[] weightsDelta = new Matrix[this.depth];
			Matrix[] biasesDelta = new Matrix[this.depth];
			
			for (int l=0; l < this.depth; l++) {
				weightsDelta[l] = Matrix.constant(this.weights[l].height, this.weights[l].width, 0.0);
				biasesDelta[l] = Matrix.constant(this.biases[l].height, 1, 0.0);
			}
			
			// calculate metrics and gradient sums
			for (int i=0; i < batch_size; i++) {
				
				// unpack x,y pair
				Matrix x = target_x[i];
				Matrix y = target_y[i];
				
				// setup layer activations and z vectors
				Matrix[] zs = new Matrix[prop_depth]; // layer z vectors
				Matrix[] as = new Matrix[prop_depth]; // layer activations
				
				// setup input activation
				zs[0] = null;
				as[0] = x;
				
				// forward propagate
				for (int l=0; l < this.depth; l++) {
					zs[l+1] = Matrix.dot(this.weights[l], as[l]).add(this.biases[l]);
					as[l+1] = Activations.sigmoid(zs[l+1]);
				}
				
				// record results
				costSum += Metrics.crossEntropy(as[output], y);
				accuracySum += Metrics.classificationAccuracy(as[output], y);
				
				// backpropagate error through output layer
				Matrix error = Metrics.crossEntropyDerivative(as[output], y);
				Matrix delta = error.multiply(Activations.sigmoidDerivative(zs[output]));
				weightsDelta[output-1].sum(Matrix.dot(delta, as[output-1].transpose()));
				biasesDelta[output-1].sum(delta);
				
				// backpropagate delta through subsequent layers
				for (int l=output-2; l >= 0; l--) {
					delta = Matrix.dot(this.weights[l+1].transpose(), delta).multiply(Activations.sigmoidDerivative(zs[l+1]));
					weightsDelta[l].sum(Matrix.dot(delta, as[l].transpose()));
					biasesDelta[l].sum(delta);
				}
			}
			
			// apply average negative gradient ( gradient descent )
			for (int l=0; l < this.depth; l++) {
				this.weights[l].dif(weightsDelta[l].multiply(learning_rate / batch_size));
				this.biases[l].dif(biasesDelta[l].multiply(learning_rate / batch_size));
			}
			
			// calculate cost and accuracy
			cost = costSum / batch_size;
			accuracy = accuracySum / batch_size;
			System.out.println("step="+step+", cost="+cost+", accuracy="+accuracy);
			
			// try early stop condition
			if (accuracy >= target_accuracy) {
				System.out.println("stopping in " + (buffer_steps - convStep) + " steps");
				convStep++;
				if (convStep > buffer_steps) {
					break;
				}
			} else {
				convStep = 0;
			}
		}
		
		// handle failure to converge
		if (accuracy < target_accuracy) throw new IllegalStateException("Warning: failed to converge.");
	}
}
