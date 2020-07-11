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

# Calculate support.
length = len(transactions)
threshhold = 1 / 100 * length
for i in itemsets_1:
    itemsets_1[i] /= threshhold

# Delete items below min support.
keys = [k for k, v in itemsets_1.items() if v < 1]
for SIZE in keys:
    del itemsets_1[SIZE]

# Print itemsets sorted by support.
print(f"There are {len(itemsets_1)} itemsets with 1 item:")
print(sorted(itemsets_1.items(), key=lambda x: x[1], reverse=True))

# Size of itemsets.
SIZE = 2
# Number of itemsets as exit condition.
NUMBER = len(itemsets_1)
# Only itemsets without support for iteration.
itemsets_1 = set(itemsets_1.keys())
itemsets = itemsets_1
while NUMBER != 0:
    # Get candidates from itemsets_1.
    candidates = set()
    for c in set(combinations(itemsets_1, SIZE)):
        if all(s[0] in itemsets for s in set(combinations(c, SIZE - 1))):
            candidates.add(c)

    itemsets_k = {}
    for t in transactions:
        for c in candidates:
            if all(e in t for e in c):
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
        print(f"There are {len(itemsets_k)} itemset with {SIZE} items:")
    print(sorted(itemsets_k.items(), key=lambda x: x[1], reverse=True))

    # Change variables after iteration.
    itemsets = set(itemsets_k.keys())
    NUMBER = len(itemsets_k)
    SIZE += 1
