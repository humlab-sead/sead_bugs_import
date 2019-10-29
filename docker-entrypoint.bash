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

download_latest_bugsdata

bugsdata=./data/bugsdata.mdb

java -jar $BUGS_IMPORT_JAR --file=$bugsdata

