from psycopg2 import connect, Error
import csv


def execute_scripts():
    try:
        connection = connect(user="velcorn",
                             password="L5u8c2y9",
                             host="localhost",
                             port="5432",
                             database="sheet6")
        cursor = connection.cursor()
        cursor.execute(open("stores-and-products.sql", "r").read())
        cursor.close()
        connection.close()
    except (Exception, Error) as error:
        return error
    finally:
        if connection:
            cursor.close()
            connection.close()


def import_csv():
    with open("sales.csv") as f:
        reader = csv.reader(f)
        for row in reader:
            print(row)


print(execute_scripts())
