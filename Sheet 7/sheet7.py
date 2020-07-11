from time import time
from itertools import combinations

start = time()

# Transform transactions to list of sets.
with open("transactions.txt", "r") as f:
    transactions = [set(line.strip().split(" ")) for line in f.readlines()]

# Get counts for itemsets of size 1.
itemsets_1 = {}
for t in transactions:
    for i in t:
        if i in itemsets_1:
            itemsets_1[i] += 1
        else:
            itemsets_1[i] = 1

# Calculate support.
length = len(transactions)
threshhold = 1 / 100 * length
for i in itemsets_1:
    itemsets_1[i] /= threshhold

# Remove items below min support.
keys = [k for k, v in itemsets_1.items() if v < 1]
for k in keys:
    del itemsets_1[k]

# Print itemsets sorted by support.
print(f"There are {len(itemsets_1)} itemsets with 1 item:")
print(sorted(itemsets_1.items(), key=lambda x: x[1], reverse=True))
print("\n")

# Size of itemsets.
SIZE = 2
# Number of itemsets as exit condition.
NUMBER = len(itemsets_1)
# Only items for creating tuples.
items = list(itemsets_1.keys())
while NUMBER != 0:
    # Get candidates from items.
    candidates = [c for c in combinations(items, SIZE)]

    # Get count of itemsets of size k.
    itemsets_k = {}
    for t in transactions:
        for c in candidates:
            if t.issuperset(c):
                if c in itemsets_k:
                    itemsets_k[c] += 1
                else:
                    itemsets_k[c] = 1

    for i in itemsets_k:
        itemsets_k[i] /= threshhold
        round(itemsets_k[i], 2)

    keys = [k for k, v in itemsets_k.items() if v < 1]
    for k in keys:
        del itemsets_k[k]

    if len(itemsets_k) == 1:
        print(f"There is {len(itemsets_k)} itemset with {SIZE} items:")
    else:
        print(f"There are {len(itemsets_k)} itemsets with {SIZE} items:")
    print(sorted(itemsets_k.items(), key=lambda x: x[1], reverse=True))
    print("\n")

    # Change variables after iteration.
    NUMBER = len(itemsets_k)
    SIZE += 1
    # Get unique items from nested list.
    items = set(item for items in list(itemsets_k.keys()) for item in items)

end = time()
print(f"Program finished in {round(end-start, 0)} seconds.")
