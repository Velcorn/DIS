from itertools import combinations

# Iterate over transactions and get count of each itemset.
with open("transactions.txt", "r") as f:
    itemsets_1 = {}
    lines = f.readlines()
    for line in lines:
        for i in line.strip().split(" "):
            if i not in itemsets_1:
                itemsets_1[i] = 1
            else:
                itemsets_1[i] += 1

    # Delete items with count below threshhold.
    length = len(lines)
    threshhold = 1 / 100 * length
    keys = [k for k, v in itemsets_1.items() if v < threshhold]
    for size in keys:
        del itemsets_1[size]

    # Get support as value.
    for i in itemsets_1:
        itemsets_1[i] /= length / 100

print("There are", len(itemsets_1), "itemsets with 1 item:")
print(sorted(itemsets_1.items(), key=lambda x: x[1], reverse=True))
print("\n")

# Size of itemsets.
size = 2

# Number of itemsets as exit condition.
number = len(itemsets_1)

# Only itemsets without number/support for iteration.
itemsets_1 = list(itemsets_1.keys())

while number != 0:
    # Get candidates from itemsets_1.
    candidates = []
    itemsets = itemsets_1
    for c in list(combinations(itemsets_1, size)):
        if all(s[0] in c for s in combinations(c, size-1)):
            candidates.append(c)

    with open("transactions.txt", "r") as f:
        itemsets_k = {}
        lines = f.readlines()
        for line in lines:
            for c in candidates:
                if all(e in line.strip().split(" ") for e in c):
                    if tuple(c) not in itemsets_k:
                        itemsets_k[tuple(c)] = 1
                    else:
                        itemsets_k[tuple(c)] += 1

        keys = [k for k, v in itemsets_k.items() if v < threshhold]
        for k in keys:
            del itemsets_k[k]

        for i in itemsets_k.keys():
            itemsets_k[i] = round(itemsets_k[i] / threshhold, 2)

    if len(itemsets_k) == 1:
        print("There is", len(itemsets_k), "itemset with", size, "items:")
    else:
        print("There are", len(itemsets_k), "itemsets with", size, "items:")
    print(sorted(itemsets_k.items(), key=lambda x: x[1], reverse=True))
    print("\n")

    # Change variables after iteration.
    number = len(itemsets_k)
    itemsets = list(itemsets_k.keys())
    size += 1
