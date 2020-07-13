from time import time
from itertools import chain, combinations

start = time()

# Transform transactions to list of sets.
with open("transactions.txt", "r") as f:
    transactions = [set(line.strip().split(" ")) for line in f.readlines()]

# Get counts for itemsets of size 1.
itemsets_1 = {}
for i in chain(*transactions):
    if i in itemsets_1:
        itemsets_1[i] += 1
    else:
        itemsets_1[i] = 1

# Calculate support.
length = len(transactions)
threshhold = 1 / 100 * length
for i in itemsets_1:
    itemsets_1[i] /= threshhold

# Remove itemsets below min support.
keys = [k for k, v in itemsets_1.items() if v < 1]
for k in keys:
    del itemsets_1[k]

# Print itemsets sorted by support.
print(f"There are {len(itemsets_1)} itemsets with 1 item:")
print(sorted(itemsets_1.items(), key=lambda x: x[1], reverse=True))
print("\n")

# Itemsets without support for creating candidates.
itemsets = sorted(list(itemsets_1.keys()), key=int)
# Size of itemsets.
K = 2
# Number of itemsets as exit condition.
NUMBER = len(itemsets_1)
while NUMBER != 0:
    # Get candidates from items.
    if K == 2:
        # All possible combinations if k is 2.
        candidates = list(combinations(itemsets, K))
    else:
        # Combinations from previous itemsets if k > 2.
        candidates = []
        # Generate possible candidates.
        for i1 in itemsets:
            for i2 in itemsets:
                if i1[:-1] == i2[:-1] and i1[-1] < i2[-1]:
                    pc = list(i1)
                    pc.append(i2[-1])
                    # Add possible candidate to candidates if all subsets are frequent itemsets.
                    if all(s in itemsets for s in combinations(pc, K-1)):
                        candidates.append(tuple(pc))

    # Get counts of k-itemsets.
    itemsets_k = {}
    if len(candidates) > 10000:
        # Iterate subsets of transaction for each transaction if more than x candidates.
        for t in transactions:
            subsets = combinations(sorted(list(t)), K)
            for s in subsets:
                if s in itemsets_k:
                    itemsets_k[s] += 1
                else:
                    itemsets_k[s] = 1
    else:
        # Iterate all candidates for each transaction otherwise (normal apriori algorithm).
        for t in transactions:
            for c in candidates:
                if t.issuperset(c):
                    if c in itemsets_k:
                        itemsets_k[c] += 1
                    else:
                        itemsets_k[c] = 1

    # Calculate support and round to 2 decimals.
    for i in itemsets_k:
        itemsets_k[i] /= threshhold
        round(itemsets_k[i], 2)

    # Remove if under min support.
    keys = [k for k, v in itemsets_k.items() if v < 1]
    for k in keys:
        del itemsets_k[k]

    if len(itemsets_k) == 1:
        print(f"There is {len(itemsets_k)} itemset with {K} items:")
    else:
        print(f"There are {len(itemsets_k)} itemsets with {K} items:")
    print(sorted(itemsets_k.items(), key=lambda x: x[1], reverse=True))
    print("\n")

    # Change variables after iteration.
    itemsets = list(itemsets_k.keys())
    NUMBER = len(itemsets_k)
    K += 1

print(f"Program finished in {round(time()-start)} seconds.")
