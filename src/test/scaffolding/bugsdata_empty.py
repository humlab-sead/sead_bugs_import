import shutil
import jaydebeapi
from os.path import dirname, realpath, join
from typing import Any
import os
import click


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

def truncate(cnxn: Any, table_name: str) -> None:
    with cnxn.cursor() as crsr:
        crsr.execute(f'DELETE * FROM "{table_name}"')
    cnxn.commit()

def truncate_all(cnxn: Any, table_names: list[str]) -> None:
    with cnxn.cursor() as crsr:
        for table_name in table_names:
            try:
                crsr.execute(f'delete from {table_name}')
                cnxn.commit()
                print(f"table: {table_name} OK")
            except Exception as ex:
                print(f"table: {table_name} FAILED {ex}")

@click.command()
@click.argument('source-filename')
@click.argument('target-filename')
def main(source_filename: str, target_filename: str):


    mdb_tables=[

        "TFossilUncertainty",
        "TFossil",
        "TDatesCalendar",
        "TDatesRadio",
        "TDatesPeriod",
        "TSample",
        "TLab",
        "TLookupUncertaintyType",
        "TDatesMethods",
        "TPeriods",
        "TCountsheet",
        "TLookupCountsheetContext",
        "TLookupCountsheetTypes",
        "TSiteOtherProxies",
        "TINDEXReplacements",
        "TSiteRef",
        "TSite",
        "TBiology",
        "TDistrib",
        "TKeys",
        "TTaxoNotes",
        "TSpeciesAssociations",
        "TAttributes",
        "TMCRSummaryData",
        "TEcoDefBugs",
        "TEcoBugs",
        "TMCRNames",
        "TEcoKoch",
        "TEcoDefKoch",
        "TEcoDefGroups",

        "TLookupMonths",
        "TbirmBEETLEdat",
        "TDatesTypes",
        "TSynonymNotes",
        "TSynonym"

        "TRDBCodes",
        "TRDB",
        "TRDBSystems",
        "TSeasonActiveAdult",
        "TCountry",

        "INDEX",
        "TBiblio",
    ]

    if os.path.isfile(target_filename):
        # os.unlink(target_filename)
        raise ValueError("Target file already exists. Please remove and run again.")

    shutil.copy(source_filename, target_filename)

    cnxn = create_connection(target_filename)

    for _ in range(1, 3):
        truncate_all(cnxn, mdb_tables)

    cnxn = create_connection(target_filename)


if __name__ == '__main__':
    main()
