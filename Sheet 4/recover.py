import os

# Transform log into sorted list.
log = []
with open("log.txt", "r") as f:
    lines = f.readlines()
    for line in lines:
        log.append(line.strip().split(","))
log = sorted(log, key=lambda x: int(x[0]))


def analyze():
    taids = []
    for e in log:
        if e[2] == "EOT":
            taids.append(e[1])

    winners = []
    for e in log:
        if e[1] in taids and e[2] != "EOT":
            winners.append(e)

    return winners


def redo():
    winners = analyze()
    for w in winners:
        if not os.path.exists(w[2] + ".txt"):
            with open(w[2] + ".txt", "w") as g:
                g.write(w[0] + "," + w[3])
        else:
            with open(w[2] + ".txt", "r") as g:
                line = g.readline().strip()
                lsn, string = line.split(",")
                if lsn < w[0]:
                    data = w[0] + "," + w[3]

            with open(w[2] + ".txt", "w") as g:
                    g.write(data)


print(redo())