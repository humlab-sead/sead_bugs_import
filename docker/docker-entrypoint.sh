#!/bin/bash

function download_latest_bugsdata()
{
    download_filename=bugsdata_`date "+%Y%m%d%H%M%S"`.zip
    tempdir='./_temp_bugsimport'
    if [[ -f $tempdir ]]; then
        rm -rf $tempdir
    fi
    cd $datadir
    wget -d --user-agent="Mozilla/5.0 (Windows NT x.y; rv:10.0) Gecko/20100101 Firefox/10.0"  https://www.bugscep.com/downloads/bugsdata.zip
    unzip bugsdata.zip bugsdata.mdb

}

POSITIONAL=()
while [[ $# -gt 0 ]]
do
    key="$1"
    case $key in
        -h|--db-host)
            BUGSIMPORT_DBHOST="$2"; shift; shift
        ;;
        -u|--db-user)
            BUGSIMPORT_DBUSER="$2"; shift; shift
        ;;
        -t|--db-name)
            BUGSIMPORT_DBNAME="$2"; shift; shift
        ;;
        *)
        POSITIONAL+=("$1")
        shift
        ;;
    esac
done

set -- "${POSITIONAL[@]}" # restore positional parameters

function usage()
{
    echo "usage: bugsimport [OPTION]... [FILE]"
    echo "Imports BugsCEP MS Access data file FILE into SEAD database."
    echo ""
    echo "   -h, --db-host                  target database server"
    echo "   -u, --db-user                  target database user"
    echo "   -u, --db-name                  target database name"
    echo ""
}

mkdir -p config

if [ "$#" != "1" ]; then
    usage
    exit 64
fi
ls -al /home/bugger/bugsdata

BUGSIMPORT_MDB_NAME="$1"

if [ "$BUGSIMPORT_MDB_NAME" == "" ]; then
    usage
    exit 64
fi

if [ ! -f "./bugsdata/$BUGSIMPORT_MDB_NAME" ]; then
    echo "error: please specify an existing MDB source file"
    exit 64
fi

if [ "$BUGSIMPORT_DBHOST" == "" ]; then
    BUGSIMPORT_DBHOST="seadserv.humlab.umu.se"
    echo "info: target server not specified, falling back to $BUGSIMPORT_DBHOST"
    exit 64
fi

if [ "$BUGSIMPORT_DBNAME" == "" ]; then
    echo "error: please specify target database server or set environment variable BUGSIMPORT_DBHOST"
    usage
    exit 64
fi

if [ "$BUGSIMPORT_DBUSER" == "" ]; then
    echo "error: please specify username or set environment variable BUGSIMPORT_DBUSER"
    usage
    exit 64
fi

if [ "$BUGSIMPORT_DBPASSWORD" == "" ]; then
    echo "error: please password via environment BUGSIMPORT_DBPASSWORD"
    usage
    exit 64
fi

eval "cat <<EOF
$(<application.properties.template)
EOF
" 2> /dev/null > ./config/application.properties

cat ./config/application.properties

#download_latest_bugsdata

echo "java -jar $BUGS_IMPORT_JAR --file=./bugsdata/$BUGSIMPORT_MDB_NAME"
