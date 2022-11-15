#!/bin/bash
#######################################################################
# WORK IN PROGRESS
# THIS WILL MOST LIKELY BE DEPRECATED IN FAVOR OF DOCKER SOLUTION
#
# The intention of this script is to enable running a bugs bugs import
# with this script as the single starting poiint
#
#######################################################################

export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")

bugsdir=~/bugs
tmpdir=~/$bugsdir/tmp
dbhostfile=~/.default.sead.server
dbuserfile=~/.default.sead.username
dbtarget=sead_staging
dbdeprecated=${dbtarget}_`date "+%Y%m%d%H%M%S"`
do_clean_build=no
cr_version=@v0.1

#echo "seadserv.humlab.umu.se" >  ~/.default.sead.server
#echo "humlab_admin" >  ~/.default.sead.username

# if [[ -f "$dbhostfile" ]]; then
#     dbhost=`cat $dbhostfile`
# fi
# if [[ -f "$dbuserfile" ]]; then
#     dbuser=`cat $dbuserfile`
# fi

# if [[ ! -f $bugsdir ]]; then
#     mkdir $bugsdir
# fi

rm -rf $bugsdir/tmp
mkdir $bugsdir/tmp

POSITIONAL=()
while [[ $# -gt 0 ]]
do
    key="$1"
    case $key in
        # -h|--host)
        #     dbhost="$2"; shift; shift
        # ;;
        # -u|--user)
        #     dbuser="$2"; shift; shift
        # ;;
        # -t|--target-database)
        #     dbtarget="$2"; shift; shift
        # ;;
        -c|--clean-build)
            do_clean_build="yes"; shift
        ;;
        *)
        POSITIONAL+=("$1")
        shift
        ;;
    esac
done
set -- "${POSITIONAL[@]}" # restore positional parameters

if [ "$dbhost" != "130.239.1.181" ]; then
    echo "This script can for now only be run on 130.239.1.181";
    exit 64
fi

function build_from_source()
{
    cd $bugsdir
    rm -rf $bugsdir/tmp && mkdir $bugsdir/tmp
    git clone 'https://github.com/humlab-sead/sead_bugs_import.git'
    cd sead_bugs_import
    mvn clean package
    cp ./target/*.jar $bugsdir/
    cd $bugsdir && rm -rf ./tmp
}
# sudo apt-get install openjdk-8-jre-headless

filename_inside_zip=bugsdata.mdb

function download_latest_bugsdata()
{
    download_filename=bugsdata_`date "+%Y%m%d%H%M%S"`.zip
    tempdir='./_temp_bugsimport'
    if [[ -f $tempdir ]]; then
        rm -rf $tempdir
    fi
    cd $datadir
    wget -d --user-agent="Mozilla/5.0 (Windows NT x.y; rv:10.0) Gecko/20100101 Firefox/10.0"  https://www.bugscep.com/downloads/bugsdata.zip
    filename_inside_zip=$(unzip -Z1 bugsdata.zip)
    unzip bugsdata.zip
}



download_latest_bugsdata

mvn -Dmaven.test.skip=true clean package
mkdir dist
cp $BUGS_IMPORT_JAR ~/dist

java -jar $BUGS_IMPORT_JAR --file=$bugsdata

