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

print("Found", len(itemsets_1), "itemsets with 1 item:")
print(sorted(itemsets_1.items(), key=lambda x: int(x[0])))
print("\n")

size = 2
length = 1
itemsets_1 = sorted(itemsets_1.keys(), key=lambda x: int(x))
while length != 0:
    candidates = []
    for index, item in enumerate(itemsets_1):
        c = []
        if index < len(itemsets_1)-size:
            for i in range(size):
                c.append(itemsets_1[index + i])
        if c:
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

        # Delete items with count below threshhold.
        '''threshhold = 1 / 100 * length
        keys = [k for k, v in itemsets.items() if v < threshhold]
        for k in keys:
            del itemsets[k]'''

        '''# Get support as value.
        for i in itemsets:
            itemsets[i] /= length / 100'''

    print("Found", len(itemsets_k), "itemsets with", size, "items:")
    print(sorted(itemsets_k.items(), key=lambda x: int(x[0][0])))
    print("\n")

    length = len(itemsets_k)
    size += 1
