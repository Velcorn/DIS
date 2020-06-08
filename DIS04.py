import time
import threading
import random

buffer = []
lsns = []
transactions = []


def begin_transaction():
    if not transactions:
        taid = 1
    else:
        taid = transactions[-1]
    transactions.append(taid + 1)
    return taid


def write(taid, pid):
    data = ["foo", "bar", "hello", "world", "random"]
    string = data[random.choice(range(4))]
    log(taid, pid, string)
    buffer.append([taid, pid, string])
    if len(buffer) > 5:
        with open("log.txt", "r") as f:
            lines = f.readlines()
            for e in buffer:
                for line in lines:
                    items = line.split(",")
                    if str(e[1]) == items[1] and items[3] == "EOT":
                        commit(items[1], items[2])
                        buffer.remove(e)


def commit(taid):
    log(taid, 0, "EOT")
    for e in buffer:
        if e[0] == taid:
            index = buffer.index(e)
            with open(str(buffer[index][1]) + ".txt", "w") as f:
                f.write(str(buffer[index][0]) + "," + buffer[index][2])


def log(taid, pid, data):
    if not lsns:
        lsn = 1
    else:
        lsn = lsns[-1]
    lsns.append(lsn + 1)
    if pid != 0:
        with open("log.txt", "a") as f:
            f.write(str(lsn) + "," + str(taid) + "," + str(pid) + "," + str(data) + "\n")
    else:
        with open("log.txt", "a") as f:
            f.write(str(lsn) + "," + str(taid) + "," + str(data) + "\n")


def run():
    taid = begin_transaction()
    name = client.getName()
    pids = range(int(name + "0"), int(name + "9"))
    for _ in range(random.choice(range(1, 5))):
        pid = random.choice(pids)
        write(taid, pid)
        time.sleep(random.choice(range(3)))
    commit(taid)


clients = []
for i in range(5):
    client = threading.Thread(target=run)
    client.setName(str(i + 1))
    clients.append(client)
    client.start()

for client in clients:
    client.join()
