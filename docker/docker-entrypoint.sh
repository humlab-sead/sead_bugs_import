#!/bin/bash

BUGS_IMPORT_JAR=bugs.import-0.1-SNAPSHOT.jar

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

function usage()
{
    echo "usage: bugsimport [FILE]"
    echo "Imports BugsCEP MS Access data file FILE into SEAD database."
}

env

if [ "$#" != "1" ]; then
    usage
    exit 64
fi

BUGSIMPORT_MDB_NAME="$1"

if [ "./data/$BUGSIMPORT_MDB_NAME" == "" ]; then
    usage
    exit 64
fi

echo "info: importing file $BUGSIMPORT_MDB_NAME (./data/$BUGSIMPORT_MDB_NAME inside image)"

if [ ! -f "./data/$BUGSIMPORT_MDB_NAME" ]; then
    echo "error: file ./data/$BUGSIMPORT_MDB_NAME please specify an existing MDB source file"
    echo "       note that current directory is mounted as ./data in the docker image"
    echo "       the MDB file must reside in current folder or in a sub-folder "
    exit 64
fi

if [ "$BUGSIMPORT_DBHOST" == "" ]; then
    BUGSIMPORT_DBHOST="seadserv.humlab.umu.se"
    echo "info: target server not specified, falling back to $BUGSIMPORT_DBHOST"
fi

if [ "$BUGSIMPORT_DBNAME" == "" ]; then
    echo "error: please specify target database name BUGSIMPORT_DBNAME"
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

# cat ./config/application.properties

#download_latest_bugsdata

java -jar $BUGS_IMPORT_JAR --file=./data/$BUGSIMPORT_MDB_NAME
