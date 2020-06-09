import time
import threading
import random

# Initialize a buffer, a list of LSNs and a list of taids.
buffer = []
lsns = []
taids = []


# Create a taid for a transaction and return it.
def begin_transaction():
    if not taids:
        taid = 1
        taids.append(taid)
    else:
        taid = taids[-1]
    taids.append(taid + 1)
    return taid


# Write data to the buffer or to its text file based on taid.
def write(taid, pid):
    strings = ["foo", "bar", "hello", "world", "random"]
    string = strings[random.choice(range(5))]
    log(taid, pid, string)
    lsn = lsns[-2]
    buffer.append([lsn, taid, pid, string])

    # If more than 5 datasets in buffer, get those that have already been committed.
    data = []
    if len(buffer) > 5:
        for e in buffer:
            if e[1] not in taids:
                data.append(e)
                buffer.remove(e)

    # Write those datasets to their corresponding text files.
    for d in data:
        with open(str(d[2]) + ".txt", "w") as f:
            f.write(str(lsn) + "," + d[3])


# Commit a transaction by logging it and removing its ID from the taid list.
def commit(taid):
    log(taid, 0, "EOT")
    taids.remove(taid)


# Log an entry of a transaction.
def log(taid, pid, data):
    if not lsns:
        lsn = 1
        lsns.append(1)
    else:
        lsn = lsns[-1]
    lsns.append(lsn + 1)

    # Discern write operations and EOTs.
    if pid != 0:
        with open("log.txt", "a") as f:
            f.write(str(lsn) + "," + str(taid) + "," + str(pid) + "," + str(data) + "\n")
    else:
        with open("log.txt", "a") as f:
            f.write(str(lsn) + "," + str(taid) + "," + str(data) + "\n")


# Run function for clients.
def run():
    taid = begin_transaction()
    name = client.getName()
    pids = range(int(name + "0"), int(name + "9"))
    for _ in range(random.choice(range(5))):
        pid = random.choice(pids)
        write(taid, pid)
        time.sleep(random.choice(range(3)))
    commit(taid)


# Create threads for clients to run concurrently.
clients = []
for i in range(5):
    client = threading.Thread(target=run)
    client.setName(str(i + 1))
    clients.append(client)
    client.start()

for client in clients:
    client.join()
