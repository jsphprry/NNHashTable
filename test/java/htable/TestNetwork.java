package htable;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import htable.network.DenseNetwork;
import htable.network.Encoding;
import htable.network.Metrics;
import htable.util.Matrix;

public class TestNetwork {

	@Before
	public void setUp() throws Exception {
		// set RNG seed for network parameter initialisation
		//Matrix.randomSeed(987432598273L);
	}

	// test if the accuracy metric can correctly classify predictions
	@Test
	public void testAccuracyMetric() {
		Matrix prediction = Matrix.column(0.5,0.2);
		Matrix label0 = Matrix.column(1,0);
		Matrix label1 = Matrix.column(0,1);
		
		assertEquals(1, Metrics.classificationAccuracy(prediction, label0));
		assertEquals(0, Metrics.classificationAccuracy(prediction, label1));
	}

	// test the model's fit to exclusive-or.
	// modelling xor is a good first test to 
	// show that the network can learn 
	// non-linear models
	@Test
	public void xorTest() {
		
		// xor inputs
		Matrix[] target_x = new Matrix[] {
			//             a,   b
			Matrix.column(0.0, 0.0),
			Matrix.column(0.0, 1.0),
			Matrix.column(1.0, 0.0),
			Matrix.column(1.0, 1.0)
		};
		
		// xor outputs
		Matrix[] target_y = new Matrix[] {
			//           true, false
			Matrix.column(1.0, 0.0),
			Matrix.column(0.0, 1.0),
			Matrix.column(0.0, 1.0),
			Matrix.column(1.0, 0.0)
		};
		
		// model exclusive-or
		DenseNetwork network = new DenseNetwork(2,3,2);
		network.fit(target_x, target_y, 1.0, 1000, 100, 1.0);
		
		// assess predictions
		for (int i=0; i < target_x.length; i++) {
			assertEquals(1, Metrics.classificationAccuracy(network.predict(target_x[i]), target_y[i]));
		}
	}

	// the functionality needed for the nnhash table
	@Test
	public void keyTest() {
		
		// keys
		String[] ref_keys = new String[] {"012","ABC","XwdYZ","a longer key value","gddog","emu","xyz","asd","4fw","sb7"};
		
		// setup target data
		Matrix[] target_x = new Matrix[ref_keys.length];
		Matrix[] target_y = new Matrix[ref_keys.length];
		for (int i=0; i < ref_keys.length; i++) {
			target_x[i] = Encoding.stringNorm(ref_keys[i], 18, 48, 122);
			target_y[i] = Encoding.oneHot(i,ref_keys.length);
		}
		
		// learn mapping
		DenseNetwork network = new DenseNetwork(18,20,10);
		network.fit(target_x, target_y, 1.0, 10_000, 10, 1.0);
		
		// assess predictions
		for (int i=0; i < target_x.length; i++) {
			assertEquals(1, Metrics.classificationAccuracy(network.predict(target_x[i]), target_y[i]));
		}
	}
}
