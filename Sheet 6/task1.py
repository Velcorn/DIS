from psycopg2 import connect, Error
from config import db_config
import csv


def etl():
    try:
        params = db_config()
        connection = connect(**params)
        cursor = connection.cursor()

        # Clear DB before executing scripts.
        print("Clearing DB...")
        cursor.execute("drop schema public cascade; "
                       "create schema public;")
        print("Cleared DB.\n")

        # Execute given and star schema script.
        print("Executing SQL scripts...")
        cursor.execute(open("stores-and-products.sql", encoding="utf-8").read())
        cursor.execute(open("star-schema.sql", encoding="utf-8").read())
        connection.commit()
        print("Executed SQL scripts.\n")

        # Transfer existing data to the new tables.
        print("Transferring existing data...")
        print("Transferring shops...")
        cursor.execute("select s.shopid, s.name, ci.name, r.name, co.name "
                       "from shop s "
                       "join city ci on s.cityid = ci.cityid "
                       "join region r on ci.regionid = r.regionid "
                       "join country co on r.countryid = co.countryid")
        shops = cursor.fetchall()
        for s in shops:
            cursor.execute("insert into shopid(id, name, city, region, country) "
                           "values(%s, %s, %s, %s, %s) "
                           "on conflict (id) do nothing",
                           (s[0], s[1], s[2], s[3], s[4]))
            connection.commit()

        print("Transferring articles...")
        cursor.execute("select a.articleid, a.name, a.price, pg.name, pf.name, pc.name "
                       "from article a "
                       "join productgroup pg on a.productgroupid = pg.productgroupid "
                       "join productfamily pf on pg.productfamilyid = pf.productfamilyid "
                       "join productcategory pc on pf.productcategoryid = pc.productcategoryid")
        articles = cursor.fetchall()
        for a in articles:
            cursor.execute("insert into articleid(id, name, price, productgroup, productfamily, productcategory) "
                           "values(%s, %s, %s, %s, %s, %s) "
                           "on conflict (id) do nothing",
                           (a[0], a[1], a[2], a[3], a[4], a[5]))
            connection.commit()
        print("Transferred existing data.\n")

        print("Writing data from CSV file to DB...")
        print("Writing dates...")
        with open("sales.csv", "r", encoding="latin-1") as f:
            # Open a CSV reader, skip headline, initialize dateid, list of dateids and list of dates.
            reader = csv.reader(f, delimiter=";")
            next(reader)
            did = 1
            dids = []
            dates = []
            # Iterate over each line, split the date string into day, month, year,
            # check if it's already in the list and append if not. Increase count.
            for row in reader:
                if row[0].split(".") not in dates:
                    dids.append(did)
                    dates.append(row[0].split("."))
                    did += 1

            # Zip dateids and dates together and write to the table.
            dateids = list(zip(dids, dates))
            for d in dateids:
                cursor.execute("insert into dateid(id, day, month, year) "
                               "values(%s, %s, %s, %s) "
                               "on conflict (id) do nothing",
                               (d[0], d[1][0], d[1][1], d[1][2]))
                connection.commit()

        print("Writing data...")
        amount = len(open("sales.csv").readlines())
        with open("sales.csv", "r", encoding="latin-1") as f:
            reader = csv.reader(f, delimiter=";")
            next(reader)
            id = 1
            count = 0
            # Give a progress counter for amount of lines already processed.
            for row in reader:
                if count % 1000 == 0:
                    print(str(count) + "/" + str(amount))

                # Get ids from tables, check if exists.
                cursor.execute("select id from dateid "
                               "where day = %s "
                               "and month = %s "
                               "and year = %s",
                               (row[0][:2], row[0][3:5], row[0][6:10]))
                did = cursor.fetchone()
                if did:
                    did = did[0]

                cursor.execute("select id from shopid "
                               "where name = %s",
                               (row[1],))
                sid = cursor.fetchone()
                if sid:
                    sid = sid[0]

                cursor.execute("select id from articleid "
                               "where name = %s",
                               (row[2],))
                aid = cursor.fetchone()
                if aid:
                    aid = aid[0]

                # Check if proper values are present.
                try:
                    sold = int(row[3])
                except ValueError:
                    sold = row[3]

                try:
                    rev = float(row[4].replace(",", "."))
                except ValueError:
                    rev = row[4]

                # Check again before writing to DB.
                if isinstance(did, int) and isinstance(sid, int) and isinstance(aid, int) \
                        and isinstance(sold, int) and isinstance(rev, float):
                    cursor.execute("insert into facttable(id, dateid, shopid, articleid, sold, revenue) "
                                   "values(%s, %s, %s, %s, %s, %s) "
                                   "on conflict (id) do nothing",
                                   (id, did, sid, aid, sold, rev))
                    connection.commit()
                    id += 1
                    count += 1

        cursor.close()
        connection.close()
        return "\nFinished writing data from CSV file."
    except (Exception, Error) as error:
        return error
    finally:
        if connection:
            cursor.close()
            connection.close()


etl()
