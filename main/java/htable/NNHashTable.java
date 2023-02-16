package htable;

import htable.network.DenseNetwork;
import htable.network.Encoding;
import htable.util.Matrix;

// Neural network based hash table with deletions.
// 
// Requires updating with gradient descent for every unique key insert
// and deletion. O(delete) = O(put) = O(kn), where k is #(training steps)
// and n is #(records), since k is somewhat related to n these are roughly 
// exponential time.
//
// Deletions are especially expensive because they require deframentation of
// the records O(n) as well as relearning the record-index mapping O(kn)
// 
// Retrieving records is always constant time O(get) = O(1) regardless of n
// 
public class NNHashTable {
	
	// parallel arrays
	public final int size;
	private String[] keys;
	private Object[] values;
	private int records;
	
	// keys metadata
	public final int keyLimit;
	private int minChar;
	private int maxChar;
	
	// hash function
	private DenseNetwork hashfn;
	private int maxSteps;
	private int bufferSteps;
	private double eta;
	
	public NNHashTable(int keyLimit, int capacity, int trainMaxSteps, int trainBufferSteps, double trainEta) {
		
		// handle invalid parameters
		if (keyLimit < 1 || capacity < 1) throw new IllegalArgumentException("Invalid parameters, keyLimit and capacity cannot be less then 1");
		
		// setup arrays
		this.size = capacity;
		this.keys = new String[this.size];
		this.values = new Object[this.size];
		this.records = 0;
		
		// setup keys metadata
		this.keyLimit = keyLimit;
		this.minChar = 0;
		this.maxChar = 128;
		
		// setup network
		this.hashfn = new DenseNetwork(this.keyLimit, this.size);
		this.maxSteps = trainMaxSteps;
		this.bufferSteps = trainBufferSteps;
		this.eta = trainEta;
	}
	
	
	// constructor with default training parameters
	public NNHashTable(int keyLimit, int capacity) {
		this(keyLimit, capacity, 10_000, 0, 1.0);
	}
	
	
	// insert record to table
	public void put(String key, Object value) {
		
		// map key to index
		int index = this.hash(key);
		
		// overwrite value if the key is found at the index
		if (key.equals(this.keys[index])) {
			this.values[index] = value;
		
		// otherwise insert at next available position
		} else {
			
			// handle table full
			if (this.records == this.size) throw new ArrayIndexOutOfBoundsException("Could not insert "+key+" because table is full.");
			
			// insert record
			this.keys[this.records] = key;
			this.values[this.records] = value;
			this.records++;
			
			// update keys metadata
			for (int i=0; i < key.length(); i++) {
				this.minChar = Math.min(this.minChar, key.charAt(i));
				this.maxChar = Math.max(this.maxChar, key.charAt(i));
			}
			
			// update hash function
			this.update();
		}
	}
	
	
	// get value from table by key
	public Object get(String key) {
		
		// handle table empty
		if (this.records == 0) throw new ArrayIndexOutOfBoundsException("Could not find "+key+" because table is empty.");
		
		// map key to index 
		int index = this.hash(key);
		
		// handle key not found
		if (!key.equals(this.keys[index])) throw new ArrayIndexOutOfBoundsException("Could not find "+key+" at index "+index);
			
		// return value at index
		return this.values[index];
	}
	
	
	// delete record from table by key
	public void delete(String key) {
		
		// handle table empty
		if (this.records == 0) throw new ArrayIndexOutOfBoundsException("Could not find "+key+" because table is empty.");
		
		// map key to index 
		int index = this.hash(key);
		
		// handle key not found
		if (!key.equals(this.keys[index])) throw new ArrayIndexOutOfBoundsException("Could not find "+key+" at index "+index);
		
		// delete record
		//this.keys[index] = null;
		//this.values[index] = null;
		this.records--;
		
		// defragment
		for (int i=index; i < this.records; i++) {
			this.keys[i] = this.keys[i+1];
			this.values[i] = this.values[i+1];
		}
		
		// update hash function
		this.update();
	}
	
	
	// get the number of records in the table
	public int totalRecords() {
		return this.records;
	}
	
	
	// update the hash function
	private void update() {
		
		// setup target
		Matrix[] target_x = new Matrix[this.records];
		Matrix[] target_y = new Matrix[this.records];
		for (int i=0; i < this.records; i++) {
			target_x[i] = this.keyNorm(this.keys[i]);
			target_y[i] = this.keyLabel(i);
		}
		
		// fit network to target
		// 
		// target_accuracy = 1.0
		// lower accuracies would mean the mapping is incorrect.
		// 
		// max_step = this.maxSteps
		// value is problem dependent, more steps are needed for some datasets than for 
		// other datasets
		// 
		// buffer_steps = this.bufferSteps
		// value is problem dependent, for configurations that oscillate around the 
		// minimum cost, making sure the model configuration holds the target accuracy 
		// is important, however increasing this too much will result in the model 
		// overfitting between inserts
		// 
		// learning_rate = this.eta
		// value is problem dependent, value should be chosen so that the model converges 
		// as fast as possible without overshooting. This usually means eta=1.0. if the 
		// value results in oscillation around the minimum then buffer_steps can be 
		// increased to train the model for longer.
		// 
		this.hashfn.fit(target_x, target_y, 1.0, this.maxSteps, this.bufferSteps, this.eta);
	}
	
	
	// map key to index
	private int hash(String key) {
		
		// handle invalid keys
		this.throwInvalidKey(key);
		
		// string key -> normalised key -> prediction -> max index
		return this.hashfn.predict(this.keyNorm(key)).max()[0];
	}
	
	
	// convert key to column vector of normalised character values
	private Matrix keyNorm(String key) {
		return Encoding.stringNorm(key, this.keyLimit, this.minChar, this.maxChar);
	}
	
	
	// convert index to one hot encoding
	private Matrix keyLabel(int index) {
		return Encoding.oneHot(index, this.size);
	}
	
	
	// throw an exception if a key is invalid
	private void throwInvalidKey(String key) {
		if (key == null) {
			throw new IllegalArgumentException("Key is invalid becuase it is null");
		
		} else if (key.equals("")) {
			throw new IllegalArgumentException("Key is invalid becuase it is empty");
		
		} else if (key.length() > this.keyLimit) {
			throw new IllegalArgumentException("\""+key+"\" is an invalid key because its length ("
					+key.length()+") is greater than the maximum key length ("+this.keyLimit+")");
		}
	}
}
