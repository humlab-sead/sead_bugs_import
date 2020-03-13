#!/bin/bash

export PGCLIENTENCODING=UTF8

#echo "x.x.1.181" >  ~/.default.sead.server
#echo "x" >  ~/.default.sead.username

dothostfile=~/vault/.default.sead.server
dotuserfile=~/vault/.default.sead.username

#target_name=bugsdata_`date "+%Y%m%d"`
target_name=
source_path=

if [[ -f "$dothostfile" ]]; then
    dbhost=`cat $dothostfile`
fi
if [[ -f "$dotuserfile" ]]; then
    dbuser=`cat $dotuserfile`
fi

usage_message=$(cat <<EOF
usage: deploy_staging OPTIONS...

    --host SERVERNAME               Target server (${dbhost})
    --user USERNAME                 User on target server (${dbuser})
    --target DBNAME         Target database name. Mandatory.
    --source MDB_FILE               Name of source MDB file

EOF
)

POSITIONAL=()
while [[ $# -gt 0 ]]
do
    key="$1"

    case $key in
        --host)
            dbhost="$2"; shift; shift
        ;;
        --user)
            dbuser="$2"; shift; shift
        ;;
        --target)
            target_name="$2"; shift; shift
        ;;
        --source)
            source_path="$2";
            shift; shift
        ;;
        *)
            POSITIONAL+=("$1") # save it in an array for later
            shift
        ;;
    esac
done

function usage() {
    echo "$usage_message"
}

set -- "${POSITIONAL[@]}" # restore positional parameters

if [ "$dbhost" != "seadserv.humlab.umu.se" ]; then
    echo "This script can for now only be run on 130.239.1.181";
    exit 64
fi

if [ "${source_path}" == "" ]; then
    echo "error: MDB source file name not specified"
    usage
    exit 64
fi

if [ ! -f "${source_path}"  ]; then
    echo "error: file ${source_path} not found"
    usage
    exit 64
fi

source_filename=$(basename -- "$source_path")

if [ "$target_name" == "" ]; then
    target_name="${source_filename%.*}"
fi

echo "info: copying ${source_path} into ${target_name}...";

function dbexec() {
    db_name=$1
    sql=$2
    psql -v ON_ERROR_STOP=1 --host=$dbhost --username=$dbuser --no-password --dbname=$dbname --command "$sql"
    if [ $? -ne 0 ];  then
        echo "fatal: psql command failed! Deploy aborted." >&2
        exit 64
    fi
}

function setup_database() {

    target_db_exists="$( psql --host=$dbhost --username=$dbuser --no-password --dbname=postgres -tAc "select 1 from pg_database where datname='${target_name}'" )"

	if [ "$target_db_exists" = "1" ]
	then
	    #echo "error: target database ${target_name} exists. Drop database or specify another name"
	    #exit 64
        echo "info: removing existing database $target_name"
        dropdb --if-exists --interactive  --host=$dbhost --username=$dbuser --no-password $target_name
	fi


    createdb --encoding=UTF8 --host=$dbhost --username=$dbuser --owner=sead_master --no-password $target_name
    if [ $? -ne 0 ];  then
        echo "fatal: createdb failed." >&2
        exit 64
    fi

    echo "info: creating schema DDL ${target_name}_schema.sql..."
    mdb-schema $source_path postgres > ${target_name}_schema.tmp.sql

    cat ${target_name}_schema.tmp.sql | \
        sed -e 's/\"CODE\"\s*DOUBLE PRECISION/\"CODE\" VARCHAR(20)/g' | \
        sed -e 's/CREATE UNIQUE INDEX/CREATE INDEX/g' | \
        sed -e 's/BOOL NOT NULL/SMALLINT/g' | \
        grep -vE 'MSysNavPaneGroups' >  ${target_name}_schema.sql

    if [ $? -ne 0 ];  then
        echo "fatal: sed failed." >&2
        exit 64
    fi

    rm ${target_name}_schema.tmp.sql

    PGOPTIONS='--client-min-messages=warning --quite -P pager'
    psql -f ${target_name}_schema.sql --host=$dbhost --username=$dbuser --no-password --dbname=$target_name \
            | grep -vE 'CREATE|ALTER|COMMENT|SET' > ${target_name}_schema.log 2>&1

    # if [ $? -ne 0 ];  then
    #     echo "fatal: schema DDL failed" >&2
    #     exit 64
    # fi

}

mdb_tables=" \
    INDEX
    TEcoDefGroups
    TSite
    TBiblio
    TEcoDefBugs
    TCountry
    TSample
    TEcoDefKoch
    TFossil
    TAttributes
    TEcoBugs
    TEcoKoch
    TbirmBEETLEdat
    TCountsheet
    TDatesCalendar
    TDatesMethods
    TDatesRadio
    TDatesTypes
    TDistrib
    TFossilUncertainty
    TINDEXReplacements
    TKeys
    TLab
    TLookupCountsheetContext
    TLookupMonths
    TLookupUncertaintyType
    TMCRNames
    TMCRSummaryData
    TPeriods
    TRDB
    TRDBCodes
    TSeasonActiveAdult
    TSiteOtherProxies
    TSiteRef
    TSpeciesAssociations
    TSynonymNotes
    TTaxoNotes
    TBiology
    TDatesPeriod
    TLookupCountsheetTypes
    TSynonym
    "

function copy_database() {

    #for table in $(mdb-tables -t table $source_path); do \
    for table in $mdb_tables; do \
        echo "Copying $table..."
        #mdb-schema $source_path -T $table postgres | \
        #     psql -v ON_ERROR_STOP=1 --host=$dbhost --username=$dbuser --no-password --dbname=$target_name

        PGOPTIONS='--client-min-messages=warning --quite -P pager --echo-errors -v ON_ERROR_STOP=1'
        mdb-export -I postgres -q\' $source_path $table | \
             psql --host=$dbhost --username=$dbuser --no-password --dbname=$target_name | \
                grep -vE 'INSERT' > ${target_name}_data.log 2>&1

        # if [ $? -ne 0 ];  then
        #     echo "fatal:  $table failed." >&2
        #     exit 64
        # fi

    done
}

setup_database
copy_database
