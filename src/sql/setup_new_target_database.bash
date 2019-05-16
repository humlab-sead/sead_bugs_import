#!/bin/bash

REPOSITORY="https://raw.githubusercontent.com/humlab-sead/sead_change_control/master"

echo "This script is deprecated"
exit 64

source_db="template_bugs_import"
target_db="sead_staging_bugs"

dbhost=`cat ~/.default.sead.server`
dbuser=`cat  ~/.default.sead.username`

dbexec() {
    psql -bqw -h $dbhost -U $dbuser -c "$1" $target_db
}

kickout() {
    xsql="select pg_terminate_backend(pid) from pg_stat_activity where datname = '$1' and pid <> pg_backend_pid();"
    psql -bqw -h $dbhost -U $dbuser -c "$xsql" "postgres"
}

apply_crs() {
    project=$1
    items=$2
    for CR in ${items}; do
        echo "Applying ${project}/${CR}..."
        rm -f ${CR}.sql
        wget -q -nv ${REPOSITORY}/${project}/deploy/${CR}.sql --output-document=${CR}.sql
        psql --echo-errors --quiet -h $dbhost -U $dbuser --no-password --file="${CR}.sql" $target_db
        rm -f ${CR}.sql
    done
}

BugsCRs="\
    CS_BUGS_20190503_SETUP_SCHEMA\
    CS_BUGS_20190503_ADD_TRANSLATIONS"

GeneralCRs="\
    CS_DATINGLAB_20190503_ADD_UNKNOWN_LAB\
    CS_METHOD_20190503_ADD_BUGS_METHODS\
    CS_TAXA_20190503_ADD_SPECIES_ASSOC_TYPES\
    CS_TAXA_20190503_ATTRIBUTE_TYPE_LENGTH\
    CS_DATA_TYPE_20190513_ADD_TYPES_CALENDER_DATES\
    CS_ECOCODE_20190513_ADD_GROUP_UPDATE_SYSTEM"

kickout $target_db
dropdb --echo --if-exists --host=$dbhost --username=$dbuser --no-password "$target_db"

kickout $source_db
createdb --echo --owner=sead_master --host=$dbhost --username=$dbuser --no-password --encoding="UTF8" -T "$source_db" "$target_db"

apply_crs "bugs" "$BugsCRs"
apply_crs "general" "$GeneralCRs"

echo "done!"