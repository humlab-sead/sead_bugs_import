"""
Runs input SQL script on target database
"""
import jaydebeapi
from os.path import dirname, realpath, join
from typing import Any, Iterable
import os
import shutil
import re


LIB_PATH = realpath(join(dirname(__file__), "../../..", "lib"))

UCANACCESS_JARS = [
    f"{LIB_PATH}/UCanAccess/ucanaccess-5.0.1.jar",
    f"{LIB_PATH}/UCanAccess/lib/commons-lang3-3.8.1.jar",
    f"{LIB_PATH}/UCanAccess/lib/commons-logging-1.2.jar",
    f"{LIB_PATH}/UCanAccess/lib/hsqldb-2.5.0.jar",
    f"{LIB_PATH}/UCanAccess/lib/jackcess-3.0.1.jar",
]

def create_connection(db_path: str):
    assert all(os.path.isfile(x) for x in UCANACCESS_JARS)
    classpath = ':'.join(UCANACCESS_JARS)
    cnxn = jaydebeapi.connect(
        "net.ucanaccess.jdbc.UcanaccessDriver",
        f"jdbc:ucanaccess://{db_path};newDatabaseVersion=V2010",
        ["SA", ""],
        classpath
        )
    return cnxn


def get_sql_statements(filename: str) -> Iterable[str]:

    with open(filename, encoding="utf-8") as fp:
        data: str = fp.read()

    data = re.sub("--.*", "", data)
    data = re.sub(";\s+\n", ";\n", data)
    data = data.replace('"', '')

    sql_statements: list[str] = data.split(";\n")
    for sql in sql_statements:
        if sql.strip() != "":
            yield f"{sql.strip()};"

@click.command()
@click.argument('target-database')
@click.argument('input-filename')
@click.argument('template-database')
def main(target_database: str, input_filename: str, template_database: str=None):

    if template_database is not None:

        if os.path.isfile(target_database):
            os.unlink(target_database)

        shutil.copy(template_database,target_database)

    if not os.path.isfile(target_database):
        raise FileNotFoundError(target_database)

    if not os.path.isfile(input_filename):
        raise FileNotFoundError(input_filename)

    with create_connection(target_database) as cnxn:
        for sql_statement in get_sql_statements(input_filename):
            print(sql_statement.replace("\n", "")[:80] + "...")
            with cnxn.cursor() as crsr:
                crsr.execute(sql_statement)
        cnxn.commit()

if __name__ == '__main__':

    # template_database = '/home/roger/source/sead_bugs_import/bugsdata/bugsdata_empty.mdb'
    # target_database = '/home/roger/source/sead_bugs_import/bugsdata/bugsdata_samp000546.mdb'
    # input_filename = '/home/roger/source/sead_bugs_import/bugsdata/samp000546.sql'

    main() # target_database=target_database, input_filename=input_filename, template_database=template_database)
