from itertools import combinations

# Iterate over transactions and get count of each itemset.
with open("transactions.txt", "r") as f:
    transactions = [line.strip().split(" ") for line in f.readlines()]

itemsets_1 = {}
for t in transactions:
    for i in t:
        if i in itemsets_1:
            itemsets_1[i] += 1
        else:
            itemsets_1[i] = 1

# Delete items with count below threshhold.
length = len(transactions)
threshhold = 1 / 100 * length
keys = [k for k, v in itemsets_1.items() if v < threshhold]
for size in keys:
    del itemsets_1[size]

# Get support as value.
for i in itemsets_1:
    itemsets_1[i] /= threshhold

# Print itemsets sorted by support value.
print(f"There are {len(itemsets_1)} itemsets with 1 item:")
print(sorted(itemsets_1.items(), key=lambda x: x[1], reverse=True))

# Size of itemsets.
size = 2
# Number of itemsets as exit condition.
number = len(itemsets_1)
# Only itemsets without support for iteration.
itemsets_1 = set(itemsets_1.keys())
itemsets = itemsets_1
while number != 0:
    # Get candidates from itemsets_1.
    candidates = set()
    for c in set(combinations(itemsets_1, size)):
        if all(s[0] in itemsets for s in set(combinations(c, size-1))):
            candidates.add(c)

    itemsets_k = {}
    for t in transactions:
        for c in candidates:
            if all(e in t for e in c):
                if c in itemsets_k:
                    itemsets_k[c] += 1
                else:
                    itemsets_k[c] = 1

    keys = [k for k, v in itemsets_k.items() if v < threshhold]
    for k in keys:
        del itemsets_k[k]

    for i in itemsets_k:
        itemsets_k[i] /= threshhold
        round(itemsets_k[i], 2)

    if len(itemsets_k) == 1:
        print(f"There is {len(itemsets_k)} itemset with {size} items:")
    else:
        print(f"There are {len(itemsets_k)} itemset with {size} items:")
    print(sorted(itemsets_k.items(), key=lambda x: x[1], reverse=True))

    # Change variables after iteration.
    number = len(itemsets_k)
    itemsets = set(itemsets_k.keys())
    size += 1
