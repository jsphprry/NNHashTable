# libraries
import matplotlib.pyplot as plt


# data
unique_chars = [1,2,30,40,100]
n_chars = len(unique_chars)
minchar = min(unique_chars)
maxchar = max(unique_chars)

minmax_norms = [(i - minchar) / (maxchar - minchar) for i in unique_chars]
index_norms = [i / n_chars for i in range(n_chars)]


# plot
fig, axis = plt.subplots(1, 2, figsize=(9,4))

axis[0].plot(unique_chars, minmax_norms, color='black')
axis[0].scatter(unique_chars, minmax_norms, color='black')
for y in minmax_norms:
	axis[0].axhline(y, color='red')
axis[0].set_xlabel("Unique characters")
axis[0].set_ylabel("MinMax norms")

axis[1].plot(unique_chars, index_norms, color='black')
axis[1].scatter(unique_chars, index_norms, color='black')
for y in index_norms:
	axis[1].axhline(y, color='red')
axis[1].set_xlabel("Unique characters")
axis[1].set_ylabel("Index norms")

plt.tight_layout()
plt.show()

