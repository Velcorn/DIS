from psycopg2 import connect, Error
from config import db_config


def data_analysis():
    while True:
        print("Please enter a comma seperated string like: geo,time,product")
        print("Possible inputs: Geo(shop, city, region, country); "
              "Time(day, month, year); "
              "Product(product, productgroup, productfamily, productcategory)")

        # Get user input from console, catching possible errors.
        try:
            geo, time, product = input("> ").replace(" ", "").split(",")
        except ValueError:
            print("Invalid format!\n")
            continue
        if geo == "shop":
            geo = "name"
        if geo not in ["name", "city", "region", "country"]:
            print("Invalid geo!\n")
            continue
        if time not in ["day", "month", "year"]:
            print("Invalid time!\n")
            continue
        if product == "product":
            product = "name"
        if product not in ["product", "productgroup", "productfamily", "productcategory"]:
            print("Invalid product!\n")
            continue
        print("\n")

        try:
            params = db_config()
            connection = connect(**params)
            cursor = connection.cursor()

            # Execute query for specified filters; use group by rollup.
            # Print results line by line.
            if time == "year":
                cursor.execute("select s." + geo + " as geo, d.year, a." + product +
                               " as product, sum(f.sold) as sold "
                               "from shopid s, dateid d, articleid a, facttable f "
                               "where f.shopid = s.id "
                               "and f.dateid = d.id "
                               "and f.articleid = a.id "
                               "group by rollup(s." + geo + ", d.year, a." + product + ")")
                data = cursor.fetchall()

                print("Geo | Year | Product | Sold")
                for d in data:
                    print(" | ".join(str(d) for d in d))
                print("\n")
            elif time == "month":
                cursor.execute("select s." + geo + " as geo, d.month, d.year, a." + product +
                               " as product, sum(f.sold) as sold "
                               "from shopid s, dateid d, articleid a, facttable f "
                               "where f.shopid = s.id "
                               "and f.dateid = d.id "
                               "and f.articleid = a.id "
                               "group by rollup(s." + geo + ", d.month, d.year, a." + product + ")")
                data = cursor.fetchall()

                print("Geo | Month | Product | Sold")
                for d in data:
                    print(str(d[0]) + " | " + str(d[1]) + "." + str(d[2]) + " | " + str(d[3]) + " | " + str(d[4]))
                print("\n")
            else:
                cursor.execute("select s." + geo + " as geo, d.day, d.month, d.year, a." + product +
                               " as product, sum(f.sold) as sold "
                               "from shopid s, dateid d, articleid a, facttable f "
                               "where f.shopid = s.id "
                               "and f.dateid = d.id "
                               "and f.articleid = a.id "
                               "group by rollup(s." + geo + ", d.day, d.month, d.year, a." + product + ")")
                data = cursor.fetchall()

                print("Geo | Day | Products | Sold")
                for d in data:
                    print(str(d[0]) + " | " + str(d[1]) + "." + str(d[2]) + "." + str(d[3]) +
                          " | " + str(d[4]) + " | " + str(d[5]))
                print("\n")

            cursor.close()
            connection.close()
        except (Exception, Error) as error:
            return error
        finally:
            if connection:
                cursor.close()
                connection.close()


data_analysis()
