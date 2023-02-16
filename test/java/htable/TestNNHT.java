package htable;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import htable.NNHashTable;
import htable.util.Matrix;

public class TestNNHT {
	
	@Before
	public void setUp() throws Exception {
		// set RNG seed for network parameter initialisation
		//Matrix.randomSeed(987432598273L);
	}
	
	// test inserting and retrieving easily distinguishable keys
	@Test
	public void testWordKeys() {
		
		// create hash table
		NNHashTable h = new NNHashTable(20, 10);
		
		// insert data
		String[] ref_keys = new String[] {"012","ABC","XwdYZ","a longer key value","gddog","emu","xyz","asd","4fw","sb7"};
		String[] ref_values = new String[] {"the","long","brown","fox ","jumped","over","the","tall","red","gate."};
		for (int i=0; i < ref_keys.length; i++) {
			h.put(ref_keys[i], ref_values[i]);
		}
		
		// get data
		for (int i=0; i < ref_keys.length; i++) {
			assertEquals(ref_values[i], (String)h.get(ref_keys[i]));
		}
	}
	
	// test if the table overwrites duplicate keys
	@Test
	public void testDuplicates() {
		
		// create hash table
		NNHashTable h = new NNHashTable(20, 5);
		
		// insert data
		String[] ref_keys = new String[] {"012","ABC","XwdYZ","a longer key value","gddog"};
		String[] ref_values = new String[] {"the","long","brown","fox ","jumped"};
		for (int i=0; i < ref_keys.length; i++) {
			h.put(ref_keys[i], ref_values[i]);
		}
		
		// insert duplicates
		for (int i=0; i < ref_keys.length; i++) {
			h.put(ref_keys[i], ref_values[i]+".new");
		}
		
		// check the number of entries in the table
		assertEquals(ref_keys.length, h.totalRecords());
		
		// check the values are overwritten
		for (int i=0; i < ref_keys.length; i++) {
			assertEquals(ref_values[i]+".new", (String)h.get(ref_keys[i]));
		}
	}
	
	// test the delete method
	@Test
	public void testDeletions() {
		
		// create hash table
		NNHashTable h = new NNHashTable(20, 5);
		
		// insert data
		String[] ref_keys = new String[] {"012","ABC","XwdYZ","a longer key value","gddog"};
		String[] ref_values = new String[] {"the","long","brown","fox ","jumped"};
		for (int i=0; i < ref_keys.length; i++) {
			h.put(ref_keys[i], ref_values[i]);
		}
		assertEquals(5, h.totalRecords());
		
		// delete record
		h.delete("ABC");
		
		// get data
		String[] ref_keys_deleted = new String[] {"012","XwdYZ","a longer key value","gddog"};
		String[] ref_values_deleted = new String[] {"the","brown","fox ","jumped"};
		for (int i=0; i < ref_keys_deleted.length; i++) {
			assertEquals(ref_values_deleted[i], (String)h.get(ref_keys_deleted[i]));
		}
		assertEquals(4, h.totalRecords());
	}
	
	// test inserting and retrieving difficult to distinguish keys
	//
	// this test highlights the main limitation of this
	// method, where keys that are numerous and difficult 
	// to distinguish are a much more difficult target for 
	// the basic optimisation strategy used in the network,
	// so the fit does not converge. better optimisation 
	// and normalisation strategies would probably solve this.
	//
	@Test
	public void testGridKeys() {
		
		// set grid size
		int height = 5;
		int width = height;
		
		// create hash table
		NNHashTable h = new NNHashTable(3, height*width);
		
		// put data
		for (int i=0; i < height; i++) {
			for (int j=0; j < width; j++) {
				System.out.println(i*j + j);
				h.put(i+":"+j, j*(i+1));
			}
		}
		
		// get data
		for (int i=0; i < height; i++) {
			for (int j=0; j < width; j++) {
				assertEquals(j*(i+1), (int)h.get(i+":"+j));
			}
		}
	}
	
	// test that an error is thrown when inserting to a full table
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testInsertToFullTable() {
		
		// create hash table with space for one record
		// then try inserting two records
		NNHashTable h = new NNHashTable(1, 1);
		h.put("a",false);
		h.put("b",true);
	}
	
	// test that an error is thrown when getting from an empty table
	//
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetFromEmptyTable() {
		NNHashTable h = new NNHashTable(5, 10);
		h.get("key");
	}
	
	// test that an error is thrown when getting a record that doesn't exist
	//
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetUnseenKey() {
		
		// create hash table with records
		NNHashTable h = new NNHashTable(1, 3);
		h.put("a",false);
		h.put("b",true);
		
		// get a record that doesn't exist
		h.get("c");
	}
	
	// test that empty keys throw errors
	// 
	// whilst empty keys would map to an index, they are not allowed because they would be confused with any key that
	// consisted of only minChar, since both keys map to a zeros column vector in their normalised form
	//
	@Test(expected = IllegalArgumentException.class)
	public void testInsertEmptyKey() {
		NNHashTable h = new NNHashTable(5, 10);
		h.put("", false);
	}
	
	// test that null keys throw errors
	//
	// null values are not allowed because inserting a null value will cause the put method to 'overwrite existing pair at index'
	// which would break the order of record insertion on the first insert because the first insert is mapped to a random
	// index by an untrained network
	//
	@Test(expected = IllegalArgumentException.class)
	public void testInsertNullKey() {
		NNHashTable h = new NNHashTable(5, 10);
		h.put(null, false);
	}

	// test that keys that are too long throw errors
	//
	@Test(expected = IllegalArgumentException.class)
	public void testInsertKeyTooLarge() {
		
		// create hash table with maximum key length 5
		// and try inserting a key longer than 5
		NNHashTable h = new NNHashTable(5, 10);
		h.put("a_key_longer_than_5", false);
	}
}
