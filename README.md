NNHashTable (standalone version) 2023
---------------------------------------
Extension of the CI583 Data Structures and Operating 
Systems 2022/23 hash table assignment. A neural-network 
based hash table allowing deletions, a neural network, 
unit tests and a matrix arithmetic class.

The neural network is implemented as a fully connected 
network with sigmoid activation functions and cross 
entropy cost function. Parameters are initialised with 
random values sampled from the standard normal 
distribution and optimised by gradient descent. 

The network learns the associations between normalised 
key values and one hot encoding labels in order to map 
keys to indices. The mapped index is determined by the 
index of the output node with the maximum activation. 

The key normalisation works by calculating the min-max 
normalisation of each character in a key using the 
minimum and maximum character range in the table. 
The normalised values are returned in a zeros column 
vector, with a size preset by a maximum key length.

For more details please read the [coursework report](CI583%20coursework%20report.pdf).
