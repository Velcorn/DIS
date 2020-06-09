import os

# Check if log is present.
if not os.path.exists("log.txt"):
    print("There is no log.txt... First run write.py!")
    exit()

# Transform log into sorted list.
log = []
with open("log.txt", "r") as f:
    lines = f.readlines()
    for line in lines:
        log.append(line.strip().split(","))
log = sorted(log, key=lambda x: int(x[0]))


# Get all winner operations from the log.
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


# Redo winner operations either if the file doesn't exist or if the file LSN is smaller than the log LSN.
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
                data = []
                if int(lsn) < int(w[0]):
                    data.append(w[0] + "," + w[3])

            if data:
                with open(w[2] + ".txt", "w") as g:
                        g.write(data[0])

    return "Recovery finished."


print(redo())
